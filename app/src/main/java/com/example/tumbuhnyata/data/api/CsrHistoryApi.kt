package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.CsrHistoryItem
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
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

    @DELETE("csr/history/{id}")
    suspend fun deleteCsrHistory(
        @Path("id") id: Int,
        @Query("user_id") userId: Int
    ): Response<Unit>

    @POST("csr/ajukan")
    suspend fun addCsrHistory(@Body request: AddCsrRequest): Response<AddCsrResponse>
}

data class AddCsrRequest(
    val user_id: Int,
    val program_name: String,
    val category: String,
    val description: String,
    val location: String,
    val partner_name: String,
    val start_date: String,
    val end_date: String,
    val budget: Long,
    val status: String,
    val agreed: Boolean
)

data class AddCsrResponse(
    val message: String,
    val data: CsrHistoryItem? = null
) 