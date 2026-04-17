package com.example.lab5mobile.domain.repository

import com.example.lab5mobile.domain.model.AirportSuggestion
import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.model.Flight
import kotlinx.coroutines.flow.Flow

interface FlightSearchRepository {
    fun getAirportSuggestions(query: String): Flow<List<AirportSuggestion>>

    fun getFlightsFromAirport(departureCode: String): Flow<List<Flight>>

    fun getFavoriteRoutes(): Flow<List<FavoriteRoute>>

    suspend fun addFavorite(departureCode: String, destinationCode: String)

    suspend fun removeFavorite(departureCode: String, destinationCode: String)
}