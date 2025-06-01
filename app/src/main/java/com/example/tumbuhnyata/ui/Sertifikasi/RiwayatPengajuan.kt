package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.example.tumbuhnyata.viewmodel.RiwayatPengajuan
import com.example.tumbuhnyata.viewmodel.RiwayatPengajuanViewModel

@Composable
fun RiwayatPengajuanScreen(navController: NavController) {
    val viewModel: RiwayatPengajuanViewModel = viewModel()
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
                RiwayatPengajuanSection(state.riwayatList)
            }
        }
    }
}

@Composable
fun RiwayatPengajuanSection(riwayatList: List<RiwayatPengajuan>) {
    Spacer(modifier = Modifier.height(8.dp))

    riwayatList.forEach { riwayat ->
        RiwayatPengajuanCard(riwayat)
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun RiwayatPengajuanCard(data: RiwayatPengajuan) {
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
                    .padding(end = 16.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    data.title,
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 17.sp
                )
                Text(
                    data.status,
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    color = when (data.status) {
                        "Dalam Proses" -> Color(0xFFFFA500)
                        "Selesai" -> Color(0xFF4CAF50)
                        else -> Color.Gray
                    }
                )
                Text(
                    data.tanggal,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewRiwayatPengajuanScreen() {
    val navController = rememberNavController()
    RiwayatPengajuanScreen(navController)
}