package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.tumbuhnyata.R

@Composable
fun LocationSelector(onLocationSelected: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF5F5F5), shape = RoundedCornerShape(10.dp))
            .border(1.dp, Color(0xFF4B4B4B), shape = RoundedCornerShape(10.dp))
    ) {
        LocationOption(iconId = R.drawable.lg_alamat, text = "Cari Alamat", iconSize = Pair(15.85.dp, 18.dp), onClick = onLocationSelected)
        Divider(color = Color(0xFF4B4B4B))
        LocationOption(iconId = R.drawable.lg_terkini, text = "Lokasi Anda Saat Ini", iconSize = Pair(20.dp, 20.dp), onClick = onLocationSelected)
        Divider(color = Color(0xFF4B4B4B))
        LocationOption(iconId = R.drawable.lg_peta, text = "Pilih Lewat Peta", iconSize = Pair(20.dp, 20.dp), onClick = onLocationSelected)
    }
}