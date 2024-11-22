import android.content.res.Configuration
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import com.example.ctracker.ui.theme.CTrackerTheme

@Composable
fun RegisterView(viewModel: RegistrationViewModel, navController: NavController) {
    RegisterContent(login = viewModel.login.value,
        password = viewModel.password.value,
        errorMessage = viewModel.errorMessage.value,
        onLoginChanged = viewModel::onLoginChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onRegisterClick = { onSuccess ->
            viewModel.onRegisterClick(onSuccess)
        },
        onBackClick = { navController.popBackStack() },
        navigateToLogin = { navController.navigate("login") },
        isLoginError = viewModel.isLoginError.value,
        isPasswordError = viewModel.isPasswordError.value
    )
}

@Composable
fun RegisterContent(
    login: String,
    password: String,
    errorMessage: String,
    onLoginChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRegisterClick: (onSuccess: () -> Unit) -> Unit,
    onBackClick: () -> Unit,
    navigateToLogin: () -> Unit,
    isLoginError: Boolean,
    isPasswordError: Boolean
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
                    .padding(horizontal = 16.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Ctracker",
                    fontSize = 32.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Start,
                    fontFamily = FontFamily.Serif
                )
                TextButton(onClick = onBackClick) {
                    Text(text = "Назад", color = MaterialTheme.colorScheme.onSurface)
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


                if (errorMessage.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        modifier = Modifier.padding(bottom = 8.dp),
                        textAlign = TextAlign.Center
                    )
                }
                else{
                    Spacer(modifier = Modifier.height(40.dp))
                }
                OutlinedTextField(
                    value = login,
                    onValueChange = onLoginChanged,
                    label = { Text(text = "Логин") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    isError = isLoginError
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Поле ввода пароля
                OutlinedTextField(value = password,
                    onValueChange = onPasswordChanged,
                    label = { Text(text = "Пароль") },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        onRegisterClick(
                            navigateToLogin
                        )
                    }),
                    isError = isPasswordError
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Кнопка "Регистрация"
                Button(
                    onClick = {
                        onRegisterClick(
                            navigateToLogin
                        )
                    }, modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Регистрация")
                }

                Spacer(modifier = Modifier.height(200.dp))
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

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun PreviewRegisterContent() {
    CTrackerTheme {
        RegisterContent(login = "",
            password = "",
            errorMessage = "213",
            onLoginChanged = {},
            onPasswordChanged = {},
            onRegisterClick = { _ -> },
            onBackClick = {},
            navigateToLogin = {} ,
            isLoginError = true,
            isPasswordError = true)
    }

}
