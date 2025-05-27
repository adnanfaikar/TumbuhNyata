package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.layout.ContentScale
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.data.model.Notification
import com.example.tumbuhnyata.viewmodel.NotificationViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun VerificationSuccess(navController: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.background_sc),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(300.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_verification_success),
                contentDescription = "Verification Success",
                modifier = Modifier
                    .width(136.dp)
                    .height(131.dp)
            )

            Spacer(modifier = Modifier.height(34.dp))

            Text(
                text = "Pengajuan Verifikasi Berhasil Dilakukan",
                fontSize = 24.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .width(322.dp)
                    .height(76.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Aktivasi akun anda diterima paling lama 24 jam setelah pengajuan",
                fontSize = 18.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Normal,
                modifier = Modifier
                    .width(353.dp)
            )

            Spacer(modifier = Modifier.height(100.dp))

            Button(
                onClick = { navController.navigate("profile") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF151E11))
            ) {
                Text(
                    text = "Kembali ke Beranda",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}


@Preview
@Composable
fun PreviewVerificationSuccess() {
    VerificationSuccess(navController = rememberNavController())
}