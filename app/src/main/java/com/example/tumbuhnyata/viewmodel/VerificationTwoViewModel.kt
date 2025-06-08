package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class VerificationTwoState(
    val picFile: String? = null,
    val isFileUploaded: Boolean = false
)

class VerificationTwoViewModel : ViewModel() {
    private val _verificationState = MutableStateFlow(VerificationTwoState())
    val verificationState: StateFlow<VerificationTwoState> = _verificationState.asStateFlow()

    fun uploadPicFile(fileName: String) {
        _verificationState.value = _verificationState.value.copy(
            picFile = fileName,
            isFileUploaded = true
        )
    }

    fun deletePicFile() {
        _verificationState.value = _verificationState.value.copy(
            picFile = null,
            isFileUploaded = false
        )
    }
} 