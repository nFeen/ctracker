from flask import Flask, request, jsonify, abort
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
import os
import requests
from requests_oauthlib import OAuth1
from dotenv import load_dotenv
import time
import uuid
import hmac
import hashlib
import base64
import urllib
from urllib.parse import urlencode, quote
from requests_oauthlib import OAuth1Session

# Добавьте ваши ключи аутентификации для FatSecret API
CONSUMER_KEY = 'ваш_consumer_key'
CONSUMER_SECRET = 'ваш_consumer_secret'

app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))
app.config['SQLALCHEMY_DATABASE_URI'] =\
        'sqlite:///' + os.path.join(basedir, 'database.db')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)
bcrypt = Bcrypt(app)
with open('avatar_standart.txt') as file:
    avatar_in_base64 = file.read()
    
# Загружаем переменные окружения из .env
load_dotenv()

# Получаем ключи из .env
FATSECRET_CONSUMER_KEY = os.getenv("FATSECRET_CONSUMER_KEY")
FATSECRET_CONSUMER_SECRET = os.getenv("FATSECRET_CONSUMER_SECRET")
    
class User(db.Model):
    __tablename__ = 'Users'
    user_id = db.Column(db.Integer, primary_key=True)
    login = db.Column(db.String(80), unique=True, nullable=False)
    password_hash = db.Column(db.String(128), nullable=False)
    weight = db.Column(db.Float)
    height = db.Column(db.Integer)
    calorieGoal = db.Column(db.Integer, default=2000)
    profile_pic = db.Column(db.Text)

class Meal(db.Model):
    __tablename__ = 'Meals'
    meal_id = db.Column(db.Integer, primary_key=True)
    user_id = db.Column(db.Integer, db.ForeignKey('Users.user_id'), nullable=False)
    date = db.Column(db.String, nullable=False)
    part_of_the_day = db.Column(db.Integer) # 0-breakfast 1-lunch 2-dinner 3-additional
    food_id = db.Column(db.Integer, db.ForeignKey('Foods.food_id'), nullable=False)
    quantity = db.Column(db.Integer, default=1)
    calories = db.Column(db.Integer, nullable=False)
    protein = db.Column(db.Integer, nullable=False)
    carbs = db.Column(db.Integer, nullable=False)
    fats = db.Column(db.Integer, nullable=False)

class Food(db.Model):
    __tablename__ = 'Foods'
    food_id = db.Column(db.Integer, primary_key=True)
    name = db.Column(db.String(80), nullable=False)
    calories = db.Column(db.Integer, nullable=False)
    protein = db.Column(db.Float, nullable=False)
    fats = db.Column(db.Float, nullable=False)
    carbs = db.Column(db.Float, nullable=False)
    description = db.Column(db.String(128))

# User Login
@app.route('/user/login', methods=['GET'])
def login():
    login = request.args.get('login')
    password = request.args.get('password')
    user = User.query.filter_by(login=login).first()
    if user and bcrypt.check_password_hash(user.password_hash, password):
        return jsonify({'user_id': user.user_id})
    else:
        abort(401)

# User Registration
@app.route('/user/register', methods=['POST'])
def register():
    data = request.json
    login = data.get('login')
    password = data.get('password')
    if User.query.filter_by(login=login).first():
        abort(409)  # Conflict
    password_hash = bcrypt.generate_password_hash(password).decode('utf-8')
    new_user = User(login=login, password_hash=password_hash, profile_pic=base64)
    db.session.add(new_user)
    db.session.commit()
    return jsonify({'user_id': new_user.user_id})

# Get User Profile
@app.route('/user/getprofile', methods=['GET'])
def get_profile():
    user_id = request.args.get('user_id')
    user = User.query.get(user_id)
    if user:
        return jsonify({
            'weight': user.weight,
            'login': user.login,
            'calorieGoal': user.calorieGoal,
            'profile_picture' : user.profile_pic,
            'height' : user.height
        })
    abort(404)

