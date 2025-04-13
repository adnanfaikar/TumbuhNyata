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


// Placeholder data class for KPI details
data class KpiDetails(
    val id: String,
    val title: String,
    // Add fields for chart data, stats, analysis etc.
)

// Function to get placeholder data based on ID
fun getKpiDetails(kpiId: String): KpiDetails {
    // In a real app, fetch this from a ViewModel/Repository
    return when (kpiId) {
        "carbon_footprint" -> KpiDetails(id = kpiId, title = "Carbon Footprint")
        "energy_consumption" -> KpiDetails(id = kpiId, title = "Konsumsi Energi")
        "water_usage" -> KpiDetails(id = kpiId, title = "Penggunaan Air")
        "trees_planted" -> KpiDetails(id = kpiId, title = "Pohon Tertanam")
        "waste_management" -> KpiDetails(id = kpiId, title = "Pengelolaan Sampah")
        "benefit receivers"-> KpiDetails(id = kpiId, title = "Penerima Manfaat")
        else -> KpiDetails(id = kpiId, title = "Detail KPI") // Default title
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class) // Opt-in for TextMeasurer
@Composable
fun KpiDetailScreen(
    navController: NavController,
    kpiId: String // Accept the KPI ID from navigation
) {
    // --- State ---
    val kpiDetails = remember(kpiId) { getKpiDetails(kpiId) }
    var selectedFilter by remember { mutableStateOf("Tahunan") }
    val filterOptions = listOf("Tahunan", "5 Tahun")
    var selectedYear by remember { mutableStateOf(2025) }

    // Sample data for the chart (replace with actual data)
    // Represents values for 12 months (Jan-Dec)
    val chartData = remember(selectedFilter, selectedYear) {
        // TODO: Fetch real data based on filter/year
        listOf(60f, 70f, 85f, 75f, 90f, 80f, 100f, 110f, 105f, 120f, 115f, 130f)
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

            // --- Canvas Chart ---
            SimpleLineChart(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                data = chartData,
                highlightIndex = 6, // Highlight July (index 6)
                highlightValueText = "80 kg CO₂e" // Text for highlight
            )

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
                    value = "139",
                    unit = "kg CO₂e",
                    label = "Rata-rata"
                )
                KpiStatCapsule(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Filled.Info,
                    value = "68",
                    unit = "kg CO₂e",
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
                text = "Jejak karbon meningkat 8% minggu ini, seiring distribusi logistik program bantuan sosial. Pertimbangkan opsi pengiriman ramah lingkungan agar dampak sosial tetap tinggi tanpa menambah emisi.",
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
    fillColor: Color = Color(0xFF4CAF50).copy(alpha = 0.3f), // Lighter green fill
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
    val maxValue = data.maxOrNull() ?: 1f // Avoid division by zero if empty

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


@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun KpiDetailScreenPreview() {
    KpiDetailScreen(
        navController = rememberNavController(),
        kpiId = "carbon_footprint" // Example ID for preview
    )
}
