package com.example.tumbuhnyata.data.local

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

object TokenManager {
    private const val PREFS_NAME = "auth_prefs"
    private const val KEY_ACCESS_TOKEN = "ACCESS_TOKEN"

    private fun getPrefs(context: Context) = EncryptedSharedPreferences.create(
        PREFS_NAME,
        MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveToken(context: Context, token: String) {
        getPrefs(context).edit()
            .putString(KEY_ACCESS_TOKEN, token)
            .apply()
    }

    fun getToken(context: Context): String? =
        getPrefs(context).getString(KEY_ACCESS_TOKEN, null)

    fun clearToken(context: Context) {
        getPrefs(context).edit()
            .remove(KEY_ACCESS_TOKEN)
            .apply()
    }
}
