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
            .setContentTitle("Backing up data")
            .setContentText("This may take a while...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.baseline_backup_24, "Cancel", pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        val successNotificationBuilder = NotificationCompat.Builder(applicationContext, GymBroApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_backup_24)
            .setContentTitle("Backup Successful")
            .setContentText("Data has been uploaded to your account.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        val failureNotificationBuilder = NotificationCompat.Builder(applicationContext, GymBroApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_backup_24)
            .setContentTitle("Backup Failed")
            .setContentText("Something went wrong.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify(48, notificationBuilder.build())
        }

        return if (userRepository.dataBackup()) {
            NotificationManagerCompat.from(context).cancel(48)
            NotificationManagerCompat.from(applicationContext).notify(48, successNotificationBuilder.build())
            Result.success()
        } else {
            NotificationManagerCompat.from(context).cancel(48)
            NotificationManagerCompat.from(applicationContext).notify(48, failureNotificationBuilder.build())
            Result.failure()
        }
    }
}