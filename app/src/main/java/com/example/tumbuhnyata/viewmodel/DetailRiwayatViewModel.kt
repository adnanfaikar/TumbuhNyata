package com.example.tumbuhnyata.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.di.NetworkModule
import com.example.tumbuhnyata.util.UserSessionManager
import com.example.tumbuhnyata.util.NetworkUtils
import com.example.tumbuhnyata.TumbuhNyataApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailRiwayatViewModel : ViewModel() {
    private val repository = NetworkModule.csrHistoryRepositoryOffline

    //    state untuk detail csr
    private val _csrDetail = MutableStateFlow<CsrHistoryItem?>(null)
    val csrDetail: StateFlow<CsrHistoryItem?> = _csrDetail

    //    state untuk loading
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    //    state untuk menangani eror
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    //   state untuk memuat detail
    fun loadCsrDetail(csrId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                
                // Gunakan user ID yang sesungguhnya dari UserSessionManager
                val userId = UserSessionManager.getUserId(TumbuhNyataApp.appContext)
                
                // Fetch detail from API
                val detail = repository.getCsrHistoryDetail(csrId, userId)
                _csrDetail.value = detail
                
            } catch (e: Exception) {
                _error.value = when {
                    e.message?.contains("timeout", ignoreCase = true) == true -> 
                        "Koneksi timeout. Silakan coba lagi."
                    e.message?.contains("connection", ignoreCase = true) == true -> 
                        "Tidak ada koneksi internet. Periksa koneksi Anda."
                    else -> "Gagal memuat detail CSR: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh(csrId: Int) {
        loadCsrDetail(csrId)
    }

    fun validateFile(uri: android.net.Uri, context: android.content.Context): String? {
        // Check file type
        val contentResolver = context.contentResolver
        val mimeType = contentResolver.getType(uri)
        if (mimeType != "application/pdf") {
            return "File harus berformat PDF"
        }

        // Check file size (max 10MB)
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val sizeIndex = it.getColumnIndex(android.provider.OpenableColumns.SIZE)
            it.moveToFirst()
            val size = it.getLong(sizeIndex)
            if (size > 10 * 1024 * 1024) { // 10MB in bytes
                return "Ukuran file tidak boleh lebih dari 10MB"
            }
        }

        return null
    }

    fun uploadRevision(uri: android.net.Uri, context: android.content.Context, csrId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Validate file first
                validateFile(uri, context)?.let { error ->
                    _error.value = error
                    return@launch
                }

                // TODO: Implement actual file upload to API
                // repository.uploadRevision(uri, csrId)
                
            } catch (e: Exception) {
                _error.value = when {
                    e.message?.contains("timeout", ignoreCase = true) == true -> 
                        "Koneksi timeout. Silakan coba lagi."
                    e.message?.contains("connection", ignoreCase = true) == true -> 
                        "Tidak ada koneksi internet. Periksa koneksi Anda."
                    else -> "Gagal mengupload file: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _error.value = null
    }
} 