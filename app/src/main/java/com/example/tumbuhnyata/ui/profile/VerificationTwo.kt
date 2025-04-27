package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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

@Composable
fun VerificationTwo(
    navController: NavController,
    viewModel: VerificationTwoViewModel = viewModel()
) {
    val verificationState by viewModel.verificationState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_circle_top),
            contentDescription = "background",
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopStart)
                .offset(x = (-30).dp, y = (-10).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.bg_circle_bottom),
            contentDescription = "background",
            modifier = Modifier
                .size(310.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (40).dp, y = (0).dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 95.dp, end = 20.dp)
        ) {
            TopBarProfile(
                title = "Verifikasi",
                step = "Langkah 2/2",
                iconResId = R.drawable.btn_back,
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Unggah Data Diri PIC",
                fontSize = 23.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 2.dp)
            )
            Text(
                text = "Lakukan unggah scan kartu identitas PIC atau penanggungjawab perusahaan (KTP/Paspor)",
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B4B4B),
                modifier = Modifier.padding(start = 2.dp, top = 10.dp, end = 5.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            UploadBox(
                fileName = verificationState.picFile,
                onUploadClick = { viewModel.uploadPicFile("KTP_Virna.jpg") },
                onCancelClick = { viewModel.deletePicFile() }
            )

            Spacer(modifier = Modifier.height(256.dp))

            Button(
                onClick = {navController.navigate("verification_success")},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = verificationState.isFileUploaded,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (verificationState.isFileUploaded) Color(0xFF27361F) else Color(0xFF989898)
                )
            ) {
                Text(
                    text = "Selanjutnya",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun UploadBox(
    fileName: String?,
    onUploadClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .border(2.dp, Color(0xFFB9B9B9), shape = RoundedCornerShape(10.dp))
            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
            .clickable { if (fileName == null) onUploadClick() },
        contentAlignment = Alignment.Center
    ) {
        if (fileName != null) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_doc),
                    contentDescription = "Uploaded File Icon",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = fileName,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(0Xff686868),
                    textAlign = TextAlign.Center
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_cancel),
                contentDescription = "Cancel Upload",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clickable { onCancelClick() }
            )
        } else {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_upload_file),
                    contentDescription = "Upload Icon",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Pilih file JPG, JPEG, PNG, dan PDF",
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFB9B9B9),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewVerificationTwo() {
    VerificationTwo(navController = rememberNavController())
}package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.components.TopBarProfile
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun VerificationTwo(navController: NavController) {
    var uploadedFileName by remember { mutableStateOf<String?>(null) }
    val isFileUploaded = uploadedFileName != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8))
    ) {
        Image(
            painter = painterResource(id = R.drawable.bg_circle_top),
            contentDescription = "background",
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopStart)
                .offset(x = (-30).dp, y = (-10).dp)
        )
        Image(
            painter = painterResource(id = R.drawable.bg_circle_bottom),
            contentDescription = "background",
            modifier = Modifier
                .size(310.dp)
                .align(Alignment.BottomEnd)
                .offset(x = (40).dp, y = (0).dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 20.dp, top = 95.dp, end = 20.dp)
        ) {
            TopBarProfile(
                title = "Verifikasi",
                step = "Langkah 2/2",
                iconResId = R.drawable.btn_back,
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = "Unggah Data Diri PIC",
                fontSize = 23.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 2.dp)
            )
            Text(
                text = "Lakukan unggah scan kartu identitas PIC atau penanggungjawab perusahaan (KTP/Paspor)",
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B4B4B),
                modifier = Modifier.padding(start = 2.dp, top = 10.dp, end = 5.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            UploadBox(
                fileName = uploadedFileName,
                onUploadClick = {
                    uploadedFileName = "KTP_Virna.jpg" // Simulasi file berhasil diupload
                },
                onCancelClick = {
                    uploadedFileName = null
                }
            )

            Spacer(modifier = Modifier.height(256.dp))

            Button(
                onClick = {navController.navigate("verification_success")},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(10.dp),
                enabled = isFileUploaded,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFileUploaded) Color(0xFF27361F) else Color(0xFF989898)
                )
            ) {
                Text(
                    text = "Selanjutnya",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun UploadBox(
    fileName: String?,
    onUploadClick: () -> Unit,
    onCancelClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(170.dp)
            .border(2.dp, Color(0xFFB9B9B9), shape = RoundedCornerShape(10.dp))
            .background(Color(0xFFFFFFFF), shape = RoundedCornerShape(10.dp))
            .clickable { if (fileName == null) onUploadClick() },
        contentAlignment = Alignment.Center
    ) {
        if (fileName != null) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_doc),
                    contentDescription = "Uploaded File Icon",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = fileName,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(0Xff686868),
                    textAlign = TextAlign.Center
                )
            }

            Image(
                painter = painterResource(id = R.drawable.ic_cancel),
                contentDescription = "Cancel Upload",
                modifier = Modifier
                    .size(50.dp)
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .clickable { onCancelClick() }
            )
        } else {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_upload_file),
                    contentDescription = "Upload Icon",
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.height(18.dp))
                Text(
                    text = "Pilih file JPG, JPEG, PNG, dan PDF",
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFFB9B9B9),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewVerificationTwo() {
    VerificationTwo(navController = rememberNavController())
}