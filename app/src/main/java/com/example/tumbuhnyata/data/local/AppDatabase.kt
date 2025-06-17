package com.example.tumbuhnyata.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tumbuhnyata.data.local.dao.CsrDraftDao
import com.example.tumbuhnyata.data.local.dao.OfflineProfileDao
import com.example.tumbuhnyata.data.local.dao.OfflineWorkshopRegistrationDao
import com.example.tumbuhnyata.data.local.entity.CsrDraftEntity
import com.example.tumbuhnyata.data.local.entity.OfflineProfile
import com.example.tumbuhnyata.data.local.entity.OfflineWorkshopRegistration

@Database(
    entities = [
        OfflineWorkshopRegistration::class,
        OfflineProfile::class,
        CsrDraftEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun offlineWorkshopRegistrationDao(): OfflineWorkshopRegistrationDao
    abstract fun offlineProfileDao(): OfflineProfileDao
    abstract fun csrDraftDao(): CsrDraftDao


    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tumbuh_nyata_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}