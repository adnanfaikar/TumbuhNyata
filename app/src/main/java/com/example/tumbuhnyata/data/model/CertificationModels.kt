package com.example.tumbuhnyata.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data class for a single supporting document
 */
data class SupportingDocument(
    @SerializedName("url") val url: String,
    @SerializedName("name") val name: String
)

/**
 * Request model for creating certification application
 * Maps to POST /certifications/apply endpoint
 */
data class CreateCertificationRequest(
    val name: String,
    val description: String,
    val credential_body: String,
    val benefits: String,
    val cost: Float,
    val supporting_documents: List<SupportingDocument>, // Use the data class here
    val user_id: Int = 1 // Hardcode user_id = 1 temporarily (backend requires this)
)

/**
 * Response model for certification creation
 * Expected from POST /certifications/apply endpoint
 */
data class CreateCertificationResponse(
    val message: String,
    val id: Int
)

/**
 * Response model for fetching user certifications
 * Expected from GET /certifications endpoint
 */
data class CertificationListResponse(
    val data: List<CertificationData>
)

/**
 * Response model for fetching single certification detail
 * Expected from GET /certifications/{id} endpoint
 */
data class CertificationDetailResponse(
    val data: CertificationData
)

/**
 * Request model for updating certification status (admin only)
 * Maps to PUT /certifications/{id}/status endpoint
 */
data class UpdateCertificationStatusRequest(
    val status: String // submitted, in_review, approved, rejected
)

/**
 * Response model for status update
 * Expected from PUT /certifications/{id}/status endpoint
 */
data class UpdateStatusResponse(
    val message: String
)

/**
 * Data model for certification in API responses
 */
data class CertificationData(
    val id: Int,
    val user_id: Int,
    val name: String,
    val description: String,
    val credential_body: String,
    val benefits: String,
    val cost: Float,
    val status: String, // submitted, in_review, approved, rejected
    val submission_date: String, // ISO 8601 format
    val supporting_documents: String // JSON string from server
) 