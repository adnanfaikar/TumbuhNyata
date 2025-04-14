package com.example.tumbuhnyata.ui.dashboard.components // Adjust package if needed

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowUpward // Placeholder for status icon
import androidx.compose.material.icons.filled.Info // Placeholder for top-right icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush // Import Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R // Make sure this matches your project's R file package
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun KPIItem(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes topIcon: Int,
    statusText: String,
    statusPercentage: String,
    statusIcon: ImageVector,
    value: String,
    unit: String,
    targetLabel: String = "Target:",
    targetValue: String,
    contentColor: Color = Color.White,
    mainValueColor: Color = Color(0xFFE6FD4B), // Added specific color for main value
    statusBackgroundColor: Color = Color.White.copy(alpha = 0.15f),
    statusIndicatorColor: Color = Color(0xFF8BC34A),
    onClick: () -> Unit
) {
    // Define gradient colors
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF5A7C47), Color(0xFF415A33))
    )
    // Define card shape
    val cardShape = RoundedCornerShape(10.dp) // 10px radius

    Card(
        modifier = modifier
            .size(width = 169.dp, height = 198.dp) // Apply fixed size
            .background(brush = gradientBrush, shape = cardShape) // Apply gradient background with matching shape
            .clickable(onClick = onClick),
        shape = cardShape, // Apply shape to card
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent, // Make container transparent to show gradient
            contentColor = contentColor // Default color for most text/icons
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Set elevation to 0dp to remove shadow/outline
    ) {
        Column(
            modifier = Modifier
                // Apply specific padding: 14px top, 18px sides, 14px bottom
                .padding(start = 14.dp, top = 14.dp, end = 14.dp, bottom = 14.dp)
                .fillMaxHeight(), // Fill height to allow arrangement
            verticalArrangement = Arrangement.spacedBy(10.dp) // 10px gap between most elements
        ) {
            // --- Top Row (Title + Icon) ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp, // 10px font size
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold
                    // Inherits default contentColor (White)
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = topIcon),
                    contentDescription = "$title icon",
                    modifier = Modifier.size(14.dp)
                    // Inherits default contentColor (White)
                )
            }

            // --- Divider ---
            HorizontalDivider(
                thickness = 1.dp, // Keep divider thin
                color = contentColor.copy(alpha = 0.5f)
            )

            // --- Status Row ---
            Box(
                modifier = Modifier.fillMaxWidth(), // Box takes full width
                contentAlignment = Alignment.Center // Center content (the Surface)
            ) {
                Surface(
                    modifier = Modifier.size(width = 144.dp, height = 25.dp),
                    shape = RoundedCornerShape(50), // Keep capsule shape
                    color = statusBackgroundColor,
                    contentColor = contentColor // Content (text/icons) inside Surface uses this
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp), // Adjust padding if needed
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp) // Keep circle small
                                .background(statusIndicatorColor, CircleShape)
                        )
                        Text(
                            text = statusText,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 10.sp // 10px font size inside status
                        )
                        Spacer(Modifier.weight(1f))
                        Text(
                            text = statusPercentage,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 10.sp // 10px font size inside status
                        )
                    }
                }
            }

            // --- Spacer for extra gap ---
            // Added Spacer for explicit control over the gap below the status row
            Spacer(Modifier.height(8.dp)) // Adjust height as needed (e.g., 8.dp, 12.dp, 16.dp)

            // --- Value Row ---
            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    fontSize = 25.sp, // 25px font size for main value
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 26.sp, // Adjust line height close to font size
                    color = mainValueColor // Apply specific color here
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = unit,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 10.sp, // Make unit smaller, 10px
                    modifier = Modifier.padding(bottom = 2.dp) // Adjust alignment padding
                    // Inherits default contentColor (White)
                )
            }

            // --- Target Section (Label + Value below) ---
            Column {
                Text(
                    text = targetLabel,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp // 12px font size for target label
                    // Inherits default contentColor (White)
                )
                Text(
                    text = targetValue,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 10.sp // 10px font size for target value
                    // Inherits default contentColor (White)
                )
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true, backgroundColor = 0xFFEEEEEE)
@Composable
private fun KPIItemPreview() {
    Column(modifier = Modifier.padding(16.dp)) {
        KPIItem(
            title = "Carbon Footprint",
            topIcon = R.drawable.ic_launcher_foreground,
            statusText = "100% target",
            statusPercentage = "▲ 5%",
            statusIcon = Icons.Filled.ArrowUpward,
            value = "12.300",
            unit = "kg CO₂e",
            targetValue = "10.000 kg CO₂e",
            onClick = { /* Preview click does nothing */ }
        )
    }
}
