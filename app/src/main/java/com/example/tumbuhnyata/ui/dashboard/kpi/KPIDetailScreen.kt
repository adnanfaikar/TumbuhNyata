package com.example.tumbuhnyata.ui.dashboard.kpi

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons

import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import androidx.compose.runtime.*
import com.example.tumbuhnyata.ui.dashboard.kpi.components.KpiFilterSwitch
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlin.math.roundToInt
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLineCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberLine
import kotlinx.coroutines.runBlocking
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.LineCartesianLayer // Use wildcard import for cartesian functions
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.shape.markerCorneredShape
import com.patrykandpatrick.vico.core.cartesian.marker.CartesianMarker
import com.patrykandpatrick.vico.core.cartesian.marker.DefaultCartesianMarker
import com.example.tumbuhnyata.ui.dashboard.kpi.components.VicoLineChart


// Enhanced data class for KPI details with all related data
data class KpiDetails(
    val id: String,
    val title: String,
    val unit: String,
    val yearlyChartData: List<Float>, // Monthly data points for yearly view
    val fiveYearChartData: List<Float>, // Yearly data points for 5-year view
    val averageValue: String,
    val minValue: String,
    val analysis: String
)

// Function to get placeholder data based on ID
fun getKpiDetails(kpiId: String): KpiDetails {
    // PLACEHOLDER DATA - Replace with real data from API/database in the future
    return when (kpiId) {
        "carbon_footprint" -> KpiDetails(
            id = kpiId,
            title = "Carbon Footprint",
            unit = "kg CO₂e",
            yearlyChartData = listOf(65f, 75f, 85f, 72f, 93f, 80f, 100f, 110f, 105f, 115f, 110f, 130f),
            fiveYearChartData = listOf(850f, 920f, 1050f, 980f, 1230f),
            averageValue = "139",
            minValue = "68",
            analysis = "Jejak karbon meningkat 8% minggu ini, seiring distribusi logistik program bantuan sosial. Pertimbangkan opsi pengiriman ramah lingkungan agar dampak sosial tetap tinggi tanpa menambah emisi."
        )
        "energy_usage" -> KpiDetails(
            id = kpiId,
            title = "Konsumsi Energi",
            unit = "kWh",
            yearlyChartData = listOf(245f, 275f, 305f, 290f, 320f, 330f, 290f, 310f, 340f, 320f, 350f, 365f),
            fiveYearChartData = listOf(3200f, 3450f, 3600f, 3750f, 3890f),
            averageValue = "321",
            minValue = "245",
            analysis = "Konsumsi energi meningkat 5% dari bulan lalu, sebagian besar terjadi pada jam operasional tinggi. Pertimbangkan pengaturan suhu AC dan penggunaan peralatan hemat energi."
        )
        "water_usage" -> KpiDetails(
            id = kpiId,
            title = "Penggunaan Air",
            unit = "m³",
            yearlyChartData = listOf(42f, 48f, 53f, 45f, 50f, 55f, 47f, 49f, 52f, 50f, 54f, 57f),
            fiveYearChartData = listOf(520f, 550f, 590f, 570f, 610f),
            averageValue = "50.2",
            minValue = "42",
            analysis = "Penggunaan air relatif stabil dengan peningkatan kecil 3% dalam tiga bulan terakhir. Implementasi sistem pengolahan air daur ulang akan membantu mengurangi konsumsi keseluruhan."
        )
        "biodiversity" -> KpiDetails(
            id = kpiId,
            title = "Pohon Tertanam",
            unit = "pohon",
            yearlyChartData = listOf(15f, 20f, 30f, 25f, 35f, 50f, 45f, 40f, 60f, 55f, 65f, 70f),
            fiveYearChartData = listOf(350f, 420f, 510f, 580f, 630f),
            averageValue = "42.5",
            minValue = "15",
            analysis = "Program penanaman pohon menunjukkan peningkatan signifikan sebesar 25% dibandingkan tahun lalu. Fokus pada jenis pohon lokal telah meningkatkan keberhasilan pertumbuhan."
        )
        "waste" -> KpiDetails(
            id = kpiId,
            title = "Pengelolaan Sampah",
            unit = "kg",
            yearlyChartData = listOf(320f, 310f, 290f, 300f, 270f, 260f, 240f, 230f, 210f, 220f, 200f, 190f),
            fiveYearChartData = listOf(3800f, 3500f, 3200f, 2800f, 2450f),
            averageValue = "253",
            minValue = "190",
            analysis = "Volume sampah terus menurun berkat program pemilahan dan daur ulang. Pengurangan 12% dalam tiga bulan terakhir menunjukkan efektivitas program."
        )
        "sustainability" -> KpiDetails(
            id = kpiId,
            title = "Penerima Manfaat",
            unit = "orang",
            yearlyChartData = listOf(120f, 150f, 180f, 210f, 250f, 280f, 310f, 340f, 370f, 400f, 430f, 450f),
            fiveYearChartData = listOf(1500f, 2200f, 2900f, 3600f, 4300f),
            averageValue = "290",
            minValue = "120",
            analysis = "Jumlah penerima manfaat meningkat secara konsisten dengan pertumbuhan 15% per bulan. Program pemberdayaan masyarakat menunjukkan dampak positif berkelanjutan."
        )
        // Keep compatibility with old IDs
        "energy_consumption" -> getKpiDetails("energy_usage")
        "trees_planted" -> getKpiDetails("biodiversity")
        "waste_management" -> getKpiDetails("waste")
        "benefit_receivers" -> getKpiDetails("sustainability")
        else -> KpiDetails(
            id = kpiId,
            title = "Detail KPI",
            unit = "unit",
            yearlyChartData = List(12) { 50f + (it * 5f) }, // Generic increasing data
            fiveYearChartData = List(5) { 500f + (it * 100f) },
            averageValue = "100",
            minValue = "50",
            analysis = "Data KPI belum tersedia secara lengkap. Silakan pilih KPI spesifik untuk melihat analisis detailnya."
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun KpiDetailScreen(
    navController: NavController,
    kpiId: String
) {
    // --- State ---
    val kpiDetails = remember(kpiId) { getKpiDetails(kpiId) }
    var selectedFilter by remember { mutableStateOf("Tahunan") }
    val filterOptions = listOf("Tahunan", "5 Tahun")
    var selectedYear by remember { mutableStateOf(2025) }

    // Get chart data based on selected filter
    val chartData = remember(selectedFilter, selectedYear, kpiDetails) {
        if (selectedFilter == "Tahunan") {
            kpiDetails.yearlyChartData
        } else {
            kpiDetails.fiveYearChartData
        }
    }

    // Determine highlight data - different for yearly vs 5-year
    val highlightIndex = remember(selectedFilter) {
        if (selectedFilter == "Tahunan") 6 else 3 // Highlight July for monthly, 4th year for 5-year
    }

    val highlightValueText = remember(selectedFilter, kpiDetails, highlightIndex) {
        val value = if (selectedFilter == "Tahunan") {
            kpiDetails.yearlyChartData.getOrNull(highlightIndex) ?: 0f
        } else {
            kpiDetails.fiveYearChartData.getOrNull(highlightIndex) ?: 0f
        }
        "${value.roundToInt()} ${kpiDetails.unit}"
    }

    // Needed for drawing text on Canvas
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current

    // --- UI ---
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = kpiDetails.title,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF27361F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.arrow_left),
                                contentDescription = "Back",
                                modifier = Modifier.size(16.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // --- Filter Switch ---
            KpiFilterSwitch(
                options = filterOptions,
                selectedOption = selectedFilter,
                onOptionSelected = { selectedFilter = it },
                selectedBackgroundColor = Color(0xFF27361F),
                unselectedContentColor = Color(0xFF27361F),
                unselectedBackgroundColor = Color(0xFFe9ebe9)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Year Selector ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = { selectedYear-- }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Year")
                }
                Text(
                    text = selectedYear.toString(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                IconButton(onClick = { selectedYear++ }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Year")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // --- Chart Section ---
            Text(
                text = kpiDetails.title,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // --- Chart ---
            if (selectedFilter == "Tahunan") {
                // Create a model producer for the line chart
                val lineModelProducer = remember { CartesianChartModelProducer() }

                // Update chart data when needed
                LaunchedEffect(kpiDetails.yearlyChartData) {
                    println("Line Chart data: ${kpiDetails.yearlyChartData}") // Debug print
                    lineModelProducer.runTransaction {
                        lineSeries {
                            series(kpiDetails.yearlyChartData.map { it.toDouble() })
                        }
                    }
                }

                // Display the decoupled line chart component
                VicoLineChart(
                    modelProducer = lineModelProducer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
//                        .border(1.dp, Color.Red) // Optional: Keep border for debugging layout
                )

            } else { // 5 Tahun filter
                // Create a model producer for the column chart
                val columnModelProducer = remember { CartesianChartModelProducer() }

                // Update chart data when needed
                LaunchedEffect(kpiDetails.fiveYearChartData) {
                    println("Column Chart data: ${kpiDetails.fiveYearChartData}") // Debug print
                    columnModelProducer.runTransaction {
                        columnSeries { // Use columnSeries for bar chart
                            series(kpiDetails.fiveYearChartData.map { it.toDouble() })
                        }
                    }
                }

                // Display the basic column chart (still defined in this file)
                JetpackComposeBasicColumnChart(
                    modelProducer = columnModelProducer,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .border(1.dp, Color.Blue) // Changed border color for distinction
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Tambah Data Button ---
            Button(
                onClick = { navController.navigate("upload_data") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .border(1.dp, Color.Gray, RoundedCornerShape(50)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color(0xFF27361F),
                    disabledContentColor = Color.Gray,
                    disabledContainerColor = Color.Transparent
                ),
                shape = RoundedCornerShape(50)
            ) {
                // Use Box to create an Icon that ignores the Button's contentColor
                Box {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_tambahdata),
                        contentDescription = "Add Data",
                        modifier = Modifier.size(ButtonDefaults.IconSize),
                        // Override tinting by forcing null tint
                        tint = Color.Unspecified
                    )
                }
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text("Tambah Data")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Statistics Capsules ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                KpiStatCapsule(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.TrendingUp,
                    value = kpiDetails.averageValue,
                    unit = kpiDetails.unit,
                    label = "Rata-rata"
                )
                KpiStatCapsule(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Info,
                    value = kpiDetails.minValue,
                    unit = kpiDetails.unit,
                    label = "Terkecil"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // --- Analysis Section ---
            Text(
                text = "Analisis",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = kpiDetails.analysis,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

// --- Keep the Column Chart Composable here for now ---
@Composable
private fun JetpackComposeBasicColumnChart(
    modelProducer: CartesianChartModelProducer,
    modifier: Modifier = Modifier,
) {
    CartesianChartHost(
        chart =
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
        ),
        modelProducer = modelProducer,
        modifier = modifier,
    )
}

// Public version that creates its own model producer (Can be removed if not used elsewhere)
@Composable
fun JetpackComposeBasicColumnChart(modifier: Modifier = Modifier) {
    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            // Learn more: https://patrykandpatrick.com/eji9zq.
            columnSeries { series(5, 6, 5, 2, 11, 8, 5, 2, 15, 11, 8, 13, 12, 10, 2, 7) }
        }
    }
    JetpackComposeBasicColumnChart(modelProducer, modifier)
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun KpiDetailScreenPreview() {
    KpiDetailScreen(
        navController = rememberNavController(),
        kpiId = "carbon_footprint" // Example ID for preview
    )
}
