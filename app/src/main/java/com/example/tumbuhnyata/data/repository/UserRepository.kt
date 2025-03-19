package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.model.User
import kotlinx.coroutines.delay

class UserRepository {
    suspend fun getUsers(): List<User> {
        delay(2000) // Simulasi network delay
        return listOf(
            User(1, "Alice"),
            User(2, "Bob"),
            User(3, "Charlie")
        )
    }
}