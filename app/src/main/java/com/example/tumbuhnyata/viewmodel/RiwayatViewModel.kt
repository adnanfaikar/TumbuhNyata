package com.example.tumbuhnyata.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.CsrHistoryItem
import com.example.tumbuhnyata.data.model.SubStatus
import com.example.tumbuhnyata.di.NetworkModule
import com.example.tumbuhnyata.util.UserSessionManager
import com.example.tumbuhnyata.util.NetworkUtils
import com.example.tumbuhnyata.TumbuhNyataApp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RiwayatViewModel : ViewModel() {
    // Repository untuk mengakses data dengan offline support
    private val repository = NetworkModule.csrHistoryRepositoryOffline

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

    // State untuk delete success
    private val _deleteSuccess = MutableStateFlow<String?>(null)
    val deleteSuccess: StateFlow<String?> = _deleteSuccess

    // State untuk unsynced count
    private val _unsyncedCount = MutableStateFlow(0)
    val unsyncedCount: StateFlow<Int> = _unsyncedCount

    // State untuk sync status
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing

    // State untuk sync success message
    private val _syncSuccess = MutableStateFlow<String?>(null)
    val syncSuccess: StateFlow<String?> = _syncSuccess

    // Fungsi untuk memuat data dengan reactive flow
    init {
        loadCsrHistoryFlow()
        loadUnsyncedCount()
    }

    private fun loadCsrHistoryFlow() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val userId = UserSessionManager.getUserId(TumbuhNyataApp.appContext)

                // Gunakan flow untuk reactive UI updates
                repository.getPerluTindakanItems(userId).collect { items ->
                    _perluTindakanItems.value = items
                }

            } catch (e: Exception) {
                _error.value = when {
                    e.message?.contains("timeout", ignoreCase = true) == true -> 
                        "Koneksi timeout. Silakan coba lagi."
                    e.message?.contains("connection", ignoreCase = true) == true -> 
                        "Tidak ada koneksi internet. Menampilkan data offline."
                    else -> "Gagal memuat riwayat CSR: ${e.message}"
                }
            } finally {
                _isLoading.value = false
            }
        }

        viewModelScope.launch {
            try {
                val userId = UserSessionManager.getUserId(TumbuhNyataApp.appContext)
                
                // Load diterima items
                repository.getDiterimaItems(userId).collect { items ->
                    _diterimaItems.value = items
                }

            } catch (e: Exception) {
                // Error sudah dihandle di atas
            }
        }

        // Initial load untuk trigger API call dan sync
        refreshData()
        
        // Debug filter status (bisa dihapus di production)
        debugFilterStatus()
    }

    private fun loadUnsyncedCount() {
        viewModelScope.launch {
            repository.getUnsyncedCountFlow().collect { count ->
                _unsyncedCount.value = count
            }
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val userId = UserSessionManager.getUserId(TumbuhNyataApp.appContext)
                
                // Trigger data refresh (akan ambil dari API jika online, Room jika offline)
                val result = repository.getCsrHistory(userId)
                
                result.fold(
                    onSuccess = { data ->
                        // Data berhasil dimuat, tidak perlu set ke state karena menggunakan Flow
                        Log.d("RiwayatViewModel", "Data loaded successfully: ${data.size} items")
                    },
                    onFailure = { exception ->
                        _error.value = when (exception) {
                            is com.example.tumbuhnyata.data.repository.OfflineException -> 
                                "Tidak ada koneksi internet dan belum ada data tersimpan. Silakan coba lagi saat online."
                            is com.example.tumbuhnyata.data.repository.NetworkException -> 
                                "Terjadi masalah jaringan. ${if (exception.cause != null) "Menampilkan data offline." else "Silakan coba lagi."}"
                            is com.example.tumbuhnyata.data.repository.DatabaseException ->
                                "Terjadi masalah database lokal. Silakan restart aplikasi."
                            else -> when {
                                exception.message?.contains("timeout", ignoreCase = true) == true -> 
                                    "Koneksi timeout. Menampilkan data offline."
                                exception.message?.contains("connection", ignoreCase = true) == true -> 
                                    "Tidak ada koneksi internet. Menampilkan data offline."
                                else -> "Gagal memuat riwayat CSR: ${exception.message}"
                            }
                        }
                    }
                )
                
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan tidak terduga: ${e.message}"
                Log.e("RiwayatViewModel", "Unexpected error in refreshData", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun deleteCsrHistory(csrItem: CsrHistoryItem) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                // Gunakan user ID yang sesungguhnya dari UserSessionManager
                val userId = UserSessionManager.getUserId(TumbuhNyataApp.appContext)

                val isDeleted = repository.deleteCsrHistory(csrItem.id, userId)
                
                if (isDeleted) {
                    _deleteSuccess.value = "CSR berhasil dihapus"
                    // Refresh data setelah berhasil delete
                    refreshData()
                } else {
                    _error.value = "Gagal menghapus CSR. Silakan coba lagi."
                }
            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan saat menghapus CSR: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refresh() {
        refreshData()
    }

    fun clearError() {
        _error.value = null
    }

    fun clearDeleteSuccess() {
        _deleteSuccess.value = null
    }

    fun clearSyncSuccess() {
        _syncSuccess.value = null
    }

    // Manual sync function
    fun syncData() {
        viewModelScope.launch {
            try {
                // Cek koneksi internet terlebih dahulu
                if (!NetworkUtils.isNetworkAvailable(TumbuhNyataApp.appContext)) {
                    _error.value = "Tidak ada koneksi internet. Silakan cek koneksi Anda dan coba lagi."
                    return@launch
                }

                _isSyncing.value = true
                _error.value = null
                val userId = UserSessionManager.getUserId(TumbuhNyataApp.appContext)
                
                // Get unsynced count before sync
                val unsyncedCountBefore = repository.getUnsyncedCount()
                
                if (unsyncedCountBefore == 0) {
                    _syncSuccess.value = "Semua data sudah tersinkronisasi"
                    return@launch
                }
                
                // Perform sync
                repository.syncUnsyncedData(userId)
                
                // Clean up synced deleted items
                repository.cleanupSyncedDeletedItems()
                
                // Check unsynced count after sync
                val unsyncedCountAfter = repository.getUnsyncedCount()
                val syncedItemsCount = unsyncedCountBefore - unsyncedCountAfter
                
                if (syncedItemsCount > 0) {
                    _syncSuccess.value = "Berhasil menyinkronkan $syncedItemsCount data"
                } else {
                    _syncSuccess.value = "Sinkronisasi selesai"
                }
                
            } catch (e: Exception) {
                _error.value = "Gagal melakukan sinkronisasi: ${e.message}"
            } finally {
                _isSyncing.value = false
            }
        }
    }

    // Check if offline mode
    fun hasUnsyncedData(): Boolean {
        return _unsyncedCount.value > 0
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

    // Debug method untuk testing filter status
    private fun debugFilterStatus() {
        viewModelScope.launch {
            try {
                val userId = UserSessionManager.getUserId(TumbuhNyataApp.appContext)
                repository.debugFilterStatus(userId)
            } catch (e: Exception) {
                Log.e("RiwayatViewModel", "Error in debugFilterStatus: ${e.message}")
            }
        }
    }
}