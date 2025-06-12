package com.example.tumbuhnyata.ui.dashboard.kpi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.api.DashboardApiService
import com.example.tumbuhnyata.data.local.AppDatabase
import com.example.tumbuhnyata.data.repository.DashboardRepository
import com.example.tumbuhnyata.data.repository.Resource
import com.example.tumbuhnyata.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

// Data class sesuai struktur KpiDetails di KpiDetailScreen
data class KPIDetailState(
    val kpiDetails: KpiDetails? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class KPIDetailViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(KPIDetailState(isLoading = true))
    val uiState: StateFlow<KPIDetailState> = _uiState.asStateFlow()

    // Manual DI untuk Repository (sama seperti DashboardViewModel)
    private val dashboardRepository: DashboardRepository

    init {
        // Inisialisasi manual repository
        val dashboardApi = RetrofitInstance.dashboardApi
        val dashboardDao = AppDatabase.getDatabase(application).dashboardDao()
        dashboardRepository = DashboardRepository(dashboardApi, dashboardDao, application.applicationContext)
    }

    fun loadKPIDetails(kpiId: String, companyId: Int? = null, year: Int? = null) {
        viewModelScope.launch {
            // Use provided year or default to current year
            val actualYear = year ?: java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
            
            println("KPIDetailViewModel: Loading KPI details for kpiId=$kpiId, companyId=$companyId, year=$actualYear")
            
            dashboardRepository.getKpiDetail(kpiId, companyId, actualYear)
                .collect { resource ->
                    when (resource) {
                        is Resource.Loading -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = true,
                                error = null
                            )
                        }
                        is Resource.Success -> {
                            _uiState.value = KPIDetailState(
                                isLoading = false,
                                kpiDetails = resource.data,
                                error = null
                            )
                        }
                        is Resource.Error -> {
                            _uiState.value = KPIDetailState(
                                isLoading = false,
                                kpiDetails = null, // No more dummy data fallback
                                error = resource.message
                            )
                        }
                    }
                }
        }
    }

    fun retryLoadKPIDetails(kpiId: String, companyId: Int? = null, year: Int? = null) {
        loadKPIDetails(kpiId, companyId, year)
    }
} 