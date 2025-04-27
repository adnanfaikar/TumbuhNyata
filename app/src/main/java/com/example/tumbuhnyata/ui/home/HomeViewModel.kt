package com.example.tumbuhnyata.ui.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CompanyInfo(
    val name: String = "PT Paragon Corp",
    val address: String = "Kampung Baru, No.1 Jakarta",
    val hasNotifications: Boolean = true
)

data class CSRStatus(
    val completed: Int = 12,
    val inProgress: Int = 4,
    val upcoming: Int = 7
)

data class CSRFund(
    val amount: String = "Rp 2.987.450.725",
    val note: String = "*terhitung dari dana CSR yang telah selesai"
)

data class BadgeInfo(
    val levelBadge: String = "Super Star",
    val emissionReduction: String = "4.250 kg CO₂e"
)

data class Activity(
    val title: String,
    val community: String,
    val status: String,
    val kategori: String,
    val lokasi: String,
    val periode: String,
    val statusType: StatusType
)

data class HomeState(
    val companyInfo: CompanyInfo = CompanyInfo(),
    val csrStatus: CSRStatus = CSRStatus(),
    val csrFund: CSRFund = CSRFund(),
    val badgeInfo: BadgeInfo = BadgeInfo(),
    val activities: List<Activity> = listOf(
        Activity(
            title = "Penanaman 1000 Pohon",
            community = "Komunitas Jaya Hijau",
            status = "Program Selesai",
            kategori = "Lingkungan",
            lokasi = "Jakarta Timur",
            periode = "12 Mar - 20 Jun 24",
            statusType = StatusType.COMPLETED
        ),
        Activity(
            title = "Penghijauan Hutan Kaltim",
            community = "PT Hijau Sejati",
            status = "Mendatang",
            kategori = "Lingkungan",
            lokasi = "Kalimantan",
            periode = "12 Mar - 20 Mar 25",
            statusType = StatusType.UPCOMING
        ),
        Activity(
            title = "Beasiswa Yatim Jabar",
            community = "Pemerintah Prov. Jabar",
            status = "Progress",
            kategori = "Sosial",
            lokasi = "Jawa Barat",
            periode = "6 Mar - 15 Jun 25",
            statusType = StatusType.IN_PROGRESS
        ),
        Activity(
            title = "Donor Darah Paragon 2025",
            community = "RS Bunda Mulia",
            status = "Progress",
            kategori = "Sosial",
            lokasi = "Jakarta Raya",
            periode = "12 Jan - 2 Apr 25",
            statusType = StatusType.IN_PROGRESS
        ),
        Activity(
            title = "Penanaman Mangrove",
            community = "Pemkot Kota Lombok",
            status = "Program Selesai",
            kategori = "Lingkungan",
            lokasi = "Pantai Barat, Lombok",
            periode = "12 Mar - 20 Jun 24",
            statusType = StatusType.COMPLETED
        )
    )
)

class HomeViewModel : ViewModel() {
    private val _homeState = MutableStateFlow(HomeState())
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    fun updateCompanyInfo(name: String, address: String) {
        _homeState.value = _homeState.value.copy(
            companyInfo = _homeState.value.companyInfo.copy(
                name = name,
                address = address
            )
        )
    }

    fun updateCSRStatus(completed: Int, inProgress: Int, upcoming: Int) {
        _homeState.value = _homeState.value.copy(
            csrStatus = CSRStatus(
                completed = completed,
                inProgress = inProgress,
                upcoming = upcoming
            )
        )
    }

    fun updateCSRFund(amount: String) {
        _homeState.value = _homeState.value.copy(
            csrFund = _homeState.value.csrFund.copy(
                amount = amount
            )
        )
    }

    fun updateBadgeInfo(levelBadge: String, emissionReduction: String) {
        _homeState.value = _homeState.value.copy(
            badgeInfo = BadgeInfo(
                levelBadge = levelBadge,
                emissionReduction = emissionReduction
            )
        )
    }

    fun updateActivities(activities: List<Activity>) {
        _homeState.value = _homeState.value.copy(
            activities = activities
        )
    }
} 