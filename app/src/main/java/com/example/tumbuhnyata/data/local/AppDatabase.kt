package com.example.tumbuhnyata.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tumbuhnyata.data.local.dao.CsrDraftDao
import com.example.tumbuhnyata.data.local.dao.CsrHistoryDao
import com.example.tumbuhnyata.data.local.dao.OfflineProfileDao
import com.example.tumbuhnyata.data.local.dao.OfflineWorkshopRegistrationDao
import com.example.tumbuhnyata.data.local.entity.CsrDraftEntity
import com.example.tumbuhnyata.data.local.entity.CsrHistoryEntity
import com.example.tumbuhnyata.data.local.entity.OfflineProfile
import com.example.tumbuhnyata.data.local.entity.OfflineWorkshopRegistration
import com.example.tumbuhnyata.data.local.dao.DashboardDao // DAO yang sudah kita buat
import com.example.tumbuhnyata.data.local.dao.CertificationDao // DAO untuk certification
import com.example.tumbuhnyata.data.local.entity.CsrReportEntity // Entity yang sudah kita buat
import com.example.tumbuhnyata.data.local.entity.CertificationEntity // Entity untuk certification
import com.example.tumbuhnyata.data.database.NotificationDao
import com.example.tumbuhnyata.data.database.NotificationEntity

@Database(
    entities = [
        OfflineWorkshopRegistration::class,
        OfflineProfile::class,
        CsrDraftEntity::class,
        CsrHistoryEntity::class,
        CsrReportEntity::class,
        CertificationEntity::class,
        NotificationEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun offlineWorkshopRegistrationDao(): OfflineWorkshopRegistrationDao
    abstract fun offlineProfileDao(): OfflineProfileDao
    abstract fun csrDraftDao(): CsrDraftDao
    abstract fun csrHistoryDao(): CsrHistoryDao
    abstract fun dashboardDao(): DashboardDao // Expose DashboardDao
    abstract fun certificationDao(): CertificationDao
    abstract fun notificationDao(): NotificationDao


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