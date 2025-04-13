package com.example.tumbuhnyata.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.Workshop
import com.example.tumbuhnyata.data.model.recommendedWorkshops
import com.example.tumbuhnyata.data.model.recentWorkshops
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WorkshopDetailViewModel : ViewModel() {

    private val _selectedWorkshop = MutableStateFlow<Workshop?>(null)
    val selectedWorkshop: StateFlow<Workshop?> = _selectedWorkshop

    fun loadWorkshopById(id: String) {
        viewModelScope.launch {
            val found = recommendedWorkshops.find { it.id == id }
                ?: recentWorkshops.find { it.id == id }
            _selectedWorkshop.value = found
        }
    }
}
