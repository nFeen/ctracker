package com.example.ctracker.repository

import com.example.ctracker.entity.User

interface UserRepository {
    fun getUserById(id: Int): User?
    fun addUser(login: String, password: String) : Boolean
    fun authenticateUser(login: String, password: String): User?
}