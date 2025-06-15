package com.example.tumbuhnyata.ui.riwayat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.data.model.dummyCsrList
import com.example.tumbuhnyata.ui.component.CsrCard
import com.example.tumbuhnyata.ui.component.ErrorSnackbar
import com.example.tumbuhnyata.ui.component.poppins
import com.example.tumbuhnyata.ui.components.SectionHeader
import com.example.tumbuhnyata.viewmodel.RiwayatViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatScreen(
    navController: NavController,
    riwayatViewModel: RiwayatViewModel = viewModel(),
    onCsrCardClick: (CsrHistoryItem) -> Unit,
    onLihatSemuaPerluTindakan: () -> Unit,
    onLihatSemuaDiterima: () -> Unit
) {
    val perluTindakanList by riwayatViewModel.perluTindakanItems.collectAsState()
    val diterimaList by riwayatViewModel.diterimaItems.collectAsState()
    val error by riwayatViewModel.error.collectAsState()
    val isLoading by riwayatViewModel.isLoading.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        snackbarHost = { 
            ErrorSnackbar(
                error = error,
                onDismiss = { riwayatViewModel.clearError() },
                onRetry = { riwayatViewModel.refresh() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF7F7F7))
                .padding(paddingValues)
        ) {
            // Header
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
                        .clickable { navController.popBackStack() }, // Navigasi ke Home
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

            // Search Bar
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                leadingIcon = {
                    IconButton(onClick = { /* TODO: Implement filter functionality */ }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_filter),
                            contentDescription = "Filter"
                        )
                    }
                },
                trailingIcon = {
                    IconButton(onClick = { /* TODO: Implement dropdown functionality */ }) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Dropdown")
                    }
                },
                placeholder = { Text("Cari Riwayat") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.White, RoundedCornerShape(8.dp)),
                shape = RoundedCornerShape(8.dp)
            )

            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Color(0xFF2C3E1F))
                }
            } else {
                // Lazy Column
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 16.dp)
                ) {
                    // Section "Perlu Tindakan"
                    item {
                        SectionHeader(
                            title = "Perlu Tindakan",
                            onLihatSemua = onLihatSemuaPerluTindakan
                        )
                    }
                    
                    if (perluTindakanList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tidak ada CSR yang perlu tindakan",
                                    color = Color.Gray,
                                    fontFamily = poppins
                                )
                            }
                        }
                    } else {
                        items(perluTindakanList.take(4)) { item ->
                            CsrCard(item = item) {
                                onCsrCardClick(item)
                            }
                        }
                    }

                    // Section "Diterima"
                    item {
                        SectionHeader(
                            title = "Diterima",
                            onLihatSemua = onLihatSemuaDiterima
                        )
                    }
                    
                    if (diterimaList.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tidak ada CSR yang diterima",
                                    color = Color.Gray,
                                    fontFamily = poppins
                                )
                            }
                        }
                    } else {
                        items(diterimaList.take(4)) { item ->
                            CsrCard(item = item) {
                                onCsrCardClick(item)
                            }
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun RiwayatScreenPreview() {
//    val navController = rememberNavController()
//    val dummyViewModel = remember {
//        RiwayatViewModel(dummyList = dummyCsrList)
//    }
//    RiwayatScreen(
//        navController = navController,
//        riwayatViewModel = dummyViewModel,
//        onCsrCardClick = { Log.d("Preview", "Card Clicked: ${it.programName}") },
//        onLihatSemuaPerluTindakan = { Log.d("Preview", "Lihat Semua Perlu Tindakan") },
//        onLihatSemuaDiterima = { Log.d("Preview", "Lihat Semua Diterima") }
//    )
//}