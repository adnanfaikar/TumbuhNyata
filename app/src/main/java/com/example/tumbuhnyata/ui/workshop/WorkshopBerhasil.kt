package com.example.tumbuhnyata.ui.workshop

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun WorkshopBerhasil(navController: NavController) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.bg_sukses),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            Image(
                painter = painterResource(id = R.drawable.lg_check),
                contentDescription = "Berhasil Buat Akun",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Pendaftaran Workshop Berhasil",
                fontSize = 24.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Informasi mengenai pembayaran dan event workshop akan dikirim melalui email",
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { navController.navigate("workshop") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151E11))
            ) {
                Text(
                    text = "Kembali ke Beranda",
                    fontFamily = PoppinsFontFamily,
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview
@Composable
fun PreviewWorkshopBerhasil() {
    WorkshopBerhasil(navController = rememberNavController())
}