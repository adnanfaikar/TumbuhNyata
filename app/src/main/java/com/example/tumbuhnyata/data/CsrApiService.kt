package com.example.tumbuhnyata.data

import com.example.tumbuhnyata.ui.eventcsr.CsrSubmissionRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CsrApiService {
    @POST("csr/ajukan ")
    suspend fun submitCSR(@Body request: CsrSubmissionRequest): Response<CsrSubmissionResponse>
}

data class CsrSubmissionResponse(
    val success: Boolean,
    val message: String?
)