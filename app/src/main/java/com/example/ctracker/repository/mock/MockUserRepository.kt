package com.example.ctracker.repository.mock

import com.example.ctracker.entity.User
import com.example.ctracker.repository.UserRepository

object MockUserRepository : UserRepository {
    private val users = mutableListOf(
        User(id = 1, login = "1", mealsId = listOf(1, 2, 3), password = "1")
    )

    override fun authenticateUser(login: String, password: String): User? {
        return users.find { it.login == login && password == it.password }
    }

    override fun getUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    override fun addUser(login: String, password: String): Boolean {
        if (users.find { it.login == login } == null) {
            val newUserId = (users.maxOfOrNull { user -> user.id } ?: 0) + 1
            users.add(User(id = newUserId, login = login, password = password, mealsId = listOf()))
            return true
        }
        return false
    }
}
