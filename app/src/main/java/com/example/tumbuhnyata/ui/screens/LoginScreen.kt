package com.example.tumbuhnyata.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.tumbuhnyata.R
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import com.example.tumbuhnyata.ui.components.InputField


@Composable
fun LoginScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }

    val isFormFilled = email.isNotEmpty() && email.contains("@") && password.isNotEmpty() && password.length >= 8

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(140.dp))

        Text(
            text = "Selamat datang kembali",
            fontSize = 17.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF1E1E1E)
        )

        Spacer(modifier = Modifier.height(30.dp))

        Image(
            painter = painterResource(id = R.drawable.logo_hitam),
            contentDescription = "Logo Hitam",
            modifier = Modifier.size(243.dp, 72.dp)
        )

        Spacer(modifier = Modifier.height(70.dp))

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

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Text(
                text = "Lupa sandi?",
                fontSize = 12.sp,
                color = Color(0xFF27361F),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {  }
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Button(
            onClick = { /* Navigate to login */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormFilled) Color(0xFF27361F) else Color.Gray
            )
        ) {
            Text(
                text = "Masuk",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Divider(
                modifier = Modifier.weight(1f),
                color = Color.Gray,
                thickness = 1.dp
            )

            Text(
                text = "Atau dengan",
                fontSize = 12.sp,
                color = Color(0xFF4B4B4B),
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Divider(
                modifier = Modifier.weight(1f),
                color = Color.Gray,
                thickness = 1.dp
            )
        }

        Spacer(modifier = Modifier.height(34.dp))

        Image(
            painter = painterResource(id = R.drawable.btn_google),
            contentDescription = "Masuk dengan Google",
            modifier = Modifier.fillMaxWidth().clickable {  }
        )

        Spacer(modifier = Modifier.height(73.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Belum punya akun? ", fontSize = 14.sp, color = Color(0xFF27361F))
            Text(
                text = "Daftar",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF27361F),
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { navController.navigate("register") }
            )
        }
    }
}

@Composable
fun PasswordInputField(
    value: String,
    onValueChange: (String) -> Unit
) {
    var passwordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text("Kata Sandi") },
        singleLine = true,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.lg_password),
                contentDescription = "Password Icon"
            )
        },
        trailingIcon = {
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "Toggle Password Visibility"
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth().background(Color.White),
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFF27361F),
            unfocusedIndicatorColor = Color.Gray,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
}
