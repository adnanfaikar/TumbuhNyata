package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.ProfileApi
import com.example.tumbuhnyata.data.api.WorkshopApiService
import com.example.tumbuhnyata.data.model.RegisterWorkshop
import com.example.tumbuhnyata.data.model.Workshop
import com.example.tumbuhnyata.data.model.recentWorkshops
import com.example.tumbuhnyata.data.model.recommendedWorkshops
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WorkshopRepository(
    private val api: WorkshopApiService,
    private val apiProfile: ProfileApi,
    private val offlineWorkshopRepository: OfflineWorkshopRepository
) {

    fun getAllWorkshops(): List<Workshop> {
        return recommendedWorkshops + recentWorkshops.filter { workshop ->
            !recommendedWorkshops.any { it.id == workshop.id }
        }
    }

    fun getWorkshopById(id: String): Workshop? {
        val allWorkshops = getAllWorkshops()
        return allWorkshops.find { it.id == id }
    }

    fun getRecommendedWorkshops(): List<Workshop> {
        return recommendedWorkshops
    }

    fun getRecentWorkshops(): List<Workshop> {
        return recentWorkshops
    }

    // FIXED: Removed automatic offline saving - this should be handled by the caller
    suspend fun registerWorkshopOnline(
        workshopId: String,
        companyName: String,
        email: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val registerWorkshopData = RegisterWorkshop(
                    workshop_id = workshopId,
                    company_name = companyName,
                    email = email
                )
                val response = api.registerWorkshop(registerWorkshopData)
                response.isSuccessful
            } catch (e: Exception) {
                // Return false instead of automatically saving offline
                // Let the caller (ViewModel) handle offline saving
                false
            }
        }
    }

    suspend fun hasPendingSyncRegistrations(): Boolean {
        return offlineWorkshopRepository.hasPendingSyncRegistrations()
    }

    suspend fun isDatabaseOnline(): Boolean {
        return try {
            apiProfile.getUserProfile()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun syncWorkshopHistoryFromServer(email: String): Boolean {
        return try {
            val response = api.getWorkshopHistory(email)
            if (response.isSuccessful && response.body() != null) {
                offlineWorkshopRepository.saveServerHistoryToLocal(response.body()!!)
                true
            } else false
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteWorkshopOnline(workshopId: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.deleteWorkshopRegistration(workshopId)
                response.isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }
}