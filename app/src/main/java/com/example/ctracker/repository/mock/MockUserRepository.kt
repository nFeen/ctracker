package com.example.ctracker.repository.mock

import com.example.ctracker.entity.User

object MockUserRepository {
    private val users = mutableListOf(
        User(id = 1, login = "testuser1", mealsId = listOf(1, 2, 3), password = "testuser1", currentCalorie = 3000, weight = 75)
    )

    fun authenticateUser(login: String, password: String): User? {
        return users.find { it.login == login && password == it.password }
    }

    fun getUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    fun addUser(login: String, password: String): Boolean {
        if (users.find { it.login == login } == null) {
            val newUserId = (users.maxOfOrNull { user -> user.id } ?: 0) + 1
            users.add(User(id = newUserId, login = login, password = password, mealsId = listOf()))
            return true
        }
        return false
    }

    private fun updateUser(updatedUser: User) {
        val index = users.indexOfFirst { it.id == updatedUser.id }
        if (index != -1) {
            users[index] = updatedUser
        }
    }

}

