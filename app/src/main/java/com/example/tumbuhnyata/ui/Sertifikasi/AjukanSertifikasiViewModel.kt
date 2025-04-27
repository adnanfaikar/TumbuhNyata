package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AjukanSertifikasiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val availableSertifikasiList: List<AjukanSertifikasi> = emptyList(),
    val selectedSertifikasi: AjukanSertifikasi? = null
)

class AjukanSertifikasiViewModel : ViewModel() {
    private val _state = MutableStateFlow(AjukanSertifikasiState())
    val state: StateFlow<AjukanSertifikasiState> = _state.asStateFlow()

    init {
        loadAvailableSertifikasi()
    }

    private fun loadAvailableSertifikasi() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                // TODO: Implement API call to fetch available certifications
                // For now using dummy data
                val dummyData = listOf(
                    AjukanSertifikasi(
                        title = "Social Responsibility",
                        code = "ISO 26000",
                        deskripsi = "International Organization for Standardization (ISO)",
                        imageRes = R.drawable.iso_26000
                    ),
                    AjukanSertifikasi(
                        title = "PROPER",
                        code = "Program Penilaian Peringkat Kinerja Perusahaan dalam Pengelolaan Lingkungan",
                        deskripsi = "Kementerian Lingkungan Hidup dan Kehutanan (KLHK)",
                        imageRes = R.drawable.proper
                    ),
                    AjukanSertifikasi(
                        title = "Ecolabel Indonesia",
                        code = "Ecolabel",
                        deskripsi = "Kementerian Perindustrian RI",
                        imageRes = R.drawable.ecolabel
                    ),
                    AjukanSertifikasi(
                        title = "Social Accountability Certification",
                        code = "SA8000",
                        deskripsi = "Social Accountability International (SAI)",
                        imageRes = R.drawable.sai
                    )
                )
                _state.value = _state.value.copy(
                    isLoading = false,
                    availableSertifikasiList = dummyData
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }

    fun selectSertifikasi(sertifikasi: AjukanSertifikasi) {
        _state.value = _state.value.copy(selectedSertifikasi = sertifikasi)
    }

    fun clearSelection() {
        _state.value = _state.value.copy(selectedSertifikasi = null)
    }

    fun refreshAvailableSertifikasi() {
        loadAvailableSertifikasi()
    }
} 