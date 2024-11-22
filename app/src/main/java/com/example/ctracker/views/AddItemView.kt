package com.example.ctracker.views

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ctracker.viewmodel.AddItemViewModel
import com.example.ctracker.ui.theme.CTrackerTheme

@Composable
fun AddItemView(viewModel: AddItemViewModel, navController: NavController) {
    AddItemContent(
        productName = viewModel.food.name,
        calories = viewModel.food.calories,
        protein = viewModel.food.protein,
        fats = viewModel.food.fat,
        carbs = viewModel.food.carb,
        weight = viewModel.weightState.value,
        isError = viewModel.isError.value, // Используем флаг из ViewModel
        onWeightChange = { input -> viewModel.updateWeight(input) },
        onAddClick = {
            viewModel.addMealToUser()
            navController.popBackStack("home", false)
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddItemContent(
    productName: String,
    calories: Int,
    protein: Float,
    fats: Float,
    carbs: Float,
    weight: String,
    isError: Boolean, // Принимаем флаг ошибки
    onWeightChange: (String) -> Unit,
    onAddClick: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
                title = {
                    Text(
                        "CTracker",
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
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
                text = productName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
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
                    InfoBlock("Белки", "$protein г", Modifier.weight(1f))
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    InfoBlock("Жиры", "$fats г", Modifier.weight(1f))
                    Spacer(modifier = Modifier.width(16.dp))
                    InfoBlock("Углеводы", "$carbs г", Modifier.weight(1f))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

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

                // Блоки на вес (два ряда)
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

            Spacer(modifier = Modifier.height(24.dp))

            // Поле ввода для количества еды
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

            Spacer(modifier = Modifier.height(32.dp))

            // Кнопка "Добавить"
            Button(
                onClick = {
                    if (!isError) onAddClick()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !isError
            ) {
                Text(text = "Добавить")
            }
        }
    }
}

@Composable
fun InfoBlock(title: String, value: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                softWrap = false, // Отключает перенос
                modifier = Modifier.fillMaxWidth(),
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis // Уменьшение текста
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.fillMaxWidth(),
                overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
            )
        }
    }
}

// Extension function for formatting floats
private fun Float.format(digits: Int) = "%.${digits}f".format(this)

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun PreviewAddItemContent() {
    CTrackerTheme {
        AddItemContent(
            productName = "Яблоко",
            calories = 52,
            protein = 0.3f,
            fats = 0.2f,
            carbs = 14f,
            weight = "200",
            onWeightChange = {},
            onAddClick = {},
            isError = true
        )
    }
}