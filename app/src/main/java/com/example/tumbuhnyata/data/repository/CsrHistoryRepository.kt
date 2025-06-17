package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.AddCsrRequest
import com.example.tumbuhnyata.data.api.CsrHistoryApi
import com.example.tumbuhnyata.data.model.CsrHistoryItem

class CsrHistoryRepository(private val api: CsrHistoryApi) {
    suspend fun getCsrHistory(userId: Int): List<CsrHistoryItem> {
        return api.getCsrHistory(userId).body() ?: emptyList()
    }

    suspend fun getCsrHistoryDetail(id: Int, userId: Int): CsrHistoryItem? {
        return api.getCsrHistoryDetail(id, userId).body()
    }

    suspend fun deleteCsrHistory(id: Int, userId: Int): Boolean {
        return try {
            val response = api.deleteCsrHistory(id, userId)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }

    suspend fun addCsrHistory(request: AddCsrRequest): Boolean {
        return try {
            val response = api.addCsrHistory(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
} 