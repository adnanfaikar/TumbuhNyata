package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.api.AddCsrRequest
import com.example.tumbuhnyata.di.NetworkModule
import com.example.tumbuhnyata.util.UserSessionManager
import com.example.tumbuhnyata.util.NetworkUtils
import com.example.tumbuhnyata.TumbuhNyataApp
import com.example.tumbuhnyata.data.model.normalizeStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class TambahRiwayatViewModel : ViewModel() {
    private val repository = NetworkModule.csrHistoryRepositoryOffline

    // Form states
    private val _programName = MutableStateFlow("")
    val programName: StateFlow<String> = _programName

    private val _mitra = MutableStateFlow("")
    val mitra: StateFlow<String> = _mitra

    private val _status = MutableStateFlow("")
    val status: StateFlow<String> = _status

    private val _kategori = MutableStateFlow("")
    val kategori: StateFlow<String> = _kategori

    private val _lokasi = MutableStateFlow("")
    val lokasi: StateFlow<String> = _lokasi

    private val _startDate = MutableStateFlow("")
    val startDate: StateFlow<String> = _startDate

    private val _endDate = MutableStateFlow("")
    val endDate: StateFlow<String> = _endDate

    private val _deskripsi = MutableStateFlow("")
    val deskripsi: StateFlow<String> = _deskripsi

    private val _budget = MutableStateFlow("")
    val budget: StateFlow<String> = _budget

    // UI states
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isSuccess = MutableStateFlow(false)
    val isSuccess: StateFlow<Boolean> = _isSuccess

    private val _successMessage = MutableStateFlow<String?>(null)
    val successMessage: StateFlow<String?> = _successMessage

    // Dropdown options
    val statusOptions = listOf(
        "Proses Review", "Memerlukan Revisi", "Menunggu Pembayaran",
        "Akan Datang", "Sedang Berlangsung", "Program Selesai"
    )

    val kategoriOptions = listOf("Lingkungan", "Sosial")

    // Update functions
    fun updateProgramName(value: String) { _programName.value = value }
    fun updateMitra(value: String) { _mitra.value = value }
    fun updateStatus(value: String) { _status.value = value }
    fun updateKategori(value: String) { _kategori.value = value }
    fun updateLokasi(value: String) { _lokasi.value = value }
    fun updateStartDate(value: String) { _startDate.value = value }
    fun updateEndDate(value: String) { _endDate.value = value }
    fun updateDeskripsi(value: String) { _deskripsi.value = value }
    fun updateBudget(value: String) { _budget.value = value }

    fun clearError() { _error.value = null }
    
    fun clearSuccessMessage() { _successMessage.value = null }

    private fun convertDateToApiFormat(dateString: String): String {
        return try {
            val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = inputFormat.parse(dateString)
            outputFormat.format(date!!)
        } catch (e: Exception) {
            dateString
        }
    }

    fun submitForm(onSuccess: () -> Unit) {
        if (!validateForm()) return

        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                val budgetValue = _budget.value.replace(Regex("[^\\d]"), "").toLongOrNull() ?: 0L

                // Gunakan user ID yang sesungguhnya dari UserSessionManager
                val userId = UserSessionManager.getUserId(TumbuhNyataApp.appContext)

                val request = AddCsrRequest(
                    user_id = userId,
                    program_name = _programName.value,
                    category = _kategori.value,
                    description = _deskripsi.value,
                    location = _lokasi.value,
                    partner_name = _mitra.value,
                    start_date = convertDateToApiFormat(_startDate.value),
                    end_date = convertDateToApiFormat(_endDate.value),
                    budget = budgetValue,
                    status = normalizeStatus(_status.value), // Normalisasi status
                    agreed = true
                )

                val isSuccess = repository.addCsrHistory(request)
                
                if (isSuccess) {
                    _isSuccess.value = true
                    // Cek apakah data disimpan offline atau online
                    if (!NetworkUtils.isNetworkAvailable(TumbuhNyataApp.appContext)) {
                        _successMessage.value = "Data CSR berhasil disimpan secara offline. Data akan tersinkron ketika kembali online."
                    } else {
                        _successMessage.value = "Data CSR berhasil disimpan"
                    }
                    onSuccess()
                } else {
                    _error.value = "Gagal menambahkan CSR. Silakan coba lagi."
                }

            } catch (e: Exception) {
                _error.value = "Terjadi kesalahan: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun validateForm(): Boolean {
        return when {
            _programName.value.isBlank() -> {
                _error.value = "Nama program harus diisi"
                false
            }
            _mitra.value.isBlank() -> {
                _error.value = "Nama mitra harus diisi"
                false
            }
            _status.value.isBlank() -> {
                _error.value = "Status harus dipilih"
                false
            }
            _kategori.value.isBlank() -> {
                _error.value = "Kategori program harus dipilih"
                false
            }
            _lokasi.value.isBlank() -> {
                _error.value = "Lokasi harus diisi"
                false
            }
            _startDate.value.isBlank() -> {
                _error.value = "Tanggal mulai harus diisi"
                false
            }
            _endDate.value.isBlank() -> {
                _error.value = "Tanggal selesai harus diisi"
                false
            }
            _deskripsi.value.isBlank() -> {
                _error.value = "Deskripsi harus diisi"
                false
            }
            _budget.value.isBlank() -> {
                _error.value = "Budget harus diisi"
                false
            }
            else -> true
        }
    }

    fun resetForm() {
        _programName.value = ""
        _mitra.value = ""
        _status.value = ""
        _kategori.value = ""
        _lokasi.value = ""
        _startDate.value = ""
        _endDate.value = ""
        _deskripsi.value = ""
        _budget.value = ""
        _isLoading.value = false
        _error.value = null
        _isSuccess.value = false
        _successMessage.value = null
    }
} 