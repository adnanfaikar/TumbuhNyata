package com.example.tumbuhnyata.ui.dashboard.upload

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle // Using CheckCircle for success
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R // Assuming R file location
import com.example.tumbuhnyata.ui.theme.PoppinsFontFamily // Assuming theme location

@Composable
fun UploadSuccessScreen(navController: NavController) {
    // Define gradient similar to KPIItem for consistency
    val gradientBrush = Brush.verticalGradient(
        colors = listOf(Color(0xFF5A7C47), Color(0xFF415A33)) // Example gradient
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradientBrush), // Apply gradient background
        contentAlignment = Alignment.Center
    )
    {
        Image(
            painter = painterResource(id = R.drawable.background_sc),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()) // Allow scrolling if content overflows
                .padding(horizontal = 32.dp, vertical = 16.dp), // Add padding
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center // Center content vertically
        ) {

            Spacer(modifier = Modifier.weight(1f)) // Push content towards center

            // Success Icon (using CheckCircle)
            Icon(
                imageVector = Icons.Filled.CheckCircle, // Placeholder for the pie chart graphic
                contentDescription = "Success",
                modifier = Modifier.size(100.dp), // Adjust size as needed
                tint = Color.White // Make icon white
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Main Title Text
            Text(
                text = "Pembaruan Data Anda Berhasil Diunggah",
                fontSize = 22.sp, // Adjust size
                fontFamily = PoppinsFontFamily, // Optional font
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold // Bold title
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Subtitle/Body Text
            Text(
                text = "Pembaruan data telah diterima dan sedang dalam proses verifikasi dan analisis oleh tim",
                fontSize = 14.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White.copy(alpha = 0.8f), // Slightly transparent white
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Estimation Text
            Text(
                text = "Estimasi waktu proses: 2-3 hari kerja",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp)) // More space before notification text

            // Notification Text
            Text(
                text = "Notifikasi Email: Perusahaan akan menerima email konfirmasi dan link untuk melacak status pembaruan",
                fontSize = 12.sp,
                fontFamily = PoppinsFontFamily,
                color = Color.White.copy(alpha = 0.8f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f)) // Push button towards bottom

            // Button to go back
            Button(
                // Navigate back to dashboard, clearing upload/success screens
                onClick = {
                    navController.navigate("dashboard") {
                        popUpTo("dashboard") { inclusive = true }
                        launchSingleTop = true // Avoid multiple dashboard instances
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(bottom = 16.dp), // Padding at the very bottom
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF151E11).copy(alpha = 0.8f) // Dark button color from example
                )
            ) {
                Text(
                    text = "Kembali ke Dashboard",
                    color = Color.White,
                    fontSize = 16.sp, // Adjust size
                    fontFamily = PoppinsFontFamily,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF415A33) // Set preview bg
@Composable
fun UploadSuccessScreenPreview() {
    UploadSuccessScreen(navController = rememberNavController())
}
