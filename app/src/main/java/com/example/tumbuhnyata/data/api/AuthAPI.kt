package com.example.tumbuhnyata.data.api

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call
import com.example.tumbuhnyata.data.model.LoginRequest
import com.example.tumbuhnyata.data.model.LoginResponse
import com.example.tumbuhnyata.data.model.RegisterRequest
import com.example.tumbuhnyata.data.model.RegisterResponse

interface AuthApi {
    @POST("auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>
    
    @POST("auth/register")
    fun register(@Body request: RegisterRequest): Call<RegisterResponse>
}