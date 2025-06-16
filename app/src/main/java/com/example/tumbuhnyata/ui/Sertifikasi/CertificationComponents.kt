package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tumbuhnyata.data.local.entity.CertificationEntity
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun CertificationEntityCard(
    data: CertificationEntity,
    navController: NavController
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp)
            .clickable { 
                // Navigate to certification detail with ID
                navController.navigate("certification_detail/${data.id}")
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Status indicator
            Card(
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = when (data.status) {
                        "approved" -> Color(0xFF4CAF50)
                        "rejected" -> Color(0xFFF44336)
                        "in_review" -> Color(0xFFFF9800)
                        else -> Color(0xFF2196F3)
                    }
                ),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = data.status.uppercase().take(2),
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily
                    )
                }
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    data.name, 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily
                )
                Text(
                    data.credentialBody, 
                    fontSize = 12.sp, 
                    color = Color.Gray,
                    fontFamily = PoppinsFontFamily
                )
                Text(
                    "Status: ${data.status.uppercase()}", 
                    fontSize = 11.sp,
                    fontFamily = PoppinsFontFamily,
                    color = when (data.status) {
                        "approved" -> Color(0xFF4CAF50)
                        "rejected" -> Color(0xFFF44336)
                        "in_review" -> Color(0xFFFF9800)
                        else -> Color(0xFF2196F3)
                    }
                )
                Text(
                    "Cost: $${data.cost}", 
                    fontSize = 11.sp,
                    color = Color.Gray,
                    fontFamily = PoppinsFontFamily
                )
                if (!data.isSynced) {
                    Text(
                        "Pending sync...", 
                        fontSize = 10.sp,
                        color = Color(0xFFFF9800),
                        fontFamily = PoppinsFontFamily
                    )
                }
            }
        }
    }
} 