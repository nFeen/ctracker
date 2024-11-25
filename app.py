from flask import Flask, request, jsonify, abort
from flask_sqlalchemy import SQLAlchemy
from flask_bcrypt import Bcrypt
import os

app = Flask(__name__)
basedir = os.path.abspath(os.path.dirname(__file__))
app.config['SQLALCHEMY_DATABASE_URI'] =\
        'sqlite:///' + os.path.join(basedir, 'database.db')
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False
db = SQLAlchemy(app)
bcrypt = Bcrypt(app)
with open('avatar_standart.txt') as file:
    base64 = file.read()
    
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
    return jsonify({"status": "User registered successfully"}), 200

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

# List Foods
@app.route('/fooddb/search', methods=['GET'])
def food_list():
    food_name = request.args.get('food')
    foods = Food.query.filter(Food.name.contains(food_name))
    if foods:
        result = [
            {
                'name': food.name,
                'food_id': food.food_id,
                'calorie': food.calories,
                'carbs': food.carbs,
                'fats': food.fats,
                'protein': food.protein
            } for food in foods
        ]
        return jsonify(result)
    else:
        abort(404)

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