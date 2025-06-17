package com.example.tumbuhnyata.data.mapper

import com.example.tumbuhnyata.R // Pastikan R diimport dengan benar dan ikon tersedia
import com.example.tumbuhnyata.data.local.entity.CsrReportEntity
import com.example.tumbuhnyata.data.model.CsrReportModel // Model dari API
import com.example.tumbuhnyata.data.model.DashboardData // Model baru untuk /dashboard endpoint
import com.example.tumbuhnyata.viewmodel.KPIItemState // UI State Model dari DashboardViewModel
import java.text.DecimalFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.tumbuhnyata.data.model.KpiDetailData
import com.example.tumbuhnyata.ui.dashboard.kpi.KpiDetails

// Mapper dari Model API (CsrReportModel) ke Entity Room (CsrReportEntity)
fun CsrReportModel.toEntity(): CsrReportEntity {
    return CsrReportEntity(
        id = this.id,
        companyId = this.companyId,
        year = this.year,
        month = this.month,
        carbonValue = this.carbonValue,
        documentType = this.documentType,
        documentName = this.documentName,
        documentPath = this.documentPath,
        analysis = this.analysis,
        createdAt = this.createdAt,
        isSynced = true,
        isLocalOnly = false,
        serverId = this.id,
        localId = null,
        lastModified = this.createdAt,
        syncRetryCount = 0
    )
}

fun List<CsrReportModel>.toEntityList(): List<CsrReportEntity> {
    return this.map { it.toEntity() }
}

// Helper function untuk parsing tanggal dengan beberapa format umum
private fun parseDateString(dateString: String?): Date? {
    if (dateString == null) return null
    val formats = listOf(
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault()),
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault()),
        SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss 'GMT'Z (zzzz)", Locale.ENGLISH) // Format dari new Date().toString()
    )
    for (format in formats) {
        try {
            return format.parse(dateString)
        } catch (e: ParseException) {
            // Lanjutkan mencoba format berikutnya
        }
    }
    return null
}

