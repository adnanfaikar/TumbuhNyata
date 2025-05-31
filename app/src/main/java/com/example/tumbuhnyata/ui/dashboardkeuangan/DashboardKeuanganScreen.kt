package com.example.tumbuhnyata.ui.dashboardkeuangan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily
import com.example.tumbuhnyata.viewmodel.DashboardKeuanganViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun DashboardKeuanganScreen(
    navController: NavController = rememberNavController(),
    viewModel: DashboardKeuanganViewModel = DashboardKeuanganViewModel()
) {
    val categories = viewModel.categories
    val total = viewModel.total
    var showPopup by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color.White)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Header with back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.btn_back),
                    contentDescription = "Kembali",
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { navController.navigateUp() }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Dashboard Keuangan",
                    fontSize = 25.sp,
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }
            
            Text(
                text = buildAnnotatedString {
                    append("Semua laporan keuangan pada dashboard ini merupakan pengeluaran program CSR anda pada Tumbuh Nyata ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("(Anggaran + Biaya Jasa)")
                    }
                },
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp, bottom = 16.dp)
            )

            // Download Button
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(195.dp)
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF27361F)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "Unduh Laporan Keuangan",
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "CSR Anda Sekarang",
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Pantau dan kelola lebih mudah",
                            fontSize = 12.sp,
                            fontFamily = PoppinsFontFamily,
                            color = Color.White,
                        )
                        Button(
                            onClick = { showPopup = true },
                            modifier = Modifier
                                .padding(top = 8.dp)
                                .height(32.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White
                            )
                        ) {
                            Text(
                                text = "Download",
                                fontSize = 12.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF27361F)
                            )
                        }
                    }
                    
                    Image(
                        painter = painterResource(id = R.drawable.ic_dbkeuangan_folder),
                        contentDescription = "Folder",
                        modifier = Modifier.width(87.dp).height(81.dp)
                    )
                }
            }

            // Total Section
            Text(
                text = "Total",
                fontSize = 25.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.ExtraBold ,
                color = Color(0xFF4B4B4B)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF27361F)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Rp",
                        fontSize = 21.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                    Text(
                        text = String.format("%,.0f", total),
                        fontSize = 21.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFFEBFD6F)
                    )
                }
            }

            // Category Cards with border
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                categories.forEach { category ->
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFB9B9B9))
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = category.name,
                                    fontSize = 17.sp,
                                    fontFamily = PoppinsFontFamily,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF686868)
                                )
                                Icon(
                                    painter = painterResource(
                                        id = if (category.name == "Sosial") R.drawable.ic_social
                                        else R.drawable.ic_environment
                                    ),
                                    contentDescription = category.name,
                                    modifier = Modifier.size(16.dp),
                                    tint = category.color
                                )
                            }
                            Text(
                                text = if (category.name == "Sosial") "12" else "9",
                                fontSize = 36.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Rp ${String.format("%,.0f", category.amount)}",
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Pie Chart Card with border
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, Color(0xFFB9B9B9))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    DynamicPieChart(categories = categories)
                }
            }

            // Status Boxes with borders
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Selesai Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFB9B9B9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFF8F8F8),
                                        Color(0xFF989898)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Selesai ✅",
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                color = Color.Black
                            )
                            Text(
                                text = "Rp 886.240.100",
                                fontSize = 18.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }

                // Progress Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFB9B9B9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFF8F8F8),
                                        Color(0xFFFFD95D)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Progress ⏳",
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                color = Color.Black
                            )
                            Text(
                                text = "Rp 1.732.890.150",
                                fontSize = 18.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }

                // Mendatang Box
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, Color(0xFFB9B9B9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(
                                        Color(0xFFF8F8F8),
                                        Color(0xFF9CDEFF)
                                    )
                                )
                            )
                            .padding(16.dp)
                    ) {
                        Column {
                            Text(
                                text = "Mendatang ⏱️",
                                fontSize = 14.sp,
                                fontFamily = PoppinsFontFamily,
                                color = Color.Black
                            )
                            Text(
                                text = "Rp 568.320.475",
                                fontSize = 18.sp,
                                fontFamily = PoppinsFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // Add some bottom padding
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

    if (showPopup) {
        Dialog(
            onDismissRequest = { showPopup = false }
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Download",
                        fontSize = 12.sp,
                        fontFamily = PoppinsFontFamily,
                        color = Color(0xFF27361F)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Image(
                        painter = painterResource(id = R.drawable.ic_db_download_success),
                        contentDescription = "Success",
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Berhasil Mengunduh",
                        fontSize = 16.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "Laporan Keuangan",
                        fontSize = 16.sp,
                        fontFamily = PoppinsFontFamily,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    Button(
                        onClick = { showPopup = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF27361F)
                        )
                    ) {
                        Text(
                            text = "Ok",
                            fontSize = 14.sp,
                            fontFamily = PoppinsFontFamily,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Preview(
    name = "Dashboard Keuangan Screen",
    showBackground = true,
    backgroundColor = 0xFFFFFFFF,
    showSystemUi = true
)
@Composable
fun PreviewDashboardKeuanganScreen() {
    MaterialTheme {
        DashboardKeuanganScreen()
    }
}
