package com.example.tumbuhnyata.data.model


data class TimelineItem(
    val title: String,
    val timestamp: String = "",
    val isCompleted: Boolean = false,
    val isInProgress: Boolean = false
)
