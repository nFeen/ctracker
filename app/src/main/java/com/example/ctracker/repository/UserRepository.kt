package com.example.ctracker.repository

import com.example.ctracker.entity.User

interface UserRepository {
    fun getUserById(id: Int): User?
    fun addUser(user: User)
    fun authenticateUser(login: String, password: String): User?
}