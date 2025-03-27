package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun StepIndicator(step: String, color: Color, strokeColor: Color = Color.Black, textColor: Color = Color.Black, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.size(30.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(color = strokeColor, radius = size.minDimension / 2, style = Stroke(width = 3f))
            drawCircle(color = color, radius = size.minDimension / 2.2f)
        }
        Text(
            text = step,
            color = textColor,
            fontWeight = FontWeight.Bold
        )
    }
}