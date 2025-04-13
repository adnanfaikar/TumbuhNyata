package com.example.tumbuhnyata.ui.dashboard.kpi.components // Package based on your structure

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun KpiFilterSwitch(
    modifier: Modifier = Modifier,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    selectedBackgroundColor: Color = MaterialTheme.colorScheme.primary, // Color for selected button
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary, // Text color for selected
    unselectedBackgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant, // Color for unselected
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant // Text color for unselected
) {
    Surface( // Use Surface for background and shape
        modifier = modifier,
        shape = RoundedCornerShape(50), // Capsule shape
        color = unselectedBackgroundColor, // Base background
        tonalElevation = 2.dp // Slight elevation
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp) // Padding inside the surface
                .fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Button(
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier
                        .weight(1f) // Each button takes equal space
                        .height(36.dp), // Fixed height for buttons
                    shape = RoundedCornerShape(50), // Match capsule shape
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) selectedBackgroundColor else Color.Transparent, // Selected color or transparent
                        contentColor = if (isSelected) selectedContentColor else unselectedContentColor
                    ),
                    elevation = null, // No elevation for individual buttons
                    contentPadding = PaddingValues(horizontal = 16.dp) // Adjust padding
                ) {
                    Text(
                        text = option,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp // Adjust font size
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KpiFilterSwitchPreview() {
    var selected by remember { mutableStateOf("Tahunan") }
    val options = listOf("Tahunan", "5 Tahun")

    Column(modifier = Modifier.padding(16.dp)) {
        KpiFilterSwitch(
            options = options,
            selectedOption = selected,
            onOptionSelected = { selected = it }
        )
    }
}
