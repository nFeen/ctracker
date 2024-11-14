package com.example.ctracker.views

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ctracker.viewmodel.ProfileViewModel

@Composable
fun ProfileView(viewModel: ProfileViewModel = viewModel()) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, top = 16.dp, end= 16.dp, bottom= 0.dp)
    ) {
        // Заголовок "Profile" в верхнем левом углу
        Text(
            text = "Profile",
            fontSize = 28.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Блок с иконкой и информацией о пользователе
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)  // Фиксированная высота Row
                .padding(vertical = 16.dp)
        ) {
            // Левая часть с иконкой пользователя, занимает половину ширины Row
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f) // Иконка занимает половину ширины Row
                    .background(MaterialTheme.colorScheme.primaryContainer), // Цвет из темы
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "ICON",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontSize = 24.sp
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Правая часть с двумя блоками: Имя сверху и Вес снизу
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f) // Правая часть занимает оставшуюся половину
            ) {
                // Блок для имени пользователя, занимает половину высоты
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Равномерное распределение по высоте
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = viewModel.userName.value,
                        fontSize = 24.sp, // Увеличенный размер текста
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp)) // Отступ между блоками

                // Блок для веса поль   зователя, занимает половину высоты
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f) // Равномерное распределение по высоте
                        .background(
                            MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = viewModel.userWeight.value,
                        fontSize = 24.sp, // Увеличенный размер текста
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Калории",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp) // Увеличиваем высоту для размещения всех элементов
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Колонка с круговым индикатором калорий и текстом с целью
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Блок с круговым индикатором калорий
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val strokeWidth = 8.dp.toPx()

                        // Серый фон круга
                        drawCircle(
                            color = Color.LightGray,
                            style = Stroke(width = strokeWidth)
                        )

                        // Определяем цвет для заполненной части круга
                        val color = if (viewModel.calorie.intValue > viewModel.maxCalorie.intValue) {
                            Color(0xFFFFA500) // Оранжевый, если калорий больше, чем максимум
                        } else {
                            Color.Green // Зеленый, если калории в пределах нормы
                        }

                        // Заполненная часть круга
                        val sweepAngle = 360 * (viewModel.calorie.intValue.toFloat() / viewModel.maxCalorie.intValue)
                        drawArc(
                            color = color,
                            startAngle = -90f,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            style = Stroke(width = strokeWidth)
                        )
                    }
                    // Текст в центре круга
                    Text(
                        text = "${viewModel.calorie.intValue}\nКалорий",
                        fontSize = TextUnit(40f, TextUnitType.Unspecified),
                        color = Color.Black,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Текст с целью калорий под кругом
                Text(
                    text = "Цель: ${viewModel.maxCalorie.intValue}",
                    fontSize = TextUnit(40f, TextUnitType.Unspecified),
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Информация о нутриентах, занимает половину ширины Row и выровнена по высоте с Canvas
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Белки: ${viewModel.protein.intValue}",
                        fontSize = TextUnit(20f, TextUnitType.Unspecified),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Жиры: ${viewModel.fats.intValue}",
                        fontSize = TextUnit(20f, TextUnitType.Unspecified),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Углеводы: ${viewModel.carbs.intValue}",
                        fontSize = TextUnit(20f, TextUnitType.Unspecified),
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Статистика",
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.primary
        )

        
        val maxBarHeight = 200
        val maxCalories = viewModel.maxCalorie

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(maxBarHeight.dp),// Высота Row задает maxBarHeight
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            viewModel.chartData.forEach { (calories, date) ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Отображение количества калорий сверху
                    Text(
                        text = calories.toString(),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Контейнер для столбика калорий с фиксированной высотой
                    Box(
                        modifier = Modifier
                            .width(24.dp)
                            .height((maxBarHeight * 0.6f).dp) // Ограничиваем высоту столбика до 60% от maxBarHeight
                            .background(Color.LightGray),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        // Определяем цвет столбика на основе значения калорий
                        val barColor = if (calories > maxCalories.intValue) {
                            Color(0xFFFFA500) // Оранжевый, если калорий больше, чем максимум
                        } else {
                            Color.Green // Зеленый, если калории в пределах нормы
                        }

                        // Высота столбика пропорционально калориям, ограниченная maxCalories
                        val fillFraction = (calories / maxCalories.intValue.toFloat()).coerceAtMost(1f)

                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .fillMaxHeight(fraction = fillFraction) // Высота относительно maxCalories, максимум 100%
                                .background(barColor)
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Отображение даты внизу
                    Text(
                        text = date,
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileViewPreview() {
    ProfileView(ProfileViewModel())
}
