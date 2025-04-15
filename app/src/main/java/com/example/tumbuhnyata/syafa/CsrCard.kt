package com.example.tumbuhnyata.ui.component

import com.example.tumbuhnyata.ui.riwayat.RiwayatScreen


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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.data.model.CsrItem
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.model.SubStatus
import com.example.tumbuhnyata.data.model.getSubStatusEmoji

val poppins = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_medium, FontWeight.Medium),
    Font(R.font.poppins_semibold, FontWeight.SemiBold),
    Font(R.font.poppins_bold, FontWeight.Bold)
)

@Composable
fun CsrCard(item: CsrItem, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth()
            .height(IntrinsicSize.Min) // penting agar tinggi row menyesuaikan tinggi konten
    ) {
        // Strip warna kiri
        Box(
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight()
                .clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
                .background(Color(android.graphics.Color.parseColor(item.subStatus.colorHex)))
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
                text = item.title,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins
            )
            Text(
                text = item.organization,
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = poppins
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Status : ${
                    item.subStatus.name.replace("_", " ").lowercase()
                        .replaceFirstChar { it.uppercase() }
                } ${getSubStatusEmoji(item.subStatus)}",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = poppins
            )

            // 🔽 Garis di bawah status
            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color.LightGray, thickness = 0.5.dp)
            Spacer(modifier = Modifier.height(12.dp))

            // 🔽 Info 3 kolom
            // 🔽 Info 3 kolom dengan garis vertikal pemisah
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
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppins
                    )
                    Text(item.category, fontSize = 11.sp, fontFamily = poppins)
                }

                // 🔽 Garis vertikal pemisah antara kategori dan lokasi
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
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppins
                    )
                    Text(item.location, fontSize = 11.sp, fontFamily = poppins)
                }

                // 🔽 Garis vertikal pemisah antara lokasi dan periode
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
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = poppins
                    )
                    Text(item.period, fontSize = 11.sp, fontFamily = poppins)
                }
            }

        }
    }
}

@Preview
@Composable
fun CsrCardPreview() {
    CsrCard(
        CsrItem("Penghijauan Hutan Kaltim", "PT Hijau Sejati", "Diterima", SubStatus.MENDATANG, "Lingkungan", "Kalimantan", "12 Mei - 20 Mei 25")
    )
}