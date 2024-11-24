package com.example.ctracker.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ctracker.ui.theme.CTrackerTheme
import com.example.ctracker.viewmodel.EditMealViewModel

@Composable
fun EditItemView(viewModel: EditMealViewModel, navController: NavController) {
    EditItemContent(
        mealName = viewModel.meal?.name ?: "Продукт",
        calories = viewModel.meal?.calories?.toInt() ?: 0,
        protein = viewModel.meal?.protein ?: 0f,
        fats = viewModel.meal?.fats ?: 0f,
        carbs = viewModel.meal?.carbs ?: 0f,
        weight = viewModel.weightState.value,
        isError = viewModel.isError.value,
        onWeightChange = { input -> viewModel.updateWeight(input) },
        onSaveClick = {
            if (!viewModel.isError.value) {
                viewModel.updateMeal()
                navController.popBackStack("home", false)
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditItemContent(
    mealName: String,
    calories: Int,
    protein: Float,
    fats: Float,
    carbs: Float,
    weight: String,
    isError: Boolean,
    onWeightChange: (String) -> Unit,
    onSaveClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                title = {
                    Text("Редактировать продукт")
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            // Название продукта
            Text(
                text = mealName,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(8.dp))

            // "На 100 грамм"
            Text(
                text = "На 100 грамм",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Блоки на 100 грамм (два ряда)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoBlock("Калории", "$calories ккал", Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(16.dp))
                    InfoBlock("Белки", "${protein.format(1)} г", Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoBlock("Жиры", "${fats.format(1)} г", Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(16.dp))
                    InfoBlock("Углеводы", "${carbs.format(1)} г", Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Поле ввода веса
            OutlinedTextField(
                value = weight,
                onValueChange = onWeightChange,
                label = { Text("Вес (г)") },
                isError = isError,
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
            )
            if (isError) {
                Text(
                    text = "Введите корректное значение больше 0",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // "На $weight грамм"
            val weightValue = weight.toFloatOrNull() ?: 0f
            if (!isError) {
                Text(
                    text = "На $weight грамм",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Блоки на введённый вес
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoBlock("Калории", "${(calories * weightValue / 100).toInt()} ккал", Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        InfoBlock("Белки", "${(protein * weightValue / 100).format(1)} г", Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoBlock("Жиры", "${(fats * weightValue / 100).format(1)} г", Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        InfoBlock("Углеводы", "${(carbs * weightValue / 100).format(1)} г", Modifier.weight(1f))
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка "Сохранить"
            Button(
                onClick = {
                    if (!isError) onSaveClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isError
            ) {
                Text(text = "Сохранить")
            }
        }
    }
}

// Extension function for formatting floats
private fun Float.format(digits: Int) = "%.${digits}f".format(this)

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun PreviewEditItemContent() {
    CTrackerTheme {
        EditItemContent(
            mealName = "Яичница",
            calories = 150,
            protein = 12.3f,
            fats = 10.5f,
            carbs = 3.2f,
            weight = "200",
            isError = false,
            onWeightChange = {},
            onSaveClick = {}
        )
    }
}
