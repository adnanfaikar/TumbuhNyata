package com.example.tumbuhnyata.data.repository

import android.content.Context // Diperlukan untuk cek koneksi internet (jika dilakukan di sini)
import com.example.tumbuhnyata.data.api.DashboardApiService
import com.example.tumbuhnyata.data.local.dao.DashboardDao
import com.example.tumbuhnyata.data.mapper.toEntityList
import com.example.tumbuhnyata.data.mapper.toKpiItemStateList // Mapper untuk DashboardData -> List<KPIItemState>
import com.example.tumbuhnyata.data.mapper.toKpiItemStateListForOffline // Mapper untuk List<CsrReportEntity> -> List<KPIItemState>
import com.example.tumbuhnyata.data.mapper.toKpiDetails // Mapper untuk KpiDetailData -> KpiDetails
import com.example.tumbuhnyata.data.mapper.toCreateRequest // Sync mapper
import com.example.tumbuhnyata.data.mapper.toUpdateRequest // Sync mapper
import com.example.tumbuhnyata.util.NetworkConnectivityUtil // Anda perlu membuat utilitas ini
import com.example.tumbuhnyata.viewmodel.KPIItemState
import com.example.tumbuhnyata.ui.dashboard.kpi.KpiDetails // Import KpiDetails
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import java.io.IOException
import java.text.DecimalFormat
import com.example.tumbuhnyata.data.local.entity.CsrReportEntity

// Definisikan Resource wrapper jika belum ada di util
sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data) // Opsional: bisa juga tanpa data saat loading
}

