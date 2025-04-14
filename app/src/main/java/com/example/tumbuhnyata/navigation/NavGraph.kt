package com.example.tumbuhnyata.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tumbuhnyata.ui.home.HomeScreen
import com.example.tumbuhnyata.ui.splashscreen.SplashScreen
import com.example.tumbuhnyata.ui.login.LoginScreen
import com.example.tumbuhnyata.ui.profile.AboutScreen
import com.example.tumbuhnyata.ui.profile.ProfileScreen
import com.example.tumbuhnyata.ui.profile.VerificationOne
import com.example.tumbuhnyata.ui.profile.VerificationSuccess
import com.example.tumbuhnyata.ui.profile.VerificationTwo
import com.example.tumbuhnyata.ui.notifikasi.NotifikasiDetailScreen
import com.example.tumbuhnyata.ui.notifikasi.NotifikasiScreen
import com.example.tumbuhnyata.ui.register.AkunBerhasil
import com.example.tumbuhnyata.ui.register.OtpScreen
import com.example.tumbuhnyata.ui.register.RegisterScreen
import com.example.tumbuhnyata.ui.register.VerifikasiScreen
import com.example.tumbuhnyata.ui.screens.OptionScreen
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen1
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen2
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen3
import com.example.tumbuhnyata.ui.dashboard.DashboardScreen
import com.example.tumbuhnyata.ui.dashboard.kpi.KpiDetailScreen
import com.example.tumbuhnyata.ui.dashboard.upload.UploadDataScreen
import com.example.tumbuhnyata.ui.dashboard.upload.UploadSuccessScreen

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
        composable("notifikasi") {
            NotifikasiScreen(navController = navController)
        }
        composable("notifikasi_detail") {
            NotifikasiDetailScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("about") {
            AboutScreen(navController)
        }
        composable("verification_one") {
            VerificationOne(navController)
        }
        composable("verification_two") {
            VerificationTwo(navController)
        }
        composable("verification_success") {
            VerificationSuccess(navController)
        }
        composable("profile") {
            ProfileScreen(navController)
        }
        composable("about") {
            AboutScreen(navController)
        }
        composable("verification_one") {
            VerificationOne(navController)
        }
        composable("verification_two") {
            VerificationTwo(navController)
        }
        composable("verification_success") {
            VerificationSuccess(navController)
        }
        // Main Dashboard Screen
        composable("dashboard") {
            // Assuming DashboardScreen is the main screen showing KPIItems
            DashboardScreen(navController = navController)
            // TODO: In DashboardScreen, when a KPIItem is clicked, call:
            // navController.navigate("kpi_detail/your_kpi_id_here")
        }

        // KPI Detail Screen (with argument)
        composable(
            route = "kpi_detail/{kpiId}", // Route with argument placeholder
            arguments = listOf(navArgument("kpiId") { // Define the argument
                type = NavType.StringType
                // defaultValue = "default_id" // Optional: Add default value
                // nullable = true // Optional: Make argument nullable
            })
        ) { backStackEntry ->
            // Retrieve the argument
            val kpiId = backStackEntry.arguments?.getString("kpiId")
            // Handle cases where argument might be missing (shouldn't happen if navigated correctly)
            requireNotNull(kpiId) { "kpiId parameter wasn't found. Please make sure it's set!" }

            KpiDetailScreen(navController = navController, kpiId = kpiId)
            // TODO: In KpiDetailScreen, add a button/action for "Tambah Data" that calls:
            // navController.navigate("upload_data")
        }

        // Upload Data Screen
        composable("upload_data") {
            UploadDataScreen(navController = navController)
            // TODO: In UploadDataScreen, the "Unggah Data" button's onClick should call:
            // navController.navigate("upload_success")
        }

        // Upload Success Screen
        composable("upload_success") {
            UploadSuccessScreen(navController = navController)
            // Button inside this screen handles navigation back to Dashboard
        }
        // Main Dashboard Screen
        composable("dashboard") {
            // Assuming DashboardScreen is the main screen showing KPIItems
            DashboardScreen(navController = navController)
            // TODO: In DashboardScreen, when a KPIItem is clicked, call:
            // navController.navigate("kpi_detail/your_kpi_id_here")
        }

        // KPI Detail Screen (with argument)
        composable(
            route = "kpi_detail/{kpiId}", // Route with argument placeholder
            arguments = listOf(navArgument("kpiId") { // Define the argument
                type = NavType.StringType
                // defaultValue = "default_id" // Optional: Add default value
                // nullable = true // Optional: Make argument nullable
            })
        ) { backStackEntry ->
            // Retrieve the argument
            val kpiId = backStackEntry.arguments?.getString("kpiId")
            // Handle cases where argument might be missing (shouldn't happen if navigated correctly)
            requireNotNull(kpiId) { "kpiId parameter wasn't found. Please make sure it's set!" }

            KpiDetailScreen(navController = navController, kpiId = kpiId)
            // TODO: In KpiDetailScreen, add a button/action for "Tambah Data" that calls:
            // navController.navigate("upload_data")
        }

        // Upload Data Screen
        composable("upload_data") {
            UploadDataScreen(navController = navController)
            // TODO: In UploadDataScreen, the "Unggah Data" button's onClick should call:
            // navController.navigate("upload_success")
        }

        // Upload Success Screen
        composable("upload_success") {
            UploadSuccessScreen(navController = navController)
            // Button inside this screen handles navigation back to Dashboard
        }
    }
}
