package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.asModel
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.utils.DateUtils.endDate
import com.udacity.asteroidradar.utils.DateUtils.startDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    var asteroidsList: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it
        }

    suspend fun refreshAsteroidList() {
        withContext(Dispatchers.IO) {
            val asteroids = NasaApi.retrofitService.getAsteroidListAsync(
                startDate = startDate(),
                endDate = endDate()
            ).body()
            val result = parseAsteroidsJsonResult(JSONObject(asteroids))
            database.asteroidDao.insertAll(*result.asModel())
        }
    }

    suspend fun getAsteroidForToday() {
        withContext(Dispatchers.IO) {
            asteroidsList = database.asteroidDao.getAsteroids(startDate())
        }
    }

    suspend fun getAllAsteroids() {
        withContext(Dispatchers.IO) {
            asteroidsList = database.asteroidDao.getAsteroids()
        }
    }

}