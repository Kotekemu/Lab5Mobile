package com.example.lab5mobile.ui.state

import androidx.annotation.StringRes
import com.example.lab5mobile.domain.model.AirportSuggestion
import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.model.Flight

data class FlightSearchUiState(
    val searchQuery: String = "",
    val selectedAirportCode: String? = null,
    val suggestions: List<AirportSuggestion> = emptyList(),
    val flights: List<Flight> = emptyList(),
    val favoriteRoutes: List<FavoriteRoute> = emptyList(),
    val isInitialized: Boolean = false,
    @StringRes val errorMessageRes: Int? = null
) {
    val isShowingFavorites: Boolean
        get() = searchQuery.isBlank()

    val showSuggestions: Boolean
        get() = searchQuery.isNotBlank() &&
                selectedAirportCode == null &&
                suggestions.isNotEmpty()

    val showEmptySuggestions: Boolean
        get() = searchQuery.isNotBlank() &&
                selectedAirportCode == null &&
                suggestions.isEmpty() &&
                errorMessageRes == null

    val showFlights: Boolean
        get() = !isShowingFavorites && selectedAirportCode != null

    val showEmptyFlights: Boolean
        get() = showFlights &&
                flights.isEmpty() &&
                errorMessageRes == null

    val showEmptyFavorites: Boolean
        get() = isShowingFavorites &&
                isInitialized &&
                favoriteRoutes.isEmpty() &&
                errorMessageRes == null
}