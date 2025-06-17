package com.example.tumbuhnyata.util

import android.content.Context
import android.content.SharedPreferences

object UserSessionManager {
    private const val PREFS_NAME = "user_session_prefs"
    private const val USER_ID_KEY = "user_id"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserId(context: Context, userId: Int) {
        val editor = getPreferences(context).edit()
        editor.putInt(USER_ID_KEY, userId)
        editor.apply()
    }

    fun getUserId(context: Context): Int {
        return getPreferences(context).getInt(USER_ID_KEY, 1) // default 1 jika tidak ada
    }

    fun clearUserId(context: Context) {
        val editor = getPreferences(context).edit()
        editor.remove(USER_ID_KEY)
        editor.apply()
    }

    fun hasUserId(context: Context): Boolean {
        return getPreferences(context).contains(USER_ID_KEY)
    }
} 