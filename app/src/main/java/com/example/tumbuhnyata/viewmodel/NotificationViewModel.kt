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
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun getNotifications(userId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            try {
                val result = repository.getNotifications(userId)
                _notifications.value = result
                
                // Jika berhasil dan ada data, clear error
                if (result.isNotEmpty()) {
                    _error.value = null
                }
            } catch (e: Exception) {
                _error.value = "Gagal mengambil notifikasi. Menampilkan data lokal jika tersedia."
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createNotification(notification: Notification) {
        viewModelScope.launch {
            try {
                repository.createNotification(notification)
                // Refresh notifications after creating new one
                getNotifications(notification.userId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Gagal membuat notifikasi: ${e.message}"
                e.printStackTrace()
            }
        }
    }

    fun markAsRead(id: Int) {
        viewModelScope.launch {
            try {
                repository.markAsRead(id)
                
                // Update local state - mark notification as read
                _notifications.value = _notifications.value.map { 
                    if (it.id == id) it.copy(isRead = 1) else it 
                }
                
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Gagal menandai notifikasi sebagai dibaca"
                e.printStackTrace()
            }
        }
    }

    fun deleteNotification(id: Int) {
        viewModelScope.launch {
            try {
                repository.deleteNotification(id)
                
                // Remove notification from local state
                _notifications.value = _notifications.value.filter { it.id != id }
                
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Gagal menghapus notifikasi"
                e.printStackTrace()
            }
        }
    }
    
    fun clearError() {
        _error.value = null
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