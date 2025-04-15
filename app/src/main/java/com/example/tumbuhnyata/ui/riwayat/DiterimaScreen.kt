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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.model.CsrItem
import com.example.tumbuhnyata.data.model.dummyCsrList
import com.example.tumbuhnyata.ui.component.CsrCard
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun DiterimaScreen(
    navController: NavController,
    riwayatViewModel: RiwayatViewModel = viewModel(),
    onCsrCardClick: (CsrItem) -> Unit = {}
) {
    val diterimaList by riwayatViewModel.diterimaItems.collectAsState()
    var searchText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
    ) {
        // Header
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
                    .clickable { navController.popBackStack() },
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
                "CSR Diterima",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = PoppinsFontFamily
            )
        }

        // Search Bar
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            leadingIcon = {
                IconButton(onClick = { /* TODO: Implement filter functionality */ }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Filter"
                    )
                }
            },
            placeholder = { Text("Cari CSR Diterima") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .background(Color.White, RoundedCornerShape(8.dp)),
            shape = RoundedCornerShape(8.dp)
        )

        // List of Diterima items
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp)
        ) {
            items(diterimaList) { item ->
                CsrCard(item = item) {
                    onCsrCardClick(item)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DiterimaScreenPreview() {
    val navController = rememberNavController()
    val dummyViewModel = remember {
        RiwayatViewModel(dummyList = dummyCsrList)
    }
    DiterimaScreen(
        navController = navController,
        riwayatViewModel = dummyViewModel
    )
} 