package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.repository.CertificationRepository
import com.example.tumbuhnyata.data.repository.CertificationResource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CertificationSubmissionForm(
    val name: String = "",
    val description: String = "",
    val credentialBody: String = "",
    val benefits: String = "",
    val cost: String = "",
    val supportingDocuments: List<String> = emptyList()
)

data class AjukanSertifikasiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSubmitting: Boolean = false,
    val submissionSuccess: Boolean = false,
    val submissionMessage: String? = null,
    val form: CertificationSubmissionForm = CertificationSubmissionForm()
)

class AjukanSertifikasiViewModel(
    private val certificationRepository: CertificationRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AjukanSertifikasiState())
    val state: StateFlow<AjukanSertifikasiState> = _state.asStateFlow()

    fun updateFormField(field: String, value: String) {
        val currentForm = _state.value.form
        val updatedForm = when (field) {
            "name" -> currentForm.copy(name = value)
            "description" -> currentForm.copy(description = value)
            "credentialBody" -> currentForm.copy(credentialBody = value)
            "benefits" -> currentForm.copy(benefits = value)
            "cost" -> currentForm.copy(cost = value)
            else -> currentForm
        }
        _state.value = _state.value.copy(form = updatedForm)
    }

    fun addSupportingDocument(document: String) {
        val currentForm = _state.value.form
        val updatedDocuments = currentForm.supportingDocuments + document
        val updatedForm = currentForm.copy(supportingDocuments = updatedDocuments)
        _state.value = _state.value.copy(form = updatedForm)
    }

    fun addSupportingDocument(url: String, name: String) {
        val currentForm = _state.value.form
        val documentEntry = "$url|$name" // Use pipe separator instead of JSON
        val updatedDocuments = currentForm.supportingDocuments + documentEntry
        val updatedForm = currentForm.copy(supportingDocuments = updatedDocuments)
        _state.value = _state.value.copy(form = updatedForm)
    }

    fun clearSupportingDocuments() {
        val currentForm = _state.value.form
        val updatedForm = currentForm.copy(supportingDocuments = emptyList())
        _state.value = _state.value.copy(form = updatedForm)
    }

    fun removeSupportingDocument(index: Int) {
        val currentForm = _state.value.form
        val updatedDocuments = currentForm.supportingDocuments.toMutableList().apply {
            removeAt(index)
        }
        val updatedForm = currentForm.copy(supportingDocuments = updatedDocuments)
        _state.value = _state.value.copy(form = updatedForm)
    }

    fun submitCertification() {
        viewModelScope.launch {
            try {
                val form = _state.value.form
                
                // Validate form
                if (form.name.isBlank() || form.description.isBlank() || 
                    form.credentialBody.isBlank() || form.benefits.isBlank() || 
                    form.cost.isBlank()) {
                    _state.value = _state.value.copy(
                        error = "Please fill all required fields"
                    )
                    return@launch
                }

                val costValue = try {
                    form.cost.toFloat()
                } catch (e: NumberFormatException) {
                    _state.value = _state.value.copy(
                        error = "Invalid cost format"
                    )
                    return@launch
                }

                _state.value = _state.value.copy(
                    isSubmitting = true,
                    error = null
                )

                val result = certificationRepository.submitCertification(
                    name = form.name,
                    description = form.description,
                    credentialBody = form.credentialBody,
                    benefits = form.benefits,
                    cost = costValue,
                    supportingDocuments = form.supportingDocuments
                )

                when (result) {
                    is CertificationResource.Success -> {
                        _state.value = _state.value.copy(
                            isSubmitting = false,
                            submissionSuccess = true,
                            submissionMessage = "Certification application submitted successfully!",
                            form = CertificationSubmissionForm() // Reset form
                        )
                    }
                    is CertificationResource.Error -> {
                        _state.value = _state.value.copy(
                            isSubmitting = false,
                            error = result.message ?: "Failed to submit certification application"
                        )
                    }
                    is CertificationResource.Loading -> {
                        // Already handled by isSubmitting
                    }
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    isSubmitting = false,
                    error = e.message ?: "An unexpected error occurred"
                )
            }
        }
    }

    fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    fun clearSubmissionStatus() {
        _state.value = _state.value.copy(
            submissionSuccess = false,
            submissionMessage = null
        )
    }

    fun resetForm() {
        _state.value = _state.value.copy(form = CertificationSubmissionForm())
    }
} 