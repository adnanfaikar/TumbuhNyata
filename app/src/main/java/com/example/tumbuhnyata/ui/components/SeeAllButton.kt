package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun SeeAllButton(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.End
    ) {
        TextButton(onClick = onClick) {
            Text("Lihat Semua", fontFamily = PoppinsFontFamily)
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun PreviewSeeAllButton() {
    SeeAllButton(onClick = { /*TODO*/ })
}