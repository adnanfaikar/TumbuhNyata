package com.example.tumbuhnyata.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "csr_drafts")
data class CsrDraftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val userId: Int,
    val programName: String,
    val category: String,
    val description: String,
    val location: String,
    val partnerName: String,
    val startDate: String,
    val endDate: String,
    val budget: String,
    val isSynced: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
) 