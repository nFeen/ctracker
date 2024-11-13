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

@Composable
fun LoginView(
    viewModel: LoginViewModel
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
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = viewModel.login.value,
                    onValueChange = { viewModel.onLoginChanged(it) },
                    label = { Text(text = "Логин") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = viewModel.password.value,
                    onValueChange = { viewModel.onPasswordChanged(it) },
                    label = { Text(text = "Пароль") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (viewModel.errorMessage.value.isNotEmpty()) {
                    Text(
                        text = viewModel.errorMessage.value,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }

                Button(
                    onClick = { viewModel.onLoginClick() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Войти")
                }

                Spacer(modifier = Modifier.height(16.dp))

                TextButton(onClick = { viewModel.onForgotPasswordClick() }) {
                    Text(text = "Забыл пароль")
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Не зарегистрированы?", color = Color.Gray)
                TextButton(onClick = { viewModel.onRegisterClick() }) {
                    Text(text = "Зарегистрироваться")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginView() {
    val loginViewModel = LoginViewModel()
    LoginView(viewModel = loginViewModel)
}
