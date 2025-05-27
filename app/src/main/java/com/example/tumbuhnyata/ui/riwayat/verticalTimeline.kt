package com.example.tumbuhnyata.ui.riwayat


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.ui.component.poppins

@Composable
fun VerticalTimeline(
    steps: List<TimelineStep>
) {
    Column {
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // Circle/Icon
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (step.isCompleted) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Completed",
                            tint = Color(0xFF2C3E1F),
                            modifier = Modifier.size(16.dp)
                        )
                    } else if (step.isInProgress) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawCircle(color = Color(0xFF2C3E1F), radius = size.minDimension / 2)
                        }
                    } else {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawCircle(
                                color = Color.LightGray,
                                style = Stroke(width = 2f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Text and Date
                Column {
                    Text(step.title, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, fontFamily = poppins)
                    if (step.date.isNotEmpty()) {
                        Text(step.date, color = Color.Gray, fontSize = 12.sp, fontFamily = poppins)
                    }
                }

                // Vertical Line
                if (index < steps.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Canvas(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                            .padding(start = 11.dp) // Sesuaikan posisi garis
                    ) {
                        drawLine(
                            color = Color.LightGray,
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(0f, size.height),
                            strokeWidth = 2f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

data class TimelineStep(
    val title: String,
    val date: String = "",
    val isCompleted: Boolean = false,
    val isInProgress: Boolean = false
)

@Preview(showBackground = true)
@Composable
fun PreviewVerticalTimeline() {
    val timelineSteps = listOf(
        TimelineStep("Pengajuan Dikirim", "10/05/2024 - 09:41 WIB", isCompleted = true),
        TimelineStep("Review & Evaluasi", "10/05/2024 - 09:50 WIB", isInProgress = true),
        TimelineStep("Pembayaran", "10/05/2024 - 10:00 WIB"),
        TimelineStep("Implementasi Program")
    )
    VerticalTimeline(steps = timelineSteps)
}package com.example.tumbuhnyata.ui.riwayat

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun VerticalTimeline(
    steps: List<TimelineStep>
) {
    Column {
        steps.forEachIndexed { index, step ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                // Circle/Icon
                Box(
                    modifier = Modifier.size(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (step.isCompleted) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Completed",
                            tint = Color(0xFF2C3E1F),
                            modifier = Modifier.size(16.dp)
                        )
                    } else if (step.isInProgress) {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawCircle(color = Color(0xFF2C3E1F), radius = size.minDimension / 2)
                        }
                    } else {
                        Canvas(modifier = Modifier.size(16.dp)) {
                            drawCircle(
                                color = Color.LightGray,
                                style = Stroke(width = 2f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))

                // Text and Date
                Column {
                    Text(step.title, fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold, fontFamily = PoppinsFontFamily)
                    if (step.date.isNotEmpty()) {
                        Text(step.date, color = Color.Gray, fontSize = 12.sp, fontFamily = PoppinsFontFamily)
                    }
                }

                // Vertical Line
                if (index < steps.size - 1) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Canvas(
                        modifier = Modifier
                            .fillMaxHeight()
                            .width(2.dp)
                            .padding(start = 11.dp) // Sesuaikan posisi garis
                    ) {
                        drawLine(
                            color = Color.LightGray,
                            start = androidx.compose.ui.geometry.Offset(0f, 0f),
                            end = androidx.compose.ui.geometry.Offset(0f, size.height),
                            strokeWidth = 2f,
                            cap = StrokeCap.Round
                        )
                    }
                }
            }
        }
    }
}

data class TimelineStep(
    val title: String,
    val date: String = "",
    val isCompleted: Boolean = false,
    val isInProgress: Boolean = false
)

@Preview(showBackground = true)
@Composable
fun PreviewVerticalTimeline() {
    val timelineSteps = listOf(
        TimelineStep("Pengajuan Dikirim", "10/05/2024 - 09:41 WIB", isCompleted = true),
        TimelineStep("Review & Evaluasi", "10/05/2024 - 09:50 WIB", isInProgress = true),
        TimelineStep("Pembayaran", "10/05/2024 - 10:00 WIB"),
        TimelineStep("Implementasi Program")
    )
    VerticalTimeline(steps = timelineSteps)
}