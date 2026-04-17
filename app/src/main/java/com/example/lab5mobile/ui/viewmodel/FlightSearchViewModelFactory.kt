package com.example.lab5mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.lab5mobile.data.preferences.SearchPreferencesRepository
import com.example.lab5mobile.domain.repository.FlightSearchRepository

class FlightSearchViewModelFactory(
    private val flightSearchRepository: FlightSearchRepository,
    private val searchPreferencesRepository: SearchPreferencesRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FlightSearchViewModel::class.java)) {
            return FlightSearchViewModel(
                flightSearchRepository = flightSearchRepository,
                searchPreferencesRepository = searchPreferencesRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}