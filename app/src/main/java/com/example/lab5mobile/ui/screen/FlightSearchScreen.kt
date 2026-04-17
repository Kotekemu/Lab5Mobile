package com.example.lab5mobile.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.lab5mobile.R
import com.example.lab5mobile.ui.components.AirportSuggestionItem
import com.example.lab5mobile.ui.components.FavoriteFlightItem
import com.example.lab5mobile.ui.components.FlightItem
import com.example.lab5mobile.ui.components.SearchBar
import com.example.lab5mobile.ui.viewmodel.FlightSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightSearchScreen(
    viewModel: FlightSearchViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.top_bar_title))
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            SearchBar(
                searchQuery = uiState.searchQuery,
                onQueryChange = viewModel::onSearchQueryChange,
                onSearch = viewModel::onSearchSubmitted
            )

            Spacer(modifier = Modifier.height(12.dp))

            uiState.errorMessageRes?.let { messageRes ->
                Text(
                    text = stringResource(messageRes),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                )
            }

            when {
                uiState.showSuggestions -> {
                    Text(
                        text = stringResource(R.string.suggestions_title),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = uiState.suggestions,
                            key = { suggestion -> suggestion.iataCode }
                        ) { suggestion ->
                            AirportSuggestionItem(
                                suggestion = suggestion,
                                onClick = viewModel::onSuggestionSelected
                            )
                        }
                    }
                }

                uiState.showEmptySuggestions -> {
                    Text(
                        text = stringResource(R.string.airports_not_found),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                uiState.showFlights -> {
                    Text(
                        text = stringResource(
                            R.string.flights_from_title,
                            uiState.selectedAirportCode.orEmpty()
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (uiState.showEmptyFlights) {
                        Text(
                            text = stringResource(R.string.flights_not_found),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                            items(
                                items = uiState.flights,
                                key = { flight -> flight.flightId }
                            ) { flight ->
                                FlightItem(
                                    flight = flight,
                                    onFavoriteClick = viewModel::onFlightFavoriteClick
                                )
                            }
                        }
                    }
                }

                uiState.showEmptyFavorites -> {
                    Text(
                        text = stringResource(R.string.no_favorite_routes),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                uiState.isShowingFavorites -> {
                    Text(
                        text = stringResource(R.string.favorite_routes_title),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(
                            items = uiState.favoriteRoutes,
                            key = { route ->
                                "${route.departureCode}-${route.destinationCode}"
                            }
                        ) { route ->
                            FavoriteFlightItem(
                                route = route,
                                onDeleteClick = viewModel::onFavoriteRouteDelete
                            )
                        }
                    }
                }
            }
        }
    }
}