package com.example.tumbuhnyata.ui.profile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ProfileState(
    val companyName: String = "PT Paragon Corp",
    val companyAddress: String = "Kampung Baru, No 1 Jakarta",
    val isLoggedIn: Boolean = true
)

class ProfileViewModel : ViewModel() {
    private val _profileState = MutableStateFlow(ProfileState())
    val profileState: StateFlow<ProfileState> = _profileState.asStateFlow()

    fun logout() {
        _profileState.value = _profileState.value.copy(isLoggedIn = false)
    }
} 