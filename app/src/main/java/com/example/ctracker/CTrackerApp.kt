package com.example.ctracker

import SharedPreferencesManager
import android.app.Application
import android.content.Context

class CTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
    }
}
