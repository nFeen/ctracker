package com.example.ctracker.views

import MainViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileView(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Изображение профиля
        Image(
            imageVector = Icons.Default.Person,
            contentDescription = "Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
                .aspectRatio(1f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Имя пользователя
        Text(text = "John Doe", fontSize = 24.sp)

        Spacer(modifier = Modifier.height(16.dp))

        // Кнопки действий
        Button(onClick = { /* TODO: Add edit profile action */ }) {
            Text(text = "Edit Profile")
        }
    }
}
