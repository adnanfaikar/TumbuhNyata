package com.example.tumbuhnyata.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tumbuhnyata.data.model.Notification
import com.example.tumbuhnyata.data.repository.NotificationRepository
import com.example.tumbuhnyata.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NotificationViewModel(
    private val repository: NotificationRepository
) : ViewModel() {

    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun getNotifications(userId: String) {
        viewModelScope.launch {
            try {
                val result = repository.getNotifications(userId)
                _notifications.value = result
            } catch (e: Exception) {
                _error.value = "Gagal mengambil notifikasi: ${e.message}"
            }
        }
    }

    fun createNotification(notification: Notification) {
        viewModelScope.launch {
            try {
                repository.createNotification(notification)
                // Refresh notifications after creating new one
                getNotifications(notification.userId)
            } catch (e: Exception) {
                _error.value = "Gagal membuat notifikasi: ${e.message}"
            }
        }
    }

    fun markAsRead(id: Int) {
        viewModelScope.launch {
            try {
                val success = repository.markAsRead(id)
                if (success) {
                    // Update local state - mark notification as read
                    _notifications.value = _notifications.value.map { 
                        if (it.id == id) it.copy(isRead = true) else it 
                    }
                }
            } catch (e: Exception) {
                _error.value = "Gagal menandai notifikasi: ${e.message}"
            }
        }
    }

    fun deleteNotification(id: Int) {
        viewModelScope.launch {
            try {
                val success = repository.deleteNotification(id)
                if (success) {
                    // Remove notification from local state
                    _notifications.value = _notifications.value.filter { it.id != id }
                }
            } catch (e: Exception) {
                _error.value = "Gagal menghapus notifikasi: ${e.message}"
            }
        }
    }
    
    // Factory pattern untuk membuat ViewModel
    class Factory : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(NotificationViewModel::class.java)) {
                return NotificationViewModel(NetworkModule.notificationRepository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
} 