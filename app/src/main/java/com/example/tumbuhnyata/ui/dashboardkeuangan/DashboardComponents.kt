package com.example.tumbuhnyata.ui.dashboardkeuangan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun InfoBox(title: String, value: String, amount: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(text = title, fontSize = 14.sp)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = amount, fontSize = 12.sp)
    }
}

@Composable
fun StatusBox(label: String, amount: String, bgColor: Color) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(bgColor.copy(alpha = 0.1f), shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(text = label, fontSize = 14.sp)
        Text(text = amount, fontSize = 18.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun PopupDownloadSuccess(onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.5f)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .background(Color.White, shape = RoundedCornerShape(16.dp))
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Berhasil Mengunduh\nLaporan Keuangan", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { onDismiss() }) {
                Text("Ok")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewInfoBox() {
    InfoBox(
        title = "Total CSR",
        value = "Rp 3.187.450.725",
        amount = "2 Program CSR"
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewStatusBox() {
    StatusBox(
        label = "Program Selesai",
        amount = "Rp 1.231.779.900",
        bgColor = Color(0xFF2196F3)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewPopupDownloadSuccess() {
    PopupDownloadSuccess(onDismiss = {})
}

