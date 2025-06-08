package com.example.tumbuhnyata.ui.riwayat

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.component.poppins

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadRevisiScreen(
    navController: NavController,
    onBack: () -> Unit,
    onUpload: (String) -> Unit
) {
    var fileName by remember { mutableStateOf("") }
    var isUploading by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 16.dp, top = 30.dp, bottom = 16.dp, end = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF2C3E1F))
                        .clickable { onBack() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    "Upload Revisi",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(start = 16.dp, end = 16.dp, top = 0.dp, bottom = 80.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = "Unggah Dokumen Rancangan",
                    fontSize = 21.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Unggah dokumen rancangan yang sudah direvisi untuk melanjutkan pengajuan CSR perusahaan anda",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    fontFamily = poppins
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Revisi Proposal Rancangan",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = poppins,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = fileName.isEmpty()) {
                            fileName = "Proposal Rancangan.pdf"
                        }
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = if (fileName.isEmpty()) R.drawable.ic_upload else R.drawable.ic_doc),
                            contentDescription = "Upload",
                            modifier = Modifier.size(20.dp),
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (fileName.isEmpty()) "Proposal Rancangan" else fileName,
                            color = if (fileName.isEmpty()) Color.Gray else Color.Black,
                            fontFamily = poppins,
                            maxLines = 1
                        )
                        if (fileName.isNotEmpty()) {
                            Spacer(modifier = Modifier.weight(1f))
                            IconButton(
                                onClick = { fileName = "" },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_close),
                                    contentDescription = "Hapus File",
                                    tint = Color(0xFFE74C3C),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
            // Tombol Upload Revisi sticky di bawah
            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        isUploading = true
                        onUpload(fileName)
                        navController.navigate("revisi_success")
                    },
                    enabled = fileName.isNotEmpty() && !isUploading,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2C3E1F)
                    )
                ) {
                    Text(
                        "Upload Revisi",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold,
                        fontFamily = poppins
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewUploadRevisiScreen() {
    val navController = rememberNavController()
    UploadRevisiScreen(
        navController = navController,
        onBack = {},
        onUpload = {}
    )
}
