package com.example.tumbuhnyata.ui.Sertifikasi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun CertificationSuccessScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_sukses), // Background image dari drawable
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.gambar_sertifikasi), // Ilustrasi sertifikat
                contentDescription = "Sertifikat",
                modifier = Modifier
                    .size(180.dp)
                    .padding(bottom = 24.dp)
            )

            Text(
                text = "Pengajuan Sertifikasi\nBerhasil!",
                textAlign = TextAlign.Center,
                fontFamily = PoppinsFontFamily,
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Pengajuan Anda telah diterima dan sedang\n" +
                        "dalam proses verifikasi dokumen oleh tim auditor",
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Estimasi waktu proses: 10–30 hari kerja",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Notifikasi Email: ",
                fontSize = 10.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "Perusahaan akan menerima email konfirmasi dan\nlink untuk melacak status pengajuan",
                fontSize = 10.sp,
                fontFamily = PoppinsFontFamily,
                textAlign = TextAlign.Center,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(5.dp))

            Button(
                onClick = { /* TODO: Lihat Riwayat */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Lihat Detail Riwayat", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold,  fontFamily = PoppinsFontFamily)
            }
            

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* TODO: Kembali ke Beranda */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Kembali ke Beranda", color = Color.Black, fontSize = 16.sp, fontWeight = FontWeight.Bold,  fontFamily = PoppinsFontFamily)
            }
        }
    }
}

@Preview()
@Composable
fun CertificationSuccessScreenPreview() {
    CertificationSuccessScreen()
}
