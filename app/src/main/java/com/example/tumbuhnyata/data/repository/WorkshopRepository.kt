package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.WorkshopApiService

class WorkshopRepository(private val api: WorkshopApiService) {
    suspend fun registerWorkshop(
        workshopId: String,
        companyName: String,
        email: String
    ): Boolean {
        val body = mapOf(
            "workshop_id" to workshopId,
            "company_name" to companyName,
            "email" to email
        )
        return api.registerWorkshop(body).isSuccessful
    }
} 