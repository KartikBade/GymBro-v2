package com.example.gymbro_v2.viewmodel

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.gymbro_v2.workmanager.BackupWorker
import com.example.gymbro_v2.workmanager.ImportWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val application: Application
): ViewModel() {

    private val workManager = WorkManager.getInstance(application)

    fun setWorkerMode(state: Int, context: Context) {
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
                checkWorkerStatus("Backup", context)
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
                checkWorkerStatus("Backup", context)
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
                checkWorkerStatus("Backup", context)
            }
        }
    }

    fun importData(context: Context) {
        workManager.cancelUniqueWork("Import")
        val importWorkRequestBuilder = OneTimeWorkRequestBuilder<ImportWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )

        workManager.enqueueUniqueWork("Import", ExistingWorkPolicy.REPLACE, importWorkRequestBuilder.build())
        checkWorkerStatus("Import", context)
    }

    private fun checkWorkerStatus(uniqueWorkName: String, context: Context) {
        val statuses = workManager.getWorkInfosForUniqueWork(uniqueWorkName)
        var workInfoList: List<WorkInfo> = emptyList()
        try {
            workInfoList = statuses.get()
        } catch (e: ExecutionException) {
            Log.d("SettingsViewModel", "ExecutionException in isWorkScheduled: $e")
        } catch (e: InterruptedException) {
            Log.d("SettingsViewModel", "InterruptedException in isWorkScheduled: $e")
        }
        for (workInfo in workInfoList) {
            val state = workInfo.state
            if (state == WorkInfo.State.ENQUEUED) {
                AlertDialog.Builder(context)
                    .setTitle("$uniqueWorkName Process Pending")
                    .setMessage("The process will resume once the device is connected to the internet.")
                    .setNeutralButton("Ok") { dialog, _ ->
                        dialog.cancel()
                    }
                    .create()
                    .show()
            }
        }
    }
}