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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: RegisterViewModel = viewModel()
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
                viewModel.clearError()
            }
        }
    }
    
    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            navController.navigate("verifikasi")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            HeaderSection()

            Spacer(modifier = Modifier.height(20.dp))

            StepIndicator(currentStep = currentStep)

            AnimatedContent(targetState = currentStep, label = "Register Steps") { step ->
                when (step) {
                    1 -> StepOne(viewModel = viewModel, onNext = { viewModel.nextStep() })
                    2 -> StepTwo(viewModel = viewModel, onNext = { viewModel.nextStep() })
                    3 -> StepThree(viewModel = viewModel, navController = navController)
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
        
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFFA5C295))
            }
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
fun StepOne(viewModel: RegisterViewModel, onNext: () -> Unit) {
    val companyName by viewModel.companyName.collectAsState()
    val email by viewModel.email.collectAsState()
    val phoneNumber by viewModel.phoneNumber.collectAsState()
    val nib by viewModel.nib.collectAsState()
    val address by viewModel.address.collectAsState()
    
    var isEmailValid by remember { mutableStateOf(true) }
    
    val isNextAvailable = companyName.isNotBlank() && 
                           email.isNotBlank() && isEmailValid && 
                           phoneNumber.isNotBlank() && 
                           nib.isNotBlank() &&
                           address.isNotBlank()

    Column(modifier = Modifier.padding(16.dp)) {

        Spacer(modifier = Modifier.height(4.dp))

        // nama perusahaan
        OutlinedTextField(
            value = companyName,
            onValueChange = { viewModel.updateCompanyName(it) },
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
                viewModel.updateEmail(it)
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
                    viewModel.updatePhoneNumber(newValue)
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

        // nib perusahaan
        OutlinedTextField(
            value = nib,
            onValueChange = { newValue ->
                if (newValue.all { it.isDigit() } && newValue.length <= 13) {
                    viewModel.updateNIB(newValue)
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

        Spacer(modifier = Modifier.height(16.dp))

        // alamat perusahaan
        OutlinedTextField(
            value = address,
            onValueChange = { viewModel.updateAddress(it) },
            label = {
                Text(
                    "Alamat Perusahaan" ,
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
            enabled = isNextAvailable
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
fun StepTwo(viewModel: RegisterViewModel, onNext: () -> Unit) {
    val address by viewModel.address.collectAsState()
    
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
                    value = address,
                    onValueChange = { viewModel.updateAddress(it) },
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
                        .clickable {
                            // Replace with actual current location
                            viewModel.updateAddress("Jl. Joyo Raharjo No.185, Merjosari, Kec. Lowokwaru. Kota Malang, Jawa Timur 65144, Indonesia")
                        }
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
                        .clickable {
                            // Handle map selection
                        }
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
fun StepThree(viewModel: RegisterViewModel, navController: NavController) {
    val companyEmail by viewModel.email.collectAsState()
    val password by viewModel.password.collectAsState()
    
    var picName by remember { mutableStateOf("") }
    var picEmail by remember { mutableStateOf("") }
    var isPicEmailValid by remember { mutableStateOf(true) }
    var isPasswordValid by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(false) }
    
    LaunchedEffect(password) {
        isPasswordValid = password.length >= 8
    }
    
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
    val isFormValid = picName.isNotBlank() && password.isNotBlank() && isPasswordValid && isChecked

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

        // nama pic
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

        // email pic (using same as company email)
        OutlinedTextField(
            value = companyEmail,
            onValueChange = {
                // Using company email, so not editable here
            },
            enabled = false,
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

        Spacer(modifier = Modifier.height(16.dp))

        // password pic
        OutlinedTextField(
            value = password,
            onValueChange = {
                viewModel.updatePassword(it)
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
            onClick = { viewModel.register() },
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
