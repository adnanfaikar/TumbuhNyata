package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class VerificationOneState(
    val aktaFile: String? = null,
    val skdpFile: String? = null,
    val isBothFilesUploaded: Boolean = false
)

class VerificationOneViewModel : ViewModel() {
    private val _verificationState = MutableStateFlow(VerificationOneState())
    val verificationState: StateFlow<VerificationOneState> = _verificationState.asStateFlow()

    fun uploadAktaFile(fileName: String) {
        _verificationState.value = _verificationState.value.copy(
            aktaFile = fileName,
            isBothFilesUploaded = _verificationState.value.skdpFile != null
        )
    }

    fun uploadSkdpFile(fileName: String) {
        _verificationState.value = _verificationState.value.copy(
            skdpFile = fileName,
            isBothFilesUploaded = _verificationState.value.aktaFile != null
        )
    }

    fun deleteAktaFile() {
        _verificationState.value = _verificationState.value.copy(
            aktaFile = null,
            isBothFilesUploaded = false
        )
    }

    fun deleteSkdpFile() {
        _verificationState.value = _verificationState.value.copy(
            skdpFile = null,
            isBothFilesUploaded = false
        )
    }
} 