package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.CsrApiService
import com.example.tumbuhnyata.data.api.CsrSubmissionRequest
import com.example.tumbuhnyata.data.local.dao.CsrDraftDao
import com.example.tumbuhnyata.data.local.entity.CsrDraftEntity
import kotlinx.coroutines.flow.Flow

class CsrDraftRepository(
    private val csrDraftDao: CsrDraftDao,
    private val csrApiService: CsrApiService
) {
    val allCsrDrafts: Flow<List<CsrDraftEntity>> = csrDraftDao.getAllCsrDrafts()

    suspend fun insertCsrDraft(csrDraft: CsrDraftEntity): Long {
        return csrDraftDao.insertCsrDraft(csrDraft)
    }

    suspend fun updateCsrDraft(csrDraft: CsrDraftEntity) {
        csrDraftDao.updateCsrDraft(csrDraft)
    }

    suspend fun deleteCsrDraft(csrDraft: CsrDraftEntity) {
        csrDraftDao.deleteCsrDraft(csrDraft)
    }

    suspend fun getCsrDraftById(id: Long): CsrDraftEntity? {
        return csrDraftDao.getCsrDraftById(id)
    }

    suspend fun submitCsrToApi(request: CsrSubmissionRequest): Boolean {
        return try {
            val response = csrApiService.submitCSR(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
} 