# Update User Weight
@app.route('/user/weight', methods=['PATCH'])
def update_weight():
    data = request.json
    user_id = data.get('user_id')
    weight = data.get('weight')
    user = User.query.get(user_id)
    if user:
        user.weight = weight
        db.session.commit()
        return jsonify({"status": "Weight updated"}), 200
    abort(404)
    
# Update User Height
@app.route('/user/height', methods=['PATCH'])
def update_height():
    data = request.json
    user_id = data.get('user_id')
    height = data.get('height')
    user = User.query.get(user_id)
    if user:
        user.height = height
        db.session.commit()
        return jsonify({"status": "height updated"}), 200
    abort(404)

# Update User Profile Picture
@app.route('/user/profile_picture', methods=['PATCH'])
def update_profile_picture():
    data = request.json
    user_id = data.get('user_id')
    profile_picture = data.get('profile_picture')
    user = User.query.get(user_id)
    if user:
        user.profile_pic = profile_picture
        db.session.commit()
        return jsonify({"status": "profile picture updated"}), 200
    abort(404)

def generate_oauth_headers(params, consumer_key, consumer_secret, url, method="GET"):
    # Добавляем параметры OAuth
    oauth_params = {
        "oauth_consumer_key": consumer_key,
        "oauth_signature_method": "HMAC-SHA1",
        "oauth_timestamp": str(int(time.time())),
        "oauth_nonce": str(uuid.uuid4().hex),
        "oauth_version": "1.0",
    }
    # Объединяем все параметры (включая параметры запроса)
    all_params = {**params, **oauth_params}
    # Сортируем параметры по ключу в лексикографическом порядке
    sorted_params = sorted(all_params.items())
    # Создаем строку для подписи
    param_string = "&".join(
        f"{urllib.parse.quote(k, safe='')}={urllib.parse.quote(str(v), safe='')}"
        for k, v in sorted_params
    )
    # Формируем базовую строку (Base String) для подписи
    base_string = "&".join(
        [
            method.upper(),
            urllib.parse.quote(url, safe=""),
            urllib.parse.quote(param_string, safe=""),
        ]
    )
    # Генерируем ключ для подписи
    signing_key = f"{consumer_secret}&"
    # Вычисляем подпись
    signature = base64.b64encode(
        hmac.new(signing_key.encode(), base_string.encode(), hashlib.sha1).digest()
    ).decode()
    # Добавляем подпись к OAuth параметрам
    oauth_params["oauth_signature"] = signature
    # Формируем заголовок Authorization
    oauth_header = "OAuth " + ", ".join(
        f'{k}="{urllib.parse.quote(v)}"' for k, v in sorted(oauth_params.items())
    )
    return {"Authorization": oauth_header}
    
