package com.example.tumbuhnyata.ui.dashboard.kpi.components

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
    selectedBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    selectedContentColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedBackgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant,
    unselectedContentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(50),
        color = unselectedBackgroundColor,
        tonalElevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        ) {
            options.forEach { option ->
                val isSelected = option == selectedOption
                Button(
                    onClick = { onOptionSelected(option) },
                    modifier = Modifier
                        .weight(1f)
                        .height(36.dp),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) selectedBackgroundColor else Color.Transparent,
                        contentColor = if (isSelected) selectedContentColor else unselectedContentColor
                    ),
                    elevation = null,
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    Text(
                        text = option,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 12.sp
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
