package com.example.lab5mobile

import android.app.Application
import com.example.lab5mobile.di.AppContainer
import com.example.lab5mobile.di.DefaultAppContainer

class FlightSearchApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}