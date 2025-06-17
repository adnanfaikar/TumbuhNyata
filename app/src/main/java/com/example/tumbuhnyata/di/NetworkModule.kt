package com.example.tumbuhnyata.di

import com.example.tumbuhnyata.TumbuhNyataApp
import com.example.tumbuhnyata.data.api.NotificationApi
import com.example.tumbuhnyata.data.api.ProfileApi
import com.example.tumbuhnyata.data.api.WorkshopApiService
import com.example.tumbuhnyata.data.local.dao.OfflineProfileDao
import com.example.tumbuhnyata.data.local.dao.OfflineWorkshopRegistrationDao
import com.example.tumbuhnyata.data.api.CsrHistoryApi
import com.example.tumbuhnyata.data.network.AuthInterceptor
import com.example.tumbuhnyata.data.repository.NotificationRepository
import com.example.tumbuhnyata.data.repository.ProfileRepository
import com.example.tumbuhnyata.data.repository.WorkshopRepository
import com.example.tumbuhnyata.data.repository.CsrHistoryRepository
import com.example.tumbuhnyata.data.repository.CsrHistoryRepositoryOffline
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.lazy
import com.example.tumbuhnyata.data.local.AppDatabase
import com.example.tumbuhnyata.data.repository.OfflineProfileRepository
import com.example.tumbuhnyata.data.repository.OfflineWorkshopRepository

object NetworkModule {

    private val database by lazy {
        AppDatabase.getInstance(TumbuhNyataApp.appContext)
    }

    private val authInterceptor by lazy {
        AuthInterceptor(TumbuhNyataApp.appContext)
    }

    private val okHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
                            .baseUrl("http://10.0.2.2:5000/")
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

    val workshopApi: WorkshopApiService by lazy {
        retrofit.create(WorkshopApiService::class.java)
    }

    // Create offline repositories
    val offlineProfileRepository: OfflineProfileRepository by lazy {
        OfflineProfileRepository(database.offlineProfileDao(), profileApi)
    }

    val offlineWorkshopRepository: OfflineWorkshopRepository by lazy {
        OfflineWorkshopRepository(
            offlineWorkshopRegistrationDao = database.offlineWorkshopRegistrationDao(),
            workshopApiService = workshopApi
        )
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepository(
            profileApi,
            offlineProfileRepository = offlineProfileRepository
        )
    }

    val workshopRepository: WorkshopRepository by lazy {
        WorkshopRepository(
            api = workshopApi,
            apiProfile = profileApi,
            offlineWorkshopRepository = offlineWorkshopRepository
        )
    }

    val csrHistoryApi: CsrHistoryApi by lazy {
        retrofit.create(CsrHistoryApi::class.java)
    }

    val csrHistoryRepository: CsrHistoryRepository by lazy {
        CsrHistoryRepository(csrHistoryApi)
    }

    val csrHistoryRepositoryOffline: CsrHistoryRepositoryOffline by lazy {
        CsrHistoryRepositoryOffline(
            api = csrHistoryApi,
            dao = database.csrHistoryDao(),
            context = TumbuhNyataApp.appContext
        )
    }

}