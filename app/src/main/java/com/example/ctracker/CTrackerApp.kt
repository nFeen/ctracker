package com.example.ctracker

import android.app.Application

class CTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
    }
}
