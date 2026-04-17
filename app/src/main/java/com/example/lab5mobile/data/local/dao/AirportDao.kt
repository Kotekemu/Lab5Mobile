package com.example.lab5mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.example.lab5mobile.data.local.entity.AirportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AirportDao {
    @Query(
        """
        SELECT *
        FROM airport
        WHERE UPPER(iata_code) LIKE '%' || UPPER(:query) || '%'
           OR UPPER(name) LIKE '%' || UPPER(:query) || '%'
        ORDER BY passengers DESC
        LIMIT 10
        """
    )
    fun getAirportSuggestions(query: String): Flow<List<AirportEntity>>

    @Query(
        """
        SELECT *
        FROM airport
        WHERE UPPER(iata_code) = UPPER(:iataCode)
        LIMIT 1
        """
    )
    suspend fun getAirportByIataCode(iataCode: String): AirportEntity?

    @Query(
        """
        SELECT *
        FROM airport
        WHERE UPPER(iata_code) != UPPER(:departureCode)
        ORDER BY passengers DESC
        """
    )
    fun getDestinationAirports(departureCode: String): Flow<List<AirportEntity>>
}