package com.example.tumbuhnyata.data.repository

import android.content.Context
import com.example.tumbuhnyata.data.api.CertificationApiService
import com.example.tumbuhnyata.data.local.dao.CertificationDao
import com.example.tumbuhnyata.data.local.entity.CertificationEntity
import com.example.tumbuhnyata.data.mapper.toCreateRequest
import com.example.tumbuhnyata.data.mapper.toEntity
import com.example.tumbuhnyata.data.mapper.createOfflineCertificationEntity
import com.example.tumbuhnyata.util.NetworkConnectivityUtil
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import java.io.IOException

// Resource wrapper for handling states
sealed class CertificationResource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : CertificationResource<T>(data)
    class Error<T>(message: String, data: T? = null) : CertificationResource<T>(data, message)
    class Loading<T>(data: T? = null) : CertificationResource<T>(data)
}

class CertificationRepository(
    private val certificationApiService: CertificationApiService,
    private val certificationDao: CertificationDao,
    private val context: Context
) {
    
    // Expose DAO for direct operations if needed
    fun getCertificationDao(): CertificationDao = certificationDao

    /**
     * Gets all certifications using SSOT pattern
     * Returns cached data immediately, then updates from server if online
     */
    fun getAllCertifications(): Flow<CertificationResource<List<CertificationEntity>>> = flow {
        try {
            // STEP 1: Emit local data immediately (SSOT)
            val localCertifications = certificationDao.getAllCertifications()
            localCertifications.collect { localData ->
                if (localData.isNotEmpty()) {
                    println("CertificationRepository: Emitting ${localData.size} local certifications")
                    emit(CertificationResource.Success(localData))
                } else {
                    println("CertificationRepository: No local certifications found")
                    emit(CertificationResource.Loading())
                }
            }
            
            // STEP 2: Try to refresh from server if online
            if (NetworkConnectivityUtil.isOnline(context)) {
                println("CertificationRepository: Device online, refreshing from server...")
                refreshCertificationsCache()
            } else {
                println("CertificationRepository: Device offline, using cached data only")
            }
            
        } catch (e: Exception) {
            println("CertificationRepository: Error in getAllCertifications: ${e.message}")
            emit(CertificationResource.Error("Failed to load certifications: ${e.message}"))
        }
    }

    /**
     * Submits a new certification application
     * Handles both online and offline scenarios
     */
    suspend fun submitCertification(
        name: String,
        description: String,
        credentialBody: String,
        benefits: String,
        cost: Float,
        supportingDocuments: List<String>
    ): CertificationResource<Long> {
        return try {
            println("CertificationRepository: === SUBMITTING CERTIFICATION ===")
            println("CertificationRepository: Name: $name")
            println("CertificationRepository: Credential Body: $credentialBody")
            println("CertificationRepository: Cost: $cost")
            println("CertificationRepository: Supporting Documents: $supportingDocuments")
            
            // Create offline entity first (ensures data is saved locally)
            val offlineEntity = createOfflineCertificationEntity(
                name = name,
                description = description,
                credentialBody = credentialBody,
                benefits = benefits,
                cost = cost,
                supportingDocuments = supportingDocuments
            )
            
            // Insert into local database immediately
            val localId = certificationDao.insertCertification(offlineEntity)
            println("CertificationRepository: Saved locally with ID: $localId")
            
            // Try to sync to server if online
            if (NetworkConnectivityUtil.isOnline(context)) {
                println("CertificationRepository: Device online, attempting immediate sync...")
                val syncSuccess = syncSingleCertification(offlineEntity.copy(id = localId.toInt()))
                
                if (syncSuccess) {
                    println("CertificationRepository: ✅ Successfully submitted and synced to server")
                    CertificationResource.Success(localId)
                } else {
                    println("CertificationRepository: ⚠️ Saved locally but failed to sync to server")
                    CertificationResource.Success(localId) // Still success - data is saved
                }
            } else {
                println("CertificationRepository: Device offline, will sync when online")
                CertificationResource.Success(localId)
            }
            
        } catch (e: Exception) {
            println("CertificationRepository: Error submitting certification: ${e.message}")
            e.printStackTrace()
            CertificationResource.Error("Failed to submit certification: ${e.message}")
        }
    }

    /**
     * Syncs all unsynced certifications to server
     * Called periodically or when user manually syncs
     */
    suspend fun syncUnsyncedCertifications(): CertificationResource<Int> {
        if (!NetworkConnectivityUtil.isOnline(context)) {
            println("CertificationRepository: Device is offline, cannot sync")
            return CertificationResource.Error("Device is offline, cannot sync")
        }

        return try {
            val unsyncedCertifications = certificationDao.getUnsyncedCertifications()
            println("CertificationRepository: === STARTING CERTIFICATION SYNC ===")
            println("CertificationRepository: Found ${unsyncedCertifications.size} unsynced certifications")
            
            if (unsyncedCertifications.isEmpty()) {
                println("CertificationRepository: No unsynced certifications found")
                return CertificationResource.Success(0)
            }
            
            var syncedCount = 0
            var errorCount = 0
            val syncErrors = mutableListOf<String>()
            
            // Process each unsynced certification
            for ((index, certification) in unsyncedCertifications.withIndex()) {
                try {
                    println("CertificationRepository: [${index + 1}/${unsyncedCertifications.size}] Syncing certification:")
                    println("  - ID: ${certification.id}")
                    println("  - LocalID: ${certification.localId}")
                    println("  - Name: ${certification.name}")
                    println("  - Status: ${certification.status}")
                    println("  - isLocalOnly: ${certification.isLocalOnly}")
                    println("  - isSynced: ${certification.isSynced}")
                    println("  - Retry Count: ${certification.syncRetryCount}")
                    
                    val success = syncSingleCertification(certification)
                    if (success) {
                        syncedCount++
                        println("  ✓ Successfully synced certification")
                    } else {
                        errorCount++
                        syncErrors.add("Failed to sync certification: ${certification.name}")
                        println("  ✗ Failed to sync certification")
                        
                        // Increment retry count for failed items
                        val newRetryCount = certification.syncRetryCount + 1
                        certificationDao.updateSyncStatus(certification.id, false, newRetryCount)
                    }
                    
                } catch (e: Exception) {
                    println("  ✗ Exception during certification sync: ${e.message}")
                    e.printStackTrace()
                    errorCount++
                    syncErrors.add("Exception syncing ${certification.name}: ${e.message}")
                    
                    // Increment retry count for failed items
                    val newRetryCount = certification.syncRetryCount + 1
                    certificationDao.updateSyncStatus(certification.id, false, newRetryCount)
                }
            }
            
            println("CertificationRepository: === CERTIFICATION SYNC COMPLETED ===")
            println("CertificationRepository: Total Certifications: ${unsyncedCertifications.size}")
            println("CertificationRepository: Successfully Synced: $syncedCount")
            println("CertificationRepository: Failed: $errorCount")
            
            when {
                errorCount == 0 -> {
                    CertificationResource.Success(syncedCount)
                }
                syncedCount > 0 -> {
                    CertificationResource.Error("Partially synced: $syncedCount success, $errorCount failed", syncedCount)
                }
                else -> {
                    CertificationResource.Error("All sync attempts failed: ${syncErrors.firstOrNull() ?: "Unknown error"}")
                }
            }
            
        } catch (e: Exception) {
            println("CertificationRepository: Critical certification sync failure: ${e.message}")
            e.printStackTrace()
            CertificationResource.Error("Certification sync failed: ${e.message}")
        }
    }

    /**
     * Syncs a single certification to server
     */
    private suspend fun syncSingleCertification(certification: CertificationEntity): Boolean {
        return try {
            if (certification.localId.isNullOrEmpty()) {
                println("CertificationRepository: ERROR - localId is null for certification!")
                return false
            }
            
            println("CertificationRepository: Applying certification to server (NO AUTH + user_id=1)...")
            val createRequest = certification.toCreateRequest()
            println("CertificationRepository: Request payload: $createRequest")
            println("CertificationRepository: ✅ user_id in request: ${createRequest.user_id}")
            println("CertificationRepository: ✅ Using certificationApiService: ${certificationApiService.javaClass.simpleName}")
            
            val response = certificationApiService.applyCertification(createRequest)
            println("CertificationRepository: Apply response code: ${response.code()}")
            
            when {
                response.isSuccessful -> {
                    val responseBody = response.body()
                    println("CertificationRepository: Apply response body: $responseBody")
                    
                    if (responseBody?.id != null && responseBody.id > 0) {
                        val serverId = responseBody.id
                        println("CertificationRepository: Server returned ID: $serverId")
                        
                        // Update local record with server ID and mark as synced
                        certificationDao.updateSyncStatusByLocalId(
                            localId = certification.localId!!,
                            isSynced = true,
                            serverId = serverId
                        )
                        
                        println("CertificationRepository: ✅ Successfully updated local record with server ID: $serverId")
                        true
                    } else {
                        println("CertificationRepository: ✗ Server response missing valid ID")
                        false
                    }
                }
                response.code() == 401 -> {
                    println("CertificationRepository: ✗ Unauthorized - authentication token may have expired")
                    false
                }
                response.code() == 400 -> {
                    val errorBody = response.errorBody()?.string()
                    println("CertificationRepository: ✗ Bad request - validation failed: $errorBody")
                    false
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    println("CertificationRepository: ✗ API error ${response.code()}: $errorBody")
                    false
                }
            }
            
        } catch (e: IOException) {
            println("CertificationRepository: ✗ Network error during certification sync: ${e.message}")
            false
        } catch (e: Exception) {
            println("CertificationRepository: ✗ Unexpected error during certification sync: ${e.message}")
            e.printStackTrace()
            false
        }
    }

    /**
     * Refreshes certifications cache from server
     */
    suspend fun refreshCertificationsCache() {
        if (!NetworkConnectivityUtil.isOnline(context)) {
            println("CertificationRepository: Cannot refresh cache - device offline")
            return
        }
        
        try {
            // STEP 1: Sync unsynced data first
            println("CertificationRepository: Syncing unsynced certifications before cache refresh...")
            val syncResult = syncUnsyncedCertifications()
            when (syncResult) {
                is CertificationResource.Success -> {
                    println("CertificationRepository: Successfully synced ${syncResult.data} unsynced certifications")
                }
                is CertificationResource.Error -> {
                    println("CertificationRepository: Sync had errors: ${syncResult.message}")
                }
                is CertificationResource.Loading -> {
                    println("CertificationRepository: Sync is loading")
                }
            }
            
            // STEP 2: Get unsynced data to preserve
            val unsyncedData = certificationDao.getUnsyncedCertifications()
            println("CertificationRepository: Found ${unsyncedData.size} remaining unsynced certifications to preserve")
            
            // STEP 3: Refresh from API with hardcoded user_id = 1
            println("CertificationRepository: Refreshing certifications cache...")
            val response = certificationApiService.getUserCertifications(userId = 1)
            println("CertificationRepository: Cache refresh API call completed with code: ${response.code()}")
            
            if (response.isSuccessful) {
                response.body()?.let { certificationListResponse ->
                    val certifications = certificationListResponse.data
                    println("CertificationRepository: Success! Got ${certifications.size} certifications from API")
                    
                    // STEP 4: Clear only synced data, preserve unsynced
                    certificationDao.clearSyncedCertifications()
                    
                    // STEP 5: Insert fresh API data
                    val entities = certifications.map { it.toEntity() }
                    certificationDao.insertOrUpdateCertifications(entities)
                    println("CertificationRepository: Inserted ${entities.size} fresh API certifications")
                    
                    // STEP 6: Re-insert preserved unsynced data
                    unsyncedData.forEach { unsyncedEntity ->
                        try {
                            certificationDao.insertCertification(unsyncedEntity)
                            println("CertificationRepository: Preserved unsynced certification ${unsyncedEntity.localId}")
                        } catch (e: Exception) {
                            println("CertificationRepository: Conflict inserting unsynced certification: ${e.message}")
                        }
                    }
                    
                    println("CertificationRepository: Successfully cached ${entities.size} API certifications + ${unsyncedData.size} unsynced")
                } ?: run {
                    println("CertificationRepository: Response body was null for certifications refresh")
                }
            } else {
                val errorBody = response.errorBody()?.string()
                println("CertificationRepository: Failed to refresh cache. Code: ${response.code()}, Error: $errorBody")
            }
            
        } catch (e: Exception) {
            println("CertificationRepository: Exception during cache refresh: ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * Gets certification by ID
     */
    fun getCertificationById(certificationId: Int): Flow<CertificationResource<CertificationEntity?>> = flow {
        try {
            // Emit local data
            certificationDao.getCertificationById(certificationId).collect { localData ->
                emit(CertificationResource.Success(localData))
            }
            
        } catch (e: Exception) {
            println("CertificationRepository: Error getting certification by ID: ${e.message}")
            emit(CertificationResource.Error("Failed to load certification: ${e.message}"))
        }
    }

    /**
     * Gets certifications by status
     */
    fun getCertificationsByStatus(status: String): Flow<CertificationResource<List<CertificationEntity>>> = flow {
        try {
            certificationDao.getCertificationsByStatus(status).collect { localData ->
                emit(CertificationResource.Success(localData))
            }
            
        } catch (e: Exception) {
            println("CertificationRepository: Error getting certifications by status: ${e.message}")
            emit(CertificationResource.Error("Failed to load certifications: ${e.message}"))
        }
    }

    /**
     * Starts periodic sync (call from ViewModel or Application)
     */
    suspend fun startPeriodicSync() {
        println("CertificationRepository: Starting periodic certification sync...")
        
        while (true) {
            try {
                if (NetworkConnectivityUtil.isOnline(context)) {
                    println("CertificationRepository: Running periodic sync...")
                    syncUnsyncedCertifications()
                    refreshCertificationsCache()
                }
                
                // Wait 30 seconds before next sync attempt
                delay(30000)
                
            } catch (e: CancellationException) {
                println("CertificationRepository: Periodic sync cancelled")
                break
            } catch (e: Exception) {
                println("CertificationRepository: Error in periodic sync: ${e.message}")
                // Wait 30 seconds before retry on error
                delay(30000)
            }
        }
    }
} 