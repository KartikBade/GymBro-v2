package com.example.gymbro_v2.viewmodel

import android.app.AlertDialog
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.gymbro_v2.workmanager.BackupWorker
import com.example.gymbro_v2.workmanager.ImportWorker
import dagger.hilt.android.lifecycle.HiltViewModel
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
                if (!isConnected(context)) {
                    showAlertDialog(context)
                }
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
                if (!isConnected(context)) {
                    showAlertDialog(context)
                }
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
                if (!isConnected(context)) {
                    showAlertDialog(context)
                }
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
        if (!isConnected(context)) {
            showAlertDialog(context)
        }
    }

    private fun showAlertDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("Process Pending")
            .setMessage("The process will resume once the device is connected to the internet.")
            .setNeutralButton("Ok") { dialog, _ ->
                dialog.cancel()
            }
            .create()
            .show()
    }

    private fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        if (network != null) {
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            if (networkCapabilities != null) {
                return networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        }

        return false
    }
}