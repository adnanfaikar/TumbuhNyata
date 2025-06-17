package com.example.tumbuhnyata.data.repository

import com.example.tumbuhnyata.data.api.NotificationApi
import com.example.tumbuhnyata.data.model.Notification
import com.example.tumbuhnyata.data.database.NotificationDao
import com.example.tumbuhnyata.data.database.toEntity
import com.example.tumbuhnyata.data.database.toNotification

class NotificationRepository(
    private val api: NotificationApi,
    private val dao: NotificationDao
) {
    suspend fun getNotifications(userId: String): List<Notification> {
        return try {
            // Coba ambil dari server dulu
            val serverNotifications = api.getNotifications(userId).body()
            
            if (serverNotifications != null) {
                // Simpan ke database lokal jika berhasil dari server
                val entities = serverNotifications.map { it.toEntity(synced = true) }
                dao.insertNotifications(entities)
                serverNotifications
            } else {
                // Fallback ke data lokal jika server response kosong
                getLocalNotifications(userId)
            }
        } catch (e: Exception) {
            // Fallback ke data lokal jika ada error dari server
            e.printStackTrace()
            getLocalNotifications(userId)
        }
    }
    
    private suspend fun getLocalNotifications(userId: String): List<Notification> {
        return try {
            dao.getNotificationsByUserIdSync(userId).map { it.toNotification() }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun createNotification(notification: Notification): Notification? {
        return try {
            val response = api.createNotification(notification)
            if (response.isSuccessful) {
                response.body()
            } else {
                // Simpan ke database lokal jika gagal kirim ke server
                dao.insertNotification(notification.toEntity(synced = false))
                notification
            }
        } catch (e: Exception) {
            // Simpan ke database lokal jika ada error
            e.printStackTrace()
            dao.insertNotification(notification.toEntity(synced = false))
            notification
        }
    }

    suspend fun markAsRead(id: Int): Boolean {
        return try {
            val serverSuccess = api.markAsRead(id).isSuccessful
            // Update database lokal terlepas dari hasil server
            dao.markAsRead(id)
            serverSuccess
        } catch (e: Exception) {
            // Update database lokal meskipun server error
            e.printStackTrace()
            dao.markAsRead(id)
            true // Return true karena berhasil update lokal
        }
    }

    suspend fun deleteNotification(id: Int): Boolean {
        return try {
            val serverSuccess = api.deleteNotification(id).isSuccessful
            // Hapus dari database lokal terlepas dari hasil server
            dao.deleteNotification(id)
            serverSuccess
        } catch (e: Exception) {
            // Hapus dari database lokal meskipun server error
            e.printStackTrace()
            dao.deleteNotification(id)
            true // Return true karena berhasil hapus lokal
        }
    }
    
    // Method untuk sync unsynced notifications
    suspend fun syncUnsyncedNotifications() {
        try {
            val unsyncedNotifications = dao.getUnsyncedNotifications()
            
            unsyncedNotifications.forEach { notificationEntity ->
                try {
                    // Coba sync dengan server
                    val notification = notificationEntity.toNotification()
                    val response = api.createNotification(notification)
                    
                    if (response.isSuccessful) {
                        // Tandai sebagai sudah disinkronkan
                        dao.markAsSynced(notificationEntity.id)
                    }
                } catch (e: Exception) {
                    // Log error tapi tetap lanjutkan dengan notifikasi lain
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    // Method untuk fetch notifications dari server
    suspend fun fetchNotifications(userId: String) {
        try {
            val notifications = api.getNotifications(userId).body()
            
            notifications?.let { notificationList ->
                // Simpan ke database lokal dengan status synced = true
                val entities = notificationList.map { it.toEntity(synced = true) }
                dao.insertNotifications(entities)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
} 