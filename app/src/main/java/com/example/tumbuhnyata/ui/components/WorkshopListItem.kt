package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.data.model.Workshop
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WorkshopListItem(
    workshop: Workshop,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(155.dp)
            .border(1.dp, Color(0xFFB9B9B9), RoundedCornerShape(10.dp))
            .clickable { onClick() } ,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Min)
                .padding(end=12.dp)
        ) {
            Image(
                painter = painterResource(id = workshop.imageRes),
                contentDescription = "Workshop image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(140.dp)
                    .fillMaxHeight()
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
            ) {
                Text(
                    text = workshop.title,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    color = Color(0xFF1E1E1E),
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = workshop.speaker,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily,
                    color = Color(0xFF4B4B4B),
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = workshop.date,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily,
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
                            fontFamily = PoppinsFontFamily,
                            color = if (workshop.isOnline) Color(0xFFE2E2E2) else Color(0xFF4B4B4B),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

