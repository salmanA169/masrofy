package com.masrofy.core.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import androidx.core.app.NotificationCompat
import com.masrofy.R

class MasrofyNotification(context: Context) {

    private val notificationManager = context.getSystemService(NotificationManager::class.java)
    companion object {
        const val REMINDER_CHANNEL_ID = "reminder-channel-id"
        const val BACKUP_CHANNEL_ID = "backup-channel-id"
        fun createDefaultChannel(context: Context) {
            val notificationManager = context.getSystemService(NotificationManager::class.java)
            val reminderChannel = NotificationChannel(
                REMINDER_CHANNEL_ID,
                "Reminder Transactions",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "reminder for record transaction"
                enableVibration(true)
                lightColor = Color.BLUE
            }
            val backupChannel = NotificationChannel(
                BACKUP_CHANNEL_ID,
                "Backup channel",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "backup"
                enableVibration(true)
                lightColor = Color.BLUE
            }
            notificationManager.createNotificationChannels(listOf(reminderChannel, backupChannel))
        }
    }

    fun notifyBackup(
        progress: Int,
        context: Context,
        isBackup: Boolean = false
    ) {
        val backupNotification = NotificationCompat.Builder(context, BACKUP_CHANNEL_ID).apply {
            setSmallIcon(R.drawable.masrofy_notification_icon)
            setContentText("Drive Backup")
            setContentText(if (isBackup) "Backup successfully" else "Backing up...")
            if (!isBackup) {
                setProgress(100, progress, false)
            }
        }
        notificationManager.notify(1, backupNotification.build())
    }
}