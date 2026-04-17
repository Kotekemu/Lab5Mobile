package com.example.lab5mobile.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.lab5mobile.data.local.dao.AirportDao
import com.example.lab5mobile.data.local.dao.FavoriteDao
import com.example.lab5mobile.data.local.entity.AirportEntity
import com.example.lab5mobile.data.local.entity.FavoriteEntity

@Database(
    entities = [
        AirportEntity::class,
        FavoriteEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class FlightSearchDatabase : RoomDatabase() {
    abstract fun airportDao(): AirportDao
    abstract fun favoriteDao(): FavoriteDao
}