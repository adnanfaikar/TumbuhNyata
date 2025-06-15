package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.CsrHistoryApi
import com.example.tumbuhnyata.data.model.CsrHistoryItem

class CsrHistoryRepository(private val api: CsrHistoryApi) {
    suspend fun getCsrHistory(userId: Int): List<CsrHistoryItem> {
        return api.getCsrHistory(userId).body() ?: emptyList()
    }

    suspend fun getCsrHistoryDetail(id: Int, userId: Int): CsrHistoryItem? {
        return api.getCsrHistoryDetail(id, userId).body()
    }
} 