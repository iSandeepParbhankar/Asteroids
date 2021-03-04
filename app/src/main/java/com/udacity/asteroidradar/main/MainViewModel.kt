package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.NasaApi
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

/**
 * Simple ViewModel class
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = getDatabase(application)
    private val asteroidRepository = AsteroidRepository(database)

    //Variables to maintain api status
    private val _status = MutableLiveData<Constants.ApiStatus>()
    val status: LiveData<Constants.ApiStatus>
        get() = _status

    //Variable to for picture of the day
    private val _pictureOfTheDay = MutableLiveData<PictureOfDay>()
    val pictureOfTheDay: LiveData<PictureOfDay>
        get() = _pictureOfTheDay

    private val _asteroids = MutableLiveData<List<Asteroid>>()
    val asteroids: LiveData<List<Asteroid>>
        get() = asteroidRepository.asteroidsList

    init {
        getPictureOfTheDay()
        //getAsteroids()
        viewModelScope.launch {
            asteroidRepository.refreshAsteroidList()
        }
    }


    //Api call to get picture of the day
    private fun getPictureOfTheDay() {
        viewModelScope.launch {
            _status.value = Constants.ApiStatus.LOADING
            try {
                val result = NasaApi.retrofitService.getPictureOfTheDay()
                _pictureOfTheDay.value = result
                _status.value = Constants.ApiStatus.DONE
            } catch (e: Exception) {
                _status.value = Constants.ApiStatus.ERROR
                _pictureOfTheDay.value = PictureOfDay("", "", "")
            }
        }
    }

    //ViewModel factory
    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

    //Api call to get list of asteroid with provided date range
    //were the date range is dependent on type of request i.e. week, today, saved.
    fun getAsteroids(getListFor: Constants.GetListFor) {
        asteroidRepository.setGetListFor(getListFor)
    }

}