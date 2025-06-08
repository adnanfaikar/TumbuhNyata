package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tumbuhnyata.data.repository.OfflineProfileRepository
import com.example.tumbuhnyata.data.repository.OfflineWorkshopRepository
import com.example.tumbuhnyata.data.repository.ProfileRepository
import com.example.tumbuhnyata.data.repository.WorkshopRepository

class WorkshopViewModelFactory(
    private val workshopRepository: WorkshopRepository,
    private val profileRepository: ProfileRepository,
    private val offlineProfileRepository: OfflineProfileRepository,
    private val offlineWorkshopRepository: OfflineWorkshopRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WorkshopViewModel::class.java)) {
            return WorkshopViewModel(
                workshopRepository,
                profileRepository,
                offlineProfileRepository,
                offlineWorkshopRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}