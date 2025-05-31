package com.example.tumbuhnyata.ui.Sertifikasi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.R
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class RiwayatPengajuan(
    val id: String,
    val title: String,
    val status: String,
    val tanggal: String,
    val imageRes: Int
)

data class RiwayatPengajuanState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val riwayatList: List<RiwayatPengajuan> = emptyList()
)

class RiwayatPengajuanViewModel : ViewModel() {
    private val _state = MutableStateFlow(RiwayatPengajuanState())
    val state: StateFlow<RiwayatPengajuanState> = _state.asStateFlow()

    init {
        loadRiwayatPengajuan()
    }

    private fun loadRiwayatPengajuan() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(isLoading = true)
                // TODO: Implement API call to fetch application history
                // For now using dummy data
                val dummyData = listOf(
                    RiwayatPengajuan(
                        id = "1",
                        title = "ISO 14001",
                        status = "Dalam Proses",
                        tanggal = "20 Mar 2024",
                        imageRes = R.drawable.iso_14001
                    ),
                    RiwayatPengajuan(
                        id = "2",
                        title = "ISO 26000",
                        status = "Selesai",
                        tanggal = "15 Feb 2024",
                        imageRes = R.drawable.iso_26000
                    )
                )
                _state.value = _state.value.copy(
                    isLoading = false,
                    riwayatList = dummyData
                )
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    error = e.message ?: "An error occurred"
                )
            }
        }
    }

    fun refreshRiwayatPengajuan() {
        loadRiwayatPengajuan()
    }
} 