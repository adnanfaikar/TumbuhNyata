package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.ProfileApi
import com.example.tumbuhnyata.data.local.dao.OfflineProfileDao
import com.example.tumbuhnyata.data.local.entity.OfflineProfile
import com.example.tumbuhnyata.data.model.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.withContext

class OfflineProfileRepository(
    private val offlineProfileDao: OfflineProfileDao,
    private val profileApi: ProfileApi
) {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())


    suspend fun getLatestProfile(): OfflineProfile? {
        return withContext(Dispatchers.IO) {
            offlineProfileDao.getLatestProfile()
        }
    }

    suspend fun saveProfile(profile: Profile) {
        return withContext(Dispatchers.IO) {
            try {
                offlineProfileDao.deleteAllProfiles()

                val offlineProfile = OfflineProfile(
                    id = profile.id,
                    companyName = profile.companyName,
                    email = profile.email,
                    phoneNumber = profile.phoneNumber,
                    nib = profile.nib,
                    address = profile.address
                )
                offlineProfileDao.insert(offlineProfile)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun updateProfile(profile: Map<String, String>): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val existing = offlineProfileDao.getLatestProfile() ?: return@withContext false

                val updated = existing.copy(
                    companyName = profile["companyName"] ?: existing.companyName,
                    email = profile["email"] ?: existing.email,
                    phoneNumber = profile["phoneNumber"] ?: existing.phoneNumber,
                    nib = profile["nib"] ?: existing.nib,
                    address = profile["address"] ?: existing.address,
                    isSynced = false // Mark as unsynced
                )

                offlineProfileDao.update(updated)
                true
            } catch (e: Exception) {
                false
            }
        }
    }

    suspend fun syncOfflineProfiles(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val unsyncedProfiles = offlineProfileDao.getUnsyncedProfile()
                var allSynced = true

                for (profile in unsyncedProfiles) {
                    val updateData = mapOf(
                        "companyName" to profile.companyName,
                        "email" to profile.email,
                        "phoneNumber" to profile.phoneNumber,
                        "address" to profile.address
                    )

                    try {
                        val response = profileApi.updateProfile(updateData)

                        if (response.isSuccessful) {
                            offlineProfileDao.update(profile.copy(isSynced = true))
                        } else {
                            allSynced = false
                        }
                    } catch (e: Exception) {
                        allSynced = false
                        e.printStackTrace()
                    }
                }

                allSynced
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }


    suspend fun hasPendingSyncProfile(): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val unsyncedProfiles = offlineProfileDao.getUnsyncedProfile()
                unsyncedProfiles.isNotEmpty()
            } catch (e: Exception) {
                false
            }
        }
    }
}