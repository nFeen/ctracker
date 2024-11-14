import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun RegisterView(viewModel: RegistrationViewModel, navController: NavController) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Заголовок CTracker
                Text(
                    text = "Ctracker",
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
                // Кнопка "Назад"
                TextButton(onClick = { navController.popBackStack() }) {
                    Text(text = "Назад", color = MaterialTheme.colorScheme.primary)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Регистрация",
                    fontSize = 40.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                var login by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                // Поле ввода логина
                OutlinedTextField(
                    value = viewModel.login.value,
                    onValueChange = {viewModel.onLoginChanged(it)},
                    label = { Text(text = "Логин") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Поле ввода пароля
                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = {viewModel.onPasswordChanged(it)},
                    label = { Text(text = "Пароль") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка "Регистрация"
                Button(
                    onClick = {viewModel.onRegisterClick(
                        onSuccess = { navController.navigate("login") },
                        onFailure = { error -> viewModel.errorMessage.value = error }
                    )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Регистрация")
                }
            }
            if (viewModel.errorMessage.value.isNotEmpty()) {
                Text(
                    text = viewModel.errorMessage.value,
                    color = Color.Red,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "", color = Color.Gray)
                TextButton(onClick = {}) {
                    Text(text = "")
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewRegistrationView() {
    val navController = rememberNavController()
    val viewModel = RegistrationViewModel()
    RegisterView(navController = navController, viewModel=viewModel)
}