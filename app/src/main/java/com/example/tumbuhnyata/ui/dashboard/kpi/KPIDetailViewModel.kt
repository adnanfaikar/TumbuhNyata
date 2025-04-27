package com.example.tumbuhnyata.ui.dashboard.kpi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data class sesuai struktur KpiDetails di KpiDetailScreen
data class KPIDetailState(
    val kpiDetails: KpiDetails? = null
)

class KPIDetailViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(KPIDetailState())
    val uiState: StateFlow<KPIDetailState> = _uiState.asStateFlow()

    fun loadKPIDetails(kpiId: String) {
        // Data dummy sama persis dengan getKpiDetails di Compose
        val kpiDetails = getKpiDetails(kpiId)
        _uiState.value = KPIDetailState(kpiDetails = kpiDetails)
    }
} 