package com.example.tumbuhnyata.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    var isVisible by remember { mutableStateOf(false) }

    // Animasi scale in dari kecil ke besar
    val scale by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0.5f,
        animationSpec = tween(durationMillis = 1200, easing = EaseOutCubic)
    )

    // Animasi fade in bersamaan dengan scale
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = EaseInOutQuad)
    )

    // Menjalankan animasi saat pertama kali muncul
    LaunchedEffect(Unit) {
        isVisible = true
        delay(3000)
        navController.navigate("home")
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background
        Image(
            painter = painterResource(id = R.drawable.background_sc),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Logo dengan animasi dari dalam keluar
        Image(
            painter = painterResource(id = R.drawable.logo_splash),
            contentDescription = "Logo",
            modifier = Modifier
                .size(254.7184.dp, 79.dp)
                .scale(scale) // Efek dari dalam keluar
                .alpha(alpha), // Efek fade in
            contentScale = ContentScale.Fit
        )
    }
}

@Preview
@Composable
fun PreviewSplashScreen() {
    SplashScreen(navController = rememberNavController())
}