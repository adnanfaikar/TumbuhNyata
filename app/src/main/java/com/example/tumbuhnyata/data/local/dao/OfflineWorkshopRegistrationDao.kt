package com.example.tumbuhnyata.data.local.dao

import androidx.room.*
import com.example.tumbuhnyata.data.local.entity.OfflineWorkshopRegistration

@Dao
interface OfflineWorkshopRegistrationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(registration: OfflineWorkshopRegistration)

    @Update
    fun update(registration: OfflineWorkshopRegistration)

    @Delete
    fun delete(registration: OfflineWorkshopRegistration)

    @Query("SELECT * FROM offline_workshop_registrations WHERE isSynced = 0")
    fun getUnsyncedRegistrations(): kotlin.collections.List<OfflineWorkshopRegistration>

    @Query("SELECT * FROM offline_workshop_registrations")
    fun getAllRegistrations(): kotlin.collections.List<OfflineWorkshopRegistration>

    @Query("SELECT * FROM offline_workshop_registrations WHERE id = :id")
    fun getRegistrationById(id: String): OfflineWorkshopRegistration?
} 