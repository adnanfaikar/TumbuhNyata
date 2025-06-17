package com.example.tumbuhnyata.data.mapper

import com.example.tumbuhnyata.data.local.entity.CsrReportEntity
import com.example.tumbuhnyata.data.model.CreateSubmissionRequest
import com.example.tumbuhnyata.data.model.UpdateSubmissionRequest
import java.util.UUID

/**
 * Converts CsrReportEntity to CreateSubmissionRequest for API
 * Used when syncing offline-created data to server
 * FIXED: Updated field names to match backend API expectations
 */
fun CsrReportEntity.toCreateRequest(): CreateSubmissionRequest {
    return CreateSubmissionRequest(
        company_id = this.companyId.toString(),
        year = this.year,
        month = this.month,
        carbon_value = this.carbonValue ?: 0f,
        document_type = this.documentType ?: "data_emisi",
        document_name = this.documentName ?: "Unknown Document",
        document_path = this.documentPath,
        analysis = this.analysis
    )
}

/**
 * Converts CsrReportEntity to UpdateSubmissionRequest for API
 * Used when syncing modified data to server
 * FIXED: Updated field names to match backend API expectations
 */
fun CsrReportEntity.toUpdateRequest(): UpdateSubmissionRequest {
    return UpdateSubmissionRequest(
        company_id = this.companyId.toString(),
        year = this.year,
        month = this.month,
        carbon_value = this.carbonValue ?: 0f,
        document_type = this.documentType ?: "data_emisi",
        document_name = this.documentName ?: "Unknown Document",
        document_path = this.documentPath,
        analysis = this.analysis
    )
}

/**
 * Creates a new CsrReportEntity for offline data entry
 * with sync fields properly set for offline-first architecture
 * 
 * Flag meanings:
 * - isLocalOnly = true: Data created offline, needs initial sync to server
 * - isLocalOnly = false: Data from server that may have local modifications
 * - isSynced = false: Needs to be synced to server
 * - isSynced = true: Already synced with server
 */
fun createOfflineEntity(
    companyId: Int,
    year: Int,
    month: Int?,
    carbonValue: Float?,
    documentType: String,
    documentName: String?,
    documentPath: String,
    analysis: String?
): CsrReportEntity {
    val timestamp = System.currentTimeMillis().toString()
    return CsrReportEntity(
        id = 0, // Use 0 for auto-increment, not -1 (prevents primary key conflicts)
        companyId = companyId,
        year = year,
        month = month,
        carbonValue = carbonValue,
        documentType = documentType,
        documentName = documentName,
        documentPath = documentPath,
        analysis = analysis,
        createdAt = timestamp,
        isSynced = false, // Not synced yet - needs to go to server
        isLocalOnly = true, // Created offline - use createSubmission endpoint
        localId = UUID.randomUUID().toString(), // Unique local identifier for tracking
        lastModified = timestamp,
        syncRetryCount = 0 // No retry attempts yet
    )
} 