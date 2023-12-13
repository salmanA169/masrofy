package com.masrofy.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.masrofy.data.entity.AutomatedBackupEntity

@Dao
interface AutomatedBackupDao {
    @Query("SELECT * FROM AUTOMATEDBACKUPENTITY")
    suspend fun getAutomatedBackup():List<AutomatedBackupEntity>

    @Upsert
    suspend fun upsertAutomatedBackup(automatedBackupEntity: AutomatedBackupEntity)
}