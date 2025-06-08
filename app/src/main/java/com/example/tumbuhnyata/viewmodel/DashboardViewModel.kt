package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import com.example.tumbuhnyata.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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

data class DashboardState(
    val kpiItems: List<KPIItemState> = emptyList()
)

class DashboardViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DashboardState())
    val uiState: StateFlow<DashboardState> = _uiState.asStateFlow()

    init {
        _uiState.value = DashboardState(
            kpiItems = listOf(
                KPIItemState(
                    title = "Carbon Footprint",
                    topIcon = R.drawable.ic_carbonfootprint,
                    statusText = "100% target",
                    statusPercentageValue = "5%",
                    isUp = true,
                    value = "12.300",
                    unit = "kg CO₂e",
                    targetValue = "10.000 kg CO₂e",
                    onClickRoute = "kpi_detail/carbon_footprint"
                ),
                KPIItemState(
                    title = "Konsumsi Energi",
                    topIcon = R.drawable.ic_konsumsienergi,
                    statusText = "94% target",
                    statusPercentageValue = "8%",
                    isUp = false,
                    value = "8.450",
                    unit = "kWh",
                    targetValue = "9.000 kWh",
                    onClickRoute = "kpi_detail/energy_usage"
                ),
                KPIItemState(
                    title = "Penggunaan Air",
                    topIcon = R.drawable.ic_penggunaanair,
                    statusText = "93% target",
                    statusPercentageValue = "4%",
                    isUp = false,
                    value = "56.000",
                    unit = "L",
                    targetValue = "60.000 L",
                    onClickRoute = "kpi_detail/water_usage"
                ),
                KPIItemState(
                    title = "Pohon Tertanam",
                    topIcon = R.drawable.ic_pohontertanam,
                    statusText = "75% target",
                    statusPercentageValue = "25%",
                    isUp = true,
                    value = "4.500",
                    unit = "Pohon",
                    targetValue = "6.000 Pohon",
                    onClickRoute = "kpi_detail/biodiversity"
                ),
                KPIItemState(
                    title = "Pengelolaan Sampah",
                    topIcon = R.drawable.ic_pengelolaansampah,
                    statusText = "78% target",
                    statusPercentageValue = "30%",
                    isUp = true,
                    value = "7.800",
                    unit = "kg",
                    targetValue = "10.000 Kg",
                    onClickRoute = "kpi_detail/waste"
                ),
                KPIItemState(
                    title = "Penerima Manfaat",
                    topIcon = R.drawable.ic_penerimamanfaat,
                    statusText = "80% target",
                    statusPercentageValue = "12%",
                    isUp = true,
                    value = "12.000",
                    unit = "orang",
                    targetValue = "15.000 Orang",
                    onClickRoute = "kpi_detail/sustainability"
                )
            )
        )
    }
} 