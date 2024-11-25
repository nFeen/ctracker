import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.*
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.ctracker.ui.theme.CTrackerTheme
import com.example.ctracker.viewmodel.AddItemViewModel
import com.example.ctracker.viewmodel.EditMealViewModel
import com.example.ctracker.viewmodel.HomeViewModel
import com.example.ctracker.views.ProfileView
import com.example.ctracker.viewmodel.ProfileViewModel
import com.example.ctracker.viewmodel.SearchViewModel
import com.example.ctracker.views.AddItemView
import com.example.ctracker.views.EditItemView
import com.example.ctracker.views.NavigationBottomBar
import com.example.ctracker.views.SearchView

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "RememberReturnType")
@Composable
fun MainView(navController: NavHostController, viewModel: MainViewModel) {
    Scaffold(
        bottomBar = {
            NavigationBottomBar(navController = navController)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavHost(
                navController = navController,
                startDestination = "home",
            ) {
                composable("profile") { val profileViewModel = remember{ProfileViewModel() }; ProfileView(profileViewModel)}
                composable("home") {     val homeViewModel = remember { HomeViewModel() }; HomeView(homeViewModel, navController)}
                composable("settings") { SettingsView(viewModel) }
                composable("search/{index}") { backStackEntry ->
                    val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                    val searchViewModel = remember{SearchViewModel(index)}
                    SearchView(searchViewModel, navController)
                }
                composable("additem/{mealType}/{index}") { backStackEntry ->
                    val mealType = backStackEntry.arguments?.getString("mealType")?.toIntOrNull() ?: 0
                    val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                    val addViewModel = remember {
                        AddItemViewModel(index = index, mealType = mealType)
                    }
                    AddItemView(addViewModel, navController)
                }
                composable("editmeal/{index}") { backStackEntry ->
                    val mealId = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
                    val editMealViewModel = remember{EditMealViewModel(mealId)}
                    EditItemView(editMealViewModel, navController)
                }
            }
        }
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun PreviewMainView() {
    CTrackerTheme {
    Scaffold(
        bottomBar = {
            NavigationBottomBar(navController = rememberNavController())
        }){}}
}