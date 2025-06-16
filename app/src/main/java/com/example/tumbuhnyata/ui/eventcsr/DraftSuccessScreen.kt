package com.example.tumbuhnyata.ui.eventcsr

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily

@Composable
fun DraftSuccessScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Draft Berhasil Disimpan!",
            fontSize = 24.sp,
            fontFamily = PoppinsFontFamily,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF27361F)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Anda dapat menemukan draft Anda di daftar draft.",
            fontSize = 16.sp,
            fontFamily = PoppinsFontFamily,
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(0.8f)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { 
                navController.popBackStack()
                navController.navigate("draft_list")
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27361F))
        ) {
            Text(
                text = "Lihat Draft",
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { navController.popBackStack("home", inclusive = false) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3A5A40))
        ) {
            Text(
                text = "Kembali ke Home",
                fontSize = 17.sp,
                fontFamily = PoppinsFontFamily,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDraftSuccessScreen() {
    val navController = rememberNavController()
    DraftSuccessScreen(navController = navController)
} 