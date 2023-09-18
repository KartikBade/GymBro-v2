package com.example.gymbro_v2.workmanager

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.gymbro_v2.GymBroApp
import com.example.gymbro_v2.R
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

class BackupWorker(
    context: Context,
    workerParameters: WorkerParameters
): Worker(context, workerParameters) {

    override fun doWork(): Result {

        val notificationBuilder = NotificationCompat.Builder(applicationContext, GymBroApp.CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_backup_24)
            .setContentTitle("GymBro Data Backup")
            .setContentText("Backup is in progress...")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(applicationContext).notify(48, notificationBuilder.build())
        }

        return Result.success()
    }
}