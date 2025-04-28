package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.NotificationApi
import com.example.tumbuhnyata.data.model.Notification

class NotificationRepository(
    private val api: NotificationApi
) {
    suspend fun getNotifications(userId: String): List<Notification> {
        return api.getNotifications(userId).body() ?: emptyList()
    }

    suspend fun createNotification(notification: Notification): Notification? {
        return api.createNotification(notification).body()
    }

    suspend fun markAsRead(id: Int): Boolean {
        return api.markAsRead(id).isSuccessful
    }

    suspend fun deleteNotification(id: Int): Boolean {
        return api.deleteNotification(id).isSuccessful
    }
} 