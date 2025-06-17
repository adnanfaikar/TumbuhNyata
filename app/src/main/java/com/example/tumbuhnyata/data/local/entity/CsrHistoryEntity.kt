package com.example.tumbuhnyata.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "csr_history")
data class CsrHistoryEntity(
    @PrimaryKey
    val id: Int,
    val userId: Int,
    val programName: String,
    val category: String,
    val description: String,
    val location: String,
    val partnerName: String,
    val startDate: String,
    val endDate: String,
    val budget: String,
    val proposalUrl: String?,
    val legalityUrl: String?,
    val agreed: Boolean,
    val status: String,
    val createdAt: String,
    val isSynced: Boolean = true, // true jika sudah sync dengan server, false jika belum
    val isDeleted: Boolean = false, // untuk soft delete
    val lastModified: Long = System.currentTimeMillis()
) 