package com.example.tumbuhnyata.ui.detail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.data.model.CsrItem
import com.example.tumbuhnyata.data.model.SubStatus
import com.example.tumbuhnyata.data.model.dummyCsrList
import com.example.tumbuhnyata.data.model.getSubStatusEmoji
import com.example.tumbuhnyata.ui.component.poppins
import com.example.tumbuhnyata.ui.riwayat.TimelineStep
import com.example.tumbuhnyata.ui.riwayat.VerticalTimeline
import com.example.tumbuhnyata.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CsrDetailScreen(onBack: () -> Unit) {
    // Ambil item dummy dengan judul "Penanaman 1000 Pohon"
    val detailItem = dummyCsrList.find { it.title == "Penanaman 1000 Pohon" }

    // Pastikan item ditemukan
    if (detailItem == null) {
        Text("Data CSR tidak ditemukan")
        return
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C3E1F)) // hijau tua
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp) // Sesuaikan ukuran ikon jika perlu
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Status Riwayat CSR",
                    fontSize = 20.sp, // Sesuaikan ukuran font agar tidak terlalu besar
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Card Detail Informasi
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                // Strip warna kiri
                Box(
                    modifier = Modifier
                        .width(20.dp)
                        .fillMaxHeight()
                        .clip(RoundedCornerShape(topStart = 6.dp, bottomStart = 6.dp))
                        .background(Color(android.graphics.Color.parseColor(detailItem.subStatus.colorHex)))
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
                        text = detailItem.title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppins
                    )
                    Text(
                        text = detailItem.organization,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        fontFamily = poppins
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Status : ${
                            detailItem.subStatus.name.replace("_", " ").lowercase()
                                .replaceFirstChar { it.uppercase() }
                        } ${getSubStatusEmoji(detailItem.subStatus)}",
                        fontSize = 12.sp,
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
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Kategori", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, fontFamily = poppins)
                            Text(detailItem.category, fontSize = 11.sp, fontFamily = poppins)
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.fillMaxHeight().width(0.5.dp).background(Color.LightGray))
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Lokasi", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, fontFamily = poppins)
                            Text(detailItem.location, fontSize = 11.sp, fontFamily = poppins)
                        }

                        Spacer(modifier = Modifier.width(8.dp))
                        Box(modifier = Modifier.fillMaxHeight().width(0.5.dp).background(Color.LightGray))
                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End) {
                            Text("Periode", color = Color.Gray, fontSize = 10.sp, fontWeight = FontWeight.SemiBold, fontFamily = poppins)
                            Text(detailItem.period, fontSize = 11.sp, fontFamily = poppins)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (detailItem.status.lowercase() == "proses review") {
                Button(
                    onClick = { /* TODO: Implement download proposal */ },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2C3E1F),
                        contentColor = Color.White
                    )
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_download), // Load drawable
                            contentDescription = "Download",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Download Proposal Rancangan",
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                VerticalTimeline(
                    steps = listOf(
                        TimelineStep("Pengajuan Dikirim", "10/05/2024 - 09:41 WIB", isCompleted = true),
                        TimelineStep("Review & Evaluasi", "10/05/2024 - 09:50 WIB", isInProgress = true),
                        TimelineStep("Pembayaran", "10/05/2024 - 10:00 WIB"),
                        TimelineStep("Implementasi Program")
                        // Tambahkan langkah-langkah timeline lainnya sesuai kebutuhan
                    )
                )
            }
            // Tambahkan penanganan status lainnya jika diperlukan
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCsrDetailScreen() {
    CsrDetailScreen(onBack = {})
}