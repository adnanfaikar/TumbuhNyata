package com.example.tumbuhnyata.data.model

/**
 * Request model for creating individual submissions
 * Maps to POST /carbon-submissions endpoint
 */
data class CreateSubmissionRequest(
    val company_id: String,
    val year: Int,
    val month: Int?,
    val carbon_value: Float,
    val document_type: String,
    val document_name: String,
    val document_path: String?,
    val analysis: String?
)

/**
 * Request model for updating individual submissions  
 * Maps to PUT /carbon-submissions/{id} endpoint
 */
data class UpdateSubmissionRequest(
    val company_id: String,
    val year: Int,
    val month: Int?,
    val carbon_value: Float,
    val document_type: String,
    val document_name: String,
    val document_path: String?,
    val analysis: String?
)

/**
 * Response model for submission creation
 * Expected from POST /carbon-submissions endpoint
 */
data class CreateSubmissionResponse(
    val success: Boolean,
    val message: String,
    val data: SubmissionData?
)

/**
 * Response model for submission updates
 * Expected from PUT /carbon-submissions/{id} endpoint
 */
data class UpdateSubmissionResponse(
    val success: Boolean,
    val message: String,
    val data: SubmissionData?
)

/**
 * Response model for submission deletion
 * Expected from DELETE /carbon-submissions/{id} endpoint
 */
data class DeleteSubmissionResponse(
    val success: Boolean,
    val message: String
)

/**
 * Data model for submission in API responses
 */
data class SubmissionData(
    val id: Int,
    val company_id: String,
    val year: Int,
    val month: Int?,
    val carbon_value: Float,
    val document_type: String,
    val document_name: String,
    val document_path: String?,
    val analysis: String?,
    val created_at: String
) 