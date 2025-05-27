// File: CsrVerificationViewModel.kt
package com.example.tumbuhnyata.ui.eventcsr

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CsrVerificationViewModel : ViewModel() {
    var isSubmitting = mutableStateOf(false)
    var submitSuccess = mutableStateOf(false)
    var errorMessage = mutableStateOf<String?>(null)

    fun submitCsr(onSuccess: () -> Unit) {
        isSubmitting.value = true
        errorMessage.value = null

        viewModelScope.launch {
            delay(2000) // Simulate network request
            isSubmitting.value = false
            submitSuccess.value = true
            onSuccess()
        }
    }
}
