package com.example.tumbuhnyata.ui.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.data.model.dummyCsrList
import com.example.tumbuhnyata.ui.component.CsrCard
import com.example.tumbuhnyata.ui.component.poppins
import com.example.tumbuhnyata.viewmodel.RiwayatViewModel

@Composable
fun DiterimaScreen(
    riwayatViewModel: RiwayatViewModel = viewModel(),
    onBack: () -> Unit,
    onCsrCardClick: (CsrHistoryItem) -> Unit
) {
    val diterimaList by riwayatViewModel.diterimaItems.collectAsState()

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
                .padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2C3E1F)) // hijau tua
                    .clickable(onClick = onBack),
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
                "Diterima",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = poppins
            )
        }

        // List Riwayat History
        if (diterimaList.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Belum ada CSR yang diterima",
                        color = Color.Gray,
                        fontFamily = poppins,
                        fontSize = 16.sp
                    )
                }
            }
        } else {
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
}

@Preview(showBackground = true)
@Composable
fun DiterimaScreenPreview() {
    DiterimaScreen(
        riwayatViewModel = viewModel(),
        onBack = {},
        onCsrCardClick = {}
    )
}