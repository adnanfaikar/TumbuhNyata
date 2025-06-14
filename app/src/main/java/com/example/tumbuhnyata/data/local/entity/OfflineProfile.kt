package com.example.tumbuhnyata.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "offline_profiles")
data class OfflineProfile(
    @PrimaryKey
    val id: Int,
    val companyName: String,
    val email: String,
    val phoneNumber: String,
    val nib: String,
    val address: String,
    val timestamp: Long = System.currentTimeMillis()
)