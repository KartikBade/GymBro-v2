package com.example.gymbro_v2.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager

class BackupNotificationReceiver: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val workManager = WorkManager.getInstance(it)
            workManager.cancelUniqueWork("Backup")

            NotificationManagerCompat.from(context).cancel(48)
        }
    }
}