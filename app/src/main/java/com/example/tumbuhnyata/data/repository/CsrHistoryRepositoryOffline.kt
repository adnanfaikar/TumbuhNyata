package com.example.tumbuhnyata.data.repository

import android.content.Context
import android.util.Log
import com.example.tumbuhnyata.data.api.AddCsrRequest
import com.example.tumbuhnyata.data.api.CsrHistoryApi
import com.example.tumbuhnyata.data.local.dao.CsrHistoryDao
import com.example.tumbuhnyata.data.mapper.CsrHistoryMapper
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.data.model.normalizeStatus
import com.example.tumbuhnyata.util.NetworkUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Custom exceptions untuk error handling yang lebih baik
class OfflineException(message: String) : Exception(message)
class NetworkException(message: String, cause: Throwable? = null) : Exception(message, cause)
class DatabaseException(message: String, cause: Throwable? = null) : Exception(message, cause)

class CsrHistoryRepositoryOffline(
    private val api: CsrHistoryApi,
    private val dao: CsrHistoryDao,
    private val context: Context
) {
    companion object {
        private const val TAG = "CsrHistoryRepository"
    }

    // Mendapatkan data riwayat CSR dengan prioritas online -> offline
    suspend fun getCsrHistory(userId: Int): Result<List<CsrHistoryItem>> {
        return try {
            if (NetworkUtils.isNetworkAvailable(context)) {
                // Online: ambil dari API dan simpan ke database
                Log.d(TAG, "Online: Fetching data from API")
                val response = api.getCsrHistory(userId)
                
                if (response.isSuccessful) {
                    val apiData = response.body() ?: emptyList()
                    
                    // Simpan data dari API ke local database
                    if (apiData.isNotEmpty()) {
                        val entities = CsrHistoryMapper.toEntityList(apiData, isSynced = true)
                        dao.insertAll(entities)
                        Log.d(TAG, "Saved ${apiData.size} items to local database")
                    }
                    
                    // Sync data yang belum sync
                    syncUnsyncedData(userId)
                    
                    Result.success(apiData)
                } else {
                    // API error, fallback ke local
                    Log.w(TAG, "API error: ${response.code()}, falling back to local data")
                    val localData = dao.getCsrHistoryByUserIdSync(userId)
                    Result.success(CsrHistoryMapper.toItemList(localData))
                }
            } else {
                // Offline: ambil dari database lokal
                Log.d(TAG, "Offline: Fetching data from local database")
                val localData = dao.getCsrHistoryByUserIdSync(userId)
                if (localData.isEmpty()) {
                    Result.failure(OfflineException("Tidak ada koneksi internet dan tidak ada data lokal tersedia"))
                } else {
                    Result.success(CsrHistoryMapper.toItemList(localData))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching CSR history: ${e.message}")
            // Fallback ke data lokal jika API gagal
            try {
                val localData = dao.getCsrHistoryByUserIdSync(userId)
                if (localData.isEmpty()) {
                    Result.failure(NetworkException("Gagal mengambil data dari server dan tidak ada data lokal", e))
                } else {
                    Result.success(CsrHistoryMapper.toItemList(localData))
                }
            } catch (dbException: Exception) {
                Result.failure(DatabaseException("Gagal mengakses database lokal", dbException))
            }
        }
    }

    // Flow untuk UI yang reactive
    fun getCsrHistoryFlow(userId: Int): Flow<List<CsrHistoryItem>> {
        return dao.getCsrHistoryByUserId(userId).map { entities ->
            CsrHistoryMapper.toItemList(entities)
        }
    }

    // Mendapatkan data untuk "Perlu Tindakan"
    fun getPerluTindakanItems(userId: Int): Flow<List<CsrHistoryItem>> {
        return dao.getPerluTindakanItems(userId).map { entities ->
            CsrHistoryMapper.toItemList(entities)
        }
    }

    // Mendapatkan data untuk "Diterima"
    fun getDiterimaItems(userId: Int): Flow<List<CsrHistoryItem>> {
        return dao.getDiterimaItems(userId).map { entities ->
            CsrHistoryMapper.toItemList(entities)
        }
    }

    // Detail CSR history
    suspend fun getCsrHistoryDetail(id: Int, userId: Int): CsrHistoryItem? {
        return try {
            if (NetworkUtils.isNetworkAvailable(context)) {
                // Online: coba ambil dari API dulu
                val apiData = api.getCsrHistoryDetail(id, userId).body()
                if (apiData != null) {
                    // Update local database
                    val entity = CsrHistoryMapper.toEntity(apiData, isSynced = true)
                    dao.insert(entity)
                    apiData
                } else {
                    // Fallback ke local
                    val localEntity = dao.getCsrHistoryById(id)
                    localEntity?.let { CsrHistoryMapper.toItem(it) }
                }
            } else {
                // Offline: ambil dari local database
                val localEntity = dao.getCsrHistoryById(id)
                localEntity?.let { CsrHistoryMapper.toItem(it) }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching CSR detail: ${e.message}")
            // Fallback ke local database
            val localEntity = dao.getCsrHistoryById(id)
            localEntity?.let { CsrHistoryMapper.toItem(it) }
        }
    }

    // Menambah CSR history baru
    suspend fun addCsrHistory(request: AddCsrRequest): Boolean {
        return try {
            if (NetworkUtils.isNetworkAvailable(context)) {
                // Online: kirim ke API
                val response = api.addCsrHistory(request)
                if (response.isSuccessful && response.body()?.data != null) {
                    // Simpan ke local database sebagai synced
                    val entity = CsrHistoryMapper.toEntity(response.body()!!.data!!, isSynced = true)
                    dao.insert(entity)
                    true
                } else {
                    // Simpan ke local sebagai unsynced jika API gagal
                    saveAsUnsynced(request)
                    true
                }
            } else {
                // Offline: simpan ke local database sebagai unsynced
                saveAsUnsynced(request)
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adding CSR history: ${e.message}")
            // Simpan ke local database sebagai unsynced
            saveAsUnsynced(request)
            true
        }
    }

    // Delete CSR history
    suspend fun deleteCsrHistory(id: Int, userId: Int): Boolean {
        return try {
            if (NetworkUtils.isNetworkAvailable(context)) {
                // Online: hapus dari API
                val response = api.deleteCsrHistory(id, userId)
                if (response.isSuccessful) {
                    // Hapus dari local database
                    dao.permanentDelete(id)
                    true
                } else {
                    // Mark as deleted locally jika API gagal
                    dao.markAsDeleted(id)
                    true
                }
            } else {
                // Offline: mark as deleted
                dao.markAsDeleted(id)
                true
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error deleting CSR history: ${e.message}")
            // Mark as deleted locally
            dao.markAsDeleted(id)
            true
        }
    }

    // Sync data yang belum sync ke server
    suspend fun syncUnsyncedData(userId: Int) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            Log.d(TAG, "No network available for sync")
            return
        }

        try {
            // Sync unsynced items (new/updated items)
            val unsyncedItems = dao.getUnsyncedItems()
            for (entity in unsyncedItems) {
                try {
                    val request = AddCsrRequest(
                        user_id = entity.userId,
                        program_name = entity.programName,
                        category = entity.category,
                        description = entity.description,
                        location = entity.location,
                        partner_name = entity.partnerName,
                        start_date = entity.startDate,
                        end_date = entity.endDate,
                        budget = entity.budget.replace(Regex("[^\\d]"), "").toLongOrNull() ?: 0L,
                        status = entity.status,
                        agreed = entity.agreed
                    )
                    
                    val response = api.addCsrHistory(request)
                    if (response.isSuccessful && response.body()?.data != null) {
                        // Update dengan data dari server dan mark as synced
                        val serverData = response.body()!!.data!!
                        val updatedEntity = CsrHistoryMapper.toEntity(serverData, isSynced = true)
                        dao.insert(updatedEntity)
                        
                        // Hapus item lama jika ID berbeda
                        if (entity.id != serverData.id) {
                            dao.permanentDelete(entity.id)
                        }
                        
                        Log.d(TAG, "Synced item: ${entity.programName}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to sync item ${entity.programName}: ${e.message}")
                }
            }

            // Sync deleted items
            val deletedItems = dao.getUnsyncedDeletedItems()
            for (entity in deletedItems) {
                try {
                    val response = api.deleteCsrHistory(entity.id, entity.userId)
                    if (response.isSuccessful) {
                        dao.permanentDelete(entity.id)
                        Log.d(TAG, "Synced deletion: ${entity.programName}")
                    }
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to sync deletion ${entity.programName}: ${e.message}")
                }
            }

        } catch (e: Exception) {
            Log.e(TAG, "Error during sync: ${e.message}")
        }
    }

    // Helper function untuk menyimpan sebagai unsynced
    private suspend fun saveAsUnsynced(request: AddCsrRequest) {
        val entity = CsrHistoryMapper.toEntity(
            CsrHistoryItem(
                id = generateTempId(), // Generate temporary ID untuk local
                userId = request.user_id,
                programName = request.program_name,
                category = request.category,
                description = request.description,
                location = request.location,
                partnerName = request.partner_name,
                startDate = request.start_date,
                endDate = request.end_date,
                budget = request.budget.toString(),
                proposalUrl = null,
                legalityUrl = null,
                agreed = request.agreed,
                status = normalizeStatus(request.status), // Normalisasi status
                createdAt = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())
                    .format(java.util.Date())
            ),
            isSynced = false
        )
        dao.insert(entity)
    }

    // Generate temporary ID untuk items yang belum sync
    private fun generateTempId(): Int {
        return -(System.currentTimeMillis() / 1000).toInt() // Negative ID untuk temporary
    }

    // Get count of unsynced items
    suspend fun getUnsyncedCount(): Int = dao.getUnsyncedCount()

    // Flow untuk unsynced count
    fun getUnsyncedCountFlow(): Flow<Int> = dao.getUnsyncedCountFlow()

    // Clean up synced deleted items
    suspend fun cleanupSyncedDeletedItems() = dao.cleanupSyncedDeletedItems()
    
    // Helper methods untuk debugging dan testing filter status
    suspend fun getAllDistinctStatuses(userId: Int): List<String> = dao.getAllDistinctStatuses(userId)
    
    suspend fun getStatusCounts(userId: Int): Pair<Int, Int> {
        val perluTindakanCount = dao.getPerluTindakanCount(userId)
        val diterimaCount = dao.getDiterimaCount(userId)
        return Pair(perluTindakanCount, diterimaCount)
    }
    
    suspend fun debugFilterStatus(userId: Int) {
        try {
            val allStatuses = dao.getAllDistinctStatuses(userId)
            val (perluTindakanCount, diterimaCount) = getStatusCounts(userId)
            
            Log.d(TAG, "=== DEBUG FILTER STATUS ===")
            Log.d(TAG, "User ID: $userId")
            Log.d(TAG, "All distinct statuses: $allStatuses")
            Log.d(TAG, "Perlu Tindakan count: $perluTindakanCount")
            Log.d(TAG, "Diterima count: $diterimaCount")
            Log.d(TAG, "=========================")
        } catch (e: Exception) {
            Log.e(TAG, "Error in debugFilterStatus: ${e.message}")
        }
    }
} 