// Mapper dari data /dashboard endpoint (DashboardData) ke List UI State Model (KPIItemState)
// Updated: Menggunakan carbon_value sebagai value general untuk semua KPI berdasarkan document_type
fun DashboardData.toKpiItemStateList(): List<KPIItemState> {
    val kpiList = mutableListOf<KPIItemState>()
    val decimalFormat = DecimalFormat("#,###.##")

    // Ambil data dari recent submissions dan group berdasarkan document_type
    val recentSubmissions = this.recentSubmissions ?: emptyList()
    val dataByType = recentSubmissions.groupBy { it.documentType }
    
    // 1. Carbon Footprint (data_emisi)
    val carbonData = dataByType["data_emisi"] ?: emptyList()
    val totalCarbon = carbonData.mapNotNull { it.carbonValue }.sum()
    val carbonTarget = 10000f
    val carbonPercentage = if (carbonTarget > 0f) (totalCarbon / carbonTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Carbon Footprint",
            topIcon = R.drawable.ic_carbonfootprint,
            statusText = "${carbonPercentage.toInt()}% target",
            statusPercentageValue = "5%",
            isUp = true,
            value = decimalFormat.format(totalCarbon),
            unit = "kg CO₂e",
            targetValue = "Target: ${decimalFormat.format(carbonTarget)} kg CO₂e",
            onClickRoute = "kpi_detail/carbon_footprint"
        )
    )

    // 2. Konsumsi Energi (data_energi) - UPDATED: default to 0
    val energyData = dataByType["data_energi"] ?: emptyList()
    val totalEnergy = energyData.mapNotNull { it.carbonValue }.sum()
    val energyTarget = 9000f
    val energyPercentage = if (energyTarget > 0f) (totalEnergy / energyTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Konsumsi Energi",
            topIcon = R.drawable.ic_konsumsienergi,
            statusText = if (totalEnergy > 0) "${energyPercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalEnergy > 0) "${energyPercentage.toInt()}%" else "0%",
            isUp = false,
            value = decimalFormat.format(totalEnergy), // Akan jadi "0" jika tidak ada data
            unit = "kWh",
            targetValue = "Target: ${decimalFormat.format(energyTarget)} kWh",
            onClickRoute = "kpi_detail/energy_consumption"
        )
    )

    // 3. Penggunaan Air (data_air) - UPDATED: default to 0
    val waterData = dataByType["data_air"] ?: emptyList()
    val totalWater = waterData.mapNotNull { it.carbonValue }.sum()
    val waterTarget = 60000f
    val waterPercentage = if (waterTarget > 0f) (totalWater / waterTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Penggunaan Air",
            topIcon = R.drawable.ic_penggunaanair,
            statusText = if (totalWater > 0) "${waterPercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalWater > 0) "${waterPercentage.toInt()}%" else "0%",
            isUp = false,
            value = decimalFormat.format(totalWater), // Akan jadi "0" jika tidak ada data
            unit = "L",
            targetValue = "Target: ${decimalFormat.format(waterTarget)} L",
            onClickRoute = "kpi_detail/water_usage"
        )
    )

    // 4. Pohon Tertanam (data_pohon) - UPDATED: default to 0
    val treeData = dataByType["data_pohon"] ?: emptyList()
    val totalTrees = treeData.mapNotNull { it.carbonValue }.sum()
    val treeTarget = 6000f
    val treePercentage = if (treeTarget > 0f) (totalTrees / treeTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Pohon Tertanam",
            topIcon = R.drawable.ic_pohontertanam,
            statusText = if (totalTrees > 0) "${treePercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalTrees > 0) "${treePercentage.toInt()}%" else "0%",
            isUp = totalTrees > 0,
            value = decimalFormat.format(totalTrees), // Akan jadi "0" jika tidak ada data
            unit = "Pohon",
            targetValue = "Target: ${decimalFormat.format(treeTarget)} Pohon",
            onClickRoute = "kpi_detail/tree_planting"
        )
    )

    // 5. Pengelolaan Sampah (data_sampah) - UPDATED: default to 0
    val wasteData = dataByType["data_sampah"] ?: emptyList()
    val totalWaste = wasteData.mapNotNull { it.carbonValue }.sum()
    val wasteTarget = 10000f
    val wastePercentage = if (wasteTarget > 0f) (totalWaste / wasteTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Pengelolaan Sampah",
            topIcon = R.drawable.ic_pengelolaansampah,
            statusText = if (totalWaste > 0) "${wastePercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalWaste > 0) "${wastePercentage.toInt()}%" else "0%",
            isUp = totalWaste > 0,
            value = decimalFormat.format(totalWaste), // Akan jadi "0" jika tidak ada data
            unit = "kg",
            targetValue = "Target: ${decimalFormat.format(wasteTarget)} kg",
            onClickRoute = "kpi_detail/waste_management"
        )
    )

    // 6. Penerima Manfaat (data_manfaat) - UPDATED: default to 0
    val beneficiaryData = dataByType["data_manfaat"] ?: emptyList()
    val totalBeneficiaries = beneficiaryData.mapNotNull { it.carbonValue }.sum()
    val beneficiaryTarget = 15000f
    val beneficiaryPercentage = if (beneficiaryTarget > 0f) (totalBeneficiaries / beneficiaryTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Penerima Manfaat",
            topIcon = R.drawable.ic_penerimamanfaat,
            statusText = if (totalBeneficiaries > 0) "${beneficiaryPercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalBeneficiaries > 0) "${beneficiaryPercentage.toInt()}%" else "0%",
            isUp = totalBeneficiaries > 0,
            value = decimalFormat.format(totalBeneficiaries), // Akan jadi "0" jika tidak ada data
            unit = "Orang",
            targetValue = "Target: ${decimalFormat.format(beneficiaryTarget)} Orang",
            onClickRoute = "kpi_detail/benefit_received"
        )
    )

    return kpiList
}

