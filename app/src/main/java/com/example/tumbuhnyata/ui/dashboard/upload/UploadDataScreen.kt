package com.example.tumbuhnyata.ui.dashboard.upload

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.util.Resource
import com.example.tumbuhnyata.ui.dashboard.upload.components.DocumentUploadItem
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.viewmodel.UploadViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDataScreen(
    navController: NavController,
    uploadViewModel: UploadViewModel = viewModel()
) {
    val uploadState by uploadViewModel.uploadState.collectAsState()
    val context = LocalContext.current

    // File picker launcher
    val filePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { selectedUri ->
            // Get file name from URI
            val fileName = getFileNameFromUri(context, selectedUri) ?: "data_file.csv"
            uploadViewModel.selectFile(selectedUri, fileName)
        }
    }

    // Handle upload result
    LaunchedEffect(uploadState.uploadResult) {
        when (val result = uploadState.uploadResult) {
            is Resource.Success -> {
                // Navigate to success screen
                navController.navigate("upload_success") {
                    popUpTo("upload_data") { inclusive = true }
                }
            }
            is Resource.Error -> {
                // Error will be shown in UI, no need to navigate
            }
            else -> {}
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
                onClick = { uploadViewModel.uploadFile() },
                enabled = uploadState.isUploadEnabled && !uploadState.isLoading,
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
                if (uploadState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Uploading...",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                } else {
                    Text(
                        text = "Unggah Data",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }
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
                text = "Upload Data File",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "* Upload file CSV berisi data emisi karbon (maksimal 10MB)",
                fontFamily = PoppinsFontFamily,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // CSV Format Guide
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF5F5F5)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Format CSV yang diperlukan:",
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    val csvColumns = listOf(
                        "id_perusahaan", "year", "month", "carbon_value",
                        "document_type", "document_name", "document_path", "analysis"
                    )
                    
                    csvColumns.forEach { column ->
                        Text(
                            text = "• $column",
                            fontFamily = PoppinsFontFamily,
                            fontSize = 12.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                        )
                    }
                }
            }

            // Single CSV file upload
            DocumentUploadItem(
                label = "Data File (.csv)",
                placeholderText = "Pilih file CSV data emisi",
                selectedFileName = uploadState.selectedFileName,
                onSelectClick = {
                    filePickerLauncher.launch("*/*")
                },
                onRemoveClick = {
                    uploadViewModel.clearSelectedFile()
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Error message
            uploadState.uploadResult?.let { result ->
                if (result is Resource.Error) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = result.message ?: "Upload failed",
                                color = Color(0xFFD32F2F),
                                fontFamily = PoppinsFontFamily,
                                style = MaterialTheme.typography.bodyMedium,
                                modifier = Modifier.weight(1f)
                            )
                            TextButton(
                                onClick = { uploadViewModel.clearUploadResult() }
                            ) {
                                Text(
                                    text = "Dismiss",
                                    color = Color(0xFFD32F2F),
                                    fontFamily = PoppinsFontFamily
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * Helper function to get file name from URI
 */
private fun getFileNameFromUri(context: android.content.Context, uri: Uri): String? {
    var fileName: String? = null
    
    if (uri.scheme == "content") {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    fileName = it.getString(nameIndex)
                }
            }
        }
    }
    
    if (fileName == null) {
        fileName = uri.path?.let {
            val cut = it.lastIndexOf('/')
            if (cut != -1) {
                it.substring(cut + 1)
            } else {
                it
            }
        }
    }
    
    return fileName
}

@Preview(showBackground = true)
@Composable
private fun UploadDataScreenPreview() {
    UploadDataScreen(navController = rememberNavController())
}
