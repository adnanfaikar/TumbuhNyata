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
fun DashboardData.toKpiItemStateList(): List<KPIItemState> {
    val kpiList = mutableListOf<KPIItemState>()
    val decimalFormat = DecimalFormat("#,###.##")

    // DEBUG: Log data yang diterima dari backend
    println("DataMapper DEBUG - Dashboard data received:")
    println("  summary.currentYearTotal: ${this.summary?.currentYearTotal}")
    println("  analytics.totalStats.totalCarbon: ${this.analytics?.totalStats?.totalCarbon}")
    println("  analytics.year: ${this.analytics?.year}")
    println("  summary.submissionCount: ${this.summary?.submissionCount}")

    // UTAMA: 1 KPI Card untuk Carbon Emission berdasarkan data summary atau analytics
    val carbonValue = this.summary?.currentYearTotal ?: this.analytics?.totalStats?.totalCarbon ?: 0f
    val year = this.analytics?.year ?: java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    val submissionCount = this.summary?.submissionCount ?: this.analytics?.totalStats?.totalSubmissions ?: 0
    
    println("DataMapper DEBUG - Calculated values:")
    println("  carbonValue: $carbonValue")
    println("  year: $year")
    println("  submissionCount: $submissionCount")
    
    // Hitung persentase berdasarkan target (misal target tahunan 10,000 kg CO₂e)
    val targetValue = 10000f // Target tahunan dalam kg CO₂e
    val achievementPercentage = if (targetValue > 0f) (carbonValue / targetValue) * 100f else 0f
    val isUp = carbonValue > (targetValue * 0.5f) // Up jika > 50% target
    
    kpiList.add(
        KPIItemState(
            title = "Carbon Emission",
            topIcon = R.drawable.ic_carbonfootprint,
            statusText = "${achievementPercentage.toInt()}% dari target",
            statusPercentageValue = if (achievementPercentage > 0) "${achievementPercentage.toInt()}%" else "",
            isUp = isUp,
            value = decimalFormat.format(carbonValue),
            unit = "kg CO₂e",
            targetValue = "Target: ${decimalFormat.format(targetValue)} kg CO₂e",
            onClickRoute = "kpi_detail/carbon_footprint"
        )
    )

    // Fallback jika tidak ada data
    if (kpiList.isEmpty() || carbonValue == 0f) {
        kpiList.clear()
        kpiList.add(
            KPIItemState(
                title = "Carbon Emission",
                topIcon = R.drawable.ic_carbonfootprint,
                statusText = "Belum ada data",
                statusPercentageValue = "",
                isUp = false,
                value = "0",
                unit = "kg CO₂e",
                targetValue = "Target: ${decimalFormat.format(targetValue)} kg CO₂e",
                onClickRoute = "kpi_detail/carbon_footprint"
            )
        )
    }

    return kpiList
}

// Mapper dari List Entity Room (CsrReportEntity) ke List UI State Model (KPIItemState) untuk OFFLINE
// FIXED: Agregasi semua data menjadi satu KPI Carbon Emission seperti mode online
fun List<CsrReportEntity>.toKpiItemStateListForOffline(filterYear: Int? = null): List<KPIItemState> {
    if (this.isEmpty()) {
        return emptyList()
    }
    val kpiList = mutableListOf<KPIItemState>()
    val decimalFormat = DecimalFormat("#,###.##")

    // AGREGASI: Filter by specified year (consistent with backend logic)
    // Smart Year Detection: Use latest year with data if no year specified (same as backend)
    val targetYear = filterYear ?: run {
        val availableYears = this.map { it.year }.distinct().sortedDescending()
        availableYears.firstOrNull() ?: java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)
    }
    val filteredData = this.filter { it.year == targetYear }
    val totalCarbonValue = filteredData.mapNotNull { it.carbonValue }.sum()
    val submissionCount = filteredData.size
    
    // Hitung persentase berdasarkan target (sama seperti mode online)
    val targetValue = 10000f // Target tahunan dalam kg CO₂e
    val achievementPercentage = if (targetValue > 0f) (totalCarbonValue / targetValue) * 100f else 0f
    val isUp = totalCarbonValue > (targetValue * 0.5f) // Up jika > 50% target
    
    println("DataMapper DEBUG - Offline Aggregation:")
    println("  filterYear param: $filterYear")
    println("  availableYears: ${this.map { it.year }.distinct().sortedDescending()}")
    println("  targetYear: $targetYear (smart detection)")
    println("  allDataCount: ${this.size}")
    println("  filteredDataCount: ${filteredData.size}")
    println("  filteredData: ${filteredData.map { "${it.year}/${it.month}: ${it.carbonValue}" }}")
    println("  totalCarbonValue: $totalCarbonValue")
    println("  submissionCount: $submissionCount")
    println("  achievementPercentage: $achievementPercentage")

    // Buat SATU KPI Card untuk Carbon Emission (sama seperti mode online)
    kpiList.add(
        KPIItemState(
            title = "Carbon Emission",
            topIcon = R.drawable.ic_carbonfootprint,
            statusText = "${achievementPercentage.toInt()}% dari target",
            statusPercentageValue = if (achievementPercentage > 0) "${achievementPercentage.toInt()}%" else "",
            isUp = isUp,
            value = decimalFormat.format(totalCarbonValue),
            unit = "kg CO₂e",
            targetValue = "Target: ${decimalFormat.format(targetValue)} kg CO₂e",
            onClickRoute = "kpi_detail/carbon_footprint" // Sama seperti online
        )
    )
    
    // Fallback jika tidak ada data atau total = 0
    if (kpiList.isEmpty() || totalCarbonValue == 0f) {
        kpiList.clear()
        kpiList.add(
            KPIItemState(
                title = "Carbon Emission",
                topIcon = R.drawable.ic_carbonfootprint, 
                statusText = "Belum ada data",
                statusPercentageValue = "",
                isUp = false, 
                value = "0",
                unit = "kg CO₂e",
                targetValue = "Target: ${decimalFormat.format(targetValue)} kg CO₂e",
                onClickRoute = "kpi_detail/carbon_footprint"
            )
        )
    }

    return kpiList
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
fun KpiDetailData.toKpiDetails(): KpiDetails {
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
    return when (kpiId) {
        "carbon_footprint" -> KpiDetails(
            id = kpiId,
            title = "Carbon Footprint",
            unit = "kg CO₂e",
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
            yearlyChartData = List(12) { 50f + (it * 5f) },
            fiveYearChartData = List(5) { 500f + (it * 100f) },
            averageValue = "100",
            minValue = "50",
            analysis = "Data template untuk KPI yang belum diintegrasikan dengan backend."
        )
    }
} 