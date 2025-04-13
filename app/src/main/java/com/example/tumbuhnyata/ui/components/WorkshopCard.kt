package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.data.model.Workshop

@Composable
fun WorkshopCard(
    workshop: Workshop,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(261.dp, 207.dp)
            .clickable(onClick = onClick)
            .border(
                width = 1.dp,
                color = Color(0xFFE2E2E2),
                shape = RoundedCornerShape(10.dp)
            ),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            Image(
                painter = painterResource(id = workshop.imageRes),
                contentDescription = "Workshop image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(102.dp)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = workshop.title,
                    fontSize = 14.sp,
                    color = Color(0xFF1E1E1E),
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = workshop.speaker,
                    fontSize = 12.sp,
                    color = Color(0xFF4B4B4B),
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(22.dp))

                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = workshop.date,
                        fontSize = 12.sp,
                        color = Color(0xFF4B4B4B)
                    )
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (workshop.isOnline) Color(0xFF4B4B4B) else Color(0xFFE2E2E2),
                                shape = RoundedCornerShape(5.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (workshop.isOnline) "Online" else "Offline",
                            fontSize = 12.sp,
                            color = if (workshop.isOnline) Color(0xFFE2E2E2) else Color(0xFF4B4B4B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}