package com.example.tumbuhnyata.ui.dashboard.upload // Adjust package if needed

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import com.example.tumbuhnyata.ui.dashboard.upload.components.DocumentUploadItem // Import reusable item
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily // Assuming you have this font defined

// Data class to represent an upload item configuration
data class UploadConfig(
    val id: String, // Unique ID for state management
    val label: String,
    val placeholder: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDataScreen(navController: NavController) {

    // Define the list of documents to upload
    val uploadItemsConfig = remember {
        listOf(
            UploadConfig(id = "laporan_csr", label = "Laporan CSR", placeholder = "Laporan"),
            UploadConfig(id = "sertifikasi_csr", label = "Sertifikasi CSR", placeholder = "Sertifikasi"),
            UploadConfig(id = "data_emisi", label = "Data Penggunaan Emisi", placeholder = "Data Emisi"),
            UploadConfig(id = "dokumen_lain", label = "Dokumen Pendukung Lain", placeholder = "Dokumen Pendukung")
        )
    }

    // State to hold the selected file names, using a map for scalability
    val selectedFiles = remember { mutableStateMapOf<String, String?>() }

    // Derived state to check if all items have a selected file
    val isUploadEnabled by remember {
        derivedStateOf {
            uploadItemsConfig.all { config -> selectedFiles[config.id] != null }
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            // Top App Bar
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
                    // Make TopAppBar background transparent to show Scaffold surface color
                    containerColor = Color.Transparent,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.White
                )
            )
        },
        bottomBar = {
            // Button placed in bottomBar for fixed position
            Button(
                onClick = { navController.navigate("upload_success") }, // Navigate on click
                enabled = isUploadEnabled, // Enable based on state
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp) // Padding around button
                    .height(48.dp),
                colors = ButtonDefaults.buttonColors(
                    // Green color when enabled, default disabled color otherwise
                    containerColor = if (isUploadEnabled) Color(0xFF4CAF50) else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isUploadEnabled) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), // Custom disabled look
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Text("Unggah Data")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues) // Apply padding from Scaffold (excluding bottomBar)
                .padding(horizontal = 16.dp) // Horizontal padding for content
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Make content scrollable
        ) {

            Text(
                text = "Upload File Pendukung",
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 25.sp,
                color = Color.Black,
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Instruction Text
            Text(
                text = "* Pastikan semua dokumen yang anda unggah merupakan dokumen yang belum pernah tercatat pada Tumbuh Nyata agar tidak terjadi tumpang tindih data",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant, // Slightly dimmer text
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Generate Upload Items dynamically
            uploadItemsConfig.forEach { config ->
                DocumentUploadItem(
                    label = config.label,
                    placeholderText = config.placeholder,
                    selectedFileName = selectedFiles[config.id],
                    onSelectClick = {
                        // Simulate file selection - replace with actual file picker logic
                        selectedFiles[config.id] = "${config.label.replace(" ", "_")}_${System.currentTimeMillis()}.pdf"
                    },
                    onRemoveClick = {
                        selectedFiles[config.id] = null
                    },
                    modifier = Modifier.padding(bottom = 16.dp) // Space between upload items
                )
            }

            Spacer(modifier = Modifier.height(16.dp)) // Add space at the bottom before button area
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun UploadDataScreenPreview() {
    // Wrap in a theme if needed, e.g., YourAppTheme { ... }
    UploadDataScreen(navController = rememberNavController())
}
