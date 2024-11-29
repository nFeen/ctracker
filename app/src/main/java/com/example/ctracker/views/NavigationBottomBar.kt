package com.example.ctracker.views

import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun NavigationBottomBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.Home,
        BottomNavItem.Settings
    )

    androidx.compose.material3.NavigationBar(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ) {
        val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Image(
                        imageVector = item.icon,
                        contentDescription = item.title
                    )
                },
                label = { Text(text = item.title, color = MaterialTheme.colorScheme.onBackground) },
                selected = currentRoute == item.route,
                colors = NavigationBarItemDefaults.colors(indicatorColor = MaterialTheme.colorScheme.onPrimary),
                onClick = {
                    if (item.route != currentRoute) {
                        if (currentRoute == "search/{index}") {
                            navController.popBackStack("home", inclusive = false)
                        }
                        if (currentRoute == "editmeal/{index}") {
                            navController.popBackStack("home", inclusive = false)
                        }
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = false
                            }
                            launchSingleTop = false
                            restoreState = false
                        }
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    data object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
    data object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    data object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")
}