// Mapper dari List Entity Room (CsrReportEntity) ke List UI State Model (KPIItemState) untuk OFFLINE
// Updated: Agregasi berdasarkan document_type menggunakan carbon_value sebagai value general
fun List<CsrReportEntity>.toKpiItemStateListForOffline(filterYear: Int? = null): List<KPIItemState> {
    if (this.isEmpty()) {
        return createDefaultKpiItems()
    }
    val kpiList = mutableListOf<KPIItemState>()
    val decimalFormat = DecimalFormat("#,###.##")

    // Smart Year Detection: Use latest year with data if no year specified
    val targetYear = filterYear ?: run {
        val availableYears = this.map { it.year }.distinct().sortedDescending()
        availableYears.firstOrNull() ?: java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    }
    val filteredData = this.filter { it.year == targetYear }
    
    // Group dan agregasi berdasarkan document_type
    val dataByType = filteredData.groupBy { it.documentType }
    
    // 1. Carbon Footprint (data_emisi)
    val carbonData = dataByType["data_emisi"] ?: emptyList()
    val totalCarbon = carbonData.mapNotNull { it.carbonValue }.sum()
    val carbonTarget = 10000f
    val carbonPercentage = if (carbonTarget > 0f) (totalCarbon / carbonTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Carbon Footprint",
            topIcon = R.drawable.ic_carbonfootprint,
            statusText = "${carbonPercentage.toInt()}% target",
            statusPercentageValue = "5%",
            isUp = true,
            value = decimalFormat.format(totalCarbon),
            unit = "kg CO₂e",
            targetValue = "Target: ${decimalFormat.format(carbonTarget)} kg CO₂e",
            onClickRoute = "kpi_detail/carbon_footprint"
        )
    )

    // 2. Konsumsi Energi (data_energi) - UPDATED: default to 0
    val energyData = dataByType["data_energi"] ?: emptyList()
    val totalEnergy = energyData.mapNotNull { it.carbonValue }.sum()
    val energyTarget = 9000f
    val energyPercentage = if (energyTarget > 0f) (totalEnergy / energyTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Konsumsi Energi",
            topIcon = R.drawable.ic_konsumsienergi,
            statusText = if (totalEnergy > 0) "${energyPercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalEnergy > 0) "${energyPercentage.toInt()}%" else "0%",
            isUp = false,
            value = decimalFormat.format(totalEnergy), // Akan jadi "0" jika tidak ada data
            unit = "kWh",
            targetValue = "Target: ${decimalFormat.format(energyTarget)} kWh",
            onClickRoute = "kpi_detail/energy_consumption"
        )
    )

    // 3. Penggunaan Air (data_air) - UPDATED: default to 0
    val waterData = dataByType["data_air"] ?: emptyList()
    val totalWater = waterData.mapNotNull { it.carbonValue }.sum()
    val waterTarget = 60000f
    val waterPercentage = if (waterTarget > 0f) (totalWater / waterTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Penggunaan Air",
            topIcon = R.drawable.ic_penggunaanair,
            statusText = if (totalWater > 0) "${waterPercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalWater > 0) "${waterPercentage.toInt()}%" else "0%",
            isUp = false,
            value = decimalFormat.format(totalWater), // Akan jadi "0" jika tidak ada data
            unit = "L",
            targetValue = "Target: ${decimalFormat.format(waterTarget)} L",
            onClickRoute = "kpi_detail/water_usage"
        )
    )

    // 4. Pohon Tertanam (data_pohon) - UPDATED: default to 0
    val treeData = dataByType["data_pohon"] ?: emptyList()
    val totalTrees = treeData.mapNotNull { it.carbonValue }.sum()
    val treeTarget = 6000f
    val treePercentage = if (treeTarget > 0f) (totalTrees / treeTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Pohon Tertanam",
            topIcon = R.drawable.ic_pohontertanam,
            statusText = if (totalTrees > 0) "${treePercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalTrees > 0) "${treePercentage.toInt()}%" else "0%",
            isUp = totalTrees > 0,
            value = decimalFormat.format(totalTrees), // Akan jadi "0" jika tidak ada data
            unit = "Pohon",
            targetValue = "Target: ${decimalFormat.format(treeTarget)} Pohon",
            onClickRoute = "kpi_detail/tree_planting"
        )
    )

    // 5. Pengelolaan Sampah (data_sampah) - UPDATED: default to 0
    val wasteData = dataByType["data_sampah"] ?: emptyList()
    val totalWaste = wasteData.mapNotNull { it.carbonValue }.sum()
    val wasteTarget = 10000f
    val wastePercentage = if (wasteTarget > 0f) (totalWaste / wasteTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Pengelolaan Sampah",
            topIcon = R.drawable.ic_pengelolaansampah,
            statusText = if (totalWaste > 0) "${wastePercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalWaste > 0) "${wastePercentage.toInt()}%" else "0%",
            isUp = totalWaste > 0,
            value = decimalFormat.format(totalWaste), // Akan jadi "0" jika tidak ada data
            unit = "kg",
            targetValue = "Target: ${decimalFormat.format(wasteTarget)} kg",
            onClickRoute = "kpi_detail/waste_management"
        )
    )

    // 6. Penerima Manfaat (data_manfaat) - UPDATED: default to 0
    val beneficiaryData = dataByType["data_manfaat"] ?: emptyList()
    val totalBeneficiaries = beneficiaryData.mapNotNull { it.carbonValue }.sum()
    val beneficiaryTarget = 15000f
    val beneficiaryPercentage = if (beneficiaryTarget > 0f) (totalBeneficiaries / beneficiaryTarget) * 100f else 0f
    
    kpiList.add(
        KPIItemState(
            title = "Penerima Manfaat",
            topIcon = R.drawable.ic_penerimamanfaat,
            statusText = if (totalBeneficiaries > 0) "${beneficiaryPercentage.toInt()}% target" else "0% target",
            statusPercentageValue = if (totalBeneficiaries > 0) "${beneficiaryPercentage.toInt()}%" else "0%",
            isUp = totalBeneficiaries > 0,
            value = decimalFormat.format(totalBeneficiaries), // Akan jadi "0" jika tidak ada data
            unit = "Orang",
            targetValue = "Target: ${decimalFormat.format(beneficiaryTarget)} Orang",
            onClickRoute = "kpi_detail/benefit_received"
        )
    )

    return kpiList
}

