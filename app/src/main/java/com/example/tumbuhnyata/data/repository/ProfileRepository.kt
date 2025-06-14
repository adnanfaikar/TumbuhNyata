package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.ProfileApi
import com.example.tumbuhnyata.data.model.Profile
import com.example.tumbuhnyata.di.NetworkModule
import com.example.tumbuhnyata.di.NetworkModule.offlineProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val api: ProfileApi,
    private val offlineProfileRepository: OfflineProfileRepository
) {

    suspend fun getUserProfile(): Profile? {
        return withContext(Dispatchers.IO) {
            try {
                val offlineProfile = getOfflineProfile()

                try {
                    val response = api.getUserProfile()
                    if (response.isSuccessful) {
                        val profile = response.body()?.data

                        profile?.let {
                            offlineProfileRepository.saveProfile(it)
                            return@withContext it
                        }
                    }
                } catch (e: Exception) {
                    if (offlineProfile != null) {
                        return@withContext offlineProfile
                    }
                    throw e
                }
                offlineProfile
            } catch (e: Exception) {
                getOfflineProfile()
            }
        }
    }

    suspend fun updateProfile(
        companyName: String,
        email: String,
        phoneNumber: String,
        address: String
    ): Boolean  {

        val updateData = mapOf(
            "companyName" to companyName,
            "email" to email,
            "phoneNumber" to phoneNumber,
            "address" to address
        )

        return withContext(Dispatchers.IO) {
            try {
                val response = api.updateProfile(updateData)


                if (response.isSuccessful) {
                    val currentProfile = offlineProfileRepository.getLatestProfile()
                    // Online update successful - save with synced status
                    currentProfile?.let { profile ->
                        val updatedProfile = Profile(
                            id = profile.id,
                            companyName = companyName,
                            email = email,
                            phoneNumber = phoneNumber,
                            nib = profile.nib,
                            address = address
                        )
                        offlineProfileRepository.saveProfile(updatedProfile)
                    }
                    true
                } else {
                    offlineProfileRepository.updateProfile(updateData)
                    true
                }
            } catch (e: Exception) {
                val updateSuccess = offlineProfileRepository.updateProfile(updateData)
                updateSuccess
            }
        }
    }

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val passwordData = mapOf(
                    "currentPassword" to currentPassword,
                    "newPassword" to newPassword
                )

                val response = api.changePassword(passwordData)
                response.isSuccessful
            } catch (e: Exception) {
                false
            }
        }
    }

    private suspend fun getOfflineProfile(): Profile? {
        return try {
            val offlineProfile = offlineProfileRepository.getLatestProfile()
            offlineProfile?.let {
                Profile(
                    id = it.id,
                    companyName = it.companyName,
                    email = it.email,
                    phoneNumber = it.phoneNumber,
                    nib = it.nib,
                    address = it.address
                )
            }
        } catch (e: Exception) {
            null
        }
    }
}