package com.example.tumbuhnyata.data.model

import com.google.gson.annotations.SerializedName

data class CsrHistoryItem(
    val id: Int,
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("program_name")
    val programName: String,
    val category: String,
    val description: String,
    val location: String,
    @SerializedName("partner_name")
    val partnerName: String,
    @SerializedName("start_date")
    val startDate: String,
    @SerializedName("end_date")
    val endDate: String,
    val budget: String,
    @SerializedName("proposal_url")
    val proposalUrl: String?,
    @SerializedName("legality_url")
    val legalityUrl: String?,
    val agreed: Boolean,
    val status: String,
    @SerializedName("created_at")
    val createdAt: String
) 