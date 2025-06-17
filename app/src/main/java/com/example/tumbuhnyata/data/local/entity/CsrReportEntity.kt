package com.example.tumbuhnyata.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a CSR Report item in the local Room database.
 * This entity is used for caching data fetched from the API and managing offline sync.
 */
@Entity(tableName = "csr_reports") // Nama tabel bisa disesuaikan jika perlu
data class CsrReportEntity(
    @PrimaryKey(autoGenerate = true) // Enable auto-increment for offline data
    val id: Int = 0, // Local auto-increment ID (always unique)

    val companyId: Int,
    val year: Int,
    val month: Int?,
    val carbonValue: Float?,
    val documentType: String,
    val documentName: String?,
    val documentPath: String,
    val analysis: String?,
    val createdAt: String, // Menyimpan timestamp sebagai String (ISO 8601) atau Long jika preferensi
    
    // Sync management fields
    val isSynced: Boolean = false, // true = data sudah sync dengan server, false = perlu sync
    val isLocalOnly: Boolean = false, // true = data dibuat offline, false = data dari server
    val localId: String? = null, // UUID untuk data offline sebelum mendapat ID dari server
    val serverId: Int? = null, // ID dari server setelah sync (for reference, not primary key)
    val lastModified: String = createdAt, // Timestamp terakhir dimodifikasi
    val syncRetryCount: Int = 0 // Counter untuk retry sync jika gagal
) 