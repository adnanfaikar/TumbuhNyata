package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.CertificationApi
import retrofit2.Response

class CertificationRepository(private val api: CertificationApi) {

    suspend fun submitCertification(
        userId: Int,
        name: String,
        description: String?,
        credentialBody: String?,
        benefits: String?,
        cost: Double?,
        supportingDocuments: List<String>?
    ): Boolean {
        val certificationData: Map<String, Any?> = mapOf(
            "user_id" to userId,
            "name" to name,
            "description" to description,
            "credential_body" to credentialBody,
            "benefits" to benefits,
            "cost" to cost,
            "supporting_documents" to supportingDocuments
        )

        val response: Response<Any> = api.submitCertification(certificationData)
        return response.isSuccessful
    }
}