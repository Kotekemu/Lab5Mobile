package com.example.lab5mobile.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab5mobile.R
import com.example.lab5mobile.domain.model.FavoriteRoute

@Composable
fun FavoriteFlightItem(
    route: FavoriteRoute,
    onDeleteClick: (FavoriteRoute) -> Unit,
    modifier: Modifier = Modifier
) {
    val removeDescription = stringResource(R.string.remove_favorite_action)

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                AirportInfoBlock(
                    label = stringResource(R.string.depart_short_label),
                    code = route.departureCode,
                    name = route.departureName
                )
                AirportInfoBlock(
                    label = stringResource(R.string.arrive_short_label),
                    code = route.destinationCode,
                    name = route.destinationName,
                    modifier = Modifier.padding(top = 14.dp)
                )
            }

            Box(
                modifier = Modifier.padding(start = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                TextButton(
                    onClick = {
                        onDeleteClick(route)
                    },
                    modifier = Modifier.semantics {
                        contentDescription = removeDescription
                    }
                ) {
                    Text(
                        text = "★",
                        fontSize = 26.sp,
                        color = Color(0xFFB26A00)
                    )
                }
            }
        }
    }
}

@Composable
private fun AirportInfoBlock(
    label: String,
    code: String,
    name: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = code,
                modifier = Modifier.width(52.dp),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 1
            )
            Text(
                text = name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}