package com.example.tumbuhnyata.ui.dashboardkeuangan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.data.model.CsrCategory
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import kotlin.math.roundToInt
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.draw.clip

@Composable
fun DynamicPieChart(categories: List<CsrCategory>) {
    val total = categories.sumOf { it.amount.toDouble() }.toFloat()
    val angles = categories.map { (it.amount / total * 360f) }
    val percentages = categories.map { ((it.amount / total) * 100).roundToInt() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        // Legend at the top
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            categories.forEach { category ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(category.color)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = category.name,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        color = Color.Black
                    )
                }
            }
        }

        // Pie Chart with percentages
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .size(140.dp)
            ) {
                val canvasSize = Size(size.width, size.height)
                var startAngle = -90f
                
                categories.forEachIndexed { index, category ->
                    drawArc(
                        color = category.color,
                        startAngle = startAngle,
                        sweepAngle = angles[index],
                        useCenter = true,
                        size = canvasSize
                    )
                    startAngle += angles[index]
                }
            }

            // Percentage texts
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "${percentages[1]}%",
                    fontSize = 20.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = categories[1].color,
                    modifier = Modifier.padding(start = 24.dp)
                )
            }
            
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                Text(
                    text = "${percentages[0]}%",
                    fontSize = 20.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    color = categories[0].color,
                    modifier = Modifier.padding(end = 24.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDynamicPieChart() {
    val previewCategories = listOf(
        CsrCategory("Sosial", 1955670784f, Color(0xFF4285F4)),
        CsrCategory("Lingkungan", 1231779840f, Color(0xFF8AB4F8))
    )
    DynamicPieChart(categories = previewCategories)
}

