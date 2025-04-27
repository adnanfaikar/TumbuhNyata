package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DetailSertifikasiScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, top = 95.dp, end = 20.dp)
            .padding(vertical = 16.dp)
    ) {
        TopBarProfile(
            title = "Detail Sertifikasi",
            step = "",
            iconResId = R.drawable.btn_back,
            onBackClick = { navController.popBackStack() }
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Logo ISO
        Image(
            painter = painterResource(id = R.drawable.iso_26000), // Ganti dengan drawable iso kamu
            contentDescription = "ISO Logo",
            modifier = Modifier
                .size(100.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Nama Sertifikasi",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp
        )
        Text(
            text = "ISO 26000 - Sustainability",
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Lembaga Kredensial",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp
        )
        Text(
            text = "International Organization for Standardization (ISO)",
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Deskripsi",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp
        )
        Text(
            text = "Standar global untuk tanggung jawab sosial perusahaan, mencakup tata kelola, hak asasi manusia, lingkungan, praktik ketenagakerjaan, dan keterlibatan masyarakat.",
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Manfaat",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp
        )
        Text(
            text = "1. Meningkatkan reputasi perusahaan\n" +
                    "2. Memastikan kepatuhan terhadap standar internasional\n" +
                    "3. Meningkatkan daya saing bisnis",
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Biaya",
            fontWeight = FontWeight.Bold,
            fontFamily = PoppinsFontFamily,
            fontSize = 14.sp
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(8.dp))
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Rp", fontWeight = FontWeight.Bold)
                Text(
                    text = "25.000.000",
                    fontWeight = FontWeight.Bold,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 18.sp
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("dokumen_one") },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2F4019)),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Ajukan Sekarang", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold, fontFamily = PoppinsFontFamily)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDetailSertifikasiScreen() {
    val navController = rememberNavController()
    DetailSertifikasiScreen(navController)
}