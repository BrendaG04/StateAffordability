package com.example.stateaffordability.data

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SeedDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            val database = AppDatabase.getDatabase(applicationContext)
            val stateList = mutableListOf<StateAffordability>()

            applicationContext.assets.open("rent_vs_minwage.csv").bufferedReader().use { reader ->
                reader.readLines().drop(1).forEach { line ->
                    val tokens = line.split(',')

                    if (tokens.size >= 10) {
                        try {
                            val state = StateAffordability(
                                stateName = tokens[0].trim(),
                                minimumWage = tokens[9].toDouble(),
                                medianHousingCost = tokens[8].toDouble()
                            )
                            stateList.add(state)
                        } catch (e: NumberFormatException) {
                            Log.e("SeedDatabaseWorker", "Failed ", e)
                        }
                    }
                }
            }

            database.stateDao().insertAll(stateList)
            Log.d("SeedDatabaseWorker", "Successfully seeded ${stateList.size} states.")
            Result.success()

        } catch (ex: Exception) {
            Log.e("SeedDatabaseWorker", "Error", ex)
            Result.failure()
        }
    }
}
