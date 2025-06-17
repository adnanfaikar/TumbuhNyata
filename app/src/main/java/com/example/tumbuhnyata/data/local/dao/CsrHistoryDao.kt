package com.example.tumbuhnyata.data.local.dao

import androidx.room.*
import com.example.tumbuhnyata.data.local.entity.CsrHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CsrHistoryDao {
    
    @Query("SELECT * FROM csr_history WHERE userId = :userId AND isDeleted = 0 ORDER BY lastModified DESC")
    fun getCsrHistoryByUserId(userId: Int): Flow<List<CsrHistoryEntity>>
    
    @Query("SELECT * FROM csr_history WHERE userId = :userId AND isDeleted = 0 ORDER BY lastModified DESC")
    suspend fun getCsrHistoryByUserIdSync(userId: Int): List<CsrHistoryEntity>
    
    @Query("SELECT * FROM csr_history WHERE id = :id AND isDeleted = 0")
    suspend fun getCsrHistoryById(id: Int): CsrHistoryEntity?
    
    @Query("SELECT * FROM csr_history WHERE userId = :userId AND status IN ('Proses Review', 'Memerlukan Revisi', 'Menunggu Pembayaran') AND isDeleted = 0 ORDER BY lastModified DESC")
    fun getPerluTindakanItems(userId: Int): Flow<List<CsrHistoryEntity>>
    
    @Query("SELECT * FROM csr_history WHERE userId = :userId AND status IN ('Akan Datang', 'Sedang Berlangsung', 'Program Selesai') AND isDeleted = 0 ORDER BY lastModified DESC")
    fun getDiterimaItems(userId: Int): Flow<List<CsrHistoryEntity>>
    
    @Query("SELECT * FROM csr_history WHERE isSynced = 0 AND isDeleted = 0")
    suspend fun getUnsyncedItems(): List<CsrHistoryEntity>
    
    @Query("SELECT * FROM csr_history WHERE isDeleted = 1 AND isSynced = 0")
    suspend fun getUnsyncedDeletedItems(): List<CsrHistoryEntity>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(csrHistory: CsrHistoryEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(csrHistoryList: List<CsrHistoryEntity>)
    
    @Update
    suspend fun update(csrHistory: CsrHistoryEntity)
    
    @Query("UPDATE csr_history SET isSynced = :isSynced WHERE id = :id")
    suspend fun updateSyncStatus(id: Int, isSynced: Boolean)
    
    @Query("UPDATE csr_history SET isDeleted = 1, isSynced = 0, lastModified = :timestamp WHERE id = :id")
    suspend fun markAsDeleted(id: Int, timestamp: Long = System.currentTimeMillis())
    
    @Query("DELETE FROM csr_history WHERE id = :id")
    suspend fun permanentDelete(id: Int)
    
    @Query("DELETE FROM csr_history WHERE isDeleted = 1 AND isSynced = 1")
    suspend fun cleanupSyncedDeletedItems()
    
    @Query("DELETE FROM csr_history")
    suspend fun deleteAll()
    
    @Query("SELECT COUNT(*) FROM csr_history WHERE isSynced = 0")
    suspend fun getUnsyncedCount(): Int
    
    @Query("SELECT COUNT(*) FROM csr_history WHERE isSynced = 0")
    fun getUnsyncedCountFlow(): Flow<Int>
    
    // Helper queries untuk testing status filtering
    @Query("SELECT DISTINCT status FROM csr_history WHERE userId = :userId AND isDeleted = 0")
    suspend fun getAllDistinctStatuses(userId: Int): List<String>
    
    @Query("SELECT COUNT(*) FROM csr_history WHERE userId = :userId AND status IN ('Proses Review', 'Memerlukan Revisi', 'Menunggu Pembayaran') AND isDeleted = 0")
    suspend fun getPerluTindakanCount(userId: Int): Int
    
    @Query("SELECT COUNT(*) FROM csr_history WHERE userId = :userId AND status IN ('Akan Datang', 'Sedang Berlangsung', 'Program Selesai') AND isDeleted = 0")
    suspend fun getDiterimaCount(userId: Int): Int
} 