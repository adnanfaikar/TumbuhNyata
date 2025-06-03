package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.repository.ProfileRepository
import com.example.tumbuhnyata.data.repository.WorkshopRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DaftarWorkshopViewModel(
    private val workshopRepository: WorkshopRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DaftarWorkshopUiState>(DaftarWorkshopUiState.Initial)
    val uiState: StateFlow<DaftarWorkshopUiState> = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            _uiState.value = DaftarWorkshopUiState.Loading
            try {
                val profile = profileRepository.getUserProfile()
                if (profile != null) {
                    _uiState.value = DaftarWorkshopUiState.Success(
                        companyName = profile.companyName,
                        email = profile.email
                    )
                } else {
                    _uiState.value = DaftarWorkshopUiState.Error("Gagal mengambil data profil perusahaan.")
                }
            } catch (e: Exception) {
                _uiState.value = DaftarWorkshopUiState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }

    fun registerWorkshop(workshopId: String) {
        viewModelScope.launch {
            _uiState.value = DaftarWorkshopUiState.Loading
            try {
                val currentState = _uiState.value
                if (currentState is DaftarWorkshopUiState.Success) {
                    val result = workshopRepository.registerWorkshop(
                        workshopId = workshopId,
                        companyName = currentState.companyName,
                        email = currentState.email
                    )
                    if (result) {
                        _uiState.value = DaftarWorkshopUiState.RegistrationSuccess
                    } else {
                        _uiState.value = DaftarWorkshopUiState.Error("Gagal daftar workshop")
                    }
                }
            } catch (e: Exception) {
                _uiState.value = DaftarWorkshopUiState.Error("Terjadi kesalahan: ${e.message}")
            }
        }
    }
}

sealed class DaftarWorkshopUiState {
    object Initial : DaftarWorkshopUiState()
    object Loading : DaftarWorkshopUiState()
    data class Success(
        val companyName: String,
        val email: String
    ) : DaftarWorkshopUiState()
    object RegistrationSuccess : DaftarWorkshopUiState()
    data class Error(val message: String) : DaftarWorkshopUiState()
} 