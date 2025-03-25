package com.example.tumbuhnyata.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.ui.onboarding.OnboardingScreen1
import com.example.tumbuhnyata.ui.onboarding.OnboardingScreen2
import com.example.tumbuhnyata.ui.onboarding.OnboardingScreen3
import com.example.tumbuhnyata.ui.option.OptionScreen
import com.example.tumbuhnyata.ui.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("onboarding") {
            OnboardingScreen1 (navController)
        }
        composable("onboarding2") {
            OnboardingScreen2 (navController)
        }
        composable("onboarding3") {
            OnboardingScreen3 (navController)
        }
        composable("option") {
            OptionScreen (navController)
        }
    }
}
