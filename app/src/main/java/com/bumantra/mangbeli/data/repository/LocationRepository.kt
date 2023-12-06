package com.bumantra.mangbeli.data.repository

import com.bumantra.mangbeli.data.local.entity.UserEntity
import com.bumantra.mangbeli.data.local.pref.UserPref
import com.bumantra.mangbeli.data.local.room.UserDao
import com.bumantra.mangbeli.data.local.room.UserDatabase
import com.bumantra.mangbeli.data.remote.network.ApiService

class LocationRepository(private val database: UserDatabase) {
    private val userLocationDao = database.userDao()
    suspend fun insertLocation(location: UserEntity) {
        userLocationDao.insertLocation(location)
    }

    suspend fun getLastLocation(): UserEntity? {
        return userLocationDao.getLastLocation()
    }

    companion object {
        @Volatile
        private var instance: LocationRepository? = null
        fun getInstance(
            database: UserDatabase
        ): LocationRepository =
            instance ?: synchronized(this) {
                instance ?: LocationRepository(database)
            }.also { instance = it }
    }
}