package com.example.tumbuhnyata.ui.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
fun VerificationOne(
    navController: NavController,
    viewModel: VerificationOneViewModel = viewModel()
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
                .padding(start = 20.dp, top = 80.dp, end = 20.dp)
        ) {
            TopBarProfile(
                title = "Verifikasi",
                step = "Langkah 1/2",
                iconResId = R.drawable.btn_back,
                onBackClick = { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.height(50.dp))
            Text(
                text = "Unggah Dokumen Pendukung",
                fontSize = 22.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 2.dp)
            )
            Text(
                text = "Unggah dokumen pendukung untuk melakukan verifikasi akun perusahaan anda",
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF4B4B4B),
                modifier = Modifier.padding(start = 2.dp, top = 10.dp, end = 5.dp)
            )

            UploadField(
                label = "Akta Pendirian Perusahaan",
                fileName = verificationState.aktaFile,
                placeholder = "Akta Pendirian",
                onUploadClick = { viewModel.uploadAktaFile("SK Kemenkumham Paragon.pdf") },
                onDelete = { viewModel.deleteAktaFile() },
                modifier = Modifier.padding(top=40.dp)
            )

            UploadField(
                label = "Surat Keterangan Domisili Perusahaan",
                fileName = verificationState.skdpFile,
                placeholder = "SKDP",
                onUploadClick = { viewModel.uploadSkdpFile("Paragon-SKDP.pdf") },
                onDelete = { viewModel.deleteSkdpFile() },
                modifier = Modifier.padding(top=20.dp)
            )

            Spacer(modifier = Modifier.height(228.dp))

            Button(
                onClick = { navController.navigate("verification_two")},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 1.dp),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    if (verificationState.isBothFilesUploaded) Color(0xFF27361F) else Color(0xFF989898)
                ),
                enabled = verificationState.isBothFilesUploaded
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
fun UploadField(
    label: String,
    fileName: String?,
    placeholder: String,
    onUploadClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = label, fontSize = 18.sp, fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .border(2.dp, Color(0xFFB9B9B9), shape = RoundedCornerShape(10.dp))
                .padding(horizontal = 16.dp)
                .clickable { onUploadClick() },
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = if (fileName != null) R.drawable.ic_doc else R.drawable.ic_upload),
                    contentDescription = "Icon",
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = fileName ?: placeholder,
                    fontSize = 16.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = if (fileName != null) FontWeight.SemiBold else FontWeight.Normal,
                    color = if (fileName != null) Color(0xFF686868) else Color(0xFFB8B8B8),
                    modifier = Modifier.weight(1f)
                )

                if (fileName != null) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_cancel),
                        contentDescription = "Delete",
                        modifier = Modifier
                            .size(20.dp)
                            .clickable { onDelete() }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewVerificationOne() {
    VerificationOne(navController = rememberNavController())
}