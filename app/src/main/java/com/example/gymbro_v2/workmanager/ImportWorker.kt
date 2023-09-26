package com.example.gymbro_v2.workmanager

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.gymbro_v2.GymBroApp
import com.example.gymbro_v2.R
import com.example.gymbro_v2.broadcastreceiver.BackupNotificationReceiver
import com.example.gymbro_v2.broadcastreceiver.ImportNotificationReceiver
import com.example.gymbro_v2.repository.UserRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class ImportWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParameters: WorkerParameters,
    val userRepository: UserRepository
): CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        val cancelNotificationIntent = Intent(context, ImportNotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, cancelNotificationIntent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, GymBroApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_backup_24)
            .setContentTitle("Importing Data")
            .setContentText("This may take a while...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .addAction(R.drawable.baseline_backup_24, "Cancel", pendingIntent)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        val successNotificationBuilder = NotificationCompat.Builder(applicationContext, GymBroApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_backup_24)
            .setContentTitle("Import Successful")
            .setContentText("Data was imported from your account successfully.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        val failureNotificationBuilder = NotificationCompat.Builder(applicationContext, GymBroApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_backup_24)
            .setContentTitle("Import Failed")
            .setContentText("Please try again after some time.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setOnlyAlertOnce(true)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify(58, notificationBuilder.build())
        }

        return if (userRepository.dataImport()) {
            NotificationManagerCompat.from(context).cancel(58)
            NotificationManagerCompat.from(applicationContext).notify(58, successNotificationBuilder.build())
            Result.success()
        } else {
            NotificationManagerCompat.from(context).cancel(58)
            NotificationManagerCompat.from(applicationContext).notify(58, failureNotificationBuilder.build())
            Result.failure()
        }
    }
}