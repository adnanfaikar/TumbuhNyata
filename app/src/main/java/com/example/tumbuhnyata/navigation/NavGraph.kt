package com.example.tumbuhnyata.navigation

import com.example.tumbuhnyata.ui.screens.RegisterScreen2
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.ui.screens.LoginScreen
import com.example.tumbuhnyata.ui.screens.OnboardingScreen1
import com.example.tumbuhnyata.ui.screens.OnboardingScreen2
import com.example.tumbuhnyata.ui.screens.OnboardingScreen3
import com.example.tumbuhnyata.ui.screens.OptionScreen
import com.example.tumbuhnyata.ui.screens.OtpScreen
import com.example.tumbuhnyata.ui.screens.VerifikasiScreen
import com.example.tumbuhnyata.ui.screens.AkunBerhasil
import com.example.tumbuhnyata.ui.screens.DaftarWorkshop
import com.example.tumbuhnyata.ui.screens.DeskripsiWorkshopScreen
import com.example.tumbuhnyata.ui.screens.NewWorkshop
import com.example.tumbuhnyata.ui.screens.RegisterScreen1
import com.example.tumbuhnyata.ui.screens.RegisterScreen3
import com.example.tumbuhnyata.ui.screens.RekomWorkshop
import com.example.tumbuhnyata.ui.screens.SplashScreen
import com.example.tumbuhnyata.ui.screens.WorkshopBerhasil
import com.example.tumbuhnyata.ui.screens.WorkshopScreen

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
        composable("register") {
            RegisterScreen1 (navController)
        }
        composable("register2") {
            RegisterScreen2 (navController)
        }
        composable("register3") {
            RegisterScreen3 (navController)
        }
        composable("login") {
            LoginScreen (navController)
        }
        composable("verifikasi") {
            VerifikasiScreen (navController)
        }
        composable("otp") {
            OtpScreen (navController)
        }
        composable("akunberhasil") {
            AkunBerhasil (navController)
        }
        composable("workshop") {
            WorkshopScreen (navController)
        }
        composable("rekomendasiworkshop") {
            RekomWorkshop (navController)
        }
        composable("workshopterbaru") {
            NewWorkshop (navController)
        }
        composable("deskripsiworkshop/{workshopId}") { backStackEntry ->
            val workshopId = backStackEntry.arguments?.getString("workshopId") ?: ""
            DeskripsiWorkshopScreen(navController, workshopId)
        }

        composable("daftarworkshop") {
            DaftarWorkshop (navController)
        }
        composable("workshopberhasil") {
            WorkshopBerhasil (navController)
        }
    }
}

