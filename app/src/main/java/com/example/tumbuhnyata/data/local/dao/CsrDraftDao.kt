package com.example.tumbuhnyata.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tumbuhnyata.data.local.entity.CsrDraftEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CsrDraftDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCsrDraft(csrDraft: CsrDraftEntity): Long

    @Update
    suspend fun updateCsrDraft(csrDraft: CsrDraftEntity)

    @Delete
    suspend fun deleteCsrDraft(csrDraft: CsrDraftEntity)

    @Query("SELECT * FROM csr_drafts ORDER BY timestamp DESC")
    fun getAllCsrDrafts(): Flow<List<CsrDraftEntity>>

    @Query("SELECT * FROM csr_drafts WHERE id = :id")
    suspend fun getCsrDraftById(id: Long): CsrDraftEntity?
} 