package com.example.tumbuhnyata.data.model

import com.google.gson.annotations.SerializedName

/**
 * Represents the overall API response from the /dashboard endpoint.
 */
data class DashboardDataResponse(
    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("data")
    val data: DashboardData?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("error")
    val error: String?
)

/**
 * Contains the main data structure for the dashboard.
 */
data class DashboardData(
    @SerializedName("analytics")
    val analytics: AnalyticsData?,

    @SerializedName("recentSubmissions")
    val recentSubmissions: List<CsrReportModel>?, // Menggunakan CsrReportModel yang sudah ada

    @SerializedName("summary")
    val summary: SummaryData?
)

/**
 * Represents analytics data, including monthly carbon data and total stats.
 */
data class AnalyticsData(
    @SerializedName("year")
    val year: Int?,

    @SerializedName("monthlyData")
    val monthlyData: List<MonthlyCarbonData>?,

    @SerializedName("totalStats")
    val totalStats: TotalStats?
)

/**
 * Represents carbon data for a specific month.
 */
data class MonthlyCarbonData(
    @SerializedName("month")
    val month: Int?,

    @SerializedName("carbon_value") // Sesuai output backend: "carbon_value"
    val carbonValue: Float?
)

/**
 * Represents total statistics for carbon submissions.
 */
data class TotalStats(
    @SerializedName("totalCarbon")
    val totalCarbon: Float?,

    @SerializedName("totalSubmissions")
    val totalSubmissions: Int?
)

/**
 * Represents summary data for the dashboard.
 */
data class SummaryData(
    @SerializedName("currentYearTotal")
    val currentYearTotal: Float?,

    @SerializedName("submissionCount")
    val submissionCount: Int?,

    @SerializedName("lastUpdated")
    val lastUpdated: String? // String representation of a Date
)

// CsrReportModel.kt (yang sudah ada) akan digunakan untuk recentSubmissions
// Jika CsrReportModel belum ada atau berbeda, definisikan di sini atau di file terpisah:
// data class CsrReportModel(
//    @SerializedName("id") val id: Int?,
//    @SerializedName("company_id") val companyId: Int?,
//    @SerializedName("year") val year: Int?,
//    @SerializedName("month") val month: Int?,
//    @SerializedName("carbon_value") val carbonValue: Float?,
//    @SerializedName("document_type") val documentType: String?,
//    @SerializedName("document_name") val documentName: String?,
//    @SerializedName("document_path") val documentPath: String?,
//    @SerializedName("analysis") val analysis: String?,
//    @SerializedName("created_at") val createdAt: String?
// ) 

/**
 * Model untuk response KPI Detail dari backend
 */
data class KpiDetailResponse(
    @SerializedName("success")
    val success: Boolean?,

    @SerializedName("data")
    val data: KpiDetailData?,

    @SerializedName("message")
    val message: String?,

    @SerializedName("error")
    val error: String?
)

/**
 * Data untuk detail KPI tertentu
 */
data class KpiDetailData(
    @SerializedName("kpi_type")
    val kpiType: String?, // carbon_footprint, energy_usage, etc.

    @SerializedName("title")
    val title: String?,

    @SerializedName("unit")
    val unit: String?,

    @SerializedName("yearly_data")
    val yearlyData: List<MonthlyKpiData>?, // Data per bulan dalam tahun

    @SerializedName("multi_year_data")
    val multiYearData: List<YearlyKpiData>?, // Data per tahun untuk 5 tahun

    @SerializedName("statistics")
    val statistics: KpiStatistics?,

    @SerializedName("analysis")
    val analysis: String?
)

/**
 * Data KPI per bulan
 */
data class MonthlyKpiData(
    @SerializedName("month")
    val month: Int?,

    @SerializedName("value")
    val value: Float?,

    @SerializedName("submission_count")
    val submissionCount: Int? // Jumlah submission dalam bulan ini
)

/**
 * Data KPI per tahun
 */
data class YearlyKpiData(
    @SerializedName("year")
    val year: Int?,

    @SerializedName("total_value")
    val totalValue: Float?,

    @SerializedName("submission_count")
    val submissionCount: Int?
)

/**
 * Statistik untuk KPI
 */
data class KpiStatistics(
    @SerializedName("average_value")
    val averageValue: Float?,

    @SerializedName("min_value")
    val minValue: Float?,

    @SerializedName("max_value")
    val maxValue: Float?,

    @SerializedName("total_value")
    val totalValue: Float?
) 