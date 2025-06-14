package com.example.tumbuhnyata.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "offline_workshop_registrations")
data class OfflineWorkshopRegistration(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val workshopId: String,
    val companyName: String,
    val email: String,
    val isSynced: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)