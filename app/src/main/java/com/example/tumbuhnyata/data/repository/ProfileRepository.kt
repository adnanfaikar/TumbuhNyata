package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.ProfileApi
import com.example.tumbuhnyata.data.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ProfileRepository(
    private val api: ProfileApi,
    private val offlineProfileRepository: OfflineProfileRepository
) {

    suspend fun getUserProfile(): Profile? {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getUserProfile()
                if (response.isSuccessful) {
                    val profile = response.body()?.data

                    profile?.let {
                        offlineProfileRepository.saveProfile(it)
                    }

                    profile
                } else {
                    getOfflineProfile()
                }
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
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val updateData = mapOf(
                    "companyName" to companyName,
                    "email" to email,
                    "phoneNumber" to phoneNumber,
                    "address" to address
                )

                val response = api.updateProfile(updateData)

                if (response.isSuccessful) {
                    try {
                        val currentProfile = offlineProfileRepository.getLatestProfile()
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
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

                response.isSuccessful
            } catch (e: Exception) {
                false
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