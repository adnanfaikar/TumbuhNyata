package com.example.tumbuhnyata.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun BottomNavBarProfile(selectedItem: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf(
        NavigationItem("Beranda", R.drawable.ic_navbar_home),
        NavigationItem("Sertifikasi", R.drawable.ic_navbar_certification),
        NavigationItem("Workshop", R.drawable.ic_navbar_workshop),
        NavigationItem("Profil", R.drawable.ic_navbar_profile)
    )

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .padding(top = 2.dp),
        color = Color.White,
        shadowElevation = 20.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onItemSelected(index) }
                        .padding(vertical = 20.dp)
                        .width(98.dp)
                ) {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        tint = if (index == selectedItem) Color(0xFF27361F) else Color(0xFF888888),
                        modifier = Modifier.size(30.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = item.label,
                        fontSize = 14.sp,
                        fontFamily = PoppinsFontFamily,
                        letterSpacing = 0.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 10.sp,
                        fontWeight = if (index == selectedItem) FontWeight.Medium else FontWeight.Normal,
                        color = if (index == selectedItem) Color(0xFF27361F) else Color(0xFF888888)
                    )
                }
            }
        }
    }
}

data class NavigationItem(val label: String, val icon: Int)