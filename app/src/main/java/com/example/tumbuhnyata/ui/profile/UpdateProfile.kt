package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
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
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UpdateProfile(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val profileState by viewModel.profileState.collectAsState()

    LaunchedEffect(profileState.isUpdated) {
        if (profileState.isUpdated) {
            viewModel.checkPendingProfileSync()
            // Navigate back or show success message
            navController.popBackStack()
            viewModel.resetUpdateState()
        }
    }

    var companyName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    var initialCompanyName by remember { mutableStateOf("") }
    var initialEmail by remember { mutableStateOf("") }
    var initialPhoneNumber by remember { mutableStateOf("") }
    var initialAddress by remember { mutableStateOf("") }

    var errorMessage by remember { mutableStateOf<String?>(null) }


    val isFormValid by remember(companyName, email, phoneNumber, address,
        initialCompanyName, initialEmail, initialPhoneNumber, initialAddress) {
        derivedStateOf {
            companyName != initialCompanyName ||
                    email != initialEmail ||
                    phoneNumber != initialPhoneNumber ||
                    address != initialAddress
        }
    }

    LaunchedEffect(profileState.isLoading) {
        if (!profileState.isLoading) {
            companyName = profileState.companyName
            email = profileState.email
            phoneNumber = profileState.phoneNumber
            address = profileState.companyAddress

            // Set nilai awal untuk pembanding
            initialCompanyName = profileState.companyName
            initialEmail = profileState.email
            initialPhoneNumber = profileState.phoneNumber
            initialAddress = profileState.companyAddress
        }
    }


    // LaunchedEffect(Unit) {
    //     snapshotFlow { profileState.isUpdated }
    //         .collectLatest { isUpdated ->
    //             if (isUpdated) {
    //                 navController.navigate("profile") {
    //                     popUpTo("updateProfile") { inclusive = true }
    //                 }
    //                 // Beri delay kecil agar recomposition tidak langsung terjadi
    //                 kotlinx.coroutines.delay(200)
    //                 viewModel.resetUpdateState()
    //             }
    //         }
    // }

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
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            TopBarProfile(
                title = "",
                step = "",
                iconResId = R.drawable.btn_back,
                onBackClick = { navController.popBackStack() }
            )
            Spacer(modifier = Modifier.height(35.dp))

            Column(
                modifier = Modifier
                    .padding(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(id = R.drawable.img_profile), // Ganti dengan resource logo kamu
                        contentDescription = "Company Logo",
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color(0xFF4B4B4B), CircleShape)
                    )

                    Spacer(modifier = Modifier.width(20.dp))

                    Column {
                        Text(
                            text = "Nama Perusahaan",
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF4B4B4B),
                            modifier = Modifier
                                .padding(bottom = 5.dp)
                        )
                        OutlinedTextField(
                            value = companyName,
                            onValueChange = { companyName = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF4B4B4B)
                            ),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .height(50.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF27361F),
                                unfocusedBorderColor = Color(0xFF4B4B4B)
                            )
                        )
                    }
                }

                Text(
                    text = "Ganti Foto",
                    fontSize = 12.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF4B4B4B),
                    modifier = Modifier
                        .clickable { /* TODO: Open image picker */ }
                )

                Spacer(modifier = Modifier.height(46.dp))

                // Email
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF4B4B4B),
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("Email")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("*")
                        }
                    }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B4B4B)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .height(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF27361F),
                        unfocusedBorderColor = Color(0xFF4B4B4B)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Nomor Telepon
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF4B4B4B),
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("Nomor Telepon")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("*")
                        }
                    }
                )

                OutlinedTextField(
                    value = phoneNumber,
                    onValueChange = { phoneNumber = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B4B4B)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .height(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF27361F),
                        unfocusedBorderColor = Color(0xFF4B4B4B)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Alamat
                Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xFF4B4B4B),
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("Alamat")
                        }
                        withStyle(
                            style = SpanStyle(
                                color = Color.Red,
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Normal
                            )
                        ) {
                            append("*")
                        }
                    }
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF4B4B4B)
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp)
                        .height(50.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF27361F),
                        unfocusedBorderColor = Color(0xFF4B4B4B)
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))
            }

            Spacer(modifier = Modifier.weight(1f))

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


            // Tombol Simpan
            Button(
                onClick = {
                    if (isFormValid) {
                        errorMessage = null
                        viewModel.updateProfile(companyName, email, phoneNumber, address)
                    } else {
                        errorMessage = "Tidak ada perubahan data yang dilakukan"
                    }
                },
                enabled = isFormValid && !profileState.isUpdated,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 1.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormValid) Color(0xFF27361F) else Color(0xFF989898)
                )
            ) {
                if (profileState.isUpdatingProfile) {
                    Text("Loading...", color = Color.White)
                } else {
                    Text(
                        text = "Simpan",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
@Preview
@Composable
fun PreviewUpdateProfile() {
    UpdateProfile(navController = rememberNavController())
}