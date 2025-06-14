package com.example.tumbuhnyata.ui.workshop

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.viewmodel.WorkshopViewModel

@Composable
fun DaftarWorkshop(
    navController: NavController,
    workshopId: String? = null,
    viewModel: WorkshopViewModel
) {
    val uriHandler = LocalUriHandler.current

    val fileSelected by viewModel.fileSelected.collectAsState()
    val fileName by viewModel.fileName.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val registerSuccess by viewModel.registerSuccess.collectAsState()
    val profileLoaded by viewModel.profileLoaded.collectAsState()
    val currentWorkshopId by viewModel.workshopId.collectAsState()

    var isChecked by remember { mutableStateOf(false) }

    LaunchedEffect(workshopId) {
        workshopId?.let { id ->
            viewModel.setWorkshopId(id)
            Log.d("DaftarWorkshop", "Workshop ID set: $id")
        }
    }

    LaunchedEffect(registerSuccess) {
        if (registerSuccess) {
            navController.navigate("workshopberhasil")
            viewModel.resetRegistrationSuccess()
        }
    }

    // Clear error when user interacts
    LaunchedEffect(fileSelected, isChecked) {
        if (errorMessage != null) {
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.btn_back),
                contentDescription = "Kembali",
                modifier = Modifier
                    .size(32.dp)
                    .clickable { navController.popBackStack() }
            )

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = "Daftar Workshop",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 25.sp,
                color = Color(0xFF1E1E1E)
            )
        }

        Spacer(modifier = Modifier.height(31.dp))

        Text(
            text = "Unggah Dokumen Daftar Karyawan",
            fontFamily = PoppinsFontFamily,
            color = Color(0xFF1E1E1E),
            fontWeight = FontWeight.Bold,
            fontSize = 21.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Lakukan unggah dokumen sheet berupa list daftar nama karyawan serta email mereka",
            fontSize = 14.sp,
            fontFamily = PoppinsFontFamily,
            color = Color(0xFF4B4B4B)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Upload Area
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(131.dp)
                .clip(RoundedCornerShape(14.dp))
                .border(1.dp, Color(0xFFB9B9B9), RoundedCornerShape(14.dp))
                .background(Color.White)
                .clickable {
                    viewModel.selectFile("daftar karyawan.xlsx")
                },
            contentAlignment = Alignment.Center
        ) {
            if (!fileSelected) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.lg_upload),
                        contentDescription = "Upload File",
                        modifier = Modifier.size(23.dp, 26.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Pilih file .xlsx, .xls atau .csv",
                        color = Color(0xFF989898),
                        fontFamily = PoppinsFontFamily,
                        fontSize = 12.sp
                    )
                }
            } else {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        painter = painterResource(id = R.drawable.lg_cancel),
                        contentDescription = "Batalkan File",
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp)
                            .size(16.dp)
                            .clickable {
                                viewModel.removeFile()
                            }
                    )

                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.lg_file),
                            contentDescription = "File Terunggah",
                            modifier = Modifier.size(22.dp, 27.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = fileName,
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Link Template
        val annotatedLinkString = buildAnnotatedString {
            append("*Anda dapat mengunduh template dokumen ")

            pushStringAnnotation(
                tag = "URL",
                annotation = "https://docs.google.com/spreadsheets/d/1v97vIrtmJJw5nC7gj7djjOb0oEPIr63rehe53bBlTOg/edit?usp=sharing"
            )
            withStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    textDecoration = TextDecoration.Underline,
                    color = Color(0xFF4B4B4B)
                )
            ) {
                append("di sini")
            }
            pop()
        }

        ClickableText(
            text = annotatedLinkString,
            onClick = { offset ->
                annotatedLinkString.getStringAnnotations("URL", offset, offset)
                    .firstOrNull()?.let { annotation ->
                        uriHandler.openUri(annotation.item)
                    }
            },
            style = LocalTextStyle.current.copy(
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                color = Color(0xFF4B4B4B)
            )
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { isChecked = it },
                modifier = Modifier.offset(x = (-12).dp)
            )
            Text(
                text = "Saya setuju mengikuti persyaratan pendaftaran Workshop",
                fontSize = 12.sp,
                color = Color.Black,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.offset(x = (-12).dp)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Show profile loading status
        if (!profileLoaded && errorMessage == null) {
            Text(
                text = "Memuat data profil...",
                color = Color.Gray,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        // Show error message
        errorMessage?.let { error ->
            Text(
                text = error,
                color = Color.Red,
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }

        Button(
            onClick = {
                Log.d("DaftarWorkshop", "Registration button clicked for workshop ID: $currentWorkshopId")
                viewModel.registerWorkshop()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (!isLoading && fileSelected && isChecked && profileLoaded)
                    Color(0xFF27361F) else Color.Gray
            ),
            enabled = !isLoading && fileSelected && isChecked && profileLoaded,
            shape = RoundedCornerShape(10.dp)
        ) {
            Text(
                text = if (isLoading) "Loading..." else "Daftarkan Sekarang",
                color = Color.White,
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}