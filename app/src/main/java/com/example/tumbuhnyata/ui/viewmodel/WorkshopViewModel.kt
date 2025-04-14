package com.example.tumbuhnyata.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.tumbuhnyata.data.model.recommendedWorkshops
import com.example.tumbuhnyata.data.model.recentWorkshops
import com.example.tumbuhnyata.data.model.Workshop

class WorkshopViewModel : ViewModel() {

    private val _recommended = mutableStateOf<List<Workshop>>(emptyList())
    val recommended: State<List<Workshop>> = _recommended

    private val _recent = mutableStateOf<List<Workshop>>(emptyList())
    val recent: State<List<Workshop>> = _recent

    init {
        _recommended.value = recommendedWorkshops.take(4)
        _recent.value = recentWorkshops.take(4)
    }
}
