package com.masrofy.repository.back_repository

interface BackupRepository {
    suspend fun backup(fileName:String)
    suspend fun restoreBackup()
}