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
import com.example.tumbuhnyata.ui.component.CsrCard
import com.example.tumbuhnyata.ui.component.poppins
import com.example.tumbuhnyata.ui.component.SuccessDialog
import com.example.tumbuhnyata.viewmodel.DetailRiwayatViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelesaiDetailScreen(
    csrId: Int,
    viewModel: DetailRiwayatViewModel = viewModel(),
    onBack: () -> Unit
) {
    var showCertificateSuccessDialog by remember { mutableStateOf(false) }
    var showBlueprintSuccessDialog by remember { mutableStateOf(false) }
    var showReportSuccessDialog by remember { mutableStateOf(false) }
    
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
                    "Status Riwayat CSR",
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

                    // Status Section
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status : Program Selesai",
                            modifier = Modifier.padding(start = 4.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppins
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "✅",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = poppins
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Event Info
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_event_calendar2),
                                contentDescription = "Date",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "${csr.startDate} - ${csr.endDate}",
                                fontSize = 14.sp,
                                fontFamily = poppins
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_event_loc2),
                                contentDescription = "Location",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = csr.location,
                                fontSize = 14.sp,
                                fontFamily = poppins
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_event_office),
                                contentDescription = "Organization",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = csr.partnerName,
                                fontSize = 14.sp,
                                fontFamily = poppins
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Certificate Section
                    Text(
                        text = "Sertifikasi Kegiatan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppins
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showCertificateSuccessDialog = true },
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
                                "Download Sertifikat",
                                fontFamily = poppins,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // Blueprint Section
                    Text(
                        text = "Blueprint dan Laporan Kegiatan",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = poppins
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "id${csr.id}. ${csr.partnerName} - ${csr.programName} - LANGKAH NYATA.pdf",
                        fontSize = 12.sp,
                        fontFamily = poppins,
                        color = Color.Gray
                    )
                    Text(
                        text = "189 halaman - 14.3 MB - PDF",
                        fontSize = 12.sp,
                        fontFamily = poppins,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

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
                                "Download Blueprint",
                                fontFamily = poppins,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "id${csr.id}. Report ${csr.partnerName} - ${csr.programName} - LANGKAH NYATA.pdf",
                        fontSize = 12.sp,
                        fontFamily = poppins,
                        color = Color.Gray
                    )
                    Text(
                        text = "189 halaman - 14.3 MB - PDF",
                        fontSize = 12.sp,
                        fontFamily = poppins,
                        color = Color.Gray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { showReportSuccessDialog = true },
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
                                "Download Laporan Kegiatan",
                                fontFamily = poppins,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }

        // Success Dialogs
        if (showCertificateSuccessDialog) {
            SuccessDialog(
                message = "Berhasil mengunduh sertifikat",
                onDismiss = { showCertificateSuccessDialog = false }
            )
        }

        if (showBlueprintSuccessDialog) {
            SuccessDialog(
                message = "Berhasil mengunduh blueprint",
                onDismiss = { showBlueprintSuccessDialog = false }
            )
        }

        if (showReportSuccessDialog) {
            SuccessDialog(
                message = "Berhasil mengunduh laporan kegiatan",
                onDismiss = { showReportSuccessDialog = false }
            )
        }
    }
}