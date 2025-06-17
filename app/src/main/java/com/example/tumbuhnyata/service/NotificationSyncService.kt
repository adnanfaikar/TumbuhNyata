package com.example.tumbuhnyata.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.tumbuhnyata.di.NetworkModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class NotificationSyncService : Service() {
    
    private val serviceJob = Job()
    private val serviceScope = CoroutineScope(Dispatchers.IO + serviceJob)
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val userId = intent?.getStringExtra("USER_ID")
        
        if (userId != null) {
            serviceScope.launch {
                try {
                    // Sync unsynced notifications
                    NetworkModule.notificationRepository.syncUnsyncedNotifications()
                    
                    // Fetch latest notifications
                    NetworkModule.notificationRepository.fetchNotifications(userId)
                } catch (e: Exception) {
                    // Log error
                } finally {
                    stopSelf(startId)
                }
            }
        } else {
            stopSelf(startId)
        }
        
        return START_NOT_STICKY
    }
    
    override fun onDestroy() {
        super.onDestroy()
        serviceJob.cancel()
    }
} 