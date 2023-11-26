package com.masrofy.core.backup

interface BackupEventListener {
    fun onBackup()
    fun onImport()
    fun onFinish()
}