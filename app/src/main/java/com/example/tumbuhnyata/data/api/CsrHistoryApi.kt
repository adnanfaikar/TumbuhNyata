package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.CsrHistoryItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CsrHistoryApi {
    @GET("csr/history/")
    suspend fun getCsrHistory(@Query("user_id") userId: Int): Response<List<CsrHistoryItem>>

    @GET("csr/history/{id}")
    suspend fun getCsrHistoryDetail(
        @Path("id") id: Int,
        @Query("user_id") userId: Int
    ): Response<CsrHistoryItem>
} 