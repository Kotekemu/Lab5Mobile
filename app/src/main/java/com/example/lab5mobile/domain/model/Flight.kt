package com.example.lab5mobile.domain.model

data class Flight(
    val flightId: String,
    val departureCode: String,
    val departureName: String,
    val destinationCode: String,
    val destinationName: String,
    val isFavorite: Boolean
)