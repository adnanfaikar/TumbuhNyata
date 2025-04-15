package com.example.tumbuhnyata.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.data.model.dummyCsrList
import com.example.tumbuhnyata.ui.detail.CsrDetailScreen // Assuming this is your DetailRiwayatScreen or you'll create a separate one
import com.example.tumbuhnyata.ui.home.HomeScreen
import com.example.tumbuhnyata.ui.login.LoginScreen
import com.example.tumbuhnyata.ui.profile.AboutScreen
import com.example.tumbuhnyata.ui.profile.ProfileScreen
import com.example.tumbuhnyata.ui.profile.VerificationOne
import com.example.tumbuhnyata.ui.profile.VerificationSuccess
import com.example.tumbuhnyata.ui.profile.VerificationTwo
import com.example.tumbuhnyata.ui.notifikasi.NotifikasiDetailScreen
import com.example.tumbuhnyata.ui.riwayat.DiterimaScreen
import com.example.tumbuhnyata.ui.riwayat.PerluTindakanScreen
import com.example.tumbuhnyata.ui.riwayat.RiwayatScreen
import com.example.tumbuhnyata.ui.riwayat.RiwayatViewModel
import com.example.tumbuhnyata.ui.notifikasi.NotifikasiScreen
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen1
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen2
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen3
import android.util.Log
import com.example.tumbuhnyata.ui.splashscreen.SplashScreen
import com.example.tumbuhnyata.ui.eventcsr.CsrSubmissionScreen
import com.example.tumbuhnyata.ui.eventcsr.CsrVerificationScreen
import com.example.tumbuhnyata.ui.eventcsr.CsrSuccessScreen
import com.google.gson.Gson
import com.example.tumbuhnyata.ui.screens.OptionScreen
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
import com.example.tumbuhnyata.ui.dashboard.DashboardScreen
import com.example.tumbuhnyata.ui.dashboard.kpi.KpiDetailScreen
import com.example.tumbuhnyata.ui.dashboard.upload.UploadDataScreen
import com.example.tumbuhnyata.ui.dashboard.upload.UploadSuccessScreen
import androidx.navigation.NavType

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "splash" // Ganti dengan "splash" jika ingin memulai dari splash screen,
    ) {
        composable("splash") {
            SplashScreen(navController)
        }
        composable("onboarding") {
            OnboardingScreen1(navController)
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
        composable("register") {
            RegisterScreen (navController)
        }
        composable("login") {
            LoginScreen (navController)
        }
        composable("verifikasi") {
            VerifikasiScreen(navController)
            VerifikasiScreen(navController)
        }
        composable("otp") {
            OtpScreen(navController)
        }
        composable("akunberhasil") {
            AkunBerhasil (navController)
        }
    }
}