@app.route('/fooddb/search', methods=['GET'])
def food_list():
    food_name = request.args.get('food')
    if not food_name:
        return jsonify({"error": "Parameter 'food' is required"}), 400

    # Параметры запроса
    search_url = "https://platform.fatsecret.com/rest/server.api"
    params = {
        "method": "foods.search",
        "format": "json",
        "max_results" : 20,
        "search_expression": food_name,
        "oauth_consumer_key": FATSECRET_CONSUMER_KEY,
        "oauth_nonce": os.urandom(8).hex(),
        "oauth_signature_method": "HMAC-SHA1",
        "oauth_timestamp": str(int(time.time())),
        "oauth_version": "1.0"
    }

    # Генерация подписи для запроса
    base_string = "&".join(f"{key}={requests.utils.quote(str(value), safe='')}" for key, value in sorted(params.items()))
    signature_base_string = f"GET&{requests.utils.quote(search_url, safe='')}&{requests.utils.quote(base_string, safe='')}"
    signing_key = f"{FATSECRET_CONSUMER_SECRET}&"
    signature = base64.b64encode(hmac.new(signing_key.encode(), signature_base_string.encode(), hashlib.sha1).digest()).decode()
    params["oauth_signature"] = signature

    # Выполнение GET-запроса к FatSecret API
    response = requests.get(search_url, params=params)

    if response.status_code != 200:
        return jsonify({"error": "Failed to fetch data from FatSecret API"}), response.status_code

    fatsecret_data = response.json()
    foods = fatsecret_data.get("foods", {}).get("food", [])

    # Формирование результата
    result = []
    for food in foods:
        food_id = food["food_id"]
        food_name = food["food_name"]
        food_description = food["food_description"]

        # Проверяем, существует ли продукт в базе
        existing_food = Food.query.filter_by(food_id=food_id).first()
        if not existing_food:
            # Если продукта нет в базе, добавляем его
            details_url = "https://platform.fatsecret.com/rest/server.api"
            details_params = {
                "method": "food.get",
                "format": "json",
                "food_id": food_id,
                "oauth_consumer_key": FATSECRET_CONSUMER_KEY,
                "oauth_nonce": os.urandom(8).hex(),
                "oauth_signature_method": "HMAC-SHA1",
                "oauth_timestamp": str(int(time.time())),
                "oauth_version": "1.0"
            }
            # Генерация подписи
            details_base_string = "&".join(f"{key}={requests.utils.quote(str(value), safe='')}" for key, value in sorted(details_params.items()))
            details_signature_base_string = f"GET&{requests.utils.quote(details_url, safe='')}&{requests.utils.quote(details_base_string, safe='')}"
            details_signature = base64.b64encode(hmac.new(signing_key.encode(), details_signature_base_string.encode(), hashlib.sha1).digest()).decode()
            details_params["oauth_signature"] = details_signature

            details_response = requests.get(details_url, params=details_params)
            if details_response.status_code == 200:
                food_details = details_response.json()["food"]
                servings = food_details["servings"]["serving"]
                if isinstance(servings, list):
                    primary_serving = servings[0]
                else:
                    primary_serving = servings
                if primary_serving.get("measurement_description", "").lower() == "serving":
                    continue
                # Получаем значение единицы измерения
                metric_serving_amount = float(primary_serving.get("metric_serving_amount", 100))  # Если отсутствует, устанавливаем 100
                metric_serving_unit = primary_serving.get("metric_serving_unit", "g")

                # Конвертация в граммы, если необходимо
                if metric_serving_unit == "oz":
                    metric_serving_amount *= 28.3495  # 1 унция = 28.3495 грамм
                elif metric_serving_unit == "ml":
                    # Для ml предполагаем, что 1 мл = 1 г (для воды и подобных жидкостей)
                    metric_serving_amount = metric_serving_amount
                elif metric_serving_unit != "g":
                    # Если единица не поддерживается, пропускаем
                    continue

                # Пересчёт КБЖУ на 100 граммов
                calories = float(primary_serving["calories"]) * (100 / metric_serving_amount)
                protein = float(primary_serving["protein"]) * (100 / metric_serving_amount)
                fats = float(primary_serving["fat"]) * (100 / metric_serving_amount)
                carbs = float(primary_serving["carbohydrate"]) * (100 / metric_serving_amount)

                # Добавляем продукт в базу
                new_food = Food(
                    food_id=food_id,
                    name=food_name,
                    calories=round(calories, 2),
                    protein=round(protein, 2),
                    fats=round(fats, 2),
                    carbs=round(carbs, 2),
                    description=food_description
                )
                db.session.add(new_food)
                db.session.commit()

        # Формируем результат
        result.append({
            'name': food_name,
            'food_id': food_id,
            'calorie': existing_food.calories if existing_food else round(calories, 2),
            'carbs': existing_food.carbs if existing_food else round(carbs, 2),
            'fats': existing_food.fats if existing_food else round(fats, 2),
            'protein': existing_food.protein if existing_food else round(protein, 2)
        })

    return jsonify(result), 200

