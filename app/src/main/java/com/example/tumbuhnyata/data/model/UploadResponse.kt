package com.example.tumbuhnyata.data.model

/**
 * Response model for file upload API.
 * Represents the response from the /carbon-submissions/upload-csv endpoint.
 */
data class UploadResponse(
    val success: Boolean,
    val message: String,
    val submissionId: String? = null, // Optional submission ID if upload creates a new submission
    val processedRecords: Int? = null, // Number of records processed from the CSV
    val errors: List<String>? = null // Any validation errors from the CSV processing
) 