package com.example.tumbuhnyata.navigation

import LanguagePreference
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tumbuhnyata.ui.home.HomeScreen
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
import com.example.tumbuhnyata.ui.notification.NotifikasiDetailScreen
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen1
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen2
import com.example.tumbuhnyata.ui.splashscreen.OnboardingScreen3
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tumbuhnyata.ui.splashscreen.SplashScreen
import com.example.tumbuhnyata.ui.eventcsr.CsrSubmissionScreen
import com.example.tumbuhnyata.ui.eventcsr.CsrVerificationScreen
import com.example.tumbuhnyata.ui.eventcsr.CsrSuccessScreen
import com.google.gson.Gson
import com.example.tumbuhnyata.ui.splashscreen.OptionScreen
import com.example.tumbuhnyata.ui.workshop.DaftarWorkshop
import com.example.tumbuhnyata.ui.workshop.DeskripsiWorkshopScreen
import com.example.tumbuhnyata.ui.workshop.NewWorkshop
import com.example.tumbuhnyata.ui.workshop.RekomWorkshop
import com.example.tumbuhnyata.ui.workshop.WorkshopBerhasil
import com.example.tumbuhnyata.ui.workshop.WorkshopScreen
import com.example.tumbuhnyata.ui.dashboard.DashboardScreen
import com.example.tumbuhnyata.ui.dashboard.kpi.KpiDetailScreen
import com.example.tumbuhnyata.ui.dashboard.upload.UploadDataScreen
import com.example.tumbuhnyata.ui.dashboard.upload.UploadSuccessScreen
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.tumbuhnyata.di.NetworkModule
import com.example.tumbuhnyata.ui.riwayat.*
import com.example.tumbuhnyata.ui.Sertifikasi.SertifikasiScreen
import com.example.tumbuhnyata.ui.Sertifikasi.AjukanSertifikasiScreen
import com.example.tumbuhnyata.ui.Sertifikasi.SertifikasiAndaScreen
import com.example.tumbuhnyata.ui.Sertifikasi.RiwayatPengajuanScreen
import com.example.tumbuhnyata.ui.Sertifikasi.DetailSertifikasiScreen
import com.example.tumbuhnyata.ui.Sertifikasi.DokumenOne
import com.example.tumbuhnyata.ui.Sertifikasi.CertificationSuccessScreen
import com.example.tumbuhnyata.ui.dashboardkeuangan.DashboardKeuanganScreen
import com.example.tumbuhnyata.data.model.CsrData
import com.example.tumbuhnyata.ui.notification.NotificationScreen
import com.example.tumbuhnyata.ui.workshop.RiwayatWorkshopScreen
import com.example.tumbuhnyata.viewmodel.RiwayatViewModel
import com.example.tumbuhnyata.viewmodel.WorkshopViewModel
import com.example.tumbuhnyata.viewmodel.WorkshopViewModelFactory
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.ui.eventcsr.DraftListScreen
import com.example.tumbuhnyata.ui.eventcsr.DraftSuccessScreen

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
        composable("register") {
            RegisterScreen(navController)
        }
        composable("login") {
            LoginScreen(navController)
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

        // Notifikasi
        composable("notifikasi") {
            NotificationScreen(
                userId = "1", // Ganti dengan ID user yang sebenarnya dari session
                onBackClick = { navController.popBackStack() }
            )
        }
        composable("notifikasi_detail") {
            NotifikasiDetailScreen(navController = navController)
        }

        // Profile
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


        // Dashboard
        composable("dashboard") {
            DashboardScreen(navController = navController)
        }

        // KPI Detail
        composable(
            route = "kpi_detail/{kpiId}",
            arguments = listOf(navArgument("kpiId") {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val kpiId = backStackEntry.arguments?.getString("kpiId")
            requireNotNull(kpiId) { "kpiId parameter wasn't found. Please make sure it's set!" }
            KpiDetailScreen(navController = navController, kpiId = kpiId)
        }

        // Upload screens
        composable("upload_data") {
            UploadDataScreen(navController = navController)
        }
        composable("upload_success") {
            UploadSuccessScreen(navController = navController)
        }

        composable("workshop") {
            val viewModelFactory = WorkshopViewModelFactory(
                NetworkModule.workshopRepository,
                NetworkModule.profileRepository,
                NetworkModule.offlineProfileRepository,
                NetworkModule.offlineWorkshopRepository
            )

            val viewModel: WorkshopViewModel = viewModel(factory = viewModelFactory)

            WorkshopScreen(
                navController = navController,
                viewModel = viewModel
            )
        }


        composable("rekomendasiworkshop") {
            RekomWorkshop(navController)
        }
        composable("workshopterbaru") {
            NewWorkshop(navController)
        }
        composable("deskripsiworkshop/{workshopId}") { backStackEntry ->
            val workshopId = backStackEntry.arguments?.getString("workshopId") ?: ""
            val viewModelFactory = WorkshopViewModelFactory(
                NetworkModule.workshopRepository,
                NetworkModule.profileRepository,
                NetworkModule.offlineProfileRepository,
                NetworkModule.offlineWorkshopRepository
            )

            val viewModel: WorkshopViewModel = viewModel(factory = viewModelFactory)
            DeskripsiWorkshopScreen(navController, workshopId, viewModel = viewModel)
        }
        composable("daftarworkshop/{workshopId}") { backStackEntry ->
            val workshopId = backStackEntry.arguments?.getString("workshopId")
            val viewModelFactory = WorkshopViewModelFactory(
                NetworkModule.workshopRepository,
                NetworkModule.profileRepository,
                NetworkModule.offlineProfileRepository,
                NetworkModule.offlineWorkshopRepository
            )

            val viewModel: WorkshopViewModel = viewModel(factory = viewModelFactory)
            DaftarWorkshop(
                navController = navController,
                workshopId = workshopId,
                viewModel = viewModel
            )
        }
        composable("workshopberhasil") {
            WorkshopBerhasil(navController)
        }

        composable("riwayatworkshop") {
            val viewModelFactory = WorkshopViewModelFactory(
                NetworkModule.workshopRepository,
                NetworkModule.profileRepository,
                NetworkModule.offlineProfileRepository,
                NetworkModule.offlineWorkshopRepository
            )

            val viewModel: WorkshopViewModel = viewModel(factory = viewModelFactory)
            RiwayatWorkshopScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // CSR Event screens
        composable("csr_submission") {
            CsrSubmissionScreen(navController)
        }
        composable(
            route = "csr_verification/{csrDataJson}",
            arguments = listOf(navArgument("csrDataJson") { type = NavType.StringType })
        ) { backStackEntry ->
            val csrDataJson = backStackEntry.arguments?.getString("csrDataJson")
            if (csrDataJson != null) {
                val csrData = Gson().fromJson(csrDataJson, CsrData::class.java)
                CsrVerificationScreen(navController, csrData)
            } else {
                // Optionally show an error or navigate back
                // navController.popBackStack()
            }
        }
        composable("csr_success") {
            CsrSuccessScreen(navController)
        }
        composable("draft_list") {
            DraftListScreen(navController)
        }
        composable("draft_success") {
            DraftSuccessScreen(navController)
        }

        // Home
        composable("home") {
            HomeScreen(navController)
        }

        // Invoice Screen
        composable("invoice") {
            InvoiceScreen(
                onBack = { navController.popBackStack() }
            )
        }

        // Riwayat screens
        composable("riwayat") {
            val riwayatViewModel = viewModel<RiwayatViewModel>()
            RiwayatScreen(
                navController = navController,
                riwayatViewModel = riwayatViewModel,
                onCsrCardClick = { csrItem: CsrHistoryItem ->
                    if (csrItem.agreed) {
                        if(csrItem.status.lowercase() == "program selesai") {
                            navController.navigate("SelesaiDetail/${csrItem.id}")
                        } else {
                            navController.navigate("ProgressDetail/${csrItem.id}")
                        }
                    } else {
                        navController.navigate("detailRiwayat/${csrItem.id}")
                    }
                },
                onLihatSemuaPerluTindakan = {
                    navController.navigate("perluTindakan")
                },
                onLihatSemuaDiterima = {
                    navController.navigate("diterima")
                }
            )
        }

        // Detail Riwayat Screen
        composable(
            route = "detailRiwayat/{csrId}",
            arguments = listOf(
                navArgument("csrId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val csrId = backStackEntry.arguments?.getInt("csrId") ?: return@composable
            DetailRiwayatScreen(
                csrId = csrId,
                onBack = { navController.popBackStack() },
                onNavigateToInvoice = { navController.navigate("invoice") },
                onNavigateToUploadRevisi = { navController.navigate("upload_revisi") }
            )
        }

        // Progress Detail Screen
        composable(
            route = "ProgressDetail/{csrId}",
            arguments = listOf(
                navArgument("csrId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val csrId = backStackEntry.arguments?.getInt("csrId") ?: return@composable
            ProgressDetailScreen(
                csrId = csrId,
                onBack = { navController.popBackStack() }
            )
        }

        // Selesai Detail Screen
        composable(
            route = "SelesaiDetail/{csrId}",
            arguments = listOf(
                navArgument("csrId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val csrId = backStackEntry.arguments?.getInt("csrId") ?: return@composable
            SelesaiDetailScreen(
                csrId = csrId,
                onBack = { navController.popBackStack() }
            )
        }

        composable("perluTindakan") {
            val riwayatViewModel = viewModel<RiwayatViewModel>()
            PerluTindakanScreen(
                riwayatViewModel = riwayatViewModel,
                onBack = { navController.popBackStack() },
                onCsrCardClick = { csrItem: CsrHistoryItem ->
                    navController.navigate("detailRiwayat/${csrItem.id}")
                }
            )
        }

        composable("diterima") {
            val riwayatViewModel = viewModel<RiwayatViewModel>()
            DiterimaScreen(
                riwayatViewModel = riwayatViewModel,
                onBack = { navController.popBackStack() },
                onCsrCardClick = { csrItem: CsrHistoryItem ->
                    navController.navigate("detailRiwayat/${csrItem.id}")
                }
            )
        }

        // Upload Revisi Screen
        composable("upload_revisi") {
            UploadRevisiScreen(
                navController = navController,
                onBack = { navController.popBackStack() },
                onUpload = { fileName ->
                    // Handle file upload logic here without navigation
                }
            )
        }

        // Sertifikasi Routes
        composable("sertifikasi") {
            SertifikasiScreen(navController)
        }
        composable("ajukansertifikasi") {
            AjukanSertifikasiScreen(navController)
        }
        composable("sertifikasianda") {
            SertifikasiAndaScreen(navController)
        }
        composable("riwayatpengajuan") {
            RiwayatPengajuanScreen(navController)
        }
        composable("detailsertifikasi") {
            DetailSertifikasiScreen(navController)
        }
        composable(
            route = "dokumenone/{certificationId}/{certificationName}/{certificationDescription}/{certificationCredentialBody}/{certificationBenefits}/{certificationCost}",
            arguments = listOf(
                navArgument("certificationId") { type = NavType.StringType },
                navArgument("certificationName") { type = NavType.StringType },
                navArgument("certificationDescription") { type = NavType.StringType },
                navArgument("certificationCredentialBody") { type = NavType.StringType },
                navArgument("certificationBenefits") { type = NavType.StringType },
                navArgument("certificationCost") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val certificationId = backStackEntry.arguments?.getString("certificationId")
            val certificationName = backStackEntry.arguments?.getString("certificationName")
            val certificationDescription = backStackEntry.arguments?.getString("certificationDescription")
            val certificationCredentialBody = backStackEntry.arguments?.getString("certificationCredentialBody")
            val certificationBenefits = backStackEntry.arguments?.getString("certificationBenefits")
            val certificationCost = backStackEntry.arguments?.getString("certificationCost")
            
            DokumenOne(
                navController = navController,
                certificationId = certificationId,
                certificationName = certificationName,
                certificationDescription = certificationDescription,
                certificationCredentialBody = certificationCredentialBody,
                certificationBenefits = certificationBenefits,
                certificationCost = certificationCost
            )
        }
        composable("berhasil") {
            CertificationSuccessScreen(navController)
        }

        composable("dashboardkeuangan") {
            DashboardKeuanganScreen(navController)
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
        composable("language_preference") {
            LanguagePreference(navController)
        }


    }
}