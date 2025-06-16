package com.example.tumbuhnyata.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tumbuhnyata.data.local.entity.CsrReportEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data class for sync status debugging
 */
data class SyncStatusSummary(
    val isSynced: Boolean,
    val isLocalOnly: Boolean,
    val count: Int
)

/**
 * Data class for debugging individual record sync status
 */
data class DebugRecordInfo(
    val id: Int,
    val localId: String?,
    val serverId: Int?,
    val isSynced: Boolean,
    val isLocalOnly: Boolean,
    val documentName: String?,
    val syncRetryCount: Int
)

/**
 * Data Access Object (DAO) for CSR Reports (or dashboard KPI source data).
 * Defines methods for interacting with the 'csr_reports' table in the Room database.
 */
@Dao
interface DashboardDao {

    /**
     * Inserts a list of CsrReportEntity into the database.
     * If a report with the same ID already exists, it will be replaced.
     * @param reports The list of CsrReportEntity to insert.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateReports(reports: List<CsrReportEntity>)

    /**
     * Fetches all CsrReportEntity from the 'csr_reports' table.
     * Returns a Flow, so an observer will be notified of any data changes.
     * @return A Flow emitting a list of all CsrReportEntity.
     */
    @Query("SELECT * FROM csr_reports ORDER BY createdAt DESC") // Urutkan berdasarkan tanggal terbaru
    fun getAllReports(): Flow<List<CsrReportEntity>>

    /**
     * Deletes all reports from the 'csr_reports' table.
     */
    @Query("DELETE FROM csr_reports")
    suspend fun clearAllReports()
    
    /**
     * Deletes only synced reports (isSynced = true), preserves unsynced offline data
     */
    @Query("DELETE FROM csr_reports WHERE isSynced = 1")
    suspend fun clearSyncedReports()
    
    /**
     * Gets all reports that need to be synced to server (isSynced = false)
     */
    @Query("SELECT * FROM csr_reports WHERE isSynced = 0 ORDER BY createdAt ASC")
    suspend fun getUnsyncedReports(): List<CsrReportEntity>
    
    /**
     * Updates sync status of a report
     */
    @Query("UPDATE csr_reports SET isSynced = :isSynced, syncRetryCount = :retryCount WHERE id = :reportId")
    suspend fun updateSyncStatus(reportId: Int, isSynced: Boolean, retryCount: Int = 0)
    
    /**
     * Updates sync status by localId (for offline-created data)
     * Stores server ID in separate field without changing primary key
     */
    @Query("UPDATE csr_reports SET isSynced = :isSynced, serverId = :serverId, syncRetryCount = 0 WHERE localId = :localId")
    suspend fun updateSyncStatusByLocalId(localId: String, isSynced: Boolean, serverId: Int)
    
    /**
     * Updates sync status by localId without changing primary key (safer approach)
     * Marks the record as synced and stores the server ID reference
     */
    @Query("UPDATE csr_reports SET isSynced = :isSynced, syncRetryCount = 0 WHERE localId = :localId")
    suspend fun markSyncedByLocalId(localId: String, isSynced: Boolean)
    
    /**
     * Gets reports by year and month
     */
    @Query("SELECT * FROM csr_reports WHERE year = :year AND month = :month ORDER BY createdAt DESC")
    fun getReportsByMonth(year: Int, month: Int): Flow<List<CsrReportEntity>>

    /**
     * Gets a single report by ID
     */
    @Query("SELECT * FROM csr_reports WHERE id = :reportId")
    fun getReportById(reportId: Int): Flow<CsrReportEntity?>
    
    /**
     * Inserts a single report (for offline-created data)
     * Using ABORT strategy to ensure each CSV row creates a new record
     * Will throw exception if there's a primary key conflict (which should not happen with auto-increment)
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertReport(report: CsrReportEntity): Long
    
    // ===== DEBUG QUERIES FOR SYNC STATUS =====
    
    /**
     * Debug: Count total records in database
     */
    @Query("SELECT COUNT(*) FROM csr_reports")
    suspend fun getTotalRecordCount(): Int
    
    /**
     * Debug: Count synced records (isSynced = true)
     */
    @Query("SELECT COUNT(*) FROM csr_reports WHERE isSynced = 1")
    suspend fun getSyncedRecordCount(): Int
    
    /**
     * Debug: Count unsynced records (isSynced = false)
     */
    @Query("SELECT COUNT(*) FROM csr_reports WHERE isSynced = 0")
    suspend fun getUnsyncedRecordCount(): Int
    
    /**
     * Debug: Get sync status summary
     */
    @Query("SELECT isSynced, isLocalOnly, COUNT(*) as count FROM csr_reports GROUP BY isSynced, isLocalOnly")
    suspend fun getSyncStatusSummary(): List<SyncStatusSummary>
    
    // ===== DEBUGGING METHODS FOR SYNC VERIFICATION =====
    
    /**
     * Debug: Find record by localId
     */
    @Query("SELECT * FROM csr_reports WHERE localId = :localId")
    suspend fun getRecordByLocalId(localId: String): CsrReportEntity?
    
    /**
     * Debug: Get all records with their sync fields for debugging
     */
    @Query("SELECT id, localId, serverId, isSynced, isLocalOnly, documentName, syncRetryCount FROM csr_reports ORDER BY createdAt DESC")
    suspend fun getAllRecordsDebugInfo(): List<DebugRecordInfo>
} 