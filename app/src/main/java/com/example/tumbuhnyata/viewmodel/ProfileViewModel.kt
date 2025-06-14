package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val companyName: String = "",
    val companyAddress: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val nib: String = "",
    val isLoggedIn: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdated: Boolean = false,
    val isChangingPassword: Boolean = false,
    val isUpdatingProfile: Boolean = false
)

class ProfileViewModel : ViewModel() {
    private val repository = NetworkModule.profileRepository
    private val offlineProfileRepository = NetworkModule.offlineProfileRepository

    private val _profileState = MutableStateFlow(ProfileState(isLoading = true))
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    // Add missing StateFlow variables for sync functionality
    private val _syncInProgress = MutableStateFlow(false)
    val syncInProgress: StateFlow<Boolean> = _syncInProgress.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    private val _hasPendingProfileSync = MutableStateFlow(false)
    val hasPendingProfileSync: StateFlow<Boolean> = _hasPendingProfileSync.asStateFlow()

    init {
        loadUserProfile()
    }

    private fun loadUserProfile() {
        viewModelScope.launch {
            try {
                _profileState.value = _profileState.value.copy(isLoading = true, error = null)
                val profile = repository.getUserProfile()
                if (profile != null) {
                    _profileState.value = _profileState.value.copy(
                        companyName = profile.companyName,
                        companyAddress = profile.address,
                        email = profile.email,
                        phoneNumber = profile.phoneNumber,
                        nib = profile.nib,
                        isLoading = false
                    )
                } else {
                    _profileState.value = _profileState.value.copy(
                        isLoading = false,
                        error = "Gagal memuat profil"
                    )
                }
            } catch (e: Exception) {
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    error = "Terjadi kesalahan saat memuat profil"
                )
            }
        }
    }

    fun logout() {
        _profileState.value = _profileState.value.copy(isLoggedIn = false)
    }

    fun refreshProfile() {
        viewModelScope.launch {
            try {
                val profile = repository.getUserProfile()
                if (profile != null) {
                    _profileState.value = _profileState.value.copy(
                        companyName = profile.companyName,
                        companyAddress = profile.address,
                        email = profile.email,
                        phoneNumber = profile.phoneNumber,
                        nib = profile.nib
                    )
                }
            } catch (e: Exception) {
                // Silently fail for refresh
            }
        }
    }

    fun updateProfile(companyName: String, email: String, phoneNumber: String, address: String) {
        viewModelScope.launch {
            try {
                _profileState.value = _profileState.value.copy(isLoading = true, isUpdated = false, isUpdatingProfile = true, error = null)
                val success = repository.updateProfile(companyName, email, phoneNumber, address)
                if (success) {
                    _profileState.value = _profileState.value.copy(
                        companyName = companyName,
                        companyAddress = address,
                        email = email,
                        phoneNumber = phoneNumber,
                        isLoading = false,
                        isUpdated = true,
                        isUpdatingProfile = false
                    )
                    checkPendingProfileSync()
                } else {
                    _profileState.value = _profileState.value.copy(
                        isLoading = false,
                        isUpdatingProfile = false,
                        error = "Gagal memperbarui profil"
                    )
                }
            } catch (e: Exception) {
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    isUpdatingProfile = false,
                    error = e.message ?: "Terjadi kesalahan saat memperbarui profil"
                )
            }
        }
    }

    fun resetUpdateState() {
        _profileState.value = _profileState.value.copy(isUpdated = false)
    }

    fun onAppResumed() {
        viewModelScope.launch {
            // Check if we have pending sync and if we're online
            if (_hasPendingProfileSync.value) {
                try {
                    // Try to sync automatically
                    val success = offlineProfileRepository.syncOfflineProfiles()
                    if (success) {
                        _hasPendingProfileSync.value = false
                        _syncMessage.value = "Profil berhasil disinkronkan!"
                        refreshProfile() // Reload profile after successful sync
                    }
                } catch (e: Exception) {
                    // Still offline, keep pending status
                    _hasPendingProfileSync.value = true
                }
            }
        }
    }

    fun checkPendingProfileSync() {
        viewModelScope.launch {
            _hasPendingProfileSync.value = offlineProfileRepository.hasPendingSyncProfile()
        }
    }

    fun clearSyncMessage() {
        _syncMessage.value = null
    }

    fun changePassword(currentPassword: String, newPassword: String) {
        viewModelScope.launch {
            _profileState.value = _profileState.value.copy(
                isChangingPassword = true,
                error = null
            )

            try {
                val success = repository.changePassword(currentPassword, newPassword)
                if (success) {
                    _profileState.value = _profileState.value.copy(
                        isChangingPassword = false,
                        error = null,
                        isUpdated = true // jika ingin trigger navigasi dari LaunchedEffect
                    )
                } else {
                    _profileState.value = _profileState.value.copy(
                        isChangingPassword = false,
                        error = "Gagal mengubah password"
                    )
                }
            } catch (e: Exception) {
                _profileState.value = _profileState.value.copy(
                    isChangingPassword = false,
                    error = e.message ?: "Terjadi kesalahan saat mengubah password"
                )
            }
        }
    }
}