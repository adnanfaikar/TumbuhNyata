// File: CsrSubmissionViewModel.kt
package com.example.tumbuhnyata.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.api.CsrApiService
import com.example.tumbuhnyata.data.api.CsrSubmissionRequest
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


private val retrofit = Retrofit.Builder()
                    .baseUrl("http://10.0.2.2:5000/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

private val apiService = retrofit.create(CsrApiService::class.java)

class CsrSubmissionViewModel : ViewModel() {
    var programName = mutableStateOf("")
    var category = mutableStateOf("")
    var description = mutableStateOf("")
    var startDate = mutableStateOf("")
    var endDate = mutableStateOf("")
    var location = mutableStateOf("")
    var partnerName = mutableStateOf("")
    var budget = mutableStateOf("")
    var agreed = mutableStateOf(false)

    var isLoading = mutableStateOf(false)
    var isSuccess = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun isFormStepOneValid(description: String): Boolean {
        return programName.value.isNotBlank() && category.value.isNotBlank() && description.isNotBlank()
    }

    fun isFormStepTwoValid(): Boolean {
        return location.value.isNotBlank() && startDate.value.isNotBlank() && endDate.value.isNotBlank() && budget.value.isNotBlank()
    }

    fun submitForm(onSuccess: () -> Unit) {
        isLoading.value = true
        viewModelScope.launch {
            try {
                val request = CsrSubmissionRequest(
                    user_id = 1, // TODO: Replace with actual user ID
                    program_name = programName.value,
                    category = category.value,
                    description = description.value,
                    location = location.value,
                    partner_name = partnerName.value,
                    start_date = startDate.value,
                    end_date = endDate.value,
                    budget = budget.value,
                    agreed = agreed.value
                )
                val response = apiService.submitCSR(request)
                
                if (response.isSuccessful) {
                    isSuccess.value = true
                    onSuccess()
                } else {
                    errorMessage.value = response.message() ?: "Gagal membuat pengajuan CSR"
                }
            } catch (e: Exception) {
                errorMessage.value = e.message ?: "Terjadi kesalahan"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun reset() {
        programName.value = ""
        category.value = ""
        description.value = ""
        startDate.value = ""
        endDate.value = ""
        location.value = ""
        partnerName.value = ""
        budget.value = ""
        agreed.value = false
        isLoading.value = false
        isSuccess.value = false
        errorMessage.value = null
    }
}

