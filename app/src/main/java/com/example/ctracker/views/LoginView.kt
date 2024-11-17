import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun LoginView(
    viewModel: LoginViewModel,
    navController: NavHostController
) {
    LoginContent(
        login = viewModel.login.value,
        onLoginChanged = viewModel::onLoginChanged,
        password = viewModel.password.value,
        onPasswordChanged = viewModel::onPasswordChanged,
        errorMessage = viewModel.errorMessage.value,
        onLoginClick = viewModel::onLoginClick,
        onRegisterClick = { navController.navigate("registration") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(
    login: String,
    onLoginChanged: (String) -> Unit,
    password: String,
    onPasswordChanged: (String) -> Unit,
    errorMessage: String,
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 16.dp)
            ) {
                Text(
                    text = "Ctracker",
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Start
                )
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
                    text = "Вход",
                    fontSize = 60.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                OutlinedTextField(
                    value = login,
                    onValueChange = onLoginChanged,
                    label = { Text(text = "Логин") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChanged,
                    label = { Text(text = "Пароль") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Войти")
                }
                Spacer(modifier = Modifier.height(200.dp))
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Не зарегистрированы?", color = Color.Gray)
                TextButton(onClick = onRegisterClick) {
                    Text(text = "Зарегистрироваться")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginView() {
    LoginContent(
        login = "",
        onLoginChanged = {},
        password = "",
        onPasswordChanged = {},
        errorMessage = "",
        onLoginClick = {},
        onRegisterClick = {}
    )
}
