from flask import Flask, jsonify, request, abort

app = Flask(__name__)

@app.route('/user/login')
def login():
    login = request.args.get('login')
    password = request.args.get('password')
    if login == 'ASD' and password == 'asdf':
        return jsonify({'user_id': 1})
    else:
        abort(401)

@app.route('/user/register', methods=['POST'])
def register():
    login = request.args.get('login')
    password = request.args.get('password')
    if login == 'ASD' and password == 'asdf':
        abort(409)
    return ''

@app.route('/user/getprofile')
def getprofile():
    user_id = request.args.get('user_id')
    if user_id == '1':
        return jsonify({'weight': 200, 'login': 'anton lox', 'calorieGoal': 5000})
    else:
        abort(401)

@app.route('/user/weight', methods=['PATCH'])
def weight():
    user_id = request.args.get('user_id')
    if user_id == '1':
        return ''
    else:
        abort(401)

@app.route('/fooddb/foodlist')
def foodlist():
    foods = [{'food': 'cheeseburger', 'food_id': 10, 'calorie': 200,
    'carbs': 20, 'fat': 20, 'protein': 20}, {'food': 'cheeseburger2',
    'food_id': 11, 'calorie': 200, 'carbs': 20, 'fat': 20,
    'protein': 20}]
    return jsonify(foods)

@app.route('/fooddb/get_item')
def get_item():
    return jsonify({'food': 'cheeseburger', 'food_id': 10,
    'calorie': 200, 'carbs': 20, 'fat': 20, 'protein': 20})

@app.route('/meals/add_meal', methods=['POST'])
def add_meal():
    return ''

@app.route('/meals/meal')
def meal():
    return jsonify([{'part_of_day': 'evening', 'meal_id': 10,
    'food_id': 1, 'calorie': 100, 'carbs': 10, 'fat': 20,
    'protein': 20, 'quantity': 2}, {'part_of_day': 'morning', 'meal_id': 5,
    'food_id': 2, 'calorie': 50, 'carbs': 5, 'fat': 10,
    'protein': 15, 'quantity': 1}])

@app.route('/meals/edit_meal', methods=['PATCH'])
def edit_meal():
    return ''

@app.route('/meals/delete_meal', methods=['DELETE'])
def delete_meal():
    return ''