package com.example.tumbuhnyata.data.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CertificationApi {

    @POST("certifications/apply")
    suspend fun submitCertification(
        @Body body: Map<String, Any?>
    ): Response<Any>
}
