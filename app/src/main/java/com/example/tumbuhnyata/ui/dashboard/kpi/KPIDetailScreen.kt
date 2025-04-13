package com.example.tumbuhnyata.ui.dashboard.kpi // Package based on your structure

import androidx.compose.foundation.Canvas // Import Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack // Use AutoMirrored for LTR/RTL
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.* // Import common icons
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset // Import Offset
import androidx.compose.ui.graphics.* // Import Graphics APIs
import androidx.compose.ui.graphics.drawscope.Stroke // Import Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.* // Import Text APIs
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import androidx.compose.runtime.*
import com.example.tumbuhnyata.ui.dashboard.kpi.components.KpiFilterSwitch
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily // Assuming you have this font defined
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import kotlin.math.roundToInt


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

            // --- Canvas Chart (Different chart types based on filter) ---
            if (selectedFilter == "Tahunan") {
                // Monthly line chart for yearly view
                SimpleLineChart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    data = chartData,
                    highlightIndex = highlightIndex,
                    highlightValueText = highlightValueText,
                    // Customize colors based on KPI type
                    lineColor = when (kpiId) {
                        "carbon_footprint" -> Color(0xFF4CAF50)
                        "energy_usage", "energy_consumption" -> Color(0xFFFF9800)
                        "water_usage" -> Color(0xFF2196F3)
                        "biodiversity", "trees_planted" -> Color(0xFF8BC34A)
                        "waste", "waste_management" -> Color(0xFFE91E63)
                        "sustainability", "benefit_receivers" -> Color(0xFF9C27B0)
                        else -> Color(0xFF607D8B)
                    }
                )
            } else {
                // Bar chart for 5-year view
                SimpleBarChart(
                modifier = Modifier
                    .fillMaxWidth()
                        .height(200.dp),
                    data = chartData,
                    barColor = when (kpiId) {
                        "carbon_footprint" -> Color(0xFF4CAF50)
                        "energy_usage", "energy_consumption" -> Color(0xFFFF9800)
                        "water_usage" -> Color(0xFF2196F3)
                        "biodiversity", "trees_planted" -> Color(0xFF8BC34A)
                        "waste", "waste_management" -> Color(0xFFE91E63)
                        "sustainability", "benefit_receivers" -> Color(0xFF9C27B0)
                        else -> Color(0xFF607D8B)
                    },
                    highlightIndex = highlightIndex,
                    highlightValueText = highlightValueText,
                    years = List(5) { (selectedYear - 4 + it).toString() } // Last 5 years
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

// --- Simple Canvas Line Chart Composable ---
@OptIn(ExperimentalTextApi::class)
@Composable
fun SimpleLineChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    lineColor: Color = Color(0xFF4CAF50), // Green line
    fillColor: Color = lineColor.copy(alpha = 0.3f), // Lighter fill based on line color
    axisColor: Color = Color.Gray,
    highlightColor: Color = Color.Black,
    textColor: Color = Color.Black,
    highlightIndex: Int? = null, // Index of the data point to highlight
    highlightValueText: String? = null // Text to display for highlight
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val labelFontSize = 10.sp

    // Find min and max values for scaling Y axis
    val minValue = data.minOrNull() ?: 0f
    val maxValue = data.maxOrNull() ?: 1f

    // Add some padding to min/max for visual appeal
    val paddedMinValue = (minValue * 0.9f).roundToInt().toFloat()
    val paddedMaxValue = (maxValue * 1.1f).roundToInt().toFloat()
    val valueRange = (paddedMaxValue - paddedMinValue).coerceAtLeast(1f)

    Canvas(modifier = modifier) {
        val paddingStart = 60f // Space for Y labels
        val paddingEnd = 20f
        val paddingTop = 20f
        val paddingBottom = 40f // Space for X labels

        val chartWidth = size.width - paddingStart - paddingEnd
        val chartHeight = size.height - paddingTop - paddingBottom

        val points = data.mapIndexed { index, value ->
            val x = paddingStart + (index.toFloat() / (data.size - 1).coerceAtLeast(1)) * chartWidth
            val y = paddingTop + chartHeight - ((value - paddedMinValue) / valueRange) * chartHeight
            Offset(x, y.coerceIn(paddingTop, paddingTop + chartHeight)) // Ensure Y is within bounds
        }

        // --- Draw Axes ---
        // Y Axis Line
        drawLine(
            color = axisColor,
            start = Offset(paddingStart, paddingTop),
            end = Offset(paddingStart, paddingTop + chartHeight),
            strokeWidth = 1.dp.toPx()
        )
        // X Axis Line
        drawLine(
            color = axisColor,
            start = Offset(paddingStart, paddingTop + chartHeight),
            end = Offset(paddingStart + chartWidth, paddingTop + chartHeight),
            strokeWidth = 1.dp.toPx()
        )

        // --- Draw Axis Labels (Simplified) ---
        val maxLabelLayout = textMeasurer.measure(
            text = AnnotatedString(paddedMaxValue.roundToInt().toString()),
            style = TextStyle(fontSize = labelFontSize, color = textColor)
        )
        drawText(
            textLayoutResult = maxLabelLayout,
            topLeft = Offset(paddingStart - maxLabelLayout.size.width - 4.dp.toPx(), paddingTop - maxLabelLayout.size.height / 2)
        )

        val minLabelLayout = textMeasurer.measure(
            text = AnnotatedString(paddedMinValue.roundToInt().toString()),
            style = TextStyle(fontSize = labelFontSize, color = textColor)
        )
        drawText(
            textLayoutResult = minLabelLayout,
            topLeft = Offset(paddingStart - minLabelLayout.size.width - 4.dp.toPx(), paddingTop + chartHeight - minLabelLayout.size.height / 2)
        )

        val janLabelLayout = textMeasurer.measure(
            text = AnnotatedString("Jan"),
            style = TextStyle(fontSize = labelFontSize, color = textColor)
        )
        drawText(
            textLayoutResult = janLabelLayout,
            topLeft = Offset(paddingStart, paddingTop + chartHeight + 4.dp.toPx())
        )

        val desLabelLayout = textMeasurer.measure(
            text = AnnotatedString("Des"),
            style = TextStyle(fontSize = labelFontSize, color = textColor)
        )
        drawText(
            textLayoutResult = desLabelLayout,
            topLeft = Offset(size.width - paddingEnd - desLabelLayout.size.width, paddingTop + chartHeight + 4.dp.toPx())
        )


        // --- Draw Chart Line and Fill ---
        if (points.isNotEmpty()) {
            val linePath = Path().apply {
                moveTo(points.first().x, points.first().y)
                points.drop(1).forEach { lineTo(it.x, it.y) }
            }

            // Draw gradient fill below line
            val fillPath = Path().apply {
                addPath(linePath) // Add the line path
                lineTo(points.last().x, paddingTop + chartHeight) // Line down to X axis at the end
                lineTo(points.first().x, paddingTop + chartHeight) // Line back along X axis
                close() // Close path back to start
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(fillColor, Color.Transparent),
                    startY = points.minOfOrNull { it.y } ?: paddingTop,
                    endY = paddingTop + chartHeight
                )
            )

            // Draw the actual line
            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        // --- Draw Highlight ---
        if (highlightIndex != null && highlightIndex >= 0 && highlightIndex < points.size) {
            val highlightPoint = points[highlightIndex]
            // Draw vertical line
            drawLine(
                color = highlightColor.copy(alpha = 0.5f),
                start = Offset(highlightPoint.x, paddingTop),
                end = Offset(highlightPoint.x, paddingTop + chartHeight),
                strokeWidth = 1.dp.toPx()
            )
            // Draw point circle
            drawCircle(
                color = highlightColor,
                radius = 4.dp.toPx(),
                center = highlightPoint
            )
            // Draw highlight text
            if (highlightValueText != null) {
                val highlightTextLayout = textMeasurer.measure(
                    text = AnnotatedString(highlightValueText),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = highlightColor)
                )
                // Position text above the highlight point
                drawText(
                    textLayoutResult = highlightTextLayout,
                    topLeft = Offset(
                        highlightPoint.x - highlightTextLayout.size.width / 2,
                        highlightPoint.y - highlightTextLayout.size.height - 8.dp.toPx() // Offset above point
                    )
                )
            }
        }
    }
}

