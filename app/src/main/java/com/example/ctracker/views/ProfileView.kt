package com.example.ctracker.views

import android.annotation.SuppressLint
import android.content.res.Configuration
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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ctracker.ui.theme.CTrackerTheme
import com.example.ctracker.viewmodel.ProfileViewModel

@Composable
fun ProfileView(viewModel: ProfileViewModel) {
    ProfileContent(
        userName = viewModel.userName.value,
        userWeight = viewModel.userWeight.value,
        calorie = viewModel.calorie.intValue,
        maxCalorie = viewModel.maxCalorie.intValue,
        protein = viewModel.protein.intValue,
        fats = viewModel.fats.intValue,
        carbs = viewModel.carbs.intValue,
        chartData = viewModel.chartData,
        height = viewModel.userHeight.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileContent(
    userName: String,
    userWeight: String,
    calorie: Int,
    maxCalorie: Int,
    protein: Int,
    fats: Int,
    carbs: Int,
    chartData: List<Pair<Int, String>>,
    height : Int
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text("Профиль",
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onPrimary)
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

            // Блок с иконкой и информацией о пользователе
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(vertical = 16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "ICON",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontSize = 24.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userName,
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(horizontal = 8.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = userWeight,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.5f)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$height см",
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Калории",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                    ) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            // Рисуем серую окружность
                            val strokeWidth = 12.dp.toPx()
                            drawArc(
                                color = Color.Gray,
                                startAngle = 0f,
                                sweepAngle = 360f,
                                useCenter = false,
                                style = Stroke(width = strokeWidth)
                            )
                        }
                        val circleColor = if (calorie > maxCalorie) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val strokeWidth = 12.dp.toPx()
                            val color = circleColor
                            val sweepAngle = 360 * (calorie.toFloat() / maxCalorie)
                            drawArc(
                                color = color,
                                startAngle = -90f,
                                sweepAngle = sweepAngle,
                                useCenter = false,
                                style = Stroke(width = strokeWidth)
                            )
                        }
                        Text(
                            text = "$calorie/$maxCalorie",
                            fontSize = TextUnit(50f, TextUnitType.Unspecified),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 16.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Белки: $protein",
                            fontSize = TextUnit(20f, TextUnitType.Unspecified),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(36.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp)
                            .weight(1f),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Жиры: $fats",
                            fontSize = TextUnit(20f, TextUnitType.Unspecified),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(36.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primaryContainer)
                            .padding(16.dp)
                            .weight(1f)
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Углеводы: $carbs",
                            fontSize = TextUnit(20f, TextUnitType.Unspecified),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Статистика",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.onSurface
            )

            val maxBarHeight = 200
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxBarHeight.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom

            ) {
                chartData.forEach { (calories, date) ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = calories.toString(),
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(24.dp)
                                .height((maxBarHeight * 0.7f).dp)
                                .background(Color.LightGray),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            val color = if (calories > maxCalorie) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary
                            val fillFraction = (calories / maxCalorie.toFloat()).coerceAtMost(1f)
                            Box(
                                modifier = Modifier
                                    .width(24.dp)
                                    .fillMaxHeight(fraction = fillFraction)
                                    .background(color)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
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
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun ProfileContentPreview() {
    CTrackerTheme {
        ProfileContent(
            userName = "Имя",
            userWeight = "70 кг",
            calorie = 30000,
            maxCalorie = 2000,
            protein = 120,
            fats = 50,
            carbs = 200,
            chartData = listOf(
                2500 to "19.11",
                1800 to "20.11",
                1700 to "21.11",
                1700 to "22.11",
                1700 to "23.11",
                1700 to "24.11",
                1700 to "25.11"
            ),
            height = 180
        )
    }
}
