package com.example.tumbuhnyata.ui.login

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.data.model.LoginRequest
import com.example.tumbuhnyata.data.model.LoginResponse
import com.example.tumbuhnyata.data.network.RetrofitInstance
import com.example.tumbuhnyata.util.TokenManager
import org.json.JSONObject

@Composable
fun LoginScreen(navController: NavController) {
    val context = LocalContext.current
    var nib by remember { mutableStateOf("") }
    var isNibValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(131.dp))

        Row {
            Text("Selamat Datang Kembali",
                fontSize = 17.sp,
                color = Color.Black,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Logo
        Image(
            painter = painterResource(id = R.drawable.logo),
            modifier = Modifier.size(243.dp, 72.dp),
            contentDescription = "Logo"
        )

        Spacer(modifier = Modifier.height(66.dp))

        // NIB TextField (replacing Email TextField)
        OutlinedTextField(
            value = nib,
            onValueChange = {
                nib = it
                isNibValid = it.isNotBlank() && it.length <= 13
            },
            label = {
                Text(
                    "NIB (Nomor Induk Berusaha)",
                    color = Color(0xFF686868),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_doc),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "NIB Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        // Menampilkan warning jika NIB tidak valid
        if (!isNibValid) {
            Text(
                text = "NIB tidak boleh kosong dan maksimal 13 karakter",
                color = Color.Red,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(13.dp))

        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                isPasswordValid = it.length >= 8
            },
            label = {
                Text(
                    "Kata Sandi",
                    color = Color(0xFF686868),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_password),
                    modifier = Modifier.size(18.dp),
                    contentDescription = "Password Icon"
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = painterResource(id = if (passwordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                        contentDescription = if (passwordVisible) "Hide password" else "Show password",
                        modifier = Modifier.size(20.dp)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true
        )

        // Menampilkan warning jika password kurang dari 8 karakter
        if (!isPasswordValid) {
            Text(
                text = "Minimal 8 karakter",
                color = Color.Red,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(13.dp))

        // Forgot Password Text
        Text(
            "Lupa Sandi?",
            color = Color.Gray,
            fontSize = 14.sp, fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.End),
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.height(34.dp))

        // Login Button
        Button(
            onClick = {
                val loginRequest = LoginRequest(NIB = nib, password = password)

                // In your LoginScreen.kt, modify the login success handler
                RetrofitInstance.api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
                    override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                        if (response.isSuccessful) {
                            val token = response.body()?.token
                            Toast.makeText(context, "Login berhasil", Toast.LENGTH_SHORT).show()
                            // Navigate to home screen and clear the back stack
                            navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                                launchSingleTop = true
                            }
                            TokenManager.saveToken(context, token ?: "")
                        } else {
                            val errorBody = response.errorBody()?.string()
                            val errorMessage = try {
                                JSONObject(errorBody).getString("message")
                            } catch (e: Exception) {
                                "Login gagal: ${response.code()}"
                            }
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(context, "Tidak dapat terhubung ke server: ${t.localizedMessage}", Toast.LENGTH_LONG).show()
                    }
                })
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                if (nib.isNotBlank() && password.isNotBlank()) Color.Black else Color.Gray)
        ) {
            Text(
                "Masuk",
                color = Color.White,
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        // Divider
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.Gray
            )
            Text(
                "  Atau dengan  ",
                color = Color.Gray
            )
            HorizontalDivider(
                modifier = Modifier.weight(1f),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Google Sign-In Button
        Button(
            onClick = { /* Handle Google Sign-In */ },
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp))
                .height(50.dp),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_google),
                contentDescription = "Google Icon",
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                "Masuk dengan Google",
                color = Color.Black,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(73.dp))

        // Sign Up Text
        Row {
            Text(
                "Belum punya akun? ",
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal
            )
            Text(
                "Daftar",
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate("register")
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginScreen() {
    val navController = rememberNavController()
    LoginScreen(navController = navController)
}
