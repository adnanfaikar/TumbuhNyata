package com.example.tumbuhnyata.data.mapper

import com.example.tumbuhnyata.data.local.entity.CertificationEntity
import com.example.tumbuhnyata.data.model.CreateCertificationRequest
import com.example.tumbuhnyata.data.model.CertificationData
import com.example.tumbuhnyata.data.model.SupportingDocument
import java.text.SimpleDateFormat
import java.util.*

/**
 * Converts CertificationEntity to CreateCertificationRequest for API
 * Used when syncing offline-created certification applications to server
 */
fun CertificationEntity.toCreateRequest(): CreateCertificationRequest {
    // Parse supporting documents from pipe-separated format: "url|name,url|name"
    val supportingDocsList = try {
        if (this.supportingDocuments.isBlank()) {
            emptyList()
        } else {
            this.supportingDocuments.split(",").mapNotNull { entry ->
                val parts = entry.trim().split("|")
                if (parts.size == 2) {
                    SupportingDocument(url = parts[0], name = parts[1])
                } else {
                    null // Ignore malformed entries
                }
            }
        }
    } catch (e: Exception) {
        emptyList() // Fallback to empty list on error
    }

    return CreateCertificationRequest(
        name = this.name,
        description = this.description,
        credential_body = this.credentialBody,
        benefits = this.benefits,
        cost = this.cost,
        supporting_documents = supportingDocsList,
        user_id = 1 // EXPLICITLY set user_id = 1 (backend requirement)
    )
}

/**
 * Converts API CertificationData to CertificationEntity for local storage
 * Used when caching server data locally
 * IMPORTANT: Sets sync flags correctly for server data
 */
fun CertificationData.toEntity(): CertificationEntity {
    return CertificationEntity(
        id = 0, // Let Room auto-generate local ID
        name = this.name,
        description = this.description,
        credentialBody = this.credential_body,
        benefits = this.benefits,
        cost = this.cost.toFloat(),
        status = this.status,
        submissionDate = this.submission_date,
        supportingDocuments = this.supporting_documents,
        isSynced = true, // Server data is by definition synced
        isLocalOnly = false, // Came from server, not offline-created
        serverId = this.id, // Store server ID for reference
        lastModified = this.submission_date,
        syncRetryCount = 0
    )
}

/**
 * Creates a new CertificationEntity for offline certification application
 * with sync fields properly set for offline-first architecture
 * 
 * Flag meanings:
 * - isLocalOnly = true: Application created offline, needs initial sync to server
 * - isLocalOnly = false: Data from server that may have local modifications
 * - isSynced = false: Needs to be synced to server
 * - isSynced = true: Already synced with server
 */
fun createOfflineCertificationEntity(
    name: String,
    description: String,
    credentialBody: String,
    benefits: String,
    cost: Float,
    supportingDocuments: List<String>
): CertificationEntity {
    val currentTime = System.currentTimeMillis()
    val timestamp = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).format(Date(currentTime))
    
    // Convert supporting documents list to pipe-separated format that matches ViewModel output
    val documentsJson = supportingDocuments.joinToString(",") // Keep comma-separated as it comes from ViewModel
    
    return CertificationEntity(
        id = 0, // Use 0 for auto-increment
        name = name,
        description = description,
        credentialBody = credentialBody,
        benefits = benefits,
        cost = cost,
        status = "submitted",
        submissionDate = timestamp,
        supportingDocuments = documentsJson,
        isSynced = false, // Not synced yet - needs to go to server
        isLocalOnly = true, // Created offline - use apply endpoint
        localId = UUID.randomUUID().toString(), // Unique local identifier for tracking
        lastModified = timestamp,
        syncRetryCount = 0 // No retry attempts yet
    )
} 