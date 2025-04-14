package com.example.tumbuhnyata.ui.dashboard.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Info
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun KPIItem(
    modifier: Modifier = Modifier,
    title: String,
    @DrawableRes topIcon: Int,
    statusText: String,
    statusPercentageValue: String,
    isUp: Boolean,
    value: String,
    unit: String,
    targetLabel: String = "Target:",
    targetValue: String,
    contentColor: Color = Color.White,
    mainValueColor: Color = Color(0xFFE6FD4B),
    statusBackgroundColor: Color = Color.White.copy(alpha = 0.15f),
    arrowDownColor: Color = Color.Red,
    onClick: () -> Unit
) {
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF5A7C47), Color(0xFF415A33))
    )
    val cardShape = RoundedCornerShape(10.dp)

    val dynamicStatusIndicatorColor = if (statusText.trim() == "100% target") {
        Color(0xFF8BC34A)
    } else {
        Color(0xFFE2C731)
    }

    val arrowIcon = if (isUp) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward
    val arrowColor = if (isUp) contentColor else arrowDownColor

    Card(
        modifier = modifier
            .size(width = 169.dp, height = 198.dp)
            .background(brush = gradientBrush, shape = cardShape)
            .clickable(onClick = onClick),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent,
            contentColor = contentColor
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(start = 14.dp, top = 14.dp, end = 14.dp, bottom = 14.dp)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = topIcon),
                    contentDescription = "$title icon",
                    modifier = Modifier.size(14.dp)
                )
            }

            HorizontalDivider(
                thickness = 1.dp,
                color = contentColor.copy(alpha = 0.5f)
            )

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.height(25.dp),
                    shape = RoundedCornerShape(50),
                    color = statusBackgroundColor,
                    contentColor = contentColor
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(dynamicStatusIndicatorColor, CircleShape)
                        )
                        Text(
                            text = statusText,
                            fontFamily = PoppinsFontFamily,
                            fontSize = 10.sp
                        )
                        Spacer(Modifier.weight(1f))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Icon(
                                imageVector = arrowIcon,
                                contentDescription = if (isUp) "Arrow Up" else "Arrow Down",
                                modifier = Modifier.size(12.dp),
                                tint = arrowColor
                            )
                            Text(
                                text = statusPercentageValue,
                                fontFamily = PoppinsFontFamily,
                                fontSize = 10.sp,
                                color = contentColor
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = value,
                    fontSize = 25.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 26.sp,
                    color = mainValueColor
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = unit,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }

            Column {
                Text(
                    text = targetLabel,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp
                )
                Text(
                    text = targetValue,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 10.sp
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEEEEE, name = "KPI 100% Target")
@Composable
private fun KPIItemPreviewGreen() {
    KPIItem(
        title = "Carbon Footprint",
        topIcon = android.R.drawable.ic_dialog_info,
        statusText = "100% target",
        statusPercentageValue = "5%",
        isUp = true,
        value = "12.300",
        unit = "kg CO₂e",
        targetValue = "10.000 kg CO₂e",
        onClick = { }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFEEEEEE, name = "KPI <100% Target Down")
@Composable
private fun KPIItemPreviewYellowRed() {
    KPIItem(
        title = "Water Usage",
        topIcon = android.R.drawable.ic_dialog_info,
        statusText = "95% target",
        statusPercentageValue = "2%",
        isUp = false,
        value = "8.500",
        unit = "Liters",
        targetValue = "8.000 Liters",
        onClick = { }
    )
}
