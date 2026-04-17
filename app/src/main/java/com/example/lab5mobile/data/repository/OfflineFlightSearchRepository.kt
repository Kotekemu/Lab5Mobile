package com.example.lab5mobile.data.repository

import com.example.lab5mobile.data.local.dao.AirportDao
import com.example.lab5mobile.data.local.dao.FavoriteDao
import com.example.lab5mobile.data.local.entity.FavoriteEntity
import com.example.lab5mobile.domain.model.AirportSuggestion
import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.model.Flight
import com.example.lab5mobile.domain.repository.FlightSearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class OfflineFlightSearchRepository(
    private val airportDao: AirportDao,
    private val favoriteDao: FavoriteDao
) : FlightSearchRepository {
    override fun getAirportSuggestions(query: String): Flow<List<AirportSuggestion>> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) {
            return flowOf(emptyList())
        }
        return airportDao.getAirportSuggestions(normalizedQuery)
            .map { airports ->
                airports.map { airport ->
                    AirportSuggestion(
                        iataCode = airport.iataCode,
                        name = airport.name
                    )
                }
            }
    }

    override fun getFlightsFromAirport(departureCode: String): Flow<List<Flight>> {
        val normalizedDepartureCode = departureCode.trim().uppercase()
        if (normalizedDepartureCode.isBlank()) {
            return flowOf(emptyList())
        }
        return combine(
            flow { emit(airportDao.getAirportByIataCode(normalizedDepartureCode)) },
            airportDao.getDestinationAirports(normalizedDepartureCode),
            favoriteDao.getAllFavorites()
        ) { departureAirport, destinationAirports, favorites ->
            if (departureAirport == null) {
                emptyList()
            } else {
                val favoriteSet = favorites
                    .map { favorite ->
                        favorite.departureCode.uppercase() to favorite.destinationCode.uppercase()
                    }
                    .toSet()
                destinationAirports.map { destinationAirport ->
                    val destinationCode = destinationAirport.iataCode.uppercase()
                    val routeKey = normalizedDepartureCode to destinationCode
                    Flight(
                        flightId = "$normalizedDepartureCode-$destinationCode",
                        departureCode = departureAirport.iataCode,
                        departureName = departureAirport.name,
                        destinationCode = destinationAirport.iataCode,
                        destinationName = destinationAirport.name,
                        isFavorite = routeKey in favoriteSet
                    )
                }
            }
        }
    }

    override fun getFavoriteRoutes(): Flow<List<FavoriteRoute>> {
        return favoriteDao.getFavoriteRoutesDetailed()
            .map { routes ->
                routes.map { route ->
                    FavoriteRoute(
                        departureCode = route.departureCode,
                        departureName = route.departureName,
                        destinationCode = route.destinationCode,
                        destinationName = route.destinationName
                    )
                }
            }
    }

    override suspend fun addFavorite(departureCode: String, destinationCode: String) {
        val normalizedDepartureCode = departureCode.trim().uppercase()
        val normalizedDestinationCode = destinationCode.trim().uppercase()
        val count = favoriteDao.getFavoriteCount(
            departureCode = normalizedDepartureCode,
            destinationCode = normalizedDestinationCode
        )
        if (count == 0) {
            favoriteDao.insertFavorite(
                FavoriteEntity(
                    departureCode = normalizedDepartureCode,
                    destinationCode = normalizedDestinationCode
                )
            )
        }
    }

    override suspend fun removeFavorite(departureCode: String, destinationCode: String) {
        favoriteDao.deleteFavoriteByCodes(
            departureCode = departureCode.trim().uppercase(),
            destinationCode = destinationCode.trim().uppercase()
        )
    }
}