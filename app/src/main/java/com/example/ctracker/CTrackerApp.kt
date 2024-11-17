package com.example.ctracker

import android.app.Application
import android.content.Context

class CTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
    }
}
