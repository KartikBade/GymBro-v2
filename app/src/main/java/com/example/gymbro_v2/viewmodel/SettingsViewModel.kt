package com.example.gymbro_v2.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.gymbro_v2.workmanager.BackupWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    fun setWorkerMode(state: Int) {
        when (state) {
            0 -> {
                workManager.cancelAllWork()
            }
            1 -> {
                workManager.cancelAllWork()
                val backupWorkRequestBuilder = PeriodicWorkRequestBuilder<BackupWorker>(24, TimeUnit.HOURS)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )

                workManager.enqueue(backupWorkRequestBuilder.build())
            }
            2 -> {
                workManager.cancelAllWork()
                val backupWorkRequestBuilder = PeriodicWorkRequestBuilder<BackupWorker>(168, TimeUnit.HOURS)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )

                workManager.enqueue(backupWorkRequestBuilder.build())
            }
            3 -> {
                workManager.cancelAllWork()
                val backupWorkRequestBuilder = OneTimeWorkRequestBuilder<BackupWorker>()
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )

                workManager.enqueue(backupWorkRequestBuilder.build())
            }
        }
    }
}