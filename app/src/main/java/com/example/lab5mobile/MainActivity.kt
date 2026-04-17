package com.example.lab5mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.lab5mobile.ui.screen.FlightSearchScreen
import com.example.lab5mobile.ui.theme.Lab5MobileTheme
import com.example.lab5mobile.ui.viewmodel.FlightSearchViewModel
import com.example.lab5mobile.ui.viewmodel.FlightSearchViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as FlightSearchApplication).container

        setContent {
            Lab5MobileTheme {
                val flightSearchViewModel: FlightSearchViewModel = viewModel(
                    factory = FlightSearchViewModelFactory(
                        flightSearchRepository = container.flightSearchRepository,
                        searchPreferencesRepository = container.searchPreferencesRepository
                    )
                )

                FlightSearchScreen(
                    viewModel = flightSearchViewModel
                )
            }
        }
    }
}