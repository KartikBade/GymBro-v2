package com.example.gymbro_v2.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.*
import com.example.gymbro_v2.repository.UserRepository
import com.example.gymbro_v2.workmanager.BackupWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application,
    private val userRepository: UserRepository
): ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    fun setWorkerMode(state: Int) {
        when (state) {
            0 -> {
                workManager.cancelUniqueWork("Backup")
            }
            1 -> {
                workManager.cancelUniqueWork("Backup")
                val backupWorkRequestBuilder = PeriodicWorkRequestBuilder<BackupWorker>(24, TimeUnit.HOURS)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )

                workManager.enqueueUniquePeriodicWork("Backup", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, backupWorkRequestBuilder.build())
            }
            2 -> {
                workManager.cancelUniqueWork("Backup")
                val backupWorkRequestBuilder = PeriodicWorkRequestBuilder<BackupWorker>(168, TimeUnit.HOURS)
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )

                workManager.enqueueUniquePeriodicWork("Backup", ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE, backupWorkRequestBuilder.build())
            }
            3 -> {
                workManager.cancelUniqueWork("Backup")
                val backupWorkRequestBuilder = OneTimeWorkRequestBuilder<BackupWorker>()
                    .setConstraints(
                        Constraints.Builder()
                            .setRequiredNetworkType(NetworkType.CONNECTED)
                            .build()
                    )

                workManager.enqueueUniqueWork("Backup", ExistingWorkPolicy.REPLACE, backupWorkRequestBuilder.build())
            }
        }
    }

    fun importData() {
        viewModelScope.launch {
            userRepository.dataImport()
        }
    }
}