// Helper function untuk default KPI items ketika tidak ada data - UPDATED: all values to 0
private fun createDefaultKpiItems(): List<KPIItemState> {
    return listOf(
        KPIItemState(
            title = "Carbon Footprint",
            topIcon = R.drawable.ic_carbonfootprint,
            statusText = "0% target",
            statusPercentageValue = "0%",
            isUp = false,
            value = "0",
            unit = "kg CO₂e",
            targetValue = "Target: 10.000 kg CO₂e",
            onClickRoute = "kpi_detail/carbon_footprint"
        ),
        KPIItemState(
            title = "Konsumsi Energi",
            topIcon = R.drawable.ic_konsumsienergi,
            statusText = "0% target",
            statusPercentageValue = "0%",
            isUp = false,
            value = "0",
            unit = "kWh",
            targetValue = "Target: 9.000 kWh",
            onClickRoute = "kpi_detail/energy_consumption"
        ),
        KPIItemState(
            title = "Penggunaan Air",
            topIcon = R.drawable.ic_penggunaanair,
            statusText = "0% target",
            statusPercentageValue = "0%",
            isUp = false,
            value = "0",
            unit = "L",
            targetValue = "Target: 60.000 L",
            onClickRoute = "kpi_detail/water_usage"
        ),
        KPIItemState(
            title = "Pohon Tertanam",
            topIcon = R.drawable.ic_pohontertanam,
            statusText = "0% target",
            statusPercentageValue = "0%",
            isUp = false,
            value = "0",
            unit = "Pohon",
            targetValue = "Target: 6.000 Pohon",
            onClickRoute = "kpi_detail/tree_planting"
        ),
        KPIItemState(
            title = "Pengelolaan Sampah",
            topIcon = R.drawable.ic_pengelolaansampah,
            statusText = "0% target",
            statusPercentageValue = "0%",
            isUp = false,
            value = "0",
            unit = "kg",
            targetValue = "Target: 10.000 kg",
            onClickRoute = "kpi_detail/waste_management"
        ),
        KPIItemState(
            title = "Penerima Manfaat",
            topIcon = R.drawable.ic_penerimamanfaat,
            statusText = "0% target",
            statusPercentageValue = "0%",
            isUp = false,
            value = "0",
            unit = "Orang",
            targetValue = "Target: 15.000 Orang",
            onClickRoute = "kpi_detail/benefit_received"
        )
    )
}