class DashboardRepository(
    private val dashboardApiService: DashboardApiService,
    private val dashboardDao: DashboardDao,
    private val context: Context // Untuk NetworkConnectivityUtil
) {
    
    // Expose dashboardDao for offline operations
    fun getDashboardDao(): DashboardDao = dashboardDao

    /**
     * Syncs unsynced data to server when online
     * COMPREHENSIVE SYNC: Handles individual submissions with proper error handling and retry logic
     */
    suspend fun syncUnsyncedData(): Resource<Int> {
        if (!NetworkConnectivityUtil.isOnline(context)) {
            println("DashboardRepository: Device is offline, cannot sync")
            return Resource.Error("Device is offline, cannot sync")
        }

        return try {
            val unsyncedReports = dashboardDao.getUnsyncedReports()
            println("DashboardRepository: === STARTING SYNC OPERATION ===")
            println("DashboardRepository: Found ${unsyncedReports.size} unsynced reports")
            
            if (unsyncedReports.isEmpty()) {
                println("DashboardRepository: No unsynced data found, sync complete")
                return Resource.Success(0)
            }
            
            var syncedCount = 0
            var errorCount = 0
            val syncErrors = mutableListOf<String>()
            
            // Process each unsynced report
            for ((index, report) in unsyncedReports.withIndex()) {
                try {
                    println("DashboardRepository: [${index + 1}/${unsyncedReports.size}] Syncing report:")
                    println("  - ID: ${report.id}")
                    println("  - LocalID: ${report.localId}")
                    println("  - Document: ${report.documentName}")
                    println("  - Carbon Value: ${report.carbonValue}")
                    println("  - isLocalOnly: ${report.isLocalOnly}")
                    println("  - isSynced: ${report.isSynced}")
                    println("  - Retry Count: ${report.syncRetryCount}")
                    
                    if (report.isLocalOnly) {
                        // Offline-created data: Use CREATE endpoint
                        val success = syncOfflineCreatedData(report)
                        if (success) {
                            syncedCount++
                            println("  ✓ Successfully synced offline-created data")
                        } else {
                            errorCount++
                            syncErrors.add("Failed to sync offline data: ${report.documentName}")
                            println("  ✗ Failed to sync offline-created data")
                        }
                    } else {
                        // Server data with local modifications: Use UPDATE endpoint
                        val success = syncModifiedServerData(report)
                        if (success) {
                            syncedCount++
                            println("  ✓ Successfully synced modified server data")
                        } else {
                            errorCount++
                            syncErrors.add("Failed to update server data: ${report.documentName}")
                            println("  ✗ Failed to sync modified server data")
                        }
                    }
                    
                } catch (e: Exception) {
                    println("  ✗ Exception during sync: ${e.message}")
                    e.printStackTrace()
                    errorCount++
                    syncErrors.add("Exception syncing ${report.documentName}: ${e.message}")
                    
                    // Increment retry count for failed items
                    val newRetryCount = report.syncRetryCount + 1
                    dashboardDao.updateSyncStatus(report.id, false, newRetryCount)
                }
            }
            
            println("DashboardRepository: === SYNC OPERATION COMPLETED ===")
            println("DashboardRepository: Total Reports: ${unsyncedReports.size}")
            println("DashboardRepository: Successfully Synced: $syncedCount")
            println("DashboardRepository: Failed: $errorCount")
            if (syncErrors.isNotEmpty()) {
                println("DashboardRepository: Errors:")
                syncErrors.forEach { error ->
                    println("  - $error")
                }
            }
            println("DashboardRepository: =========================================")
            
            when {
                errorCount == 0 -> {
                    Resource.Success(syncedCount)
                }
                syncedCount > 0 -> {
                    Resource.Error("Partially synced: $syncedCount success, $errorCount failed", syncedCount)
                }
                else -> {
                    Resource.Error("All sync attempts failed: ${syncErrors.firstOrNull() ?: "Unknown error"}")
                }
            }
            
        } catch (e: Exception) {
            println("DashboardRepository: Critical sync operation failure: ${e.message}")
            e.printStackTrace()
            Resource.Error("Sync operation failed: ${e.message}")
        }
    }
    
    /**
     * Syncs offline-created data using CREATE endpoint
     */
    private suspend fun syncOfflineCreatedData(report: CsrReportEntity): Boolean {
        return try {
            println("DashboardRepository: === SYNCING OFFLINE-CREATED DATA ===")
            println("DashboardRepository: Report ID: ${report.id}")
            println("DashboardRepository: Local ID: ${report.localId}")
            println("DashboardRepository: Company ID: ${report.companyId}")
            println("DashboardRepository: Document Name: ${report.documentName}")
            println("DashboardRepository: Is Local Only: ${report.isLocalOnly}")
            println("DashboardRepository: Is Synced: ${report.isSynced}")
            println("DashboardRepository: Retry Count: ${report.syncRetryCount}")
            
            // Validate required fields
            if (report.localId.isNullOrEmpty()) {
                println("DashboardRepository: ERROR - localId is null or empty for offline data!")
                return false
            }
            
            println("DashboardRepository: Creating new submission on server...")
            val createRequest = report.toCreateRequest()
            println("DashboardRepository: Request payload: $createRequest")
            
            val response = dashboardApiService.createSubmission(createRequest)
            println("DashboardRepository: Create response code: ${response.code()}")
            
            when {
                response.isSuccessful -> {
                    val responseBody = response.body()
                    println("DashboardRepository: Create response body: $responseBody")
                    
                    if (responseBody?.success == true) {
                        val serverId = responseBody.data?.id
                        println("DashboardRepository: Server returned success=true, serverId: $serverId")
                        
                        if (serverId != null && serverId > 0) {
                            println("DashboardRepository: Updating local record with server ID...")
                            println("DashboardRepository: Before update - localId: ${report.localId}, updating to serverId: $serverId")
                            
                            // Update local record with server ID and mark as synced
                            dashboardDao.updateSyncStatusByLocalId(
                                localId = report.localId!!,
                                isSynced = true,
                                serverId = serverId
                            )
                            
                            println("DashboardRepository: ✅ Successfully updated local record with server ID: $serverId")
                            
                            // Verify the update worked by checking the specific record
                            val updatedRecord = dashboardDao.getRecordByLocalId(report.localId!!)
                            if (updatedRecord != null) {
                                println("DashboardRepository: 🔍 Verification - Updated record found:")
                                println("  - ID: ${updatedRecord.id}")
                                println("  - LocalId: ${updatedRecord.localId}")
                                println("  - ServerId: ${updatedRecord.serverId}")
                                println("  - IsSynced: ${updatedRecord.isSynced}")
                                println("  - IsLocalOnly: ${updatedRecord.isLocalOnly}")
                                
                                if (updatedRecord.isSynced) {
                                    println("DashboardRepository: ✅ Verified - Record is now marked as synced")
                                } else {
                                    println("DashboardRepository: ❌ ERROR - Record still marked as unsynced!")
                                }
                            } else {
                                println("DashboardRepository: ❌ ERROR - Cannot find record by localId after update!")
                            }
                            
                            // Double-check: verify record no longer in unsynced list
                            val unsyncedRecords = dashboardDao.getUnsyncedReports()
                            val stillUnsynced = unsyncedRecords.any { it.localId == report.localId }
                            if (stillUnsynced) {
                                println("DashboardRepository: ❌ WARNING - Record still appears as unsynced after update!")
                                
                                // Print debug info of all unsynced records
                                println("DashboardRepository: Current unsynced records:")
                                unsyncedRecords.forEach { record ->
                                    println("  - ID: ${record.id}, LocalId: ${record.localId}, IsSynced: ${record.isSynced}")
                                }
                            } else {
                                println("DashboardRepository: ✅ Verified - Record no longer in unsynced list")
                            }
                            
                            true
                        } else {
                            println("DashboardRepository: ❌ Invalid server ID in response: $serverId")
                            incrementRetryCount(report)
                            false
                        }
                    } else {
                        println("DashboardRepository: ❌ Server returned success=false: ${responseBody?.message}")
                        incrementRetryCount(report)
                        false
                    }
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    println("DashboardRepository: ❌ HTTP ${response.code()}: $errorBody")
                    incrementRetryCount(report)
                    false
                }
            }
        } catch (e: Exception) {
            println("DashboardRepository: ❌ Exception in syncOfflineCreatedData: ${e.message}")
            e.printStackTrace()
            incrementRetryCount(report)
            false
        }
    }
    
    /**
     * Syncs server data with local modifications using UPDATE endpoint
     */
    private suspend fun syncModifiedServerData(report: CsrReportEntity): Boolean {
        return try {
            println("DashboardRepository: Updating existing submission on server...")
            val updateRequest = report.toUpdateRequest()
            println("DashboardRepository: Update payload: $updateRequest")
            
            val response = dashboardApiService.updateSubmission(report.id, updateRequest)
            println("DashboardRepository: Update response code: ${response.code()}")
            
            when {
                response.isSuccessful -> {
                    val responseBody = response.body()
                    println("DashboardRepository: Update response body: $responseBody")
                    
                    if (responseBody?.success == true) {
                        // Mark as synced
                        dashboardDao.updateSyncStatus(report.id, true, 0)
                        println("DashboardRepository: Successfully updated server record")
                        true
                    } else {
                        println("DashboardRepository: Server returned success=false: ${responseBody?.message}")
                        incrementRetryCount(report)
                        false
                    }
                }
                else -> {
                    val errorBody = response.errorBody()?.string()
                    println("DashboardRepository: HTTP ${response.code()}: $errorBody")
                    incrementRetryCount(report)
                    false
                }
            }
        } catch (e: Exception) {
            println("DashboardRepository: Exception in syncModifiedServerData: ${e.message}")
            e.printStackTrace()
            incrementRetryCount(report)
            false
        }
    }
    
    /**
     * Helper to increment retry count for failed sync attempts
     * FIXED: Handle offline data by updating via localId when available
     */
    private suspend fun incrementRetryCount(report: CsrReportEntity) {
        val newRetryCount = report.syncRetryCount + 1
        
        if (report.isLocalOnly && report.localId != null) {
            // For offline-created data, update by localId
            dashboardDao.markSyncedByLocalId(report.localId!!, false) // Use existing method
            println("DashboardRepository: Incremented retry count for offline data localId: ${report.localId}")
            
            // Also update retry count manually via direct id update (since markSyncedByLocalId doesn't handle retry count)
            dashboardDao.updateSyncStatus(report.id, false, newRetryCount)
            println("DashboardRepository: Updated retry count to $newRetryCount for offline record id: ${report.id}")
        } else {
            // For server data, update by id
            dashboardDao.updateSyncStatus(report.id, false, newRetryCount)
            println("DashboardRepository: Incremented retry count to $newRetryCount for server record id: ${report.id}")
        }
    }

    /**
     * Fetches dashboard KPI items.
     * Tries to fetch from API first. If online and successful, it also attempts to refresh
     * the local cache of all submissions and auto-sync unsynced data.
     * If API fails or offline, it attempts to load from the local cache.
     */
    fun getDashboardKpiItems(companyId: Int?, year: Int?): Flow<Resource<List<KPIItemState>>> = flow {
        emit(Resource.Loading()) // Emit loading state

        val localDataFlow = dashboardDao.getAllReports().map { it.toKpiItemStateListForOffline(year) }
        val currentLocalKpis = localDataFlow.firstOrNull() // Ambil data lokal saat ini untuk fallback cepat

        if (NetworkConnectivityUtil.isOnline(context)) {
            try {
                // CRITICAL FIX: Auto-sync unsynced data first when online (AWAIT completion)
                println("DashboardRepository: Device is online (${NetworkConnectivityUtil.getNetworkType(context)}), attempting auto-sync...")
                val syncResult = syncUnsyncedData() // AWAIT the result
                when (syncResult) {
                    is Resource.Success -> {
                        println("DashboardRepository: Successfully synced ${syncResult.data} unsynced records before dashboard load")
                    }
                    is Resource.Error -> {
                        println("DashboardRepository: Sync had errors before dashboard load: ${syncResult.message}")
                        // Continue anyway, some data might not be synced
                    }
                    is Resource.Loading -> {
                        println("DashboardRepository: Sync still loading, this should not happen")
                    }
                }
                
                // 1. Ambil data dashboard utama menggunakan endpoint
                val response = dashboardApiService.getDashboardData(companyId = companyId, year = year)
                println("DashboardRepository: Calling endpoint: carbon-submissions/dashboard")
                println("DashboardRepository: Response code: ${response.code()}")
                
                if (response.isSuccessful && response.body()?.success == true) {
                    val dashboardData = response.body()?.data
                    if (dashboardData != null) {
                        println("DashboardRepository: Success! Data received from carbon-submissions/dashboard")
                        val kpiItems = dashboardData.toKpiItemStateList()
                        emit(Resource.Success(kpiItems)) // Emit data sukses dari API

                        // Background refresh cache
                        try {
                            refreshSubmissionsCache(companyId, year)
                        } catch (e: Exception) {
                            println("DashboardRepository: Failed to refresh submissions cache: ${e.message}")
                        }
                        return@flow // Selesai jika API dashboard sukses
                    } else {
                        println("DashboardRepository: Dashboard data is null")
                        // Fall through to try local data
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    val errorMsg = "API dashboard failed. Code: ${response.code()}, Message: ${response.message()}" +
                            if (errorBody != null) ", Error: $errorBody" else ""
                    println("DashboardRepository: $errorMsg")
                    // Fall through to try local data
                }
            } catch (e: IOException) { // Network error
                println("DashboardRepository: Network error: ${e.message}")
                // Fall through to try local data
            } catch (e: Exception) { // Other errors (e.g., JSON parsing)
                println("DashboardRepository: API error: ${e.message}")
                // Fall through to try local data
            }
        }
        
        // Try local data (either offline or API failed)
        if (!currentLocalKpis.isNullOrEmpty()) {
            val networkStatus = if (NetworkConnectivityUtil.isOnline(context)) "online" else "offline"
            println("DashboardRepository: Using local data ($networkStatus mode)")
            emit(Resource.Success(currentLocalKpis))
        } else {
            val errorMsg = if (NetworkConnectivityUtil.isOnline(context)) {
                "Tidak dapat mengambil data dari server dan tidak ada data lokal tersedia."
            } else {
                "Anda offline dan tidak ada data tersimpan."
            }
            emit(Resource.Error(errorMsg))
        }
    }

    /**
     * Fetches detailed KPI data for a specific KPI type.
     * FIXED: Now follows same pattern as getDashboardKpiItems - Room first, API as background refresh
     */
    fun getKpiDetail(kpiType: String, companyId: Int?, year: Int?): Flow<Resource<KpiDetails>> = flow {
        emit(Resource.Loading())

        // --- NETWORK-FIRST STRATEGY WHEN ONLINE ---
        if (NetworkConnectivityUtil.isOnline(context)) {
            try {
                val allLocalReportsForYearCheck = dashboardDao.getAllReports().firstOrNull() ?: emptyList()
                val kpiSpecificEntitiesForYearCheck = allLocalReportsForYearCheck.filter { it.documentType == getDocumentTypeForKpi(kpiType) }
                val targetYear = year ?: kpiSpecificEntitiesForYearCheck.maxOfOrNull { it.year ?: 0 }?.takeIf { it > 0 } ?: java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)

                println("DashboardRepository: Calling KPI detail endpoint (Network-First) for $kpiType, year $targetYear")
                val companyIdString = companyId?.toString()
                val response = dashboardApiService.getKpiDetail(kpiType = kpiType, companyId = companyIdString, year = targetYear)

                if (response.isSuccessful && response.body()?.success == true) {
                    val kpiDetailData = response.body()?.data
                    if (kpiDetailData != null) {
                        println("DashboardRepository: Success! Fresh KPI detail data received.")
                        val kpiDetails = kpiDetailData.toKpiDetails(targetYear)
                        emit(Resource.Success(kpiDetails))
                        
                        // Refresh cache in background
                        try {
                            refreshSubmissionsCache(companyId, null) 
                        } catch(e: Exception) {
                            println("DashboardRepository: Background cache refresh failed: ${e.message}")
                        }
                        return@flow 
                    }
                }
                println("DashboardRepository: API call for KPI detail did not return valid data. Code: ${response.code()}. Falling back to local cache.")
            } catch (e: Exception) {
                println("DashboardRepository: API call for KPI detail failed: ${e.message}. Falling back to local cache.")
            }
        }

        // --- CACHE FALLBACK LOGIC (or OFFLINE) ---
        println("DashboardRepository: Using local cache for KPI Details.")
        val allLocalReports = dashboardDao.getAllReports().firstOrNull() ?: emptyList()
        val kpiSpecificEntities = allLocalReports.filter { it.documentType == getDocumentTypeForKpi(kpiType) }
        val targetYear = year ?: kpiSpecificEntities.maxOfOrNull { it.year ?: 0 }?.takeIf { it > 0 } ?: java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
        
        val entitiesForYear = kpiSpecificEntities.filter { it.year == targetYear }

        if (entitiesForYear.isNotEmpty()) {
            println("DashboardRepository: Found ${entitiesForYear.size} records in cache for $kpiType, year $targetYear.")
            val localData = createKpiDetailsFromRoom(kpiType, entitiesForYear, targetYear, allLocalReports)
            emit(Resource.Success(localData))
        } else {
            val errorMsg = "Tidak ada data untuk KPI '$kpiType' pada tahun $targetYear."
            println("DashboardRepository: $errorMsg")
            emit(Resource.Error(errorMsg))
        }
    }

    private fun getDocumentTypeForKpi(kpiType: String): String {
        return when(kpiType) {
            "carbon_footprint" -> "data_emisi"
            "energy_consumption" -> "data_energi"
            "water_usage" -> "data_air"
            "tree_planting", "trees_planted" -> "data_pohon"
            "waste_management" -> "data_sampah"
            "benefit_received", "beneficiary_received" -> "data_manfaat"
            else -> "data_emisi" // fallback
        }
    }

    private fun createKpiDetailsFromRoom(kpiType: String, entitiesForYear: List<CsrReportEntity>, year: Int, allEntities: List<CsrReportEntity>): KpiDetails {
        val decimalFormat = DecimalFormat("#,###.##")
        
        val monthlyData = entitiesForYear
            .filter { it.carbonValue != null && it.month != null }
            .groupBy { it.month }
            .mapValues { (_, reports) -> reports.sumOf { it.carbonValue?.toDouble() ?: 0.0 }.toFloat() }
        
        val yearlyChartData = (1..12).map { month -> monthlyData[month] ?: 0f }
        
        val yearlyData = allEntities
            .filter { it.documentType == getDocumentTypeForKpi(kpiType) && it.carbonValue != null }
            .groupBy { it.year }
            .mapValues { (_, reports) -> reports.sumOf { it.carbonValue?.toDouble() ?: 0.0 }.toFloat() }
        
        val fiveYearChartData = (year - 4..year).map { chartYear -> yearlyData[chartYear] ?: 0f }
        
        val allValues = entitiesForYear.mapNotNull { it.carbonValue }
        val averageValue = if (allValues.isNotEmpty()) decimalFormat.format(allValues.average()) else "0"
        val minValue = if (allValues.isNotEmpty()) decimalFormat.format(allValues.minOrNull() ?: 0f) else "0"
        
        val (title, unit) = when(kpiType) {
            "carbon_footprint" -> "Carbon Footprint" to "kg CO₂e"
            "energy_consumption" -> "Konsumsi Energi" to "kWh"
            "water_usage" -> "Penggunaan Air" to "L"
            "tree_planting", "trees_planted" -> "Pohon Tertanam" to "Pohon"
            "waste_management" -> "Pengelolaan Sampah" to "kg"
            "benefit_received", "beneficiary_received" -> "Penerima Manfaat" to "Orang"
            else -> "KPI Detail" to "Unit"
        }
        
        return KpiDetails(
            id = kpiType,
            title = title,
            unit = unit,
            year = year,
            yearlyChartData = yearlyChartData,
            fiveYearChartData = fiveYearChartData,
            averageValue = averageValue,
            minValue = minValue,
            analysis = "Data diambil dari laporan tersimpan lokal untuk $title. Total ${entitiesForYear.size} laporan ditemukan untuk tahun $year."
        )
    }

    /**
     * Refreshes the submissions cache by fetching from API and storing in Room.
     * FIXED: Server data now comes with correct sync flags, so we can safely replace all data
     */
    suspend fun refreshSubmissionsCache(companyId: Int? = null, year: Int? = null) {
        if (!NetworkConnectivityUtil.isOnline(context)) {
            println("DashboardRepository: Cannot refresh cache - device offline")
            return
        }
        
        try {
            // STEP 1: Sync unsynced data first before refresh
            println("DashboardRepository: Syncing unsynced data before cache refresh...")
            val syncResult = syncUnsyncedData()
            when (syncResult) {
                is Resource.Success -> {
                    println("DashboardRepository: Successfully synced ${syncResult.data} unsynced records")
                }
                is Resource.Error -> {
                    println("DashboardRepository: Sync had errors: ${syncResult.message}")
                    // Continue with refresh anyway, but some data might not be synced
                }
                is Resource.Loading -> {
                    println("DashboardRepository: Sync is loading, this should not happen")
                }
            }
            
            // STEP 2: DEBUG - Check sync status before refresh
            val totalBefore = dashboardDao.getTotalRecordCount()
            val syncedBefore = dashboardDao.getSyncedRecordCount()
            val unsyncedBefore = dashboardDao.getUnsyncedRecordCount()
            println("DashboardRepository: BEFORE REFRESH - Total: $totalBefore, Synced: $syncedBefore, Unsynced: $unsyncedBefore")
            
            // STEP 3: Get any remaining unsynced data to preserve (should be minimal after sync)
            val unsyncedData = dashboardDao.getUnsyncedReports()
            println("DashboardRepository: Found ${unsyncedData.size} remaining unsynced records to preserve")
            
            // STEP 4: Refresh from API
            println("DashboardRepository: Refreshing submissions cache...")
            val companyIdString = companyId?.toString()
            val response = dashboardApiService.getAllSubmissions(
                companyId = companyIdString, 
                year = null, // FIXED: Always fetch all years to prevent data culling
                limit = 1000 // FIXED: Set high limit to get ALL data, not just recent 10
            )
            println("DashboardRepository: Cache refresh API call completed with code: ${response.code()}")
            
            if (response.isSuccessful) {
                response.body()?.let { submissionsResponse ->
                    val submissions = submissionsResponse.data
                    println("DashboardRepository: Success! Got ${submissions.size} submissions from API")
                    
                    // STEP 5: Clear ONLY synced data, preserve unsynced
                    dashboardDao.clearSyncedReports() // Only clear isSynced = true
                    
                    // STEP 6: Insert fresh API data (now comes with correct isSynced = true)
                    val entities = submissions.toEntityList()
                    dashboardDao.insertOrUpdateReports(entities)
                    println("DashboardRepository: Inserted ${entities.size} fresh API submissions (all marked as synced)")
                    
                    // DEBUG: Check entity sync flags
                    val sampleEntity = entities.firstOrNull()
                    if (sampleEntity != null) {
                        println("DashboardRepository: Sample API entity sync flags - isSynced: ${sampleEntity.isSynced}, isLocalOnly: ${sampleEntity.isLocalOnly}")
                    }
                    
                    // STEP 7: Re-insert preserved unsynced data (in case of conflicts)
                    unsyncedData.forEach { unsyncedEntity ->
                        try {
                            dashboardDao.insertReport(unsyncedEntity)
                            println("DashboardRepository: Preserved unsynced record ${unsyncedEntity.localId}")
                        } catch (e: Exception) {
                            println("DashboardRepository: Conflict inserting unsynced record ${unsyncedEntity.localId}: ${e.message}")
                        }
                    }
                    
                    // STEP 8: DEBUG - Check sync status after refresh
                    val totalAfter = dashboardDao.getTotalRecordCount()
                    val syncedAfter = dashboardDao.getSyncedRecordCount()
                    val unsyncedAfter = dashboardDao.getUnsyncedRecordCount()
                    println("DashboardRepository: AFTER REFRESH - Total: $totalAfter, Synced: $syncedAfter, Unsynced: $unsyncedAfter")
                    
                    val syncSummary = dashboardDao.getSyncStatusSummary()
                    println("DashboardRepository: Sync Status Summary:")
                    syncSummary.forEach { summary ->
                        println("  - isSynced: ${summary.isSynced}, isLocalOnly: ${summary.isLocalOnly}, count: ${summary.count}")
                    }
                    
                    println("DashboardRepository: Successfully cached ${entities.size} API submissions (synced) + ${unsyncedData.size} unsynced records")
                } ?: run {
                    println("DashboardRepository: Response body was null for submissions refresh")
                }
            } else {
                println("DashboardRepository: Failed to refresh cache. Code: ${response.code()}, Message: ${response.message()}")
            }
        } catch (e: Exception) {
            println("DashboardRepository: Cache refresh failed: ${e.message}")
        }
    }

    /**
     * Debug method to get comprehensive sync status information
     */
    suspend fun getComprehensiveSyncStatus(): String {
        return try {
            val totalRecords = dashboardDao.getTotalRecordCount()
            val syncedRecords = dashboardDao.getSyncedRecordCount()
            val unsyncedRecords = dashboardDao.getUnsyncedRecordCount()
            val syncStatusSummary = dashboardDao.getSyncStatusSummary()

            val statusBuilder = StringBuilder()
            statusBuilder.append("=== COMPREHENSIVE SYNC STATUS ===\n")
            statusBuilder.append("Total Records: $totalRecords\n")
            statusBuilder.append("Synced Records: $syncedRecords\n")
            statusBuilder.append("Unsynced Records: $unsyncedRecords\n")
            statusBuilder.append("Detailed Breakdown:\n")

            syncStatusSummary.forEach { summary ->
                val type = if (summary.isLocalOnly) "Offline-Created" else "Server Data"
                val syncStatus = if (summary.isSynced) "Synced" else "Unsynced"
                statusBuilder.append("  - $type ($syncStatus): ${summary.count} records\n")
            }
            
            statusBuilder.append("===============================")
            statusBuilder.toString()
        } catch (e: Exception) {
            "Error getting sync status: ${e.message}"
        }
    }

} 