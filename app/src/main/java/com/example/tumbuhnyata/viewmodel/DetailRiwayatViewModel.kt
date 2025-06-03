package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailRiwayatViewModel : ViewModel() {
    private val repository = NetworkModule.csrHistoryRepository

    private val _csrDetail = MutableStateFlow<CsrHistoryItem?>(null)
    val csrDetail: StateFlow<CsrHistoryItem?> = _csrDetail

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadCsrDetail(csrId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Fetch detail from API
                val detail = repository.getCsrHistoryDetail(csrId, 1) // Assuming user_id = 1 for now
                _csrDetail.value = detail
                
            } catch (e: Exception) {
                _error.value = e.message ?: "Failed to load CSR detail"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh(csrId: Int) {
        loadCsrDetail(csrId)
    }
} 