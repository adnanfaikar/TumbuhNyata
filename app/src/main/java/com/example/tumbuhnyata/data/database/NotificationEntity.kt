package com.example.tumbuhnyata.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tumbuhnyata.data.model.Notification

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    
    @ColumnInfo(name = "user_id")
    val userId: String,
    
    @ColumnInfo(name = "title")
    val title: String,
    
    @ColumnInfo(name = "message")
    val message: String,
    
    @ColumnInfo(name = "is_read")
    val isRead: Int = 0,  // Menggunakan Int seperti di model Notification
    
    @ColumnInfo(name = "created_at")
    val createdAt: String,
    
    @ColumnInfo(name = "synced")
    val synced: Int = 0  // Menggunakan Int untuk konsistensi dengan database (0 = false, 1 = true)
)

// Extension functions untuk konversi
fun NotificationEntity.toNotification(): Notification {
    return Notification(
        id = this.id,
        userId = this.userId,
        title = this.title,
        message = this.message,
        isRead = this.isRead,  // Keduanya sekarang menggunakan Int
        createdAt = this.createdAt
    )
}

fun Notification.toEntity(synced: Boolean = false): NotificationEntity {
    return NotificationEntity(
        id = this.id,
        userId = this.userId,
        title = this.title,
        message = this.message,
        isRead = this.isRead,  // Keduanya sekarang menggunakan Int
        createdAt = this.createdAt,
        synced = if (synced) 1 else 0  // Konversi Boolean ke Int
    )
} 