import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsView(viewModel: MainViewModel) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Settings Screen",
            fontSize = 24.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        var notificationsEnabled by remember { mutableStateOf(true) }
        var darkModeEnabled by remember { mutableStateOf(false) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Enable Notifications", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Dark Mode", fontSize = 18.sp)
            Spacer(modifier = Modifier.weight(1f))
            Switch(checked = darkModeEnabled, onCheckedChange = { darkModeEnabled = it })
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(onClick = { viewModel.onLogoutClick() }) {
            Text(text = "Logout", color = Color.Red)
        }
    }
}
