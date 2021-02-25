package com.udacity.asteroidradar.utils

import com.udacity.asteroidradar.Constants.API_QUERY_DATE_FORMAT
import com.udacity.asteroidradar.Constants.DEFAULT_END_DATE_DAYS
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    /**
     * function provides string format current date
     */
    fun startDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, 0)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

    /**
     * function provides string format end date with diff. of 7 days from current date.
     */
    fun endDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, DEFAULT_END_DATE_DAYS)
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        return dateFormat.format(currentTime)
    }

}