package com.example.tumbuhnyata.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {
    private val _splashState = MutableStateFlow(0)
    val splashState = _splashState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1000) // Tahap 1: Background
            _splashState.value = 1
            delay(1000) // Tahap 2: Logo muncul
            _splashState.value = 2
            delay(1000) // Tahap 3: Glow effect
            _splashState.value = 3
        }
    }
}