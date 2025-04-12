package com.example.tumbuhnyata.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.ui.login.LoginScreen
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen1
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen2
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen3
import com.example.tumbuhnyata.ui.splashscreen.OptionScreen
import com.example.tumbuhnyata.ui.register.OtpScreen
import com.example.tumbuhnyata.ui.register.VerifikasiScreen
import com.example.tumbuhnyata.ui.register.AkunBerhasil
import com.example.tumbuhnyata.ui.workshop.DaftarWorkshop
import com.example.tumbuhnyata.ui.workshop.DeskripsiWorkshopScreen
import com.example.tumbuhnyata.ui.workshop.NewWorkshop
import com.example.tumbuhnyata.ui.register.RegisterScreen
import com.example.tumbuhnyata.ui.workshop.RekomWorkshop
import com.example.tumbuhnyata.ui.splashscreen.SplashScreen
import com.example.tumbuhnyata.ui.workshop.WorkshopBerhasil
import com.example.tumbuhnyata.ui.workshop.WorkshopScreen

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
            RegisterScreen (navController)
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

