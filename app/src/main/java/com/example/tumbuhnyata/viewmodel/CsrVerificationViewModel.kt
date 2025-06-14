// File: CsrVerificationViewModel.kt
package com.example.tumbuhnyata.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.CsrApiService
import com.example.tumbuhnyata.data.CsrSubmissionRequest
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
            Log.e("CSR_DATE", "Date conversion error: ${e.message}")
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

                // Log request untuk debugging
                Log.d("CSR_SUBMIT", "Submitting CSR with data:")
                Log.d("CSR_SUBMIT", "Request: $request")

                val response = apiService.submitCSR(request)

                Log.d("CSR_SUBMIT", "Response code: ${response.code()}")
                Log.d("CSR_SUBMIT", "Response body: ${response.body()}")

                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.message == "Pengajuan CSR berhasil dibuat") {
                        submitSuccess.value = true
                        Log.d("CSR_SUBMIT", "CSR submission successful")
                        onSuccess()
                    } else {
                        errorMessage.value = responseBody?.message ?: "Verifikasi CSR gagal"
                        Log.e("CSR_SUBMIT", "Unexpected success response: ${responseBody?.message}")
                    }
                } else {
                    // Handle error response
                    val errorBody = response.errorBody()?.string()
                    Log.e("CSR_SUBMIT", "Error response: $errorBody")
                    
                    try {
                        val gson = com.google.gson.Gson()
                        val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                        errorMessage.value = errorResponse.message ?: "Verifikasi CSR gagal"
                    } catch (e: Exception) {
                        errorMessage.value = "Verifikasi CSR gagal (${response.code()})"
                    }
                }
            } catch (e: Exception) {
                Log.e("CSR_SUBMIT", "Exception during CSR submission", e)
                errorMessage.value = "Terjadi kesalahan: ${e.localizedMessage}"
            } finally {
                isSubmitting.value = false
            }
        }
    }
}

// Data class untuk handling error response
data class ErrorResponse(
    val message: String?,
    val error: String?
)
