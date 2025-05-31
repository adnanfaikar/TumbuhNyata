package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.ui.graphics.Color
import com.example.tumbuhnyata.data.model.CsrCategory

class DashboardKeuanganViewModel : ViewModel() {
    val categories = listOf(
        CsrCategory("Sosial", 1955670825f, Color(0xFF3F51B5)),
        CsrCategory("Lingkungan", 1231779900f, Color(0xFF2196F3))
    )

    val total: Float = categories.sumOf { it.amount.toDouble() }.toFloat()
}
