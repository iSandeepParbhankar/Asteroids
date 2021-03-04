package com.udacity.asteroidradar.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
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

    private var varGetListFor = MutableLiveData(Constants.GetListFor.WEEK)

    val asteroidsList = Transformations.switchMap(varGetListFor) {
        it?.let {
            when (it) {
                Constants.GetListFor.WEEK -> {
                    database.asteroidDao.getWeeklyAsteroids(startDate(), endDate())
                }
                Constants.GetListFor.TODAY -> {
                    database.asteroidDao.getTodayAsteroids(startDate())
                }
                Constants.GetListFor.SAVED -> {
                    database.asteroidDao.getAsteroids()
                }
            }
        }
    }

    fun setGetListFor(getListFor: Constants.GetListFor){
        varGetListFor.postValue(getListFor)
    }

    var mAsteroid = MutableLiveData<List<Asteroid>>()

    suspend fun refreshAsteroidList() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = NasaApi.retrofitService.getAsteroidListAsync(
                        startDate = startDate(),
                        endDate = endDate()
                ).body()
                val result = parseAsteroidsJsonResult(JSONObject(asteroids))
                database.asteroidDao.insertAll(*result.asModel())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}