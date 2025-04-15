package com.example.tumbuhnyata.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.data.model.CsrItem
import com.example.tumbuhnyata.data.model.SubStatus
import com.example.tumbuhnyata.data.model.dummyCsrList
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun CsrCardComponent(item: CsrItem, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color(0xFFE0E0E0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Title and Status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                
                StatusChipComponent(status = item.status, subStatus = item.subStatus)
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Date and Location
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = Color.Gray,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = item.location,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily,
                    modifier = Modifier.padding(start = 4.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.period,
                    color = Color.Gray,
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily
                )
            }
        }
    }
}

@Composable
fun StatusChipComponent(status: String, subStatus: SubStatus) {
    val (backgroundColor, textColor) = when (status.lowercase()) {
        "diterima" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "ditolak" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        "proses review" -> Color(0xFFFFF8E1) to Color(0xFFF57F17)
        "menunggu pembayaran" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "memerlukan revisi" -> Color(0xFFFFEBEE) to Color(0xFFC62828)
        "mendatang" -> Color(0xFFFFF8E1) to Color(0xFFF57F17)
        "progress" -> Color(0xFFE8F5E9) to Color(0xFF2E7D32)
        "program selesai" -> Color(0xFFE0E0E0) to Color(0xFF757575)
        else -> Color(0xFFFFF8E1) to Color(0xFFF57F17)
    }
    
    val statusText = when (subStatus) {
        SubStatus.MENUNGGU_PEMBAYARAN -> "Menunggu Pembayaran"
        SubStatus.MEMERLUKAN_REVISI -> "Perlu Revisi"
        SubStatus.PROSES_REVIEW -> "Proses Review"
        SubStatus.MENDATANG -> "Akan Datang"
        SubStatus.PROGRESS -> "Sedang Berlangsung"
        SubStatus.SELESAI -> "Selesai"
    }
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor
    ) {
        Text(
            text = statusText,
            color = textColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = PoppinsFontFamily,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
fun CsrCardPreviewComponent() {
    CsrCardComponent(item = dummyCsrList[0])
} 