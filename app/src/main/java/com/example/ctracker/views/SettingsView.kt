import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SettingsView(viewModel: MainViewModel) {
    SettingsContent(
        onLogoutClick = viewModel::onLogoutClick // Передача функции из ViewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsContent(
    onLogoutClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Настройки")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
        ) {


            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка Logout
            OutlinedButton(
                onClick = onLogoutClick, // Использование переданной функции
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Logout")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsContent() {
    SettingsContent(
        onLogoutClick = { /* Пример обработчика выхода */ }
    )
}
