package com.example.lab5mobile.di

import com.example.lab5mobile.data.preferences.SearchPreferencesRepository
import com.example.lab5mobile.domain.repository.FlightSearchRepository

interface AppContainer {
    val flightSearchRepository: FlightSearchRepository
    val searchPreferencesRepository: SearchPreferencesRepository
}