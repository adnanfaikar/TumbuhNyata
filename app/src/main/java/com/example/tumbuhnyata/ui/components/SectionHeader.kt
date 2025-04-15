package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun SectionHeader(title: String, onLihatSemua: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            fontFamily = PoppinsFontFamily
        )
        TextButton(onClick = onLihatSemua) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text("Lihat Semua", fontFamily = PoppinsFontFamily, color = Color(0xFF525E4C) )
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowRight,
                    contentDescription = "Lihat Semua",
                    modifier = Modifier.size(16.dp) ,
                    tint = Color(0xFF525E4C)
                )
            }
        }
    }
}
@androidx.compose.ui.tooling.preview.Preview
@Composable
fun PreviewSectionHeader() {
    SectionHeader(title = "Judul Bagian", onLihatSemua = { /*TODO*/ })
}