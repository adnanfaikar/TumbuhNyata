package com.example.tumbuhnyata.ui.dashboard.kpi

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.style.TextAlign
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
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.lineSeries
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.example.tumbuhnyata.ui.dashboard.kpi.components.VicoLineChart
import com.example.tumbuhnyata.ui.dashboard.kpi.components.VicoBarChart
import kotlinx.coroutines.runBlocking
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState

data class KpiDetails(
    val id: String,
    val title: String,
    val unit: String,
    val year: Int,
    val yearlyChartData: List<Float>,
    val fiveYearChartData: List<Float>,
    val averageValue: String,
    val minValue: String,
    val analysis: String
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun KpiDetailScreen(
    navController: NavController,
    kpiId: String,
    viewModel: KPIDetailViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedYear by remember { mutableStateOf(2025) } // Start with 2024 since that's where our data is

    // Initial load and reload when kpiId or selectedYear changes
    LaunchedEffect(kpiId, selectedYear) {
        println("KpiDetailScreen: Loading data for kpiId=$kpiId, year=$selectedYear")
        viewModel.loadKPIDetails(kpiId, year = selectedYear) // Let ViewModel handle companyId
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.kpiDetails?.title ?: "KPI Detail",
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                uiState.error != null -> {
                    // Show no data state with upload option
                    NoDataStateContent(
                        navController = navController,
                        kpiId = kpiId,
                        selectedYear = selectedYear,
                        onRetry = { viewModel.retryLoadKPIDetails(kpiId, year = selectedYear) }
                    )
                }

                uiState.kpiDetails != null -> {
                    KpiDetailContent(
                        navController = navController,
                        kpiDetails = uiState.kpiDetails!!,
                        selectedYear = selectedYear,
                        onYearChange = { newYear -> selectedYear = newYear }
                    )
                }

                else -> {
                    // Initial state - should not happen with proper loading state
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Memuat data KPI...",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun KpiDetailContent(
    navController: NavController,
    kpiDetails: KpiDetails,
    selectedYear: Int,
    onYearChange: (Int) -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Tahunan") }
    val filterOptions = listOf("Tahunan", "5 Tahun")

    val lineModelProducer = remember { CartesianChartModelProducer() }
    val columnModelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(kpiDetails.yearlyChartData, selectedFilter) {
        if (selectedFilter == "Tahunan") {
            println("Updating Line Chart data: ${kpiDetails.yearlyChartData}")
            lineModelProducer.runTransaction {
                lineSeries {
                    series(kpiDetails.yearlyChartData.map { it.toDouble() })
                }
            }
        }
    }

    LaunchedEffect(kpiDetails.fiveYearChartData, selectedFilter) {
        if (selectedFilter == "5 Tahun") {
            println("Updating Column Chart data: ${kpiDetails.fiveYearChartData}")
            columnModelProducer.runTransaction {
                columnSeries {
                    series(kpiDetails.fiveYearChartData.map { it.toDouble() })
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        KpiFilterSwitch(
            options = filterOptions,
            selectedOption = selectedFilter,
            onOptionSelected = { selectedFilter = it },
            selectedBackgroundColor = Color(0xFF27361F),
            unselectedContentColor = Color(0xFF27361F),
            unselectedBackgroundColor = Color(0xFFe9ebe9)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { onYearChange(selectedYear - 1) }) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Year")
            }
            Text(
                text = if (selectedFilter == "Tahunan") selectedYear.toString() else "5 Tahun Terakhir",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            IconButton(onClick = { onYearChange(selectedYear + 1) }, enabled = selectedFilter == "Tahunan") {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Year")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = if (selectedFilter == "Tahunan") "Tren ${kpiDetails.title} Tahunan" else "Tren ${kpiDetails.title} 5 Tahun",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            if (selectedFilter == "Tahunan") {
                VicoLineChart(
                    modelProducer = lineModelProducer,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                VicoBarChart(
                    modelProducer = columnModelProducer,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

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
            Box {
                Icon(
                    painter = painterResource(id = R.drawable.ic_tambahdata),
                    contentDescription = "Add Data",
                    modifier = Modifier.size(ButtonDefaults.IconSize),
                    tint = Color.Unspecified
                )
            }
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text("Tambah Data")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KpiStatCapsule(
                modifier = Modifier.weight(1f),
                icon = painterResource(id = R.drawable.ic_tachometer_average),
                value = kpiDetails.averageValue,
                unit = kpiDetails.unit,
                label = "Rata-rata"
            )
            KpiStatCapsule(
                modifier = Modifier.weight(1f),
                icon = painterResource(id = R.drawable.angle_double_small_down),
                value = kpiDetails.minValue,
                unit = kpiDetails.unit,
                label = "Terkecil"
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

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

@Composable
private fun NoDataStateContent(
    navController: NavController,
    kpiId: String,
    selectedYear: Int,
    onRetry: () -> Unit
) {
    var selectedFilter by remember { mutableStateOf("Tahunan") }
    val filterOptions = listOf("Tahunan", "5 Tahun")

    Column(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Keep the filter switch (disabled state)
        KpiFilterSwitch(
            options = filterOptions,
            selectedOption = selectedFilter,
            onOptionSelected = { /* Disabled */ },
            selectedBackgroundColor = Color(0xFF989898), // Disabled color
            unselectedContentColor = Color(0xFF989898),
            unselectedBackgroundColor = Color(0xFFe9ebe9)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Keep year selector (disabled state)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /* Disabled */ }, enabled = false) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous Year")
            }
            Text(
                text = if (selectedFilter == "Tahunan") selectedYear.toString() else "5 Tahun Terakhir",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray, // Disabled color
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            IconButton(onClick = { /* Disabled */ }, enabled = false) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next Year")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Tren KPI Tahunan",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray, // Disabled color
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Replace chart with no data message
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Color(0xFFF5F5F5),
                    RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_carbonfootprint),
                    contentDescription = "No Data",
                    modifier = Modifier.size(48.dp),
                    colorFilter = ColorFilter.tint(Color.Gray)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = "Anda Belum Memiliki Data",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                
                Text(
                    text = "Upload data untuk melihat grafik KPI",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Keep the upload button (main CTA)
        Button(
            onClick = { navController.navigate("upload_data") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF27361F),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_tambahdata),
                contentDescription = "Add Data",
                modifier = Modifier.size(ButtonDefaults.IconSize),
                tint = Color.Unspecified
            )
            Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
            Text(
                text = "Tambah Data",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Secondary actions
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF27361F)
                )
            ) {
                Text("Kembali")
            }
            
            OutlinedButton(
                onClick = onRetry,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF27361F)
                )
            ) {
                Text("Refresh")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Disabled stats section to maintain layout
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KpiStatCapsule(
                modifier = Modifier.weight(1f),
                icon = painterResource(id = R.drawable.ic_tachometer_average),
                value = "0",
                unit = "Unit",
                label = "Rata-rata",
                isDisabled = true
            )
            KpiStatCapsule(
                modifier = Modifier.weight(1f),
                icon = painterResource(id = R.drawable.angle_double_small_down),
                value = "0",
                unit = "Unit",
                label = "Terkecil",
                isDisabled = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Analisis",
            style = MaterialTheme.typography.titleMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Belum ada data untuk dianalisis. Silakan upload data terlebih dahulu untuk melihat analisis KPI.",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun KpiDetailScreenPreview() {
    KpiDetailScreen(
        navController = rememberNavController(),
        kpiId = "carbon_footprint"
    )
}
