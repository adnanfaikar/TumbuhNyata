package com.example.tumbuhnyata.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.navigation.NavController
import com.example.tumbuhnyata.R

@Composable
fun OtpScreen(navController: NavController) {
    var otpCode by remember { mutableStateOf(List(4) { TextFieldValue("") }) }
    var isTimerRunning by remember { mutableStateOf(true) }
    var timer by remember { mutableStateOf(30) }
    val focusRequesters = List(4) { FocusRequester() }

    val isOtpFilled = otpCode.all { it.text.isNotEmpty() } // Cek apakah OTP sudah terisi penuh

    // Countdown timer coroutine
    LaunchedEffect(isTimerRunning) {
        while (timer > 0 && isTimerRunning) {
            delay(1000L)
            timer--
        }
        isTimerRunning = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

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
                    .size(32.dp)
                    .clickable { navController.navigate("verifikasi") }
            )
        }

        Spacer(modifier = Modifier.height(35.dp))

        Image(
            painter = painterResource(id = R.drawable.illustration2),
            contentDescription = "Illustration 2",
            modifier = Modifier
                .fillMaxWidth()
                .height(264.dp)
        )

        Spacer(modifier = Modifier.height(50.dp))

        Text(
            text = "Masukkan Kode OTP",
            fontSize = 30.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF27361F)
        )

        Spacer(modifier = Modifier.height(13.dp))

        Text(
            text = "Kode OTP sudah dikirimkan melalui email Anda",
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF27361F)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Kotak OTP berbentuk bulat
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            otpCode.forEachIndexed { index, textValue ->
                OutlinedTextField(
                    value = textValue,
                    onValueChange = {
                        if (it.text.length <= 1 && it.text.all { char -> char.isDigit() }) {
                            val newOtp = otpCode.toMutableList()
                            newOtp[index] = it
                            otpCode = newOtp

                            if (it.text.isNotEmpty() && index < 3) {
                                focusRequesters[index + 1].requestFocus()
                            }
                        }
                    },
                    shape = CircleShape,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .size(60.dp)
                        .focusRequester(focusRequesters[index])
                        .onPreviewKeyEvent { event ->
                            if (event.type == KeyEventType.KeyDown && event.key == Key.Backspace) {
                                if (otpCode[index].text.isEmpty() && index > 0) {
                                    focusRequesters[index - 1].requestFocus()
                                }
                            }
                            false
                        },
                    textStyle = LocalTextStyle.current.copy(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(62.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tidak menerima kode?",
                fontSize = 16.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Kirim ulang",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = if (isTimerRunning) Color.Gray else Color.Black,
                modifier = Modifier
                    .clickable(enabled = !isTimerRunning) {
                        if (!isTimerRunning) {
                            timer = 30
                            isTimerRunning = true
                        }
                    }
            )
        }

        Spacer(modifier = Modifier.height(19.dp))

        // Timer
        Text(
            text = "0:${if (timer < 10) "0$timer" else timer}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = { navController.navigate("akunberhasil") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isOtpFilled) Color(0xFF27361F) else Color.Gray
            ),
            enabled = isOtpFilled
        ) {
            Text(
                text = "Verifikasi",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}
