package com.example.tumbuhnyata.ui.dashboard.upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun UploadSuccessScreen(navController: NavController) {
    val gradientBrush = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to Color(0xFF5A7C47),
            0.48f to Color(0xFF415A33),
            1.0f to Color(0xFF27361F)
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {

        Image(
            painter = painterResource(id = R.drawable.background_sc),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.8f)
                    .align(Alignment.Center),
                contentAlignment = Alignment.Center
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .zIndex(1f)
                ) {

                    Icon(
                        painter = painterResource(id = R.drawable.ic_pembaruandatasplashlogo),
                        contentDescription = "Success",
                        modifier = Modifier.size(190.dp),
                        tint = Color.Unspecified
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Pembaruan Data Anda\nBerhasil Diunggah",
                        fontSize = 22.sp,
                        fontFamily = PoppinsFontFamily,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Pembaruan data telah diterima dan sedang\ndalam proses verifikasi dan analisis oleh tim",
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center,
                        lineHeight = 20.sp
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Estimasi waktu proses: 2-3 hari kerja",
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(horizontal = 32.dp, vertical = 16.dp)
            ) {

                Text(
                    text = "Notifikasi Email: Perusahaan akan menerima email konfirmasi dan link untuk melacak status pembaruan",
                    fontSize = 10.sp,
                    fontFamily = PoppinsFontFamily,
                    color = Color.White.copy(alpha = 0.7f),
                    textAlign = TextAlign.Left,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                

                Button(
                    onClick = {
                        navController.navigate("dashboard") {
                            popUpTo("dashboard") { inclusive = true }
                            launchSingleTop = true
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF151E11).copy(alpha = 0.8f)
                    )
                ) {
                    Text(
                        text = "Kembali ke Dashboard",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF415A33)
@Composable
fun UploadSuccessScreenPreview() {
    UploadSuccessScreen(navController = rememberNavController())
}
