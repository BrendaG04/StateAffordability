package com.example.stateaffordability

import android.app.Application
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.stateaffordability.data.SeedDatabaseWorker

class StateAffordabilityApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        val workRequest = OneTimeWorkRequestBuilder<SeedDatabaseWorker>().build()
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}
    