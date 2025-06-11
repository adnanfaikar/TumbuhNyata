package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.RegisterWorkshop
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface WorkshopApiService {
    @POST("workshops/register")
    suspend fun registerWorkshop(@Body request: RegisterWorkshop): Response<Any>

    @DELETE("workshops/{workshopId}")
    suspend fun deleteWorkshopRegistration(@Path("workshopId") workshopId: String): Response<Any>
}