package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.ProfileApi
import com.example.tumbuhnyata.data.model.Profile

class ProfileRepository(private val api: ProfileApi) {
    
    suspend fun getUserProfile(): Profile? {
        val response = api.getUserProfile()
        return if (response.isSuccessful) {
            response.body()?.data
        } else {
            null
        }
    }
    
    suspend fun updateProfile(
        companyName: String,
        email: String,
        phoneNumber: String,
        address: String
    ): Boolean {
        val updateData = mapOf(
            "companyName" to companyName,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "address" to address
        )
        
        val response = api.updateProfile(updateData)
        return response.isSuccessful
    }
    
    suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Boolean {
        val passwordData = mapOf(
            "currentPassword" to currentPassword,
            "newPassword" to newPassword
        )
        
        val response = api.changePassword(passwordData)
        return response.isSuccessful
    }
}