// Mapper tunggal dari Entity ke KPIItemState jika dibutuhkan (mungkin kurang umum untuk dashboard agregat)
fun CsrReportEntity.toKpiItemState(): KPIItemState {
    val currentValue = this.carbonValue ?: 0.0f
    val targetValue = 10000.0f // Contoh target
    val unit = "kg CO₂e"
    val decimalFormat = DecimalFormat("#,###.##")
    val percentageToTarget = if (targetValue > 0f) (currentValue / targetValue) * 100f else 0f

    return KPIItemState(
        title = "Laporan ${this.documentType} (${this.year})",
        topIcon = R.drawable.ic_carbonfootprint, // Pastikan ikon ini ada
        statusText = "${decimalFormat.format(percentageToTarget)}% target",
        statusPercentageValue = "N/A", // Membutuhkan logika perbandingan yang lebih kompleks
        isUp = currentValue > (targetValue * 0.8), // Contoh: up jika > 80% target
        value = decimalFormat.format(currentValue),
        unit = unit,
        targetValue = "${decimalFormat.format(targetValue)} $unit",
        onClickRoute = "kpi_detail/individual_report_${this.id}"
    )
}

// ===== MAPPERS UNTUK KPI DETAIL =====

/**
 * Mapper dari KpiDetailData (API response) ke KpiDetails (UI state)
 */
fun KpiDetailData.toKpiDetails(year: Int): KpiDetails {
    val decimalFormat = DecimalFormat("#,###.##")
    
    // DEBUG: Log data yang diterima untuk KPI detail
    println("DataMapper DEBUG - KPI Detail data received:")
    println("  kpiType: ${this.kpiType}")
    println("  title: ${this.title}")
    println("  yearlyData size: ${this.yearlyData?.size}")
    println("  yearlyData content: ${this.yearlyData}")
    println("  multiYearData size: ${this.multiYearData?.size}")
    println("  multiYearData content: ${this.multiYearData}")
    
    // Convert monthly data to list of floats for chart
    val yearlyChartData = this.yearlyData?.mapNotNull { monthData ->
        monthData.value
    } ?: List(12) { 0f } // Default 12 months dengan value 0
    
    // Convert multi-year data to list of floats for chart  
    val fiveYearChartData = this.multiYearData?.mapNotNull { yearData ->
        yearData.totalValue
    } ?: List(5) { 0f } // Default 5 years dengan value 0
    
    println("DataMapper DEBUG - Converted chart data:")
    println("  yearlyChartData: $yearlyChartData")
    println("  fiveYearChartData: $fiveYearChartData")
    
    // Get statistics
    val averageValue = this.statistics?.averageValue?.let { 
        decimalFormat.format(it) 
    } ?: "0"
    
    val minValue = this.statistics?.minValue?.let { 
        decimalFormat.format(it) 
    } ?: "0"
    
    return KpiDetails(
        id = this.kpiType ?: "unknown",
        title = this.title ?: "KPI Detail",
        unit = this.unit ?: "unit",
        year = year,
        yearlyChartData = yearlyChartData,
        fiveYearChartData = fiveYearChartData,
        averageValue = averageValue,
        minValue = minValue,
        analysis = this.analysis ?: "Analisis tidak tersedia."
    )
}

/**
 * Fallback mapper dari dummy data untuk KPI yang belum diintegrasikan
 * Ini akan digunakan jika API belum ready atau sebagai fallback
 */
fun createDummyKpiDetails(kpiId: String): KpiDetails {
    val currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    return when (kpiId) {
        "carbon_footprint" -> KpiDetails(
            id = kpiId,
            title = "Carbon Footprint",
            unit = "kg CO₂e",
            year = currentYear,
            yearlyChartData = listOf(65f, 75f, 85f, 72f, 93f, 80f, 100f, 110f, 105f, 115f, 110f, 130f),
            fiveYearChartData = listOf(850f, 920f, 1050f, 980f, 1230f),
            averageValue = "95.8",
            minValue = "65",
            analysis = "Data ini berasal dari template sementara. Jejak karbon menunjukkan tren peningkatan yang perlu dikontrol melalui program CSR yang lebih efektif."
        )
        else -> KpiDetails(
            id = kpiId,
            title = "KPI Detail",
            unit = "unit",
            year = currentYear,
            yearlyChartData = List(12) { 50f + (it * 5f) },
            fiveYearChartData = List(5) { 500f + (it * 100f) },
            averageValue = "100",
            minValue = "50",
            analysis = "Data template untuk KPI yang belum diintegrasikan dengan backend."
        )
    }
} 