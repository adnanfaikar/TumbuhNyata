package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.data.model.SubStatus
import com.example.tumbuhnyata.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RiwayatViewModel : ViewModel() {
    // Repository untuk mengakses data
    private val repository = NetworkModule.csrHistoryRepository

    // State untuk item yang perlu tindakan
    private val _perluTindakanItems = MutableStateFlow<List<CsrHistoryItem>>(emptyList())
    val perluTindakanItems: StateFlow<List<CsrHistoryItem>> = _perluTindakanItems

    // State untuk item yang sudah diterima
    private val _diterimaItems = MutableStateFlow<List<CsrHistoryItem>>(emptyList())
    val diterimaItems: StateFlow<List<CsrHistoryItem>> = _diterimaItems

    // State untuk loading indicator
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // State untuk error handling
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Fungsi untuk memuat data
    init {
        loadCsrHistory()
    }

    private fun loadCsrHistory() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Fetch data from API
                val csrHistory = repository.getCsrHistory(1) // Assuming user_id = 1 for now

                // Filter based on agreed status
                _perluTindakanItems.value = csrHistory.filter { !it.agreed }
                _diterimaItems.value = csrHistory.filter { it.agreed }

            } catch (e: Exception) {
                _error.value = when {
                    e.message?.contains("timeout", ignoreCase = true) == true ->
                        "Koneksi timeout. Silakan coba lagi."
                    e.message?.contains("connection", ignoreCase = true) == true ->
                        "Tidak ada koneksi internet. Periksa koneksi Anda."
                    else -> "Gagal memuat data: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        loadCsrHistory()
    }

    fun clearError() {
        _error.value = null
    }

    private fun formatDate(dateString: String): String {
        return try {
            val inputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("dd MMM yy", java.util.Locale("id"))
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }
}