package com.example.tumbuhnyata.ui.eventcsr

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import androidx.lifecycle.viewmodel.compose.viewModel

@Preview
@Composable
fun CsrVerificationScreen(navController: NavController, csrData: CsrData) {
    val viewModel: CsrVerificationViewModel = viewModel()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Back button with circle background
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF27361F))
                        .clickable { navController.navigateUp() }
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_back),
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }

                Text(
                    text = "Ajukan CSR",
                    fontSize = 20.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Verifikasi",
                    fontSize = 14.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = "Ringkasan CSR",
                fontSize = 16.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Summary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(15.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    // Category
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = "Kategori : ",
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            color = Color.Gray
                        )
                        Text(
                            text = csrData.category,
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Program Name
                    Text(
                        text = csrData.programName,
                        fontSize = 20.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Date
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_event_calendar2),
                            contentDescription = "Date",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = "${csrData.startDate} - ${csrData.endDate}",
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    // Location
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_event_loc2),
                            contentDescription = "Location",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = csrData.location,
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    // Partner
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_event_office),
                            contentDescription = "Partner",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = csrData.partnerName,
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Besar Anggaran",
                        fontSize = 16.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    // Budget
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_event_money1),
                            contentDescription = "Budget",
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            text = csrData.budget,
                            fontSize = 16.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    }
                }
            }

            Text(
                text = "*Pastikan semua data yang anda masukkan benar dan siap untuk diverifikasi",
                fontSize = 12.sp,
                color = Color.Gray,
                fontFamily = PoppinsFontFamily,
                modifier = Modifier.padding(top = 16.dp)
            )

            Spacer(modifier = Modifier.weight(1f))

            // Action Buttons
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                if (viewModel.isSubmitting.value) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 16.dp),
                        color = Color(0xFF27361F)
                    )
                }

                Button(
                    onClick = {
                        viewModel.submitCsr(csrData) {
                            navController.navigate("csr_success")
                        }
                    },
                    enabled = !viewModel.isSubmitting.value,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF27361F),
                        disabledContainerColor = Color(0xFF27361F).copy(alpha = 0.5f)
                    )
                ) {
                    Text(
                        text = "Ajukan Sekarang",
                        fontSize = 17.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (viewModel.errorMessage.value != null) {
                    Text(
                        text = viewModel.errorMessage.value ?: "",
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = { navController.navigate("csr_submission") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFE74C3C)
                    )
                ) {
                    Text(
                        text = "Batalkan",
                        fontSize = 17.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }

}
