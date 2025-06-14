package com.example.tumbuhnyata.data.local.dao

import androidx.room.*
import com.example.tumbuhnyata.data.local.entity.OfflineProfile

@Dao
interface OfflineProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(profile: OfflineProfile)

    @Update
    fun update(profile: OfflineProfile)

    @Delete
    fun delete(profile: OfflineProfile)

    @Query("SELECT * FROM offline_profiles")
    fun getAllProfile(): List<OfflineProfile>

    @Query("SELECT * FROM offline_profiles WHERE isSynced = 0")
    fun getUnsyncedProfile(): List<OfflineProfile>

    @Query("SELECT * FROM offline_profiles WHERE id = :id")
    fun getProfileById(id: Int): OfflineProfile?

    @Query("SELECT * FROM offline_profiles ORDER BY timestamp DESC LIMIT 1")
    fun getLatestProfile(): OfflineProfile?

    @Query("DELETE FROM offline_profiles")
    fun deleteAllProfiles()
}