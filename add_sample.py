from flask import Flask
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

# Define models
class User(db.Model):
    __tablename__ = 'Users'
    user_id = db.Column(db.Integer, primary_key=True)
    login = db.Column(db.String(80), unique=True, nullable=False)
    password_hash = db.Column(db.String(128), nullable=False)
    weight = db.Column(db.Float, default=70)
    height = db.Column(db.Integer, default=175)
    calorieGoal = db.Column(db.Integer, default=2000)
    profile_pic = db.Column(db.Text, default=base64)

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

# Initialize database and add test data
with app.app_context():
    # Drop all tables and recreate them for fresh data
    db.drop_all()
    db.create_all()

    # 1. Add Users
    users = [
        User(
            login="user1",
            password_hash=bcrypt.generate_password_hash("Password1").decode('utf-8'),
            weight=70.0,
            height=175,
            calorieGoal=2200,
            profile_pic=base64
        ),
        User(
            login="user2",
            password_hash=bcrypt.generate_password_hash("password2").decode('utf-8'),
            weight=80.0,
            height=180,
            calorieGoal=2500,
            profile_pic=base64
        ),
        User(
            login="user3",
            password_hash=bcrypt.generate_password_hash("password3").decode('utf-8'),
            weight=60.0,
            height=165,
            calorieGoal=2000,
            profile_pic=base64
        ),
        User(
            login="user4",
            password_hash=bcrypt.generate_password_hash("password4").decode('utf-8'),
            weight=90.0,
            height=185,
            calorieGoal=2800,
            profile_pic=base64
        ),
        User(
            login="user5",
            password_hash=bcrypt.generate_password_hash("password5").decode('utf-8'),
            weight=75.0,
            height=170,
            calorieGoal=2300,
            profile_pic=base64
        )
    ]

    # Add users to the session
    db.session.add_all(users)
    db.session.commit()


    # Add foods to the session
    db.session.add_all(foods)
    db.session.commit()

    # 3. Add Meals
    meals = [
        Meal(user_id=1, date='2024-11-15', part_of_the_day=0, food_id=1, quantity=100, calories=165, protein=31, carbs=0, fats=3.6),
        Meal(user_id=1, date='2024-11-15', part_of_the_day=1, food_id=2, quantity=100, calories=215, protein=5, carbs=45, fats=1.8),
        Meal(user_id=2, date='2024-11-15', part_of_the_day=2, food_id=3, quantity=200, calories=190, protein=1, carbs=50, fats=0.6),
        Meal(user_id=3, date='2024-11-15', part_of_the_day=3, food_id=4, quantity=100, calories=55, protein=3.7, carbs=11, fats=0.6),
        Meal(user_id=4, date='2024-11-16', part_of_the_day=0, food_id=5, quantity=300, calories=330, protein=21, carbs=3, fats=27)
    ]

    # Add meals to the session
    db.session.add_all(meals)
    db.session.commit()

    print("Database initialized with test data.")
