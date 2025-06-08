package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.R
import com.example.tumbuhnyata.ui.Sertifikasi.Sertifikasi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SertifikasiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val sertifikasiList: List<Sertifikasi> = emptyList()
)

class SertifikasiViewModel : ViewModel() {
    private val _state = MutableStateFlow(SertifikasiState())
    val state: StateFlow<SertifikasiState> = _state.asStateFlow()

    init {
        loadSertifikasi()
    }

    private fun loadSertifikasi() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                // TODO: Implement API call to fetch certifications
                // For now using dummy data
                val dummyData = listOf(
                    Sertifikasi(
                        title = "Environmental Management System",
                        code = "ISO 14001",
                        issued = "Issued Jun 2024 - Expires Jun 2027",
                        credentialId = "Credential ID ABC123XYZ",
                        imageRes = R.drawable.iso_14001
                    ),
                    Sertifikasi(
                        title = "Social Responsibility",
                        code = "ISO 26000",
                        issued = "Issued Feb 2023 - Expires Feb 2026",
                        credentialId = "Credential ID DEF456LMN",
                        imageRes = R.drawable.iso_26000
                    )
                )
                _state.value = _state.value.copy(
                    isLoading = false,
                    sertifikasiList = dummyData
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }

    fun refreshSertifikasi() {
        loadSertifikasi()
    }
}