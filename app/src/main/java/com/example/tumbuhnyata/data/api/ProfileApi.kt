package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Body

interface ProfileApi {
    @GET("profile/me")
    suspend fun getUserProfile(): Response<ProfileResponse>
    
    @PATCH("profile/update")
    suspend fun updateProfile(
        @Body updateData: Map<String, String>
    ): Response<Map<String, String>>
    
    @PATCH("profile/change-password")
    suspend fun changePassword(
        @Body passwordData: Map<String, String>
    ): Response<Map<String, String>>
}