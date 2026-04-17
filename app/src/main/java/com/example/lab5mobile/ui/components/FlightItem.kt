package com.example.lab5mobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab5mobile.R
import com.example.lab5mobile.domain.model.Flight

@Composable
fun FlightItem(
    flight: Flight,
    onFavoriteClick: (Flight) -> Unit,
    modifier: Modifier = Modifier
) {
    val favoriteActionDescription = if (flight.isFavorite) {
        stringResource(R.string.remove_favorite_action)
    } else {
        stringResource(R.string.add_favorite_action)
    }

    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.departure_label),
                    style = MaterialTheme.typography.labelSmall
                )

                Text(
                    text = flight.departureCode,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = flight.departureName,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(R.string.arrival_label),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = flight.destinationCode,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = flight.destinationName,
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = stringResource(R.string.flight_id_label),
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 12.dp)
                )

                Text(
                    text = flight.flightId,
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            TextButton(
                onClick = {
                    onFavoriteClick(flight)
                },
                modifier = Modifier.semantics {
                    contentDescription = favoriteActionDescription
                }
            ) {
                Text(
                    text = if (flight.isFavorite) "★" else "☆",
                    fontSize = 26.sp,
                    color = if (flight.isFavorite) {
                        Color(0xFFB26A00)
                    } else {
                        MaterialTheme.colorScheme.primary
                    }
                )
            }
        }
    }
}