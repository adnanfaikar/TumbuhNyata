package com.example.tumbuhnyata.ui.dashboard.upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.dashboard.upload.components.DocumentUploadItem
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily


data class UploadConfig(
    val id: String,
    val label: String,
    val placeholder: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDataScreen(navController: NavController) {


    val uploadItemsConfig = remember {
        listOf(
            UploadConfig(id = "laporan_csr", label = "Laporan CSR", placeholder = "Laporan"),
            UploadConfig(id = "sertifikasi_csr", label = "Sertifikasi CSR", placeholder = "Sertifikasi"),
            UploadConfig(id = "data_emisi", label = "Data Penggunaan Emisi", placeholder = "Data Emisi"),
            UploadConfig(id = "dokumen_lain", label = "Dokumen Pendukung Lain", placeholder = "Dokumen Pendukung")
        )
    }

    val selectedFiles = remember { mutableStateMapOf<String, String?>() }

    val isUploadEnabled by remember {
        derivedStateOf {
            uploadItemsConfig.all { config -> selectedFiles[config.id] != null }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Dashboard",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color = Color.Black
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() },
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF27361F)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.arrow_left),
                                contentDescription = "Back",
                                modifier = Modifier.size(16.dp),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            Button(
                onClick = { navController.navigate("upload_success") },
                enabled = isUploadEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF27361F),
                    contentColor = Color(0xFFFAFAFA),
                    disabledContainerColor = Color(0xFF989898),
                    disabledContentColor = Color(0xFFFAFAFA)
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Unggah Data",
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                    )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) 
        ) {

            Text(
                text = "Upload File Pendukung",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "* Pastikan semua dokumen yang anda unggah merupakan dokumen yang belum pernah tercatat pada Tumbuh Nyata agar tidak terjadi tumpang tindih data",
                fontFamily = PoppinsFontFamily,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            uploadItemsConfig.forEach { config ->
                DocumentUploadItem(
                    label = config.label,
                    placeholderText = config.placeholder,
                    selectedFileName = selectedFiles[config.id],
                    onSelectClick = {
                        selectedFiles[config.id] = "${config.label.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
                    },
                    onRemoveClick = {
                        selectedFiles[config.id] = null
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UploadDataScreenPreview() {
    UploadDataScreen(navController = rememberNavController())
}
