// File: CsrVerificationViewModel.kt
package com.example.tumbuhnyata.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.CsrApiService
import com.example.tumbuhnyata.ui.eventcsr.CsrData
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CsrVerificationViewModel : ViewModel() {
    var isSubmitting = mutableStateOf(false)
    var submitSuccess = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    private val apiService: CsrApiService = Retrofit.Builder()
        .baseUrl("http://10.0.2.2:5000/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CsrApiService::class.java)

    fun convertDateToBackendFormat(date: String): String {
        // Handle "DD MMM YYYY" (e.g., "01 May 2025") to "YYYY-MM-DD"
        return try {
            val inputFormat = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
            val outputFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val parsedDate = inputFormat.parse(date)
            outputFormat.format(parsedDate!!)
        } catch (e: Exception) {
            date // fallback, return original if parsing fails
        }
    }

    fun submitCsr(csrData: CsrData, onSuccess: () -> Unit) {
        isSubmitting.value = true
        errorMessage.value = null

        viewModelScope.launch {
            try {
                val request = CsrSubmissionRequest(
                    user_id = 1,
                    program_name = csrData.programName,
                    category = csrData.category,
                    description = csrData.description,
                    location = csrData.location,
                    partner_name = csrData.partnerName,
                    start_date = convertDateToBackendFormat(csrData.startDate),
                    end_date = convertDateToBackendFormat(csrData.endDate),
                    budget = csrData.budget.replace(Regex("[^\\d]"), ""),
                    agreed = true
                )

                val response = apiService.submitCSR(request)

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.message == "Pengajuan CSR berhasil dibuat") {
                        submitSuccess.value = true
                        onSuccess()
                    } else {
                        errorMessage.value = responseBody?.message ?: "Verifikasi CSR gagal"
                    }
                } else {
                    errorMessage.value = response.body()?.message ?: "Verifikasi CSR gagal"
                }
            } catch (e: Exception) {
                errorMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
            } finally {
                isSubmitting.value = false
            }
        }
    }
}
