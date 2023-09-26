package com.example.gymbro_v2.broadcastreceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import androidx.work.WorkManager
import com.example.gymbro_v2.repository.UserRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImportNotificationReceiver: BroadcastReceiver() {

    @Inject lateinit var userRepository: UserRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            val workManager = WorkManager.getInstance(it)
            workManager.cancelUniqueWork("Import")

            CoroutineScope(Dispatchers.IO).launch {
                userRepository.deleteDatabase()
            }
            NotificationManagerCompat.from(context).cancel(58)
        }
    }
}