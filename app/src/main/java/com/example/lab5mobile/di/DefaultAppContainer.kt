package com.example.lab5mobile.di

import android.content.Context
import androidx.room.Room
import com.example.lab5mobile.data.local.database.FlightSearchDatabase
import com.example.lab5mobile.data.preferences.SearchPreferencesRepository
import com.example.lab5mobile.data.repository.OfflineFlightSearchRepository
import com.example.lab5mobile.domain.repository.FlightSearchRepository

class DefaultAppContainer(
    private val context: Context
) : AppContainer {

    private val database: FlightSearchDatabase by lazy {
        Room.databaseBuilder(
            context.applicationContext,
            FlightSearchDatabase::class.java,
            DATABASE_NAME
        )
            .createFromAsset(DATABASE_ASSET_PATH)
            .build()
    }

    override val flightSearchRepository: FlightSearchRepository by lazy {
        OfflineFlightSearchRepository(
            airportDao = database.airportDao(),
            favoriteDao = database.favoriteDao()
        )
    }

    override val searchPreferencesRepository: SearchPreferencesRepository by lazy {
        SearchPreferencesRepository(context.applicationContext)
    }

    private companion object {
        const val DATABASE_NAME = "flight_search.db"
        const val DATABASE_ASSET_PATH = "flight_search.db"
    }
}