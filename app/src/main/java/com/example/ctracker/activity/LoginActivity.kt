package com.example.ctracker.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ctracker.SharedPreferencesManager
import com.example.ctracker.ui.theme.CTrackerTheme
import com.example.ctracker.viewmodel.LoginViewModel
import com.example.ctracker.viewmodel.RegistrationViewModel
import com.example.ctracker.views.LoginView
import com.example.ctracker.views.RegisterView


class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = SharedPreferencesManager.getString("userID", "-1").toInt()

        if (userId != -1) {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        } else {
            setContent {
                CTrackerTheme {
                    val loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
                    val registerViewModel =
                        ViewModelProvider(this)[RegistrationViewModel::class.java]
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable("login") { LoginView(loginViewModel, navController) }
                        composable("registration") {
                            RegisterView(
                                registerViewModel,
                                navController
                            )
                        }
                    }
                    //Проверка на удачный вход в окне "Регистрация"
                    registerViewModel.loginSuccess.observe(this) { success ->
                        if (success) {
                            val i = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    }
                    //Проверка на удачный вход в окне "Логин"
                    loginViewModel.loginSuccess.observe(this) { success ->
                        if (success) {
                            val i = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    }


                }
            }
        }
    }
}
