package com.example.tumbuhnyata.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tumbuhnyata.data.local.dao.DashboardDao // DAO yang sudah kita buat
import com.example.tumbuhnyata.data.local.dao.CertificationDao // DAO untuk certification
import com.example.tumbuhnyata.data.local.entity.CsrReportEntity // Entity yang sudah kita buat
import com.example.tumbuhnyata.data.local.entity.CertificationEntity // Entity untuk certification

/**
 * Main database class for the application using Room.
 * It should list all entities and provide access to DAOs.
 */
@Database(
    entities = [
        CsrReportEntity::class,
        CertificationEntity::class
        // Tambahkan entitas lain di sini jika ada, contoh: UserEntity::class, NotificationEntity::class
    ],
    version = 4, // Updated version for CertificationEntity
    exportSchema = false // Set true jika ingin mengekspor skema ke file JSON (untuk version control skema)
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun dashboardDao(): DashboardDao // Expose DashboardDao
    abstract fun certificationDao(): CertificationDao // Expose CertificationDao
    // abstract fun userDao(): UserDao // Contoh jika ada DAO lain

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "tumbuh_nyata_database" // Nama file database
                )
                // .addMigrations(MIGRATION_1_2) // Tambahkan migrasi jika ada perubahan skema
                .fallbackToDestructiveMigration() // Hati-hati: ini akan menghapus dan membuat ulang DB jika skema berubah dan tidak ada migrasi
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
} 