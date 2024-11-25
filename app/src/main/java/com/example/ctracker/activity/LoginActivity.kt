package com.example.ctracker.activity

import LoginView
import LoginViewModel
import RegistrationViewModel
import RegisterView
import SharedPreferencesManager
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


class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = SharedPreferencesManager.getString("UserID", "-1")?.toInt()

        if (userId != -1) {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
            finish()
        }

        else {
            setContent {
                CTrackerTheme {
                    val loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
                    val registerViewModel = ViewModelProvider(this)[RegistrationViewModel::class.java]
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                    ) {
                        composable("login") { LoginView(loginViewModel, navController) }
                        composable ("registration"){ RegisterView(registerViewModel, navController)}
                    }

                    registerViewModel.loginSuccess.observe(this, Observer { success ->
                        if (success) {
                            val i = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    })
                    //Проверка на удачный вход
                    loginViewModel.loginSuccess.observe(this, Observer { success ->
                        if (success) {
                            val i = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(i)
                            finish()
                        }
                    })


                }
            }
        }
    }
}
