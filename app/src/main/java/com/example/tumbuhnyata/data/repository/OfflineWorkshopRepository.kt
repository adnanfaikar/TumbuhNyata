package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.WorkshopApiService
import com.example.tumbuhnyata.data.local.dao.OfflineWorkshopRegistrationDao
import com.example.tumbuhnyata.data.local.entity.OfflineWorkshopRegistration
import com.example.tumbuhnyata.data.model.RegisterWorkshop
import com.example.tumbuhnyata.data.model.WorkshopHistoryResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfflineWorkshopRepository(
    private val offlineWorkshopRegistrationDao: OfflineWorkshopRegistrationDao,
    private val workshopApiService: WorkshopApiService
) {

    suspend fun saveRegistrationOffline(
        workshopId: String,
        companyName: String,
        email: String,
        isSynced: Boolean = false
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val registration = OfflineWorkshopRegistration(
                    workshopId = workshopId,
                    companyName = companyName,
                    email = email,
                    isSynced = isSynced
                )
                offlineWorkshopRegistrationDao.insert(registration)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun syncRegistrations(): Boolean {
        return withContext(Dispatchers.IO) {
            var allSuccess = true
            try {
                val unsyncedRegistrations = offlineWorkshopRegistrationDao.getUnsyncedRegistrations()

                unsyncedRegistrations.forEach { registration ->
                    try {
                        val response = workshopApiService.registerWorkshop(
                            RegisterWorkshop(
                                workshop_id = registration.workshopId,
                                company_name = registration.companyName,
                                email = registration.email
                            )
                        )

                        if (response.isSuccessful) {
                            // Update the registration to mark it as synced
                            offlineWorkshopRegistrationDao.update(
                                registration.copy(isSynced = true)
                            )
                        } else {
                            allSuccess = false
                        }
                    } catch (e: Exception) {
                        allSuccess = false
                        e.printStackTrace()
                    }
                }
            } catch (e: Exception) {
                allSuccess = false
                e.printStackTrace()
            }
            allSuccess
        }
    }

    suspend fun hasPendingSyncRegistrations(): Boolean {
        return withContext(Dispatchers.IO) {
            offlineWorkshopRegistrationDao.getUnsyncedRegistrations().isNotEmpty()
        }
    }

    suspend fun getAllRegistrations(): List<OfflineWorkshopRegistration> {
        return withContext(Dispatchers.IO) {
            offlineWorkshopRegistrationDao.getAllRegistrations()
        }
    }

    suspend fun saveServerHistoryToLocal(data: List<WorkshopHistoryResponse>) {
        withContext(Dispatchers.IO) {
            data.forEach { item ->
                val entity = OfflineWorkshopRegistration(
                    id = item.id,
                    workshopId = item.workshopId,
                    companyName = item.companyName,
                    email = item.email,
                    isSynced = true,
                    timestamp = item.timestamp
                )
                offlineWorkshopRegistrationDao.insert(entity)
            }
        }
    }

    suspend fun deleteRegistrationsByIds(ids: List<String>) {
        withContext(Dispatchers.IO) {
            offlineWorkshopRegistrationDao.deleteByIds(ids)
        }
    }

    suspend fun getRegistrationById(id: String): OfflineWorkshopRegistration? {
        return withContext(Dispatchers.IO) {
            offlineWorkshopRegistrationDao.getRegistrationById(id)
        }
    }
}