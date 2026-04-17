package com.example.lab5mobile.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab5mobile.R
import com.example.lab5mobile.data.preferences.SearchPreferencesRepository
import com.example.lab5mobile.domain.model.AirportSuggestion
import com.example.lab5mobile.domain.model.FavoriteRoute
import com.example.lab5mobile.domain.model.Flight
import com.example.lab5mobile.domain.repository.FlightSearchRepository
import com.example.lab5mobile.ui.state.FlightSearchUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FlightSearchViewModel(
    private val flightSearchRepository: FlightSearchRepository,
    private val searchPreferencesRepository: SearchPreferencesRepository
) : ViewModel() {

    private val searchQuery = MutableStateFlow("")
    private val selectedAirportCode = MutableStateFlow<String?>(null)
    private val isInitialized = MutableStateFlow(false)
    private val errorMessageRes = MutableStateFlow<Int?>(null)

    private val suggestionsFlow = searchQuery
        .flatMapLatest { query ->
            val normalizedQuery = query.trim()
            if (normalizedQuery.isBlank() || selectedAirportCode.value != null) {
                flowOf(emptyList())
            } else {
                flightSearchRepository.getAirportSuggestions(normalizedQuery)
                    .catch {
                        errorMessageRes.value = R.string.error_load_suggestions
                        emit(emptyList())
                    }
            }
        }

    private val flightsFlow = selectedAirportCode
        .flatMapLatest { departureCode ->
            if (departureCode.isNullOrBlank()) {
                flowOf(emptyList())
            } else {
                flightSearchRepository.getFlightsFromAirport(departureCode)
                    .catch {
                        errorMessageRes.value = R.string.error_load_flights
                        emit(emptyList())
                    }
            }
        }

    private val favoriteRoutesFlow = flightSearchRepository.getFavoriteRoutes()
        .catch {
            errorMessageRes.value = R.string.error_load_favorites
            emit(emptyList())
        }

    val uiState = combine(
        searchQuery,
        selectedAirportCode,
        suggestionsFlow,
        flightsFlow,
        favoriteRoutesFlow,
        isInitialized,
        errorMessageRes
    ) { values ->
        val query = values[0] as String
        val departureCode = values[1] as String?
        val suggestions = values[2] as List<AirportSuggestion>
        val flights = values[3] as List<Flight>
        val favoriteRoutes = values[4] as List<FavoriteRoute>
        val initialized = values[5] as Boolean
        val currentError = values[6] as Int?
        FlightSearchUiState(
            searchQuery = query,
            selectedAirportCode = departureCode,
            suggestions = suggestions,
            flights = flights,
            favoriteRoutes = favoriteRoutes,
            isInitialized = initialized,
            errorMessageRes = currentError
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FlightSearchUiState()
    )

    init {
        restoreLastSearchQuery()
    }

    fun onSearchQueryChange(newQuery: String) {
        errorMessageRes.value = null
        searchQuery.value = newQuery
        val normalizedQuery = newQuery.trim()
        if (normalizedQuery.isBlank()) {
            selectedAirportCode.value = null
            viewModelScope.launch {
                searchPreferencesRepository.saveLastSearchQuery("")
            }
            return
        }
        val currentSelectedAirportCode = selectedAirportCode.value
        if (
            currentSelectedAirportCode != null &&
            !normalizedQuery.equals(currentSelectedAirportCode, ignoreCase = true)
        ) {
            selectedAirportCode.value = null
        }
    }

    fun onSuggestionSelected(suggestion: AirportSuggestion) {
        val code = suggestion.iataCode.uppercase()
        errorMessageRes.value = null
        searchQuery.value = code
        selectedAirportCode.value = code
        viewModelScope.launch {
            searchPreferencesRepository.saveLastSearchQuery(code)
        }
    }

    fun onSearchSubmitted() {
        viewModelScope.launch {
            val currentQuery = searchQuery.value.trim()
            if (currentQuery.isBlank()) {
                selectedAirportCode.value = null
                searchPreferencesRepository.saveLastSearchQuery("")
                return@launch
            }
            runCatching {
                flightSearchRepository.getAirportSuggestions(currentQuery).first()
            }.onSuccess { suggestions ->
                val exactMatch = suggestions.firstOrNull { suggestion ->
                    suggestion.iataCode.equals(currentQuery, ignoreCase = true) ||
                            suggestion.name.equals(currentQuery, ignoreCase = true)
                }
                val selectedSuggestion = exactMatch ?: suggestions.firstOrNull()

                if (selectedSuggestion == null) {
                    selectedAirportCode.value = null
                    errorMessageRes.value = R.string.error_airport_not_found
                    return@onSuccess
                }
                val code = selectedSuggestion.iataCode.uppercase()
                errorMessageRes.value = null
                searchQuery.value = code
                selectedAirportCode.value = code
                searchPreferencesRepository.saveLastSearchQuery(code)
            }.onFailure {
                selectedAirportCode.value = null
                errorMessageRes.value = R.string.error_search_failed
            }
        }
    }

    fun onFlightFavoriteClick(flight: Flight) {
        viewModelScope.launch {
            runCatching {
                if (flight.isFavorite) {
                    flightSearchRepository.removeFavorite(
                        departureCode = flight.departureCode,
                        destinationCode = flight.destinationCode
                    )
                } else {
                    flightSearchRepository.addFavorite(
                        departureCode = flight.departureCode,
                        destinationCode = flight.destinationCode
                    )
                }
            }.onFailure {
                errorMessageRes.value = R.string.error_update_favorites
            }
        }
    }

    fun onFavoriteRouteDelete(route: FavoriteRoute) {
        viewModelScope.launch {
            runCatching {
                flightSearchRepository.removeFavorite(
                    departureCode = route.departureCode,
                    destinationCode = route.destinationCode
                )
            }.onFailure {
                errorMessageRes.value = R.string.error_delete_favorite
            }
        }
    }

    fun clearError() {
        errorMessageRes.value = null
    }

    private fun restoreLastSearchQuery() {
        viewModelScope.launch {
            runCatching {
                searchPreferencesRepository.lastSearchQuery.first()
            }.onSuccess { savedQuery ->
                val normalizedQuery = savedQuery.trim().uppercase()
                if (normalizedQuery.isNotBlank()) {
                    searchQuery.value = normalizedQuery
                    selectedAirportCode.value = normalizedQuery
                }
                isInitialized.value = true
            }.onFailure {
                errorMessageRes.value = R.string.error_restore_last_search
                isInitialized.value = true
            }
        }
    }
}