package com.example.ctracker

import android.app.Application
import android.content.Context

class CTrackerApp : Application() {
    companion object {
        private var instance: CTrackerApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        SharedPreferencesManager.init(this)
        val context: Context = CTrackerApp.applicationContext()

    }
}