// --- Simple Canvas Bar Chart Composable ---
@OptIn(ExperimentalTextApi::class)
@Composable
fun SimpleBarChart(
    modifier: Modifier = Modifier,
    data: List<Float>,
    barColor: Color = Color(0xFF4CAF50),
    axisColor: Color = Color.Gray,
    highlightColor: Color = Color.Black,
    textColor: Color = Color.Black,
    highlightIndex: Int? = null,
    highlightValueText: String? = null,
    years: List<String> = listOf("2021", "2022", "2023", "2024", "2025") // Default years
) {
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val labelFontSize = 10.sp

    // Find min and max values for scaling Y axis
    val minValue = data.minOrNull() ?: 0f
    val maxValue = data.maxOrNull() ?: 1f

    // Add some padding to min/max for visual appeal
    val paddedMinValue = (minValue * 0.9f).roundToInt().toFloat()
    val paddedMaxValue = (maxValue * 1.1f).roundToInt().toFloat()
    val valueRange = (paddedMaxValue - paddedMinValue).coerceAtLeast(1f)

    Canvas(modifier = modifier) {
        val paddingStart = 60f // Space for Y labels
        val paddingEnd = 20f
        val paddingTop = 20f
        val paddingBottom = 40f // Space for X labels

        val chartWidth = size.width - paddingStart - paddingEnd
        val chartHeight = size.height - paddingTop - paddingBottom

        // Calculate bar width and spacing
        val numBars = data.size
        val barMaxWidth = chartWidth / (numBars * 2f) // Make bars take up half the available space
        val barWidth = barMaxWidth.coerceAtMost(50f) // Cap maximum bar width
        val spacing = (chartWidth - (barWidth * numBars)) / (numBars + 1)

        // --- Draw Axes ---
        // Y Axis Line
        drawLine(
            color = axisColor,
            start = Offset(paddingStart, paddingTop),
            end = Offset(paddingStart, paddingTop + chartHeight),
            strokeWidth = 1.dp.toPx()
        )
        // X Axis Line
        drawLine(
            color = axisColor,
            start = Offset(paddingStart, paddingTop + chartHeight),
            end = Offset(paddingStart + chartWidth, paddingTop + chartHeight),
            strokeWidth = 1.dp.toPx()
        )

        // --- Draw Axis Labels ---
        val maxLabelLayout = textMeasurer.measure(
            text = AnnotatedString(paddedMaxValue.roundToInt().toString()),
            style = TextStyle(fontSize = labelFontSize, color = textColor)
        )
        drawText(
            textLayoutResult = maxLabelLayout,
            topLeft = Offset(paddingStart - maxLabelLayout.size.width - 4.dp.toPx(), paddingTop - maxLabelLayout.size.height / 2)
        )

        val minLabelLayout = textMeasurer.measure(
            text = AnnotatedString(paddedMinValue.roundToInt().toString()),
            style = TextStyle(fontSize = labelFontSize, color = textColor)
        )
        drawText(
            textLayoutResult = minLabelLayout,
            topLeft = Offset(paddingStart - minLabelLayout.size.width - 4.dp.toPx(), paddingTop + chartHeight - minLabelLayout.size.height / 2)
        )

        // --- Draw Bars and Year Labels ---
        data.forEachIndexed { index, value ->
            val normalized = (value - paddedMinValue) / valueRange
            val barHeight = normalized * chartHeight
            val x = paddingStart + spacing + index * (barWidth + spacing)
            val y = paddingTop + chartHeight - barHeight

            // Draw bar
            val barColor = if (index == highlightIndex) highlightColor else barColor
            drawRect(
                color = barColor,
                topLeft = Offset(x, y),
                size = androidx.compose.ui.geometry.Size(barWidth, barHeight)
            )

            // Draw year label
            val yearLabel = years.getOrElse(index) { index.toString() }
            val yearLabelLayout = textMeasurer.measure(
                text = AnnotatedString(yearLabel),
                style = TextStyle(fontSize = labelFontSize, color = textColor)
            )
            drawText(
                textLayoutResult = yearLabelLayout,
                topLeft = Offset(
                    x + (barWidth - yearLabelLayout.size.width) / 2,
                    paddingTop + chartHeight + 4.dp.toPx()
                )
            )

            // Draw highlight text if this is the highlighted bar
            if (index == highlightIndex && highlightValueText != null) {
                val highlightTextLayout = textMeasurer.measure(
                    text = AnnotatedString(highlightValueText),
                    style = TextStyle(fontSize = 12.sp, fontWeight = FontWeight.Bold, color = highlightColor)
                )
                drawText(
                    textLayoutResult = highlightTextLayout,
                    topLeft = Offset(
                        x + (barWidth - highlightTextLayout.size.width) / 2,
                        y - highlightTextLayout.size.height - 4.dp.toPx()
                    )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun KpiDetailScreenPreview() {
    KpiDetailScreen(
        navController = rememberNavController(),
        kpiId = "carbon_footprint" // Example ID for preview
    )
}
