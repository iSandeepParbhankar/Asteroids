package com.udacity.asteroidradar

object Constants {
    const val BASE_URL = "https://api.nasa.gov/"
    const val API_KEY = "DJmx4phUbdO5pa4rfnCpeAyYbEnhhHyH70dDLJDn"
    const val API_QUERY_DATE_FORMAT = "yyyy-MM-dd"  //Note: YYYY changed to yyyy to avoid version check error at NetworkUtils.
    const val DEFAULT_END_DATE_DAYS = 7

    //region API Status
    enum class ApiStatus { LOADING, ERROR, DONE }
    //endregion

    //region API request type
    enum class GetListFor{
        WEEK,
        TODAY,
        SAVED
    }
    //endregion

    //region Picture of the day
    const val IMAGE_OF_THE_DAY = "/planetary/apod?api_key=$API_KEY"
    //endregion

    //region asteroid API
    const val ASTEROID_API = "neo/rest/v1/feed"
    //endregion
}