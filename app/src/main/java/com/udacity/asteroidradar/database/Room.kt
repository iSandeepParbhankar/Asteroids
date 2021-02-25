package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Query("select * from asteroid")
    fun getAsteroids(): LiveData<List<Asteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: Asteroid)

    @Query("SELECT * FROM asteroid ORDER BY date(:forDate)")
    fun getAsteroids(forDate: String): LiveData<List<Asteroid>>

}

@Database(entities = [Asteroid::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

//Singleton implementation to access single instance of database.
private lateinit var INSTANCE: AsteroidDatabase

fun getDatabase(context: Context): AsteroidDatabase {

    if (!::INSTANCE.isInitialized) {
        synchronized(AsteroidDatabase::class.java) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext, AsteroidDatabase::class.java,
                "asteroid_database"
            ).build()
        }
    }

    return INSTANCE
}