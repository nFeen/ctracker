package com.example.ctracker.activity

import LoginView
import LoginViewModel
import SharedPreferencesManager
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.ctracker.ui.theme.CTrackerTheme


class LoginActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = SharedPreferencesManager.getString("UserID", "-1").toInt()

        if (userId != -1) {
            val i = Intent(this@LoginActivity, MainActivity::class.java)
            startActivity(i)
        }

        else {
            setContent {
                CTrackerTheme {
                    val loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
                    loginViewModel.loginSuccess.observe(this, Observer { success ->
                        if (success) {
                            val i = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(i)
                        }
                    })
                    LoginView(viewModel = loginViewModel)
                }
            }
        }
    }
}
