package com.example.lab5mobile.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.lab5mobile.data.local.entity.FavoriteEntity
import com.example.lab5mobile.data.local.model.FavoriteRouteInfo
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {
    @Query(
        """
        SELECT *
        FROM favorite
        ORDER BY id DESC
        """
    )
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Query(
        """
        SELECT
            f.departure_code AS departureCode,
            COALESCE(dep.name, f.departure_code) AS departureName,
            f.destination_code AS destinationCode,
            COALESCE(dest.name, f.destination_code) AS destinationName
        FROM favorite f
        LEFT JOIN airport dep ON dep.iata_code = f.departure_code
        LEFT JOIN airport dest ON dest.iata_code = f.destination_code
        ORDER BY f.id DESC
        """
    )
    fun getFavoriteRoutesDetailed(): Flow<List<FavoriteRouteInfo>>

    @Query(
        """
        SELECT COUNT(*)
        FROM favorite
        WHERE UPPER(departure_code) = UPPER(:departureCode)
          AND UPPER(destination_code) = UPPER(:destinationCode)
        """
    )
    suspend fun getFavoriteCount(
        departureCode: String,
        destinationCode: String
    ): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favorite: FavoriteEntity)

    @Query(
        """
        DELETE FROM favorite
        WHERE UPPER(departure_code) = UPPER(:departureCode)
          AND UPPER(destination_code) = UPPER(:destinationCode)
        """
    )
    suspend fun deleteFavoriteByCodes(
        departureCode: String,
        destinationCode: String
    )
}