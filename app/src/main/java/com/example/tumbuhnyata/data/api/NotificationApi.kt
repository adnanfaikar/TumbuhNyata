package com.example.tumbuhnyata.data.api

import com.example.tumbuhnyata.data.model.Notification
import retrofit2.Response
import retrofit2.http.*

interface NotificationApi {
    @GET("notifications/{userId}")
    suspend fun getNotifications(@Path("userId") userId: String): Response<List<Notification>>

    @POST("notifications")
    suspend fun createNotification(
        @Body notification: Notification
    ): Response<Notification>

    @PATCH("notifications/{id}/read")
    suspend fun markAsRead(@Path("id") id: Int): Response<Unit>

    @DELETE("notifications/{id}")
    suspend fun deleteNotification(@Path("id") id: Int): Response<Unit>
} 