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
    var selectedYear by remember { mutableStateOf(2024) } // Start with 2024 since that's where our data is

    // Initial load and reload when kpiId or selectedYear changes
    LaunchedEffect(kpiId, selectedYear) {
        println("KpiDetailScreen: Loading data for kpiId=$kpiId, year=$selectedYear")
        viewModel.loadKPIDetails(kpiId, companyId = null, year = selectedYear)
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
                    // Show error state - no more dummy data fallback
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Error icon
                        Icon(
                            Icons.Default.Warning,
                            contentDescription = "Error",
                            modifier = Modifier.size(64.dp),
                            tint = Color(0xFFFF9800) // Orange warning color
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = "Data KPI Tidak Tersedia",
                            style = MaterialTheme.typography.titleMedium,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "Terjadi masalah pada server saat mengambil data KPI.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = Color.Gray
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = uiState.error ?: "Error tidak diketahui",
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = Color.Gray,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { navController.navigateUp() },
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color(0xFF27361F)
                                )
                            ) {
                                Text("Kembali")
                            }
                            
                            Button(
                                onClick = { viewModel.retryLoadKPIDetails(kpiId, companyId = null, year = selectedYear) },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27361F))
                            ) {
                                Text("Coba Lagi")
                            }
                        }
                    }
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

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun KpiDetailScreenPreview() {
    KpiDetailScreen(
        navController = rememberNavController(),
        kpiId = "carbon_footprint"
    )
}
