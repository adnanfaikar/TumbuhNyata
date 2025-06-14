package com.example.tumbuhnyata.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tumbuhnyata.data.local.dao.OfflineProfileDao
import com.example.tumbuhnyata.data.local.dao.OfflineWorkshopRegistrationDao
import com.example.tumbuhnyata.data.local.entity.OfflineProfile
import com.example.tumbuhnyata.data.local.entity.OfflineWorkshopRegistration

@Database(
    entities = [
        OfflineWorkshopRegistration::class,
        OfflineProfile::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun offlineWorkshopRegistrationDao(): OfflineWorkshopRegistrationDao
    abstract fun offlineProfileDao(): OfflineProfileDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tumbuh_nyata_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}