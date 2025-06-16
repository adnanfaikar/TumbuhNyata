package com.example.tumbuhnyata.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.data.model.SubStatus
import com.example.tumbuhnyata.data.model.getSubStatusEmoji
import java.text.SimpleDateFormat
import java.util.Locale

val poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@Composable
fun CsrCard(item: CsrHistoryItem, onClick: () -> Unit = {}) {
    val subStatus = when {
        !item.agreed -> when (item.status.lowercase()) {
            "pending" -> SubStatus.PROSES_REVIEW
            "proses review" -> SubStatus.PROSES_REVIEW
            "memerlukan revisi" -> SubStatus.MEMERLUKAN_REVISI
            "menunggu pembayaran" -> SubStatus.MENUNGGU_PEMBAYARAN
            else -> SubStatus.PROSES_REVIEW
        }
        else -> when (item.status.lowercase()) {
            "pending" -> SubStatus.MENDATANG
            "akan datang" -> SubStatus.MENDATANG
            "sedang berlangsung" -> SubStatus.PROGRESS
            "program selesai" -> SubStatus.SELESAI
            else -> SubStatus.MENDATANG
        }
    }

    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Strip warna kiri
        Box(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
                .background(Color(android.graphics.Color.parseColor(subStatus.colorHex)))
        )

        // Konten utama kartu
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topEnd = 6.dp, bottomEnd = 6.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = item.programName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins
            )
            Text(
                text = item.partnerName,
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = poppins
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status : ${formatStatus(subStatus)} ${getSubStatusEmoji(subStatus)}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppins
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Kategori
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Kategori",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppins
                    )
                    Text(item.category, fontSize = 14.sp, fontFamily = poppins)
                }

                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.5.dp)
                        .background(Color.LightGray)
                )

                // Lokasi
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Lokasi",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppins
                    )
                    Text(item.location, fontSize = 14.sp, fontFamily = poppins)
                }

                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(0.5.dp)
                        .background(Color.LightGray)
                )

                // Periode
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        "Periode",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppins
                    )
                    Text(formatPeriod(item.startDate, item.endDate), fontSize = 14.sp, fontFamily = poppins)
                }
            }
        }
    }
}

private fun formatStatus(subStatus: SubStatus): String {
    return subStatus.name.replace("_", " ").lowercase()
        .replaceFirstChar { it.uppercase() }
}

private fun formatPeriod(startDate: String, endDate: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMM yy", Locale("id"))
        
        val start = inputFormat.parse(startDate)?.let { outputFormat.format(it) } ?: startDate
        val end = inputFormat.parse(endDate)?.let { outputFormat.format(it) } ?: endDate
        
        "$start - $end"
    } catch (e: Exception) {
        "$startDate - $endDate"
    }
}

@Composable
fun CsrCardPreview() {
    val previewItem = CsrHistoryItem(
        id = 1,
        userId = 1,
        programName = "Penghijauan Hutan Kaltim",
        category = "Lingkungan",
        description = "Penanaman 1000 pohon",
        location = "Kalimantan",
        partnerName = "PT. Hijau Sejati",
        startDate = "2025-05-12",
        endDate = "2025-05-20",
        budget = "290887100",
        proposalUrl = null,
        legalityUrl = null,
        agreed = true,
        status = "pending",
        createdAt = "2025-05-01T15:13:50.000Z"
    )
    CsrCard(item = previewItem)
}