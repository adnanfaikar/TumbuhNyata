package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.api.CsrSubmissionRequest
import com.example.tumbuhnyata.data.local.entity.CsrDraftEntity
import com.example.tumbuhnyata.data.repository.CsrDraftRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class CsrDraftViewModel(private val repository: CsrDraftRepository) : ViewModel() {

    // Using StateFlow to observe changes in the list of drafts
    val allDrafts: StateFlow<List<CsrDraftEntity>> = repository.allCsrDrafts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun insertDraft(csrDraft: CsrDraftEntity) = viewModelScope.launch {
        repository.insertCsrDraft(csrDraft)
    }

    fun updateDraft(csrDraft: CsrDraftEntity) = viewModelScope.launch {
        repository.updateCsrDraft(csrDraft)
    }

    fun deleteDraft(csrDraft: CsrDraftEntity) = viewModelScope.launch {
        repository.deleteCsrDraft(csrDraft)
    }

    suspend fun getDraftById(id: Long): CsrDraftEntity? {
        return repository.getCsrDraftById(id)
    }

    fun submitDraftToApi(csrDraft: CsrDraftEntity, onSuccess: () -> Unit, onError: (String) -> Unit) = viewModelScope.launch {
        val request = CsrSubmissionRequest(
            user_id = csrDraft.userId,
            program_name = csrDraft.programName,
            category = csrDraft.category,
            description = csrDraft.description,
            location = csrDraft.location,
            partner_name = csrDraft.partnerName,
            start_date = csrDraft.startDate,
            end_date = csrDraft.endDate,
            budget = csrDraft.budget,
            agreed = true // Assuming drafts are agreed upon
        )

        val isSuccessful = repository.submitCsrToApi(request)
        if (isSuccessful) {
            // Delete the draft from local storage after successful API submission
            repository.deleteCsrDraft(csrDraft)
            onSuccess()
        } else {
            onError("Submission failed. Please try again.")
        }
    }
} 