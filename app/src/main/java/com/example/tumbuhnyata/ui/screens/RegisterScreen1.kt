package com.example.tumbuhnyata.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.InputField
import com.example.tumbuhnyata.ui.components.StepIndicator

@Composable
fun RegisterScreen1(navController: NavController) {
    var companyName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var phone by remember { mutableStateOf("") }
    var nib by remember { mutableStateOf("") }

    val isFormFilled = companyName.isNotEmpty() && email.isNotEmpty() && email.contains("@") &&
            phone.isNotEmpty() && phone.all { it.isDigit() } &&
            nib.isNotEmpty() && nib.all { it.isDigit() }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_register),
            contentDescription = "Background Register",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(300.dp))

            // Indikator Langkah
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StepIndicator(step = "1", color = Color(0xFFA5C295), strokeColor = Color.Black, textColor = Color.Black)
                Divider(modifier = Modifier.width(79.dp).height(3.dp).background(Color(0xFF525E4C)))
                StepIndicator(step = "2", color = Color.White, strokeColor = Color.Black, textColor = Color.Black)
                Divider(modifier = Modifier.width(79.dp).height(3.dp).background(Color(0xFFE2E2E2)))
                StepIndicator(step = "3", color = Color.White, strokeColor = Color(0xFFBCBCBC), textColor = Color(0xFFBCBCBC))
            }

            Spacer(modifier = Modifier.height(32.dp))

            InputField(value = companyName, onValueChange = { companyName = it }, iconRes = R.drawable.lg_company, label = "Nama Perusahaan")
            Spacer(modifier = Modifier.height(13.dp))
            InputField(
                value = email,
                onValueChange = {
                    email = it
                    isEmailValid = it.contains("@")
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
            Spacer(modifier = Modifier.height(13.dp))
            InputField(value = phone, onValueChange = { if (it.all { c -> c.isDigit() }) phone = it }, iconRes = R.drawable.lg_telepon, label = "Nomor Telepon Kantor", keyboardType = KeyboardType.Number)
            Spacer(modifier = Modifier.height(13.dp))
            InputField(value = nib, onValueChange = { if (it.all { c -> c.isDigit() }) nib = it }, iconRes = R.drawable.lg_induk, label = "NIB (Nomor Induk Berusaha)", keyboardType = KeyboardType.Number)
            Spacer(modifier = Modifier.height(33.dp))

            Button(
                onClick = { navController.navigate("register2") },
                enabled = isFormFilled,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormFilled) Color(0xFF27361F) else Color.Gray
                )
            ) {
                Text(
                    text = "Selanjutnya",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(13.dp))

            Image(
                painter = painterResource(id = R.drawable.btn_google),
                contentDescription = "Masuk dengan Google",
                modifier = Modifier.fillMaxWidth().clickable {  }
            )

            Spacer(modifier = Modifier.height(33.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Sudah mempunyai akun? ", fontSize = 14.sp, color = Color(0xFF27361F))
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF27361F),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { navController.navigate("login") }
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRegisterScreen1() {
    RegisterScreen1(navController = rememberNavController())
}
