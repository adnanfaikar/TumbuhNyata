package com.example.tumbuhnyata.data.model

data class WorkshopHistoryResponse(
    val id: String,
    val workshopId: String,
    val companyName: String,
    val email: String,
    val timestamp: Long
)