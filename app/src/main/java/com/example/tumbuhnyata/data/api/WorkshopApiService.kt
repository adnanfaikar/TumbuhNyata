package com.example.tumbuhnyata.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WorkshopApiService {
    @POST("workshops/register")
    suspend fun registerWorkshop(@Body body: Map<String, String>): Response<Any>
}