import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.material.icons.filled.Person
import androidx.navigation.NavHostController
import com.example.ctracker.views.ProfileView
import com.example.ctracker.ui.theme.CTrackerTheme // Добавьте импорт вашей темы

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainView(navController: NavHostController, viewModel: MainViewModel) {
    Scaffold(
        bottomBar = {
            NavigationBar(navController = navController)
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = "home",
        ) {
            composable("profile") { ProfileView(viewModel) }
            composable("home") { HomeView(viewModel) }
            composable("settings") { SettingsView(viewModel) }
        }
    }
}

@Composable
fun NavigationBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Profile,
        BottomNavItem.Home,
        BottomNavItem.Settings
    )

    NavigationBar(
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
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String) {
    object Profile : BottomNavItem("profile", Icons.Default.Person, "Profile")
    object Home : BottomNavItem("home", Icons.Default.Home, "Home")
    object Settings : BottomNavItem("settings", Icons.Default.Settings, "Settings")
}
