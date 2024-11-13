package com.example.ctracker.repository.mock

import com.example.ctracker.entity.User
import com.example.ctracker.repository.UserRepository

class MockUserRepository : UserRepository {
    private val users = mutableListOf(
        User(id = 1, login = "1", mealsId = listOf(1, 2, 3))
    )

    // Метод для аутентификации по логину и паролю
    override fun authenticateUser(login: String, password: String): User? {
        // Предполагаем, что пароль для всех пользователей "password123" для упрощения
        val validPassword = "1"
        return users.find { it.login == login && password == validPassword }
    }

    override fun getUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    override fun addUser(user: User) {
        users.add(user)
    }
}
