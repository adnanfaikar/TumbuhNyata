package com.example.tumbuhnyata.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {
    
    @Query("SELECT * FROM notifications WHERE user_id = :userId ORDER BY created_at DESC")
    fun getNotificationsByUserId(userId: String): Flow<List<NotificationEntity>>
    
    @Query("SELECT * FROM notifications WHERE user_id = :userId ORDER BY created_at DESC")
    suspend fun getNotificationsByUserIdSync(userId: String): List<NotificationEntity>
    
    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: Int): NotificationEntity?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)
    
    @Update
    suspend fun updateNotification(notification: NotificationEntity)
    
    @Query("UPDATE notifications SET is_read = 1 WHERE id = :id")
    suspend fun markAsRead(id: Int)
    
    @Query("DELETE FROM notifications WHERE id = :id")
    suspend fun deleteNotification(id: Int)
    
    @Query("DELETE FROM notifications WHERE user_id = :userId")
    suspend fun deleteAllNotificationsByUserId(userId: String)
    
    @Query("DELETE FROM notifications")
    suspend fun deleteAllNotifications()
    
    @Query("SELECT COUNT(*) FROM notifications WHERE user_id = :userId AND is_read = 0")
    suspend fun getUnreadCount(userId: String): Int
    
    @Query("SELECT COUNT(*) FROM notifications WHERE user_id = :userId AND is_read = 0")
    fun getUnreadCountFlow(userId: String): Flow<Int>
    
    @Query("SELECT * FROM notifications WHERE synced = 0")
    suspend fun getUnsyncedNotifications(): List<NotificationEntity>
    
    @Query("UPDATE notifications SET synced = 1 WHERE id = :id")
    suspend fun markAsSynced(id: Int)
} 