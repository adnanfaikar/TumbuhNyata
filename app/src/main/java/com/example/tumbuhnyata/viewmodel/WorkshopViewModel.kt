package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.recommendedWorkshops
import com.example.tumbuhnyata.data.model.recentWorkshops
import com.example.tumbuhnyata.data.model.Workshop
import com.example.tumbuhnyata.data.repository.OfflineProfileRepository
import com.example.tumbuhnyata.data.repository.OfflineWorkshopRepository
import com.example.tumbuhnyata.data.repository.ProfileRepository
import com.example.tumbuhnyata.data.repository.WorkshopRepository
import com.example.tumbuhnyata.di.NetworkModule
import com.example.tumbuhnyata.ui.workshop.NewWorkshop
import com.example.tumbuhnyata.data.local.entity.OfflineWorkshopRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WorkshopViewModel(
    private val workshopRepository: WorkshopRepository,
    private val profileRepository: ProfileRepository,
    private val offlineProfileRepository: OfflineProfileRepository,
    private val offlineWorkshopRepository: OfflineWorkshopRepository
): ViewModel() {

    private val _recommended = mutableStateOf<List<Workshop>>(emptyList())
    val recommended: State<List<Workshop>> = _recommended

    private val _recent = mutableStateOf<List<Workshop>>(emptyList())
    val recent: State<List<Workshop>> = _recent

    private val _selectedWorkshop = mutableStateOf<Workshop?>(null)
    val selectedWorkshop: State<Workshop?> = _selectedWorkshop

    // Registration form states - Initialize these BEFORE init block
    private val _workshopId = MutableStateFlow("")
    val workshopId: StateFlow<String> = _workshopId.asStateFlow()

    private val _companyName = MutableStateFlow("")
    val companyName: StateFlow<String> = _companyName.asStateFlow()

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    // File upload states
    private val _fileSelected = MutableStateFlow(false)
    val fileSelected: StateFlow<Boolean> = _fileSelected.asStateFlow()

    private val _fileName = MutableStateFlow("")
    val fileName: StateFlow<String> = _fileName.asStateFlow()

    // UI states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _registerSuccess = MutableStateFlow(false)
    val registerSuccess: StateFlow<Boolean> = _registerSuccess.asStateFlow()

    private val _profileLoaded = MutableStateFlow(false)
    val profileLoaded: StateFlow<Boolean> = _profileLoaded.asStateFlow()

    private val _syncInProgress = MutableStateFlow(false)
    val syncInProgress: StateFlow<Boolean> = _syncInProgress.asStateFlow()

    private val _hasPendingSync = MutableStateFlow(false)
    val hasPendingSync: StateFlow<Boolean> = _hasPendingSync.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    private val _workshopHistory = MutableStateFlow<List<OfflineWorkshopRegistration>>(emptyList())
    val workshopHistory: StateFlow<List<OfflineWorkshopRegistration>> = _workshopHistory.asStateFlow()

    init {
        loadWorkshops()
        loadUserProfile()
        checkPendingSync()
        loadWorkshopHistory()
    }

    fun checkPendingSync() {
        viewModelScope.launch {
            _hasPendingSync.value = workshopRepository.hasPendingSyncRegistrations()
        }
    }

    fun syncRegistrations() {
        viewModelScope.launch {
            _syncInProgress.value = true
            _syncMessage.value = null

            try {
                val success = offlineWorkshopRepository.syncRegistrations()
                if (success) {
                    _syncMessage.value = "Semua pendaftaran berhasil disinkronkan"
                    _hasPendingSync.value = false
                } else {
                    _syncMessage.value = "Beberapa pendaftaran gagal disinkronkan. Akan dicoba lagi nanti."
                    _hasPendingSync.value = workshopRepository.hasPendingSyncRegistrations()
                }
            } catch (e: Exception) {
                _syncMessage.value = "Gagal melakukan sinkronisasi: ${e.localizedMessage}"
            } finally {
                _syncInProgress.value = false
            }
        }
    }

    fun clearSyncMessage() {
        _syncMessage.value = null
    }

    private fun loadWorkshops() {
        _recommended.value = workshopRepository.getRecommendedWorkshops().take(4)
        _recent.value = workshopRepository.getRecentWorkshops().take(4)
    }

    fun loadWorkshopDetail(workshopId: String) {
        _selectedWorkshop.value = workshopRepository.getWorkshopById(workshopId)
    }

    fun setWorkshopId(id: String) {
        _workshopId.value = id
        loadWorkshopDetail(id)
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val profile = profileRepository.getUserProfile()

                if (profile != null) {
                    _companyName.value = profile.companyName
                    _email.value = profile.email
                    _profileLoaded.value = true
                } else {
                    loadOfflineUserProfile()
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error loading profile: ${e.localizedMessage}"
                loadOfflineUserProfile()
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun loadOfflineUserProfile() {
        try {
            val profile = offlineProfileRepository.getLatestProfile()
            if (profile != null) {
                _companyName.value = profile.companyName
                _email.value = profile.email
                _profileLoaded.value = true
            } else {
                _errorMessage.value = "Tidak ada data profil tersimpan. Silakan login ulang."
            }
        } catch (e: Exception) {
            _errorMessage.value = "Error loading offline profile: ${e.localizedMessage}"
        }
    }

    fun selectFile(fileName: String) {
        _fileSelected.value = true
        _fileName.value = fileName
    }

    fun removeFile() {
        _fileSelected.value = false
        _fileName.value = ""
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun refreshProfile() {
        loadUserProfile()
    }

    fun registerWorkshop() {
        if (!_profileLoaded.value) {
            _errorMessage.value = "Data profil belum dimuat. Silakan tunggu sebentar."
            return
        }

        if (!_fileSelected.value) {
            _errorMessage.value = "Silakan pilih file terlebih dahulu"
            return
        }

        if (_companyName.value.isBlank() || _email.value.isBlank()) {
            _errorMessage.value = "Data profil tidak lengkap"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val success = workshopRepository.registerWorkshopOnline(
                    workshopId = _workshopId.value,
                    companyName = _companyName.value,
                    email = _email.value
                )

                if (success) {
                    // Simpan ke Room database dengan status tersinkronisasi
                    val saved = offlineWorkshopRepository.saveRegistrationOffline(
                        workshopId = _workshopId.value,
                        companyName = _companyName.value,
                        email = _email.value
                    )

                    if (saved) {
                        // Update status sinkronisasi
                        val registrations = offlineWorkshopRepository.getAllRegistrations()
                        val latestRegistration = registrations.maxByOrNull { it.timestamp }
                        if (latestRegistration != null) {
                            offlineWorkshopRepository.updateRegistrationSyncStatus(
                                registrationId = latestRegistration.id,
                                isSynced = true
                            )
                        }
                        _registerSuccess.value = true
                    } else {
                        _errorMessage.value = "Gagal menyimpan riwayat pendaftaran"
                    }
                } else {
                    _errorMessage.value = "Gagal daftar workshop. Silakan coba lagi."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: "Terjadi kesalahan"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resetRegistrationSuccess() {
        _registerSuccess.value = false
    }

    fun offlineRegisterWorkshop() {
        if (!_profileLoaded.value) {
            _errorMessage.value = "Data profil belum dimuat. Silakan tunggu sebentar."
            return
        }

        if (!_fileSelected.value) {
            _errorMessage.value = "Silakan pilih file terlebih dahulu"
            return
        }

        if (_companyName.value.isBlank() || _email.value.isBlank()) {
            _errorMessage.value = "Data profil tidak lengkap"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                val success = offlineWorkshopRepository.saveRegistrationOffline(
                    workshopId = _workshopId.value,
                    companyName = _companyName.value,
                    email = _email.value
                )

                if (success) {
                    _registerSuccess.value = true
                } else {
                    _errorMessage.value = "Gagal daftar workshop. Silakan coba lagi."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: "Terjadi kesalahan"}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadWorkshopHistory() {
        viewModelScope.launch {
            try {
                val history = offlineWorkshopRepository.getAllRegistrations()
                _workshopHistory.value = history
            } catch (e: Exception) {
                _errorMessage.value = "Gagal memuat riwayat workshop: ${e.localizedMessage}"
            }
        }
    }

    fun getWorkshopHistory(): List<OfflineWorkshopRegistration> {
        return workshopHistory.value
    }

    fun getWorkshopById(id: String): Workshop? {
        return workshopRepository.getWorkshopById(id)
    }

    suspend fun isDatabaseOnline(): Boolean {
        return try {
            workshopRepository.isDatabaseOnline()
        } catch (e: Exception) {
            false
        }
    }
}