// File: CsrSubmissionViewModel.kt
package com.example.tumbuhnyata.ui.eventcsr

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CsrSubmissionViewModel : ViewModel() {
    var programName = mutableStateOf("")
    var category = mutableStateOf("")
    var startDate = mutableStateOf("")
    var endDate = mutableStateOf("")
    var location = mutableStateOf("")
    var partnerName = mutableStateOf("")
    var budget = mutableStateOf("")

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
            delay(2000)
            isLoading.value = false
            isSuccess.value = true
            onSuccess()
        }
    }

    fun reset() {
        programName.value = ""
        category.value = ""
        startDate.value = ""
        endDate.value = ""
        location.value = ""
        partnerName.value = ""
        budget.value = ""
        isLoading.value = false
        isSuccess.value = false
        errorMessage.value = null
    }
}