# Get Food Item by ID
@app.route('/fooddb/get_item', methods=['GET'])
def get_food_item():
    food_id = request.args.get('food_id')
    food = Food.query.get(food_id)
    if food:
        return jsonify({
            'name': food.name,
            'food_id': food.food_id,
            'calorie': food.calories,
            'carbs': food.carbs,
            'fats': food.fats,
            'protein': food.protein
        })
    abort(404)
    
# Add Meal
@app.route('/meals/add_meal', methods=['POST'])
def add_meal():
    data = request.json
    user_id = data.get('user_id')
    user = User.query.get(user_id)
    if user:
        food_id = data.get('food_id')
        food = Food.query.get(food_id)
        quantity = data.get('quantity')
        calories = food.calories * quantity / 100
        protein = food.protein * quantity / 100
        carbs = food.carbs * quantity / 100
        fats = food.fats * quantity / 100
        date = data.get('date')
        part_of_the_day = data.get('part_of_the_day')
        new_meal = Meal(user_id=user_id, food_id=food_id,
                        quantity=quantity, date=date,
                        part_of_the_day=part_of_the_day,
                        calories=calories, protein=protein,
                        carbs=carbs, fats=fats)
        db.session.add(new_meal)
        db.session.commit()
        return jsonify({"status": "Meal added successfully"}), 200
    else:
        abort(404)

# Get Meals by User, Date, and Part of Day
@app.route('/meals/meals', methods=['GET'])
def get_meals():
    user_id = request.args.get('user_id')
    date = request.args.get('date')
    meals = Meal.query.filter(Meal.user_id == user_id, Meal.date == date).all()
    if meals:
        result = [
            {
                'meal_id': meal.meal_id,
                'food_id': meal.food_id,
                'quantity': meal.quantity,
                'calories': meal.calories,
                'protein': meal.protein,
                'fats': meal.fats,
                'carbs': meal.carbs,
                'part_of_the_day': meal.part_of_the_day
            } for meal in meals
        ]
        return jsonify(result)
    abort(404)

# Edit Meal Quantity
@app.route('/meals/edit_meal', methods=['PATCH'])
def edit_meal():
    data = request.json
    meal = Meal.query.get(data['meal_id'])
    if meal:
        meal.quantity = data['quantity']
        db.session.commit()
        return jsonify({"status": "Meal updated successfully"}), 200
    abort(404)

# Delete Meal
@app.route('/meals/delete_meal', methods=['DELETE'])
def delete_meal():
    data = request.json
    meal = Meal.query.get(data['meal_id'])
    if meal:
        db.session.delete(meal)
        db.session.commit()
        return jsonify({"status": "Meal deleted successfully"}), 200
    abort(404)

# Change profile picture in base64
@app.route('/user/change_image', methods=['PATCH'])
def edit_image():
    data = request.json
    user = User.query.get(data['user_id'])
    if user:
        user.profile_pic = data['profile_pic']
        db.session.commit()
        return jsonify({"status": "Profile picture updated successfully"}), 200
    abort(404)
    
@app.route('/user/caloriegoal', methods=['PATCH'])
def edit_calorieGoal():
    data = request.json
    user = User.query.get(data['user_id'])
    if user:
        user.calorieGoal = data['caloriegoal']
        db.session.commit()
        return jsonify({"status": "calorieGoal updated successfully"}), 200
    abort(404)
    
@app.route('/meals/get_meal', methods=['GET'])
def get_meal():
    meal_id = request.args.get('meal_id')
    if not meal_id:
        return jsonify({"error": "meal_id parameter is required"}), 400

    meal = Meal.query.get(meal_id)
    if meal:
        return jsonify({
            'meal_id': meal.meal_id,
            'user_id': meal.user_id,
            'food_id': meal.food_id,
            'quantity': meal.quantity,
            'calories': meal.calories,
            'protein': meal.protein,
            'fats': meal.fats,
            'carbs': meal.carbs,
            'date': meal.date,
            'part_of_the_day': meal.part_of_the_day
        }), 200
    else:
        return jsonify({"error": "Meal not found"}), 404


if __name__ == '__main__':
    app.run(host='10.8.0.2',port=5000,debug="true")