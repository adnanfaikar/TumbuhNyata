package com.example.tumbuhnyata.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.api.DashboardApiService
import com.example.tumbuhnyata.data.local.AppDatabase
import com.example.tumbuhnyata.data.repository.DashboardRepository
import com.example.tumbuhnyata.data.repository.Resource
import com.example.tumbuhnyata.data.network.RetrofitInstance
import com.example.tumbuhnyata.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import java.util.concurrent.atomic.AtomicBoolean

// Data class sesuai struktur KPIItem di DashboardScreen
data class KPIItemState(
    val title: String,
    val topIcon: Int,
    val statusText: String,
    val statusPercentageValue: String,
    val isUp: Boolean,
    val value: String,
    val unit: String,
    val targetValue: String,
    val onClickRoute: String
)

// DashboardState diupdate untuk menyertakan loading dan error state
data class DashboardState(
    val kpiItems: List<KPIItemState> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val syncStatus: String? = null // Added sync status for user feedback
)

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(DashboardState(isLoading = true)) // Mulai dengan isLoading = true
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    private val dashboardRepository: DashboardRepository
    private val profileRepository = NetworkModule.profileRepository
    private val isPeriodicSyncRunning = AtomicBoolean(false)
    private var currentCompanyId: Int? = null

    init {
        // Inisialisasi manual repository (sementara)
        val dashboardApi = RetrofitInstance.dashboardApi // Mengambil instance API dari RetrofitInstance
        val dashboardDao = AppDatabase.getInstance(application).dashboardDao() // Mengambil DAO
        dashboardRepository = DashboardRepository(dashboardApi, dashboardDao, application.applicationContext)
        
        // Load company ID first, then load dashboard
        loadCompanyIdAndDashboard()
        startPeriodicAutoSync() // RE-ENABLING: Endpoints are fixed, safe to re-enable
    }

    private fun loadCompanyIdAndDashboard() {
        viewModelScope.launch {
            try {
                // Get company ID from profile
                val profile = profileRepository.getUserProfile()
                currentCompanyId = profile?.id ?: 1 // Default to 1 if no profile
                println("DashboardViewModel: Using companyId = $currentCompanyId")
                
                // Load dashboard with company ID
                loadDashboardItems(currentCompanyId)
            } catch (e: Exception) {
                println("DashboardViewModel: Failed to load profile, using default companyId = 1")
                currentCompanyId = 1
                loadDashboardItems(currentCompanyId)
            }
        }
    }

    fun loadDashboardItems(companyId: Int? = null, year: Int? = null) { // Tambahkan parameter jika perlu
        viewModelScope.launch {
            // FIXED: Ensure company ID is loaded before API call (same as KPIDetailViewModel)
            val actualCompanyId = companyId ?: run {
                // If currentCompanyId is null, load it synchronously
                if (currentCompanyId == null) {
                    try {
                        val profile = profileRepository.getUserProfile()
                        currentCompanyId = profile?.id ?: 1
                        println("DashboardViewModel: Loaded companyId synchronously = $currentCompanyId")
                    } catch (e: Exception) {
                        println("DashboardViewModel: Failed to load profile synchronously, using default companyId = 1")
                        currentCompanyId = 1
                    }
                }
                currentCompanyId ?: 1
            }
            
            dashboardRepository.getDashboardKpiItems(companyId = actualCompanyId, year = year)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                // error = null // Opsional: reset error saat loading baru
                            )
                        }
                        is Resource.Success -> {
                            _uiState.value = DashboardState(
                                isLoading = false,
                                kpiItems = resource.data ?: emptyList(),
                                error = null,
                                syncStatus = _uiState.value.syncStatus // Preserve sync status
                            )
                        }
                        is Resource.Error -> {
                            _uiState.value = DashboardState(
                                isLoading = false,
                                // Tetap tampilkan data lama jika ada saat error, atau kosongkan
                                kpiItems = resource.data ?: _uiState.value.kpiItems, // Menampilkan data lama (dari cache) jika ada
                                error = resource.message,
                                syncStatus = _uiState.value.syncStatus // Preserve sync status
                            )
                        }
                    }
                }
        }
    }

    /**
     * Starts periodic auto-sync to upload unsynced data when online
     * FIXED: Proper exception handling to prevent infinite loops
     */
    private fun startPeriodicAutoSync() {
        if (isPeriodicSyncRunning.compareAndSet(false, true)) {
            viewModelScope.launch {
                try {
                    while (isPeriodicSyncRunning.get()) {
                        try {
                            // Sync every 2 minutes when online
                            delay(120_000) // 2 minutes
                            
                            val syncResult = dashboardRepository.syncUnsyncedData()
                            when (syncResult) {
                                is Resource.Success -> {
                                    if (syncResult.data != null && syncResult.data!! > 0) {
                                        _uiState.value = _uiState.value.copy(
                                            syncStatus = "Successfully synced ${syncResult.data} items"
                                        )
                                        println("DashboardViewModel: Periodic sync completed - ${syncResult.data} items synced")
                                    }
                                }
                                is Resource.Error -> {
                                    println("DashboardViewModel: Periodic sync failed: ${syncResult.message}")
                                    // Don't show error to user for background sync failures
                                }
                                is Resource.Loading -> {
                                    println("DashboardViewModel: Periodic sync is loading")
                                }
                            }
                        } catch (e: kotlinx.coroutines.CancellationException) {
                            // Job was cancelled - this is normal when ViewModel is destroyed
                            println("DashboardViewModel: Periodic sync cancelled (normal shutdown)")
                            break // Exit the loop gracefully
                        } catch (e: Exception) {
                            println("DashboardViewModel: Periodic sync exception: ${e.message}")
                            // Wait before retrying to prevent rapid fire
                            delay(30_000) // Wait 30 seconds before retry
                        }
                    }
                } catch (e: kotlinx.coroutines.CancellationException) {
                    // Outer job cancelled
                    println("DashboardViewModel: Periodic sync job cancelled")
                } catch (e: Exception) {
                    println("DashboardViewModel: Critical periodic sync error: ${e.message}")
                } finally {
                    isPeriodicSyncRunning.set(false)
                    println("DashboardViewModel: Periodic sync stopped")
                }
            }
        }
    }

    /**
     * Manual sync trigger for user-initiated sync
     */
    fun triggerManualSync() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(syncStatus = "Syncing...")
            
            val syncResult = dashboardRepository.syncUnsyncedData()
            when (syncResult) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(
                        syncStatus = if (syncResult.data != null && syncResult.data!! > 0) {
                            "Successfully synced ${syncResult.data} items"
                        } else {
                            "All data is already synced"
                        }
                    )
                    // Refresh dashboard after manual sync
                    loadDashboardItems()
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        syncStatus = "Sync failed: ${syncResult.message}"
                    )
                }
                is Resource.Loading -> {
                    // Keep showing "Syncing..."
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        isPeriodicSyncRunning.set(false) // Stop background sync when ViewModel is destroyed
    }
} 