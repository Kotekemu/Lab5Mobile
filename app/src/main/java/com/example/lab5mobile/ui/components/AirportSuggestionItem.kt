package com.example.lab5mobile.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.lab5mobile.domain.model.AirportSuggestion

@Composable
fun AirportSuggestionItem(
    suggestion: AirportSuggestion,
    onClick: (AirportSuggestion) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick(suggestion)
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = suggestion.iataCode,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = suggestion.name,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}