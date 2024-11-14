package com.example.ctracker.activity

import HomeView
import MainView
import MainViewModel
import SettingsView
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ctracker.ui.theme.CTrackerTheme
import com.example.ctracker.views.ProfileView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CTrackerTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

                // Наблюдаем за состоянием logoutClicked
                mainViewModel.logoutClicked.observe(this, Observer { logout ->
                    if (logout) {
                        mainViewModel.resetLogoutState()  // Сбрасываем состояние выхода
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                })
                // Основной экран с BottomNavigationBar и навигацией
                MainView(navController = navController, viewModel = mainViewModel)
            }
        }
    }
}