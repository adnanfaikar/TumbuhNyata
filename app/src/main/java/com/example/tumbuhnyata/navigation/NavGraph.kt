package com.example.tumbuhnyata.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tumbuhnyata.ui.eventcsr.CsrData
import com.example.tumbuhnyata.ui.home.HomeScreen
import com.example.tumbuhnyata.ui.splashscreen.SplashScreen
import com.example.tumbuhnyata.ui.login.LoginScreen
import com.example.tumbuhnyata.ui.register.AkunBerhasil
import com.example.tumbuhnyata.ui.register.OtpScreen
import com.example.tumbuhnyata.ui.register.RegisterScreen
import com.example.tumbuhnyata.ui.register.VerifikasiScreen
import com.example.tumbuhnyata.ui.screens.OptionScreen
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen1
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen2
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen3
import com.example.tumbuhnyata.ui.eventcsr.CsrSubmissionScreen
import com.example.tumbuhnyata.ui.eventcsr.CsrVerificationScreen
import com.example.tumbuhnyata.ui.eventcsr.CsrSuccessScreen
import com.google.gson.Gson

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
            OnboardingScreen1(navController)
        }
        composable("onboarding2") {
            OnboardingScreen2(navController)
        }
        composable("onboarding3") {
            OnboardingScreen3(navController)
        }
        composable("option") {
            OptionScreen(navController)
        }
        composable("home") {
            HomeScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
        }
        composable("register") {
            RegisterScreen(navController)
        }
        composable("verifikasi") {
            VerifikasiScreen(navController)
        }
        composable("otp") {
            OtpScreen(navController)
        }
        composable("akunberhasil") {
            AkunBerhasil(navController)
        }
        composable("csr_submission") {
            CsrSubmissionScreen(navController)
        }
        composable(
            route = "csr_verification/{csrData}",
            arguments = listOf(
                navArgument("csrData") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val csrDataJson = backStackEntry.arguments?.getString("csrData") ?: ""
            val csrData = Gson().fromJson(csrDataJson, CsrData::class.java)
            CsrVerificationScreen(navController = navController, csrData = csrData)
        }
        composable("csr_success") {
            CsrSuccessScreen(navController)
        }
    }
}
