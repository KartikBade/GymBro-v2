package com.example.gymbro_v2.workmanager

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gymbro_v2.GymBroApp
import com.example.gymbro_v2.R
import com.example.gymbro_v2.broadcastreceiver.BackupNotificationReceiver
import com.example.gymbro_v2.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltWorker
class BackupWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    val userRepository: UserRepository
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {

        val cancelNotificationIntent = Intent(context, BackupNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, cancelNotificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, GymBroApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_backup_24)
            .setContentTitle("GymBro Data Backup")
            .setContentText("Backup is in progress...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.baseline_backup_24, "Cancel", pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)
            .setColor(Color.GREEN)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify(48, notificationBuilder.build())
        }

        userRepository.dataBackup()

        return Result.success()
    }
}