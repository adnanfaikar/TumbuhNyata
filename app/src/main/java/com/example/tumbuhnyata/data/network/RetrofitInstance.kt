package com.example.tumbuhnyata.data.network

import android.content.Context
import com.example.tumbuhnyata.data.api.AuthApi
import com.example.tumbuhnyata.data.api.DashboardApiService
import com.example.tumbuhnyata.data.api.CertificationApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private fun createClientWithAuth(context: Context): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(context))
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val api: AuthApi by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)
    }

    val dashboardApi: DashboardApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DashboardApiService::class.java)
    }

    // NEW: Certification API WITHOUT authentication (like dashboard)
    val certificationApiNoAuth: CertificationApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .client(client) // Uses client WITHOUT AuthInterceptor
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CertificationApiService::class.java)
    }

    fun createCertificationApi(context: Context): CertificationApiService {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .client(createClientWithAuth(context))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CertificationApiService::class.java)
    }

    val certificationApi: CertificationApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CertificationApiService::class.java)
    }
}
