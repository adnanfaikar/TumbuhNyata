package com.example.tumbuhnyata.ui.Sertifikasi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun SertifikasiScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        BannerSection()
        Spacer(modifier = Modifier.height(24.dp))
        SertifikasiSection()
        Spacer(modifier = Modifier.height(24.dp))
        RiwayatPengajuanSection()
    }
}

@Composable
fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Gambar background banner
        Image(
            painter = painterResource(id = R.drawable.banner_sertifikasi), // ganti sesuai nama file kamu
            contentDescription = "Banner Sertifikasi",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .matchParentSize()
        )

        // Teks & tombol di atas gambar
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Naikkan Kredibilitas Perusahaan Anda",
                style = MaterialTheme.typography.titleMedium.copy(color = Color.White, fontFamily = PoppinsFontFamily)
            )
            Text(
                text = "Ajukan Sertifikasi Sekarang!",
                style = MaterialTheme.typography.bodySmall.copy(color = Color.White, fontFamily = PoppinsFontFamily)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = { /* Aksi klik */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            ) {
                Text("Ajukan Sertifikasi", color = Color.Black, fontFamily = PoppinsFontFamily)
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.Black)
            }
        }
    }
}

data class Sertifikasi(
    val title: String,
    val code: String,
    val issued: String,
    val credentialId: String,
    val imageRes: Int
)

@Composable
fun SertifikasiSection() {
    val sertifikasiList = listOf(
        Sertifikasi(
            title = "Environmental Management System",
            code = "ISO 14001",
            issued = "Issued Jun 2024 - Expires Jun 2027",
            credentialId = "Credential ID ABC123XYZ",
            imageRes = R.drawable.iso_14001
        ),
        Sertifikasi(
            title = "Social Responsibility",
            code = "ISO 26000",
            issued = "Issued Feb 2023 - Expires Feb 2026",
            credentialId = "Credential ID DEF456LMN",
            imageRes = R.drawable.iso_26000
        )
    )

    Text(
        "Sertifikasi Anda",
        fontWeight = FontWeight.Bold,
        fontFamily = PoppinsFontFamily,
        fontSize = 21.sp,
        color = Color(0xFF1A4218)
    )
    Spacer(modifier = Modifier.height(8.dp))

    sertifikasiList.forEach {
        SertifikasiCard(it)
        Spacer(modifier = Modifier.height(12.dp))
    }

    Button(
        onClick = { /* Lihat semua */ },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFFE9E9E9),
            contentColor = Color(0xFF4B4B4B)
        ),
        shape = RoundedCornerShape(5.dp)
    ) {
        Text("Lihat Semua Sertifikasi")
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = R.drawable.arrow_rightsertif),
            contentDescription = "Arrow Right",
            modifier = Modifier.size(18.dp)
        )
    }
}


@Composable
fun SertifikasiCard(data: Sertifikasi) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = data.imageRes),
                contentDescription = "Sertifikasi Icon",
                modifier = Modifier
                    .size(78.dp)
                    .padding(end = 16.dp),
                contentScale = ContentScale.Fit
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(data.title, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                Text(data.code, fontSize = 14.sp, color = Color.Gray)
                Text(data.issued, fontSize = 12.sp)
                Text(data.credentialId, fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Button(
                    onClick = { /* show credential */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF8F8F8),
                        contentColor = Color(0xFF4B4B4B)
                    ),
                    shape = RoundedCornerShape(5.dp),
                    border = BorderStroke(1.dp, Color(0xFF4B4B4B))
                ) {
                    Text("show credential", color = Color(0xFF4B4B4B), fontSize = 12.sp)
                    Icon(
                        Icons.Default.KeyboardArrowRight,
                        contentDescription = null,
                        tint = Color(0xFF4B4B4B)
                    )
                }
            }
        }
    }
}


data class Pengajuan(
    val status: String,
    val color: Color,
    val sertifikasi: String,
    val kategori: String,
    val pengajuan: String,
    val deadline: String
)

@Composable
fun RiwayatPengajuanSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Riwayat Pengajuan",
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp,
            color = Color(0xFF1A4218)
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = { /* Aksi lihat semua */ },
            contentPadding = PaddingValues(0.dp)
        ) {
            Text(
                "Lihat Semua",
                fontSize = 14.sp,
                color = Color(0xFF1A4218)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.KeyboardArrowRight,
                contentDescription = "Arrow Right",
                tint = Color(0xFF1A4218),
                modifier = Modifier.size(18.dp)
            )
        }
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
fun PengajuanCard(data: Pengajuan) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        border = BorderStroke(1.dp, Color(0xFF989898))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Status : ${data.status}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(data.color)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))

            InfoRow(iconId = R.drawable.sertifikasi_icon, label = "Sertifikasi", value = data.sertifikasi)
            InfoRow(iconId = R.drawable.kategori_icon, label = "Kategori", value = data.kategori)
            InfoRow(iconId = R.drawable.pengajuan_icon, label = "Pengajuan", value = data.pengajuan)
            InfoRow(iconId = R.drawable.deadline_icon, label = "Deadline", value = data.deadline)
        }
    }
}

@Composable
fun InfoRow(iconId: Int, label: String, value: String) {
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
        Text("$label : $value", fontSize = 14.sp)
    }
}


@Preview(showBackground = true)
@Composable
fun SertifikasiScreenPreview() {
    val navController = rememberNavController()
    SertifikasiScreen(navController)
}

