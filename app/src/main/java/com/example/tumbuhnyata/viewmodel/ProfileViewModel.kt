package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ProfileState(
    val companyId: Int? = null, // Added company ID
    val companyName: String = "",
    val companyAddress: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val nib: String = "",
    val isLoggedIn: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ProfileViewModel : ViewModel() {
    private val repository = NetworkModule.profileRepository
    private val _profileState = MutableStateFlow(ProfileState(isLoading = true))
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

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
                        companyId = profile.id, // Store company ID
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
                    error = e.message ?: "Terjadi kesalahan saat memuat profil"
                )
            }
        }
    }

    fun logout() {
        _profileState.value = _profileState.value.copy(isLoggedIn = false)
    }

    fun updateProfile(companyName: String, email: String, phoneNumber: String, address: String) {
        viewModelScope.launch {
            try {
                _profileState.value = _profileState.value.copy(isLoading = true, error = null)
                val success = repository.updateProfile(companyName, email, phoneNumber, address)
                if (success) {
                    _profileState.value = _profileState.value.copy(
                        companyName = companyName,
                        companyAddress = address,
                        email = email,
                        phoneNumber = phoneNumber,
                        isLoading = false
                    )
                } else {
                    _profileState.value = _profileState.value.copy(
                        isLoading = false,
                        error = "Gagal memperbarui profil"
                    )
                }
            } catch (e: Exception) {
                _profileState.value = _profileState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Terjadi kesalahan saat memperbarui profil"
                )
            }
        }
    }

    fun changePassword(currentPassword: String, newPassword: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val success = repository.changePassword(currentPassword, newPassword)
                if (success) {
                    onSuccess()
                } else {
                    onError("Gagal mengubah password")
                }
            } catch (e: Exception) {
                onError(e.message ?: "Terjadi kesalahan saat mengubah password")
            }
        }
    }
}