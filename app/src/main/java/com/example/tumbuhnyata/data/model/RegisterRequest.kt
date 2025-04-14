package com.example.tumbuhnyata.data.model

data class RegisterRequest(
    val companyName: String,
    val email: String,
    val password: String,
    val phoneNumber: String,
    val NIB: String,
    val address: String
) 