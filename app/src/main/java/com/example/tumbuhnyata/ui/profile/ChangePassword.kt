package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.viewmodel.ProfileViewModel

@Composable
fun ChangePassword(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    var newPassword by remember { mutableStateOf("") }
    var oldPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val isFormValid by remember(oldPassword, newPassword, confirmPassword) {
        derivedStateOf {
            oldPassword.length >= 7 &&
                    newPassword.length >= 8 &&
                    newPassword != oldPassword &&
                    newPassword == confirmPassword
        }
    }


    // Navigasi jika password berhasil diubah
    LaunchedEffect(profileState.isUpdated) {
        if (profileState.isUpdated) {
            navController.navigate("change_password_success") {
                popUpTo("change_password") { inclusive = true }
            }
            viewModel.resetUpdateState()
        }
    }

    if (profileState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = Color(0xFF27361F))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TopBarProfile(
                title = "",
                step = "",
                iconResId = R.drawable.btn_back,
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(35.dp))

            Image(
                painter = painterResource(id = R.drawable.img_change_password),
                contentDescription = "Reset Illustration",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(231.dp)
                    .clip(RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.height(35.dp))

            Text(
                text = "Atur Ulang Kata Sandi",
                fontSize = 30.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF27361F)
            )

            Spacer(modifier = Modifier.height(46.dp))

            // Input Fields
            PasswordTextField(
                value = oldPassword,
                onValueChange = { oldPassword = it },
                label = "Kata Sandi Lama",
                isPasswordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(13.dp))

            PasswordTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = "Kata Sandi Baru",
                isPasswordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(13.dp))

            PasswordTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = "Konfirmasi Kata Sandi",
                isPasswordVisible = passwordVisible,
                onVisibilityToggle = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Tampilkan error jika ada
            if (profileState.error != null) {
                Text(
                    text = profileState.error!!,
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Tombol
            Button(
                onClick = {
                    if (isFormValid) {
                        viewModel.changePassword(
                            currentPassword = oldPassword,
                            newPassword = newPassword
                        )
                    } else {
                        errorMessage = "Password tidak valid atau tidak sesuai"
                    }
                },
                enabled = isFormValid && !profileState.isChangingPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xFF27361F) else Color(0xFF989898)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                if (profileState.isChangingPassword) {
                    Text("Loading...", color = Color.White)
                } else {
                    Text(
                        text = "Ubah Kata Sandi",
                        fontSize = 17.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
fun PasswordTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isPasswordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                label,
                color = Color(0xFF686868),
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_password),
                contentDescription = "Password Icon",
                modifier = Modifier
                    .size(18.dp)
            )
        },
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    painter = painterResource(
                        id = if (isPasswordVisible) R.drawable.ic_visibility else R.drawable.ic_visibility_off
                    ),
                    contentDescription = if (isPasswordVisible) "Hide password" else "Show password",
                    modifier = Modifier.size(18.dp)
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(15.dp),
        singleLine = true,
        isError = isError
    )
}

@Preview
@Composable
fun PreviewChangePassword() {
    ChangePassword(navController = rememberNavController())
}