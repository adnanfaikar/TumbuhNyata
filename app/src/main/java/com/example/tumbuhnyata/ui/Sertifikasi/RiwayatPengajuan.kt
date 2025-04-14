package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun RiwayatPengajuanScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 95.dp, end = 20.dp)
    ) {
        TopBarProfile(
            title = "Riwayat Pengajuan",
            step = "",
            iconResId = R.drawable.btn_back,
            onBackClick = { }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(
            "*Semua detail informasi mengenai penerimaan sertifikat dan hasil penolakan dikirim menggunakan email",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 10.sp,
            color = Color(0xFF4B4B4B)
        )

        Spacer(modifier = Modifier.height(20.dp))

        PengajuanSection()
    }
}

@Composable
fun PengajuanSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Spacer(modifier = Modifier.weight(1f))
    }

    val dataPengajuan = listOf(
        Pengajuan(
            status = "Proses Review",
            color = Color.Black,
            sertifikasi = "ISO 26000 – Sustainability",
            kategori = "Lingkungan",
            pengajuan = "12 Maret 2024",
            deadline = "25 April 2024"
        ),
        Pengajuan(
            status = "Diterima",
            color = Color(0xFF34C759),
            sertifikasi = "ISO 14001 – Environmental Management",
            kategori = "Manajemen",
            pengajuan = "5 Februari 2024",
            deadline = "20 Maret 2024"
        ),
        Pengajuan(
            status = "Ditolak",
            color = Color.Red,
            sertifikasi = "ISO 9001 – Quality Management",
            kategori = "Kualitas",
            pengajuan = "1 Januari 2024",
            deadline = "10 Februari 2024"
        )
    )

    dataPengajuan.forEachIndexed { index, item ->
        PengajuanCard(item)
        if (index != dataPengajuan.lastIndex) Spacer(modifier = Modifier.height(8.dp))
    }
}


@Composable
fun RiwayatPengajuanCard(data: Pengajuan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFF989898))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Status : ${data.status}", fontWeight = FontWeight.Bold, fontFamily = PoppinsFontFamily)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(data.color)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow2(iconId = R.drawable.sertifikasi_icon, label = "Sertifikasi", value = data.sertifikasi)
            InfoRow2(iconId = R.drawable.kategori_icon, label = "Kategori", value = data.kategori)
            InfoRow2(iconId = R.drawable.pengajuan_icon, label = "Pengajuan", value = data.pengajuan)
            InfoRow2(iconId = R.drawable.deadline_icon, label = "Deadline", value = data.deadline)
        }
    }
}

@Composable
fun InfoRow2(iconId: Int, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = label,
            tint = Color(0xFF4B4B4B), // bisa kamu ganti warnanya
            modifier = Modifier
                .size(16.dp)
                .padding(end = 8.dp)
        )
        Text("$label : $value", fontSize = 14.sp, fontFamily = PoppinsFontFamily)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRiwayatPengajuanScreen() {
    RiwayatPengajuanScreen()
}