package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidRepository
import retrofit2.HttpException

class AsteroidRefreshWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext = appContext, params = params) {
    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = AsteroidRepository(database)

        return try {
            repository.refreshAsteroidList()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

}