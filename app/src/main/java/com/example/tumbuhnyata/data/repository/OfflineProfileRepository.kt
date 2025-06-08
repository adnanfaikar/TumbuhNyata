package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.local.dao.OfflineProfileDao
import com.example.tumbuhnyata.data.local.entity.OfflineProfile
import com.example.tumbuhnyata.data.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfflineProfileRepository(
    private val offlineProfileDao: OfflineProfileDao
) {
    suspend fun getLatestProfile(): OfflineProfile? {
        return withContext(Dispatchers.IO) {
            offlineProfileDao.getLatestProfile()
        }
    }

    suspend fun saveProfile(profile: Profile) {
        withContext(Dispatchers.IO) {

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
        }
    }
} 