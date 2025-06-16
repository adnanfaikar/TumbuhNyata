package com.example.tumbuhnyata.di

import com.example.tumbuhnyata.data.api.NotificationApi
import com.example.tumbuhnyata.data.api.ProfileApi
import com.example.tumbuhnyata.data.api.WorkshopApiService
import com.example.tumbuhnyata.data.network.AuthInterceptor
import com.example.tumbuhnyata.data.repository.NotificationRepository
import com.example.tumbuhnyata.data.repository.ProfileRepository
import com.example.tumbuhnyata.data.repository.WorkshopRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private val okHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // Menggunakan 10.0.2.2 untuk localhost pada emulator Android
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    val notificationApi: NotificationApi by lazy {
        retrofit.create(NotificationApi::class.java)
    }

    val notificationRepository: NotificationRepository by lazy {
        NotificationRepository(notificationApi)
    }
    
    val profileApi: ProfileApi by lazy {
        retrofit.create(ProfileApi::class.java)
    }
    
    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(profileApi)
    }

    val workshopApi: WorkshopApiService by lazy {
        retrofit.create(WorkshopApiService::class.java)
    }

    val workshopRepository: WorkshopRepository by lazy {
        WorkshopRepository(workshopApi)
    }
}