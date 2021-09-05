package com.baloot.mirzazade

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}