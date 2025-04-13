package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun TopBarProfile(
    title: String,
    step: String,
    iconResId: Int,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = "Icon Back",
            modifier = Modifier
                .size(32.dp)
                .clickable { onBackClick() }
        )
        Spacer(modifier = Modifier.width(15.dp))
        Text(
            text = title,
            fontSize = 28.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.ExtraBold
        )
        Spacer(modifier = Modifier.width(100.dp))
        Text(
            text = step,
            fontSize = 13.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF686868)
        )
    }
}