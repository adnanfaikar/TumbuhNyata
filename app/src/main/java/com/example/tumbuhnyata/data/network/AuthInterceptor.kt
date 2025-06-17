package com.example.tumbuhnyata.data.network

import android.content.Context
import com.example.tumbuhnyata.util.TokenManager
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        val token = TokenManager.getToken(context)
        
        println("AuthInterceptor: === TOKEN DEBUG ===")
        println("AuthInterceptor: Token retrieved: ${if (token != null) "Present (${token.take(20)}...)" else "NULL"}")
        
        token?.let {
            println("AuthInterceptor: Adding Authorization header: Bearer ${it.take(20)}...")
            requestBuilder.addHeader("Authorization", "Bearer $it")
        } ?: run {
            println("AuthInterceptor: ⚠️ NO TOKEN FOUND - Request will be sent without authorization")
        }
        
        val request = requestBuilder.build()
        println("AuthInterceptor: Request headers:")
        request.headers.forEach { (name, value) ->
            if (name.equals("Authorization", ignoreCase = true)) {
                println("AuthInterceptor:   $name: ${value.take(30)}...")
            } else {
                println("AuthInterceptor:   $name: $value")
            }
        }
        
        return chain.proceed(request)
    }
} 