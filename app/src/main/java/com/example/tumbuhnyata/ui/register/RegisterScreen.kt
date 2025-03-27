package com.example.tumbuhnyata.ui.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(navController: NavController) {
    var step by remember { mutableStateOf(1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
        ,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection()

        Spacer(modifier = Modifier.height(20.dp))

        StepIndicator(currentStep = step)



        AnimatedContent(targetState = step, label = "Register Steps") { currentStep ->
            when (currentStep) {
                1 -> StepOne { step++ }
                2 -> StepTwo { step++ }
                3 -> StepThree(onRegister = { step++ }, navController = navController)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sign Up Text
        Row {
            Text(
                "Sudah mempunyai akun? ",
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal
            )
            Text(
                "Masuk",
                color = Color.Black,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable {
                    navController.navigate("login")
                }
            )
        }
    }
}

@Composable
fun HeaderSection() {
    Image(
        painter = painterResource(id = R.drawable.register_header),
        contentDescription = "Header",
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp),
        contentScale = ContentScale.Crop
    )
}


@Composable
fun StepIndicator(currentStep: Int) {
    val stepCount = 3
    val activeColor = Color(0xFFA5C295)
    val idleColor = Color.White
    val inactiveColor = Color.LightGray.copy(alpha = 0.5f)
    val lineColor = Color.Black
    val lineInactiveColor = Color.LightGray.copy(alpha = 0.5f)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.width(IntrinsicSize.Min)
    ) {
        for (i in 1..stepCount) {
            if (i > 1) {
                val lineBetweenColor = if (i - 2 < currentStep) lineColor else lineInactiveColor

                Spacer(modifier = Modifier.width(4.dp))
                Box(
                    modifier = Modifier
                        .height(2.dp)
                        .width(79.dp)
                        .background(lineBetweenColor)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            val indicatorColor = when {
                i < currentStep -> activeColor
                i == currentStep -> activeColor
                i == currentStep + 1 -> idleColor
                else -> inactiveColor
            }

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(27.dp)
                    .clip(CircleShape)
                    .background(if (indicatorColor == activeColor) activeColor else Color.White)
                    .border(2.dp, if (indicatorColor == inactiveColor) inactiveColor else Color.Black, CircleShape)
            ) {
                Text(
                    text = i.toString(),
                    color = if (indicatorColor == inactiveColor) inactiveColor else Color.Black,
                    fontFamily = PoppinsFontFamily,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                )
            }
        }
    }
}





@Composable
fun StepOne(onNext: () -> Unit) {
    var companyName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var phoneNumber by remember { mutableStateOf("") }
    var nib by remember { mutableStateOf("") }

    val isNextAvailable = companyName.isNotBlank() && email.isNotBlank() && phoneNumber.isNotBlank() && nib.isNotBlank()

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(4.dp))

        //nama perusahaan
        OutlinedTextField(
            value = companyName,
            onValueChange = { companyName = it },
            label = {
                Text(
                    "Nama Perusahaan",
                    color = Color(0xFF686868),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                ) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_office),
                    modifier = Modifier.size(18.dp),
                    contentDescription = "Office Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(13.dp))

        // email perusahaan
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                isEmailValid = it.contains("@")
            },
            label = {
                Text(
                    "Email Perusahaan",
                    color = Color(0xFF686868),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Email Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        // Menampilkan warning jika email tidak valid
        if (!isEmailValid) {
            Text(
                text = "Email harus mengandung '@'",
                color = Color.Red,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // no telp perusahaan
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } && newValue.length <= 12) {
                    phoneNumber = newValue
                }
            },
            label = {
                Text(
                    "Nomor Telepon Kantor" ,
                    color = Color(0xFF686868),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                ) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_phone),
                    modifier = Modifier.size(18.dp),
                    contentDescription = "Phone Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        //nib perusahaan
        OutlinedTextField(
            value = nib,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } && newValue.length <= 13) {
                    nib = newValue
                }
            },
            label = {
                Text(
                    "NIB (Nomor Induk Berusaha)" ,
                    color = Color(0xFF686868),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                ) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_doc),
                    modifier = Modifier.size(18.dp),
                    contentDescription = "Document Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(33.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                if (isNextAvailable) Color(0xFF27361F) else Color(0xFF989898)
            ),
            enabled = isNextAvailable // Button hanya aktif jika valid
        ) {
            Text(
                text = "Selanjutnya",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Google Sign-Up Button
        Button(
            onClick = { /* Handle Google Sign-Up */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
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


    }
}


@Composable
fun StepTwo(onNext: () -> Unit) {
    Column(
        modifier = Modifier.padding(16.dp)
    ){
        Spacer(modifier = Modifier.height(4.dp))

        Text(
            "Lokasi Kantor",
            color = Color.Black,
            fontSize = 25.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(33.dp))

        Box (
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .border(1.dp, Color.Gray, RoundedCornerShape(15.dp))
        ){
            Column() {
                // TextField untuk "Cari Alamat"
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = {
                        Text(
                            "Cari Alamat",
                            color = Color(0xFF686868),
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                        ) },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_loc),
                            modifier = Modifier.size(18.dp),
                            contentDescription = "Location Icon"
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .height(55.dp),
                    shape = RoundedCornerShape(10.dp),
                    singleLine = true
                )

                Divider(color = Color.White, thickness = 1.dp)

                // Lokasi Saat Ini
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_mark_loc),
                        contentDescription = "Current Location Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(18.dp))
                    Column {
                        Text(
                            "Lokasi Anda Saat Ini",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color.Black
                        )
                        Text(
                            "Jl. Joyo Raharjo No.185, Merjosari, Kec. Lowokwaru. Kota Malang, Jawa Timur 65144, Indonesia",
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                Divider(color = Color.Gray, thickness = 1.dp)

                // Pilih Lewat Peta
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_map),
                        contentDescription = "Map Icon",
                        tint = Color(0xFF3A5A40),
                        modifier = Modifier.size(20.dp)
                    )

                    Spacer(modifier = Modifier.width(18.dp))

                    Text(
                        "Pilih Lewat Peta",
                        fontFamily = PoppinsFontFamily,
                        color = Color(0xFF3A5A40),
                        fontWeight = FontWeight.Bold
                    )
                }


            }
        }

        Spacer(modifier = Modifier.height(33.dp))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(Color(0xFF27361F))
        ) {
            Text(
                text = "Selanjutnya",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.White)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle Google Sign-Up */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
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
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }


}


