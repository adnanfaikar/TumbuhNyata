package com.example.tumbuhnyata.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tumbuhnyata.data.local.entity.CertificationEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data class for certification sync status debugging
 */
data class CertificationSyncStatus(
    val isSynced: Boolean,
    val isLocalOnly: Boolean,
    val count: Int
)

/**
 * Data class for debugging individual certification record sync status
 */
data class CertificationDebugInfo(
    val id: Int,
    val localId: String?,
    val serverId: Int?,
    val isSynced: Boolean,
    val isLocalOnly: Boolean,
    val name: String,
    val status: String,
    val syncRetryCount: Int
)

/**
 * Data Access Object (DAO) for Certifications.
 * Defines methods for interacting with the 'certifications' table in the Room database.
 */
@Dao
interface CertificationDao {

    /**
     * Inserts a list of CertificationEntity into the database.
     * If a certification with the same ID already exists, it will be replaced.
     * @param certifications The list of CertificationEntity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateCertifications(certifications: List<CertificationEntity>)

    /**
     * Fetches all CertificationEntity from the 'certifications' table.
     * Returns a Flow, so an observer will be notified of any data changes.
     * @return A Flow emitting a list of all CertificationEntity.
     */
    @Query("SELECT * FROM certifications ORDER BY submissionDate DESC") // Order by latest submission
    fun getAllCertifications(): Flow<List<CertificationEntity>>

    /**
     * Deletes all certifications from the 'certifications' table.
     */
    @Query("DELETE FROM certifications")
    suspend fun clearAllCertifications()
    
    /**
     * Deletes only synced certifications (isSynced = true), preserves unsynced offline data
     */
    @Query("DELETE FROM certifications WHERE isSynced = 1")
    suspend fun clearSyncedCertifications()
    
    /**
     * Gets all certifications that need to be synced to server (isSynced = false)
     */
    @Query("SELECT * FROM certifications WHERE isSynced = 0 ORDER BY submissionDate ASC")
    suspend fun getUnsyncedCertifications(): List<CertificationEntity>
    
    /**
     * Updates sync status of a certification
     */
    @Query("UPDATE certifications SET isSynced = :isSynced, syncRetryCount = :retryCount WHERE id = :certificationId")
    suspend fun updateSyncStatus(certificationId: Int, isSynced: Boolean, retryCount: Int = 0)
    
    /**
     * Updates sync status by localId (for offline-created data)
     * Stores server ID in separate field without changing primary key
     */
    @Query("UPDATE certifications SET isSynced = :isSynced, serverId = :serverId, syncRetryCount = 0 WHERE localId = :localId")
    suspend fun updateSyncStatusByLocalId(localId: String, isSynced: Boolean, serverId: Int)
    
    /**
     * Updates sync status by localId without changing primary key (safer approach)
     * Marks the record as synced and stores the server ID reference
     */
    @Query("UPDATE certifications SET isSynced = :isSynced, syncRetryCount = 0 WHERE localId = :localId")
    suspend fun markSyncedByLocalId(localId: String, isSynced: Boolean)
    
    /**
     * Gets certifications by status
     */
    @Query("SELECT * FROM certifications WHERE status = :status ORDER BY submissionDate DESC")
    fun getCertificationsByStatus(status: String): Flow<List<CertificationEntity>>

    /**
     * Gets a single certification by ID
     */
    @Query("SELECT * FROM certifications WHERE id = :certificationId")
    fun getCertificationById(certificationId: Int): Flow<CertificationEntity?>
    
    /**
     * Gets a single certification by serverId (useful when navigating from server response)
     */
    @Query("SELECT * FROM certifications WHERE serverId = :serverId")
    fun getCertificationByServerId(serverId: Int): Flow<CertificationEntity?>
    
    /**
     * Inserts a single certification (for offline-created data)
     * Using ABORT strategy to ensure each submission creates a new record
     * Will throw exception if there's a primary key conflict (which should not happen with auto-increment)
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertCertification(certification: CertificationEntity): Long
    
    // ===== DEBUG QUERIES FOR SYNC STATUS =====
    
    /**
     * Debug: Count total certifications in database
     */
    @Query("SELECT COUNT(*) FROM certifications")
    suspend fun getTotalCertificationCount(): Int
    
    /**
     * Debug: Count synced certifications (isSynced = true)
     */
    @Query("SELECT COUNT(*) FROM certifications WHERE isSynced = 1")
    suspend fun getSyncedCertificationCount(): Int
    
    /**
     * Debug: Count unsynced certifications (isSynced = false)
     */
    @Query("SELECT COUNT(*) FROM certifications WHERE isSynced = 0")
    suspend fun getUnsyncedCertificationCount(): Int
    
    /**
     * Debug: Get sync status summary
     */
    @Query("SELECT isSynced, isLocalOnly, COUNT(*) as count FROM certifications GROUP BY isSynced, isLocalOnly")
    suspend fun getCertificationSyncStatusSummary(): List<CertificationSyncStatus>
    
    // ===== DEBUGGING METHODS FOR SYNC VERIFICATION =====
    
    /**
     * Debug: Find certification by localId
     */
    @Query("SELECT * FROM certifications WHERE localId = :localId")
    suspend fun getCertificationByLocalId(localId: String): CertificationEntity?
    
    /**
     * Debug: Get all certifications with their sync fields for debugging
     */
    @Query("SELECT id, localId, serverId, isSynced, isLocalOnly, name, status, syncRetryCount FROM certifications ORDER BY submissionDate DESC")
    suspend fun getAllCertificationsDebugInfo(): List<CertificationDebugInfo>
} 