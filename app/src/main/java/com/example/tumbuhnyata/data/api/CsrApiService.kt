package com.example.tumbuhnyata.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CsrApiService {
    @POST("csr/ajukan")
    suspend fun submitCSR(@Body request: CsrSubmissionRequest): Response<CsrSubmissionResponse>
}

data class CsrSubmissionResponse(
    val message: String,
    val data: CsrSubmissionData? = null
)

data class CsrSubmissionData(
    val id: Int,
    val user_id: Int,
    val program_name: String,
    val category: String,
    val description: String,
    val location: String,
    val partner_name: String,
    val start_date: String,
    val end_date: String,
    val budget: String,
    val proposal_url: String?,
    val legality_url: String?,
    val agreed: Boolean,
    val status: String,
    val created_at: String
)

data class CsrSubmissionRequest(
    val user_id: Int,
    val program_name: String,
    val category: String,
    val description: String,
    val location: String,
    val partner_name: String,
    val start_date: String,
    val end_date: String,
    val budget: String,
    val agreed: Boolean
)