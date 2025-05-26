package com.example.tumbuhnyata.data

data class CsrData(
    val programName: String,
    val category: String,
    val description: String,
    val location: String,
    val partnerName: String,
    val startDate: String,
    val endDate: String,
    val budget: String,
    val agreed: Boolean = true // Default to true for submission
)