@Composable
fun StepThree(onRegister: () -> Unit, navController: NavController) {
    var picName by remember { mutableStateOf("") }
    var picEmail by remember { mutableStateOf("") }
    var isPicEmailValid by remember { mutableStateOf(true) }
    var password by remember { mutableStateOf("") }
    var isPasswordValid by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false)}
    var isChecked by remember { mutableStateOf(false) }
    val annotatedString = buildAnnotatedString {
        append("Dengan melakukan login atau registrasi, Anda menyetujui ")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = PoppinsFontFamily)) {
            append("Syarat & Ketentuan")
        }

        append(" serta ")

        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontFamily = PoppinsFontFamily)) {
            append("Kebijakan Privasi")
        }
    }
    val isFormValid = picName.isNotBlank() && picEmail.isNotBlank() && password.isNotBlank() && isChecked

    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            "Penanggung Jawab Akun",
            color = Color.Black,
            fontSize = 25.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(15.dp))

        //nama pic
        OutlinedTextField(
            value = picName,
            onValueChange = { picName = it },
            label = {
                Text(
                    "Nama PIC CSR",
                    color = Color(0xFF686868),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp,
                ) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_person),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "PIC Name Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true
        )


        Spacer(modifier = Modifier.height(16.dp))

        //email pic
        OutlinedTextField(
            value = picEmail,
            onValueChange = {
                picEmail = it
                isPicEmailValid = it.contains("@")
            },
            label = {
                Text(
                    "Email Perusahaan",
                    color = Color(0xFF686868),
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_email),
                    modifier = Modifier.size(20.dp),
                    contentDescription = "Email Icon"
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(15.dp),
            singleLine = true,
        )

        // Menampilkan warning jika email tidak valid
        if (!isPicEmailValid) {
            Text(
                text = "Email harus mengandung '@'",
                color = Color.Red,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                modifier = Modifier
                    .padding(top = 4.dp)
                    .align(Alignment.Start)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        //password pic
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

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it }
            )
            Text(
                text = annotatedString,
                fontSize = 12.sp,
                color = Color.Black,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { navController.navigate("verifikasi") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                if (isFormValid) Color(0xFF27361F) else Color(0xFF989898)
            ),
            enabled = isFormValid
        ) {
            Text(
                text = "Daftar",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp,
                color = Color.White,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { /* Handle Google Sign-Up */ },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(1.dp, Color.Black, RoundedCornerShape(10.dp)),
            shape = RoundedCornerShape(10.dp),
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
    }
}

@Preview
@Composable
fun PreviewRegisterScreen() {
    val navController = rememberNavController()
    RegisterScreen(navController = navController)
}
