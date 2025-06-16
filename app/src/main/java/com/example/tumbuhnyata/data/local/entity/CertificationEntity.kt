package com.example.tumbuhnyata.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a Certification submission in the local Room database.
 * This entity is used for caching data fetched from the API and managing offline sync.
 */
@Entity(tableName = "certifications")
data class CertificationEntity(
    @PrimaryKey(autoGenerate = true) // Enable auto-increment for offline data
    val id: Int = 0, // Local auto-increment ID (always unique)

    val name: String,
    val description: String,
    val credentialBody: String,
    val benefits: String,
    val cost: Float,
    val status: String = "submitted", // submitted, in_review, approved, rejected
    val submissionDate: String, // ISO 8601 timestamp
    val supportingDocuments: String, // JSON string of document list
    
    // Sync management fields
    val isSynced: Boolean = false, // true = data sudah sync dengan server, false = perlu sync
    val isLocalOnly: Boolean = false, // true = data dibuat offline, false = data dari server
    val localId: String? = null, // UUID untuk data offline sebelum mendapat ID dari server
    val serverId: Int? = null, // ID dari server setelah sync (for reference, not primary key)
    val lastModified: String = submissionDate, // Timestamp terakhir dimodifikasi
    val syncRetryCount: Int = 0 // Counter untuk retry sync jika gagal
) 