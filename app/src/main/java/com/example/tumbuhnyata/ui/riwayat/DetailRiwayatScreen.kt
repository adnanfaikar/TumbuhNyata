package com.example.tumbuhnyata.ui.detail

import androidx.compose.foundation.Image
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.data.model.CsrItem
import com.example.tumbuhnyata.data.model.SubStatus
import com.example.tumbuhnyata.data.model.dummyCsrList
import com.example.tumbuhnyata.data.model.getSubStatusEmoji
import com.example.tumbuhnyata.ui.component.poppins
import com.example.tumbuhnyata.ui.riwayat.TimelineStep
import com.example.tumbuhnyata.ui.riwayat.VerticalTimeline
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.component.CsrCard
import com.example.tumbuhnyata.ui.component.SuccessDialog
import com.example.tumbuhnyata.ui.riwayat.MendatangDetailScreen
import com.example.tumbuhnyata.ui.riwayat.ProgressDetailScreen
import com.example.tumbuhnyata.ui.riwayat.SelesaiDetailScreen

@Composable
fun CsrDetailScreen(
    csr: CsrItem,
    onBack: () -> Unit,
    onNavigateToInvoice: () -> Unit
) {
    when (csr.subStatus) {
        SubStatus.MENDATANG -> MendatangDetailScreen(csr = csr, onBack = onBack)
        SubStatus.PROGRESS -> ProgressDetailScreen(csr = csr, onBack = onBack)
        SubStatus.SELESAI -> SelesaiDetailScreen(csr = csr, onBack = onBack)
        else -> ReviewDetailScreen(csr = csr, onBack = onBack, onNavigateToInvoice = onNavigateToInvoice)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReviewDetailScreen(
    csr: CsrItem,
    onBack: () -> Unit,
    onNavigateToInvoice: () -> Unit
) {
    var showProposalSuccessDialog by remember { mutableStateOf(false) }
    var showRevisionSuccessDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
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
                    "Status Riwayat CSR",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = poppins
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // CSR Card with elevation
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                CsrCard(item = csr)
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Download Proposal Button
            Button(
                onClick = { showProposalSuccessDialog = true },
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

            // Additional button for MEMERLUKAN_REVISI status
            if (csr.subStatus == SubStatus.MEMERLUKAN_REVISI) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = { showRevisionSuccessDialog = true },
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
                            "Download Panduan Revisi",
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Additional button for MENUNGGU_PEMBAYARAN status
            if (csr.subStatus == SubStatus.MENUNGGU_PEMBAYARAN) {
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onNavigateToInvoice,
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
                            painter = painterResource(id = R.drawable.ic_invoice),
                            contentDescription = "Invoice",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Detail Invoice",
                            fontFamily = poppins,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Timeline
            val timelineSteps = when (csr.subStatus) {
                SubStatus.MENUNGGU_PEMBAYARAN -> listOf(
                    TimelineStep("Pengajuan Dikirim", "10/05/2024 - 09:41 WIB", isCompleted = true),
                    TimelineStep("Review & Evaluasi", "10/05/2024 - 09:50 WIB", isCompleted = true),
                    TimelineStep("Pembayaran", "10/05/2024 - 10:00 WIB", isInProgress = true),
                    TimelineStep("Implementasi Program")
                )
                else -> listOf(
                    TimelineStep("Pengajuan Dikirim", "10/05/2024 - 09:41 WIB", isCompleted = true),
                    TimelineStep(
                        title = when (csr.subStatus) {
                            SubStatus.MEMERLUKAN_REVISI -> "Review & Evaluasi - Revisi Diperlukan"
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
        }

        // Success Dialogs
        if (showProposalSuccessDialog) {
            SuccessDialog(
                message = "Berhasil mengunduh proposal rancangan",
                onDismiss = { showProposalSuccessDialog = false }
            )
        }

        if (showRevisionSuccessDialog) {
            SuccessDialog(
                message = "Berhasil mengunduh panduan revisi",
                onDismiss = { showRevisionSuccessDialog = false }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCsrDetailScreen() {
    CsrDetailScreen(csr = dummyCsrList[0], onBack = {}, onNavigateToInvoice = {})
}