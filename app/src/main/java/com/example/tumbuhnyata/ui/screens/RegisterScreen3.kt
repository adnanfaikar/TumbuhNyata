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
import androidx.compose.ui.text.style.TextAlign
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
fun RegisterScreen3(navController: NavController) {
    var PICName by remember { mutableStateOf("") }
    var emailPIC by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }
    var isChecked by remember { mutableStateOf(false) }

    val isFormFilled = PICName.isNotEmpty() && emailPIC.isNotEmpty() && emailPIC.contains("@") && password.isNotEmpty() && password.length >= 8 && isChecked

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
                StepIndicator(
                    step = "2", color = Color(0xFFA5C295), strokeColor = Color.Black, textColor = Color.Black,
                    modifier = Modifier.clickable { navController.navigate("register2") }
                )
                Divider(modifier = Modifier.width(79.dp).height(3.dp).background(Color(0xFF525E4C)))
                StepIndicator(step = "3", color = Color(0xFFA5C295), strokeColor = Color.Black, textColor = Color.Black)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Penanggung Jawab Akun",
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF27361F)
            )

            Spacer(modifier = Modifier.height(20.dp))

            InputField(value = PICName, onValueChange = { PICName = it }, iconRes = R.drawable.lg_pic, label = "Nama PIC CSR")
            Spacer(modifier = Modifier.height(13.dp))
            InputField(
                value = emailPIC,
                onValueChange = {
                    emailPIC = it
                    isEmailValid = it.contains("@") // Validasi email harus mengandung "@"
                },
                iconRes = R.drawable.lg_email,
                label = "Email PIC"
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
            PasswordInputField(
                value = password,
                onValueChange = {
                    password = it
                    isPasswordValid = it.length >= 8
                }
            )
            if (!isPasswordValid) {
                Text(
                    text = "Minimal 8 karakter",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.Start)
                )
            }
            Spacer(modifier = Modifier.height(13.dp))

            // Checkbox for Terms & Conditions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp).clickable { isChecked = !isChecked }
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { isChecked = it },
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF27361F))
                )
                Spacer(modifier = Modifier.width(4.dp))
                Column {
                    Text(
                        text = "Dengan melakukan login atau registrasi, Anda menyetujui ",
                        fontSize = 12.sp,
                        color = Color(0xFF27361F)
                    )
                    Row {
                        Text(
                            text = "Syarat & Ketentuan",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            color = Color(0xFF27361F),
                            modifier = Modifier.clickable { /* Handle Terms Click */ }
                        )
                        Text(
                            text = " serta ",
                            fontSize = 12.sp,
                            color = Color(0xFF27361F)
                        )
                        Text(
                            text = "Kebijakan Privasi",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textDecoration = TextDecoration.Underline,
                            color = Color(0xFF27361F),
                            modifier = Modifier.clickable { /* Handle Privacy Policy Click */ }
                        )
                    }
                }
            }

            Button(
                onClick = { navController.navigate("verifikasi") },
                enabled = isFormFilled,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormFilled) Color(0xFF27361F) else Color.Gray
                )
            ) {
                Text(
                    text = "Daftar",
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
fun PreviewRegisterScreen3() {
    RegisterScreen3(navController = rememberNavController())
}