package com.example.tumbuhnyata.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.ui.home.HomeScreen
import com.example.tumbuhnyata.ui.splashscreen.SplashScreen
import com.example.tumbuhnyata.ui.login.LoginScreen
import com.example.tumbuhnyata.ui.profile.AboutScreen
import com.example.tumbuhnyata.ui.profile.ChangePassword
import com.example.tumbuhnyata.ui.profile.ChangePasswordSuccess
import com.example.tumbuhnyata.ui.profile.ProfileScreen
import com.example.tumbuhnyata.ui.profile.UpdateProfile
import com.example.tumbuhnyata.ui.profile.VerificationOne
import com.example.tumbuhnyata.ui.profile.VerificationSuccess
import com.example.tumbuhnyata.ui.profile.VerificationTwo
import com.example.tumbuhnyata.ui.register.AkunBerhasil
import com.example.tumbuhnyata.ui.register.OtpScreen
import com.example.tumbuhnyata.ui.register.RegisterScreen
import com.example.tumbuhnyata.ui.register.VerifikasiScreen
import com.example.tumbuhnyata.ui.screens.OptionScreen
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen1
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen2
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen3

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
            VerifikasiScreen (navController)
        }
        composable("otp") {
            OtpScreen (navController)
        }
        composable("akunberhasil") {
            AkunBerhasil (navController)
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
        composable("update_profile") {
            UpdateProfile(navController)
        }
        composable("change_password") {
            ChangePassword(navController)
        }
        composable("change_password_success") {
            ChangePasswordSuccess(navController)
        }
    }
}


