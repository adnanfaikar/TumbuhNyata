package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.ProfileResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.PUT

interface ProfileApi {
    @GET("profile/me")
    suspend fun getUserProfile(): Response<ProfileResponse>

    @PUT("profile/update")
    suspend fun updateProfile(
        @Body updateData: Map<String, String>
    ): Response<Map<String, String>>

    @PUT("profile/change-password")
    suspend fun changePassword(
        @Body passwordData: Map<String, String>
    ): Response<Map<String, String>>
}