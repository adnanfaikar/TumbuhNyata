package com.example.tumbuhnyata.ui.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.ui.component.*
import com.example.tumbuhnyata.viewmodel.DetailRiwayatViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.Timeline
import com.example.tumbuhnyata.ui.riwayat.TimelineStep
import com.example.tumbuhnyata.ui.riwayat.VerticalTimeline

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailRiwayatScreen(
    csrId: Int,
    viewModel: DetailRiwayatViewModel = viewModel(),
    onBack: () -> Unit,
    onNavigateToInvoice: () -> Unit,
    onNavigateToUploadRevisi: () -> Unit
) {
    var showBlueprintSuccessDialog by remember { mutableStateOf(false) }
    val csrDetail by viewModel.csrDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(csrId) {
        viewModel.loadCsrDetail(csrId)
    }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
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
                    "Detail Riwayat CSR",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins
                )
            }
        }
    ) { paddingValues ->
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(error ?: "Unknown error occurred")
            }
        } else {
            csrDetail?.let { csr ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                        .background(Color(0xFFF7F7F7))
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    // CSR Card with elevation
                    CsrCard(item = csr)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Timeline Section
                    val timelineSteps = when (csr.status) {
                        "Menunggu Pembayaran" -> listOf(
                            TimelineStep("Pengajuan Dikirim", "10/05/2024 - 09:41 WIB", isCompleted = true),
                            TimelineStep("Review & Evaluasi", "10/05/2024 - 09:50 WIB", isCompleted = true),
                            TimelineStep("Pembayaran", "10/05/2024 - 10:00 WIB", isInProgress = true),
                            TimelineStep("Implementasi Program")
                        )
                        else -> listOf(
                            TimelineStep("Pengajuan Dikirim", "10/05/2024 - 09:41 WIB", isCompleted = true),
                            TimelineStep(
                                title = when (csr.status) {
                                    "Memerlukan Revisi" -> "Review & Evaluasi - Revisi Diperlukan"
                                    else -> "Review & Evaluasi"
                                },
                                date = "10/05/2024 - 09:50 WIB",
                                isInProgress = true
                            ),
                            TimelineStep("Pembayaran", "10/05/2024 - 10:00 WIB"),
                            TimelineStep("Implementasi Program")
                        )
                    }

                    VerticalTimeline(steps = timelineSteps)

                    Spacer(modifier = Modifier.height(46.dp))




                    // Download Blueprint Button
                    Button(
                        onClick = { showBlueprintSuccessDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2C3E1F)
                        )
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_download),
                                contentDescription = "Download",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Download Proposal Rancangan",
                                fontFamily = poppins,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Additional Buttons based on status
                    if (csr.status == "Memerlukan Revisi") {
                        Button(
                            onClick = onNavigateToUploadRevisi,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2C3E1F)
                            )
                        ) {
                            Text(
                                "Upload Revisi",
                                fontFamily = poppins,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    } else if (csr.status == "Menunggu Pembayaran") {
                        Button(
                            onClick = onNavigateToInvoice,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF2C3E1F)
                            )
                        ) {
                            Text(
                                "Lihat Invoice",
                                fontFamily = poppins,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Success Dialog
        if (showBlueprintSuccessDialog) {
            SuccessDialog(
                message = "Berhasil mengunduh blueprint",
                onDismiss = { showBlueprintSuccessDialog = false }
            )
        }
    }
}