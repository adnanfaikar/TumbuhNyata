package com.example.tumbuhnyata.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the overall API response when fetching CSR reports.
 * Assumes the API returns a list of reports战争pped in a common response structure.
 */
data class CsrReportApiResponse(
    @SerializedName("data") // Adjust if your API uses a different key for the list of reports
    val data: List<CsrReportModel>?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("status_code") // Or "status", "success", etc., depending on your API
    val statusCode: Int? // Or String, Boolean, etc.
)

/**
 * Represents a single CSR Report item, mirroring the database schema provided.
 * This class is used for parsing the JSON response from the API.
 */
data class CsrReportModel(
    @SerializedName("id")
    val id: Int,

    @SerializedName("company_id")
    val companyId: Int,

    @SerializedName("year")
    val year: Int,

    @SerializedName("month")
    val month: Int?, // Allowed to be NULL in DB

    @SerializedName("carbon_value")
    val carbonValue: Float?, // Allowed to be NULL in DB

    @SerializedName("document_type")
    val documentType: String, // ENUM from DB, represented as String

    @SerializedName("document_name")
    val documentName: String?, // Allowed to be NULL in DB

    @SerializedName("document_path")
    val documentPath: String,

    @SerializedName("analysis")
    val analysis: String?, // Allowed to be NULL in DB

    @SerializedName("created_at")
    val createdAt: String // TIMESTAMP from DB, typically represented as ISO 8601 String
)

/**
 * Response wrapper for CSR submissions endpoint with pagination
 * Matches the actual backend response format:
 * {"success": true, "data": [...], "pagination": {...}}
 */
data class CsrSubmissionsResponse(
    val success: Boolean,
    val data: List<CsrReportModel>,
    val pagination: PaginationInfo
)

/**
 * Pagination information from submissions endpoint
 */
data class PaginationInfo(
    val currentPage: Int,
    val totalPages: Int,
    val totalItems: Int,
    val itemsPerPage: Int
) 