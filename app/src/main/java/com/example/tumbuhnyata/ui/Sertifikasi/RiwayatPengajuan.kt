package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material3.Text
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tumbuhnyata.data.local.entity.CertificationEntity
import com.example.tumbuhnyata.data.factory.CertificationFactory
import com.example.tumbuhnyata.data.factory.CertificationViewModelFactory
import com.example.tumbuhnyata.viewmodel.SertifikasiViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun RiwayatPengajuanScreen(navController: NavController) {
    val context = LocalContext.current
    val certificationRepository = remember { CertificationFactory.createCertificationRepository(context) }
    val viewModelFactory = remember { CertificationViewModelFactory(certificationRepository) }
    val viewModel: SertifikasiViewModel = viewModel(factory = viewModelFactory)
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 30.dp, end = 20.dp)
    ) {
        TopBarProfile(
            title = "Riwayat Pengajuan",
            step = "",
            iconResId = R.drawable.btn_back,
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Riwayat Pengajuan Sertifikasi",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 21.sp,
            color = Color(0xFF1A4218)
        )

        when {
            state.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            state.error != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.error ?: "An error occurred")
                }
            }
            else -> {
                RiwayatPengajuanSection(state.certificationList)
            }
        }
    }
}

@Composable
fun RiwayatPengajuanSection(certificationList: List<CertificationEntity>) {
    Spacer(modifier = Modifier.height(8.dp))

    if (certificationList.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    "Belum ada riwayat pengajuan",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            certificationList.forEach { certification ->
                RiwayatPengajuanCard(certification)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun RiwayatPengajuanCard(data: CertificationEntity) {
    val statusColor = when (data.status) {
        "submitted" -> Color(0xFF2196F3) // Blue
        "in_review" -> Color(0xFFFF9800) // Orange
        "approved" -> Color(0xFF4CAF50) // Green
        "rejected" -> Color(0xFFF44336) // Red
        else -> Color.Gray
    }
    
    val statusText = when (data.status) {
        "submitted" -> "Diajukan"
        "in_review" -> "Dalam Review"
        "approved" -> "Disetujui"
        "rejected" -> "Ditolak"
        else -> data.status
    }
    
    val imageRes = when {
        data.name.contains("ISO 26000", ignoreCase = true) -> R.drawable.iso_26000
        data.name.contains("ISO 14001", ignoreCase = true) -> R.drawable.iso_14001
        data.name.contains("PROPER", ignoreCase = true) -> R.drawable.proper
        data.name.contains("EcoLabel", ignoreCase = true) -> R.drawable.ecolabel
        data.name.contains("ISCC", ignoreCase = true) -> R.drawable.iscc
        else -> R.drawable.iso_26000 // Default
    }
    
    // Format submission date
    val formattedDate = try {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(data.submissionDate)
        val displayFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        displayFormat.format(date ?: Date())
    } catch (e: Exception) {
        data.submissionDate
    }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Certificate icon
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = "Certificate Icon",
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp)
            )

            // Certificate details
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = data.name,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 16.sp,
                    color = Color(0xFF1A4218)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Status: $statusText",
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    color = statusColor,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = "Diajukan: $formattedDate",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(2.dp))
                
                Text(
                    text = "Biaya: ${String.format("%.0f", data.cost)}",
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
                
                // Show sync status if not synced
                if (!data.isSynced) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "⏳ Belum tersinkron",
                        fontFamily = PoppinsFontFamily,
                        fontSize = 11.sp,
                        color = Color(0xFFFF9800)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRiwayatPengajuanScreen() {
    val navController = rememberNavController()
    RiwayatPengajuanScreen(navController)
}package com.example.tumbuhnyata.ui.Sertifikasi

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