package com.example.ctracker.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.ctracker.ui.theme.CTrackerTheme
import com.example.ctracker.viewmodel.MainViewModel
import com.example.ctracker.views.MainView

class MainActivity : ComponentActivity() {
    @SuppressLint("NewApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CTrackerTheme {
                val navController = rememberNavController()
                val mainViewModel: MainViewModel =
                    ViewModelProvider(this)[MainViewModel::class.java]

                // Если произошел выход, перейти в LoginActivity
                mainViewModel.logoutClicked.observe(this) { logout ->
                    if (logout) {
                        mainViewModel.resetLogoutState()
                        startActivity(Intent(this, LoginActivity::class.java))
                        finish()
                    }
                }
                // Основной экран с BottomNavigationBar и навигацией
                MainView(navController = navController, viewModel = mainViewModel)
            }
        }
    }
}