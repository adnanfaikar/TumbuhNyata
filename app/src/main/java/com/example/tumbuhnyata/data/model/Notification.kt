package com.example.tumbuhnyata.data.model

import com.google.gson.annotations.SerializedName

data class Notification(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("user_id")
    val userId: String,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("message")
    val message: String,
    
    @SerializedName("is_read")
    val isRead: Boolean,
    
    @SerializedName("created_at")
    val createdAt: String
) {
    fun isReadBool(): Boolean = isRead
} 