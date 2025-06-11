package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.RegisterWorkshop
import com.example.tumbuhnyata.data.model.WorkshopHistoryResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WorkshopApiService {
    @POST("workshops/register")
    suspend fun registerWorkshop(@Body request: RegisterWorkshop): Response<Any>

    @GET("workshops/history")
    suspend fun getWorkshopHistory(@Query("email") email: String): Response<List<WorkshopHistoryResponse>>

    @DELETE("workshops/{workshopId}")
    suspend fun deleteWorkshopRegistration(@Path("workshopId") workshopId: String): Response<Any>
}