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
import com.example.tumbuhnyata.ui.register.AkunBerhasil
import com.example.tumbuhnyata.ui.register.OtpScreen
import com.example.tumbuhnyata.ui.register.RegisterScreen
import com.example.tumbuhnyata.ui.register.VerifikasiScreen
import com.example.tumbuhnyata.ui.riwayat.DiterimaScreen
import com.example.tumbuhnyata.ui.riwayat.PerluTindakanScreen
import com.example.tumbuhnyata.ui.riwayat.RiwayatScreen
import com.example.tumbuhnyata.ui.riwayat.RiwayatViewModel
import com.example.tumbuhnyata.ui.screens.OptionScreen
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen1
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen2
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen3
import android.util.Log
import com.example.tumbuhnyata.ui.splashscreen.SplashScreen

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
        composable("riwayat") {
            val riwayatViewModel = remember {
                RiwayatViewModel(dummyList = dummyCsrList)
            }
            RiwayatScreen(
                navController = navController,
                riwayatViewModel = riwayatViewModel,
                onCsrCardClick = { csrItem ->
                    Log.d("NavGraph", "CSR Card clicked: ${csrItem.title}")
                    navController.navigate("detailRiwayat/${csrItem.title.hashCode()}")
                },
                onLihatSemuaPerluTindakan = {
                    navController.navigate("perluTindakan")
                },
                onLihatSemuaDiterima = {
                    navController.navigate("diterima")
                }
            )
        }
        composable("detailRiwayat/{csrItemId}") { backStackEntry ->
            val csrItemId = backStackEntry.arguments?.getString("csrItemId")
            // TODO: Fetch detail data based on csrItemId if needed
            CsrDetailScreen(onBack = { navController.popBackStack() }) // Assuming CsrDetailScreen can act as DetailRiwayatScreen
        }
        composable("perluTindakan") {
            val riwayatViewModel = remember {
                RiwayatViewModel(dummyList = dummyCsrList)
            }
            PerluTindakanScreen(riwayatViewModel = riwayatViewModel, onBack = { navController.popBackStack() })
        }
        composable("diterima") {
            val riwayatViewModel = remember {
                RiwayatViewModel(dummyList = dummyCsrList)
            }
            DiterimaScreen(riwayatViewModel = riwayatViewModel, onBack = { navController.popBackStack() })
        }
    }
}