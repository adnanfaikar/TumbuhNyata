package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.WorkshopApiService
import com.example.tumbuhnyata.data.local.dao.OfflineWorkshopRegistrationDao
import com.example.tumbuhnyata.data.local.entity.OfflineWorkshopRegistration
import com.example.tumbuhnyata.data.model.RegisterWorkshop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class OfflineWorkshopRepository(
    private val offlineWorkshopRegistrationDao: OfflineWorkshopRegistrationDao,
    private val workshopApiService: WorkshopApiService
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    suspend fun saveRegistrationOffline(
        workshopId: String,
        companyName: String,
        email: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val registration = OfflineWorkshopRegistration(
                    workshopId = workshopId,
                    companyName = companyName,
                    email = email
                )
                offlineWorkshopRegistrationDao.insert(registration)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    fun syncRegistrations(): Boolean {
        var allSuccess = true
        scope.launch {
            try {
                val unsyncedRegistrations = withContext(Dispatchers.IO) {
                    offlineWorkshopRegistrationDao.getUnsyncedRegistrations()
                }

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
                            withContext(Dispatchers.IO) {
                                offlineWorkshopRegistrationDao.update(
                                    registration.copy(isSynced = true)
                                )
                            }
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
        }
        return allSuccess
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

    suspend fun updateRegistrationSyncStatus(registrationId: String, isSynced: Boolean) {
        withContext(Dispatchers.IO) {
            val registration = offlineWorkshopRegistrationDao.getRegistrationById(registrationId)
            registration?.let {
                offlineWorkshopRegistrationDao.update(it.copy(isSynced = isSynced))
            }
        }
    }

    suspend fun deleteRegistrationsByIds(ids: List<String>) {
        withContext(Dispatchers.IO) {
            offlineWorkshopRegistrationDao.deleteByIds(ids)
        }
    }
}