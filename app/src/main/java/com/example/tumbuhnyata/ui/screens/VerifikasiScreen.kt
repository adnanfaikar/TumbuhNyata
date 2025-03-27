package com.example.tumbuhnyata.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.InputField

@Composable
fun VerifikasiScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }

    val isFormFilled = email.isNotEmpty() && email.contains("@")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, top = 8.dp),
            contentAlignment = Alignment.TopStart
        ) {
            Image(
                painter = painterResource(id = R.drawable.btn_back),
                contentDescription = "Kembali",
                modifier = Modifier
                    .size(32.dp) // Ukuran tombol back
                    .clickable { navController.navigate("login") } // Navigasi kembali ke halaman option
            )
        }

        Spacer(modifier = Modifier.height(35.dp))

        Image(
            painter = painterResource(id = R.drawable.illustration),
            contentDescription = "Illustration",
            modifier = Modifier
                .fillMaxWidth()
                .height(264.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Masuk",
            fontSize = 30.sp,
            modifier = Modifier.align(Alignment.Start),
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF27361F)
        )

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = "Masukkan email anda untuk mendapatkan kode OTP",
            fontSize = 17.sp,
            textAlign = TextAlign.Justify,
            color = Color(0xFF27361F)
        )

        Spacer(modifier = Modifier.height(28.dp))

        InputField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = it.contains("@") // Validasi email harus mengandung "@"
            },
            iconRes = R.drawable.lg_email,
            label = "Email Perusahaan"
        )
        if (!isEmailValid) {
            Text(
                text = "Email harus mengandung '@'",
                color = Color.Red,
                fontSize = 14.sp,
                modifier = Modifier.align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(19.dp))

        Button(
            onClick = { navController.navigate("otp") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormFilled) Color(0xFF27361F) else Color.Gray
            )
        ) {
            Text(
                text = "Kirim Kode OTP",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
