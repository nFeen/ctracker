package com.example.ctracker.views

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import com.example.ctracker.ui.theme.CTrackerTheme
import com.example.ctracker.viewmodel.ProfileViewModel
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.output.ByteArrayOutputStream
import java.util.Base64

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileView(viewModel: ProfileViewModel) {
    ProfileContent(
        userName = viewModel.userName.value,
        userWeight = viewModel.userWeight.value,
        calorie = viewModel.calorie.value,
        maxCalorie = viewModel.maxCalorie.value,
        protein = viewModel.protein.value,
        fats = viewModel.fats.value,
        carbs = viewModel.carbs.value,
        chartData = viewModel.chartData.value,
        height = viewModel.userHeight.value,
        onUpdateWeight = viewModel::updateUserWeight,
        onUpdateHeight = viewModel::updateUserHeight,
        onUpdateProfilePicture = viewModel::updateUserProfilePicture,
        onUpdateCalorieGoal = viewModel::updateUserCalorieGoal,
        profilePic = viewModel.profilePic.value
    )
}

@RequiresApi(Build.VERSION_CODES.O)
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
    height: Int,
    onUpdateWeight: (Int) -> Unit, // Добавляем callback для обновления веса
    onUpdateHeight: (Int) -> Unit, // Добавляем callback для обновления веса
    onUpdateProfilePicture: (String) -> Unit,
    onUpdateCalorieGoal: (Int) -> Unit,
    profilePic: String
) {
    val isWeightDialogOpen = remember { mutableStateOf(false) }
    val newWeight = remember { mutableStateOf("") }

    val isHeightDialogOpen = remember { mutableStateOf(false) }
    val newHeight = remember { mutableStateOf("") }

    val isPictureDialogOpen = remember { mutableStateOf(false) }
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current

    val isCalorieGoalDialogOpen = remember { mutableStateOf(false) }
    val newCalorieGoal = remember { mutableStateOf("") }

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri ->
            imageUri.value = uri
        }
    val previewBitmap = remember(imageUri.value) {
        imageUri.value?.let { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)
            BitmapFactory.decodeStream(inputStream)?.also {
                inputStream?.close()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text(
                        "Профиль",
                        fontFamily = FontFamily.Serif,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
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

            // Блок с аватаркой и информацией о пользователе
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
                        .clickable { isPictureDialogOpen.value = true },
                    contentAlignment = Alignment.Center
                ) {
                    if (profilePic != null && profilePic != "") {
                        val imageBitmap = decodeBase64ToImage(profilePic)
                        if (imageBitmap != null) {
                            androidx.compose.foundation.Image(
                                bitmap = imageBitmap,
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .background(MaterialTheme.colorScheme.primaryContainer)
                            )
                        } else {
                            Text(
                                text = "Нет фото",
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                fontSize = 24.sp
                            )
                        }
                    } else {
                        Text(
                            text = "Нет фото",
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            fontSize = 24.sp
                        )
                    }
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
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable { isWeightDialogOpen.value = true },
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
                            .background(MaterialTheme.colorScheme.secondaryContainer)
                            .clickable { isHeightDialogOpen.value = true },
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
                            .weight(0.8f)
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
                        val circleColor =
                            if (calorie > maxCalorie) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary
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
                    Button(
                        onClick = { isCalorieGoalDialogOpen.value = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.2f)
                            .padding(vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    ) {
                        var multiplier by remember { mutableStateOf(1f) }
                        Text(
                            text = "Изменить цель",
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = LocalTextStyle.current.copy(
                                fontSize = LocalTextStyle.current.fontSize * multiplier
                            ),
                            onTextLayout = {
                                if (it.hasVisualOverflow) {
                                    multiplier *= 0.99f // you can tune this constant
                                }
                            }
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
                        var multiplier by remember { mutableStateOf(1f) }
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Белки: $protein",
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = LocalTextStyle.current.copy(
                                fontSize = LocalTextStyle.current.fontSize * multiplier
                            ),
                            onTextLayout = {
                                if (it.hasVisualOverflow) {
                                    multiplier *= 0.99f // you can tune this constant
                                }
                            }
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
                        var multiplier by remember { mutableStateOf(1f) }
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Жиры: $fats",
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = LocalTextStyle.current.copy(
                                fontSize = LocalTextStyle.current.fontSize * multiplier
                            ),
                            onTextLayout = {
                                if (it.hasVisualOverflow) {
                                    multiplier *= 0.99f // you can tune this constant
                                }
                            }
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
                        var multiplier by remember { mutableStateOf(1f) }
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Углеводы: $carbs",
                            maxLines = 1,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = LocalTextStyle.current.copy(
                                fontSize = LocalTextStyle.current.fontSize * multiplier
                            ),
                            onTextLayout = {
                                if (it.hasVisualOverflow) {
                                    multiplier *= 0.99f // you can tune this constant
                                }
                            }
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
                            val color =
                                if (calories > maxCalorie) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.primary
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
            // Диалог для выбора веса
            if (isWeightDialogOpen.value) {
                AlertDialog(
                    onDismissRequest = { isWeightDialogOpen.value = false },
                    title = {
                        Text("Выберите вес")
                    },
                    text = {
                        TextField(
                            value = newWeight.value,
                            onValueChange = { newWeight.value = it },
                            label = { Text("Введите вес") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            val weight = newWeight.value.toIntOrNull()
                            if (weight != null && weight in 1..300) {
                                onUpdateWeight(weight)
                                isWeightDialogOpen.value = false
                            }
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { isWeightDialogOpen.value = false }) {
                            Text("Отмена")
                        }
                    }
                )
            }
            if (isHeightDialogOpen.value) {
                AlertDialog(
                    onDismissRequest = { isHeightDialogOpen.value = false },
                    title = {
                        Text("Выберите рост")
                    },
                    text = {
                        TextField(
                            value = newHeight.value,
                            onValueChange = { newHeight.value = it },
                            label = { Text("Введите рост") },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                        )
                    },
                    confirmButton = {
                        Button(onClick = {
                            val height = newHeight.value.toIntOrNull()
                            if (height != null && height in 1..300) {
                                onUpdateHeight(height)
                                isHeightDialogOpen.value = false
                            }
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { isHeightDialogOpen.value = false }) {
                            Text("Отмена")
                        }
                    }
                )
            }
            if (isPictureDialogOpen.value) {
                AlertDialog(
                    onDismissRequest = { isPictureDialogOpen.value = false },
                    title = { Text("Выберите фото профиля") },
                    text = {
                        Column {
                            if (previewBitmap != null) {
                                androidx.compose.foundation.Image(
                                    bitmap = previewBitmap.asImageBitmap(),
                                    contentDescription = "Preview",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )
                            } else {
                                Text("Выберите файл")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(onClick = { launcher.launch(arrayOf("image/*")) }) {
                                Text("Выбрать файл")
                            }
                        }
                    },
                    confirmButton = {
                        Button(onClick = {
                            imageUri.value?.let { uri ->
                                val base64 = encodeImageToBase64(context, uri)
                                if (base64 != null) {
                                    onUpdateProfilePicture(base64)
                                }
                            }
                            isPictureDialogOpen.value = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        Button(onClick = { isPictureDialogOpen.value = false }) {
                            Text("Отмена")
                        }
                    }
                )
            }
        }
        // Диалог для изменения цели по калориям
        if (isCalorieGoalDialogOpen.value) {
            AlertDialog(
                onDismissRequest = { isCalorieGoalDialogOpen.value = false },
                title = {
                    Text("Цель по калориям")
                },
                text = {
                    TextField(
                        value = newCalorieGoal.value,
                        onValueChange = { newCalorieGoal.value = it },
                        label = { Text("Введите новую цель") },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                    )
                },
                confirmButton = {
                    Button(onClick = {
                        val calorieGoal = newCalorieGoal.value.toIntOrNull()
                        if (calorieGoal != null && calorieGoal in 1..10000) {
                            onUpdateCalorieGoal(calorieGoal)
                            isCalorieGoalDialogOpen.value = false
                        }
                    }) {
                        Text("OK")
                    }
                },
                dismissButton = {
                    Button(onClick = { isCalorieGoalDialogOpen.value = false }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, heightDp = 950)
@Preview(showBackground = true, heightDp = 950)
@Composable
fun ProfileContentPreview() {
    CTrackerTheme {
        Scaffold(
            bottomBar = {
                NavigationBottomBar(navController = rememberNavController())
            }) {
            ProfileContent(
                userName = "Имя",
                userWeight = "70 кг",
                calorie = 30000,
                maxCalorie = 2000,
                protein = 120,
                fats = 50,
                carbs = 2020,
                chartData = listOf(
                    2500 to "19.11",
                    1800 to "20.11",
                    1700 to "21.11",
                    1700 to "22.11",
                    1700 to "23.11",
                    1700 to "24.11",
                    1700 to "25.11"
                ),
                height = 180,
                profilePic = "/9j/4AAQSkZJRgABAQEBLAEsAAD/4QCpRXhpZgAASUkqAAgAAAADAA4BAgBfAAAAMgAAABoBBQABAAAAkQAAABsBBQABAAAAmQAAAAAAAABQaWN0dXJlIHByb2ZpbGUgaWNvbi4gSHVtYW4gb3IgcGVvcGxlIHNpZ24gYW5kIHN5bWJvbCBmb3IgdGVtcGxhdGUgZGVzaWduLiBWZWN0b3IgaWxsdXN0cmF0aW9uLiwBAAABAAAALAEAAAEAAAD/4QXSaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wLwA8P3hwYWNrZXQgYmVnaW49Iu+7vyIgaWQ9Ilc1TTBNcENlaGlIenJlU3pOVGN6a2M5ZCI/Pgo8eDp4bXBtZXRhIHhtbG5zOng9ImFkb2JlOm5zOm1ldGEvIj4KCTxyZGY6UkRGIHhtbG5zOnJkZj0iaHR0cDovL3d3dy53My5vcmcvMTk5OS8wMi8yMi1yZGYtc3ludGF4LW5zIyI+CgkJPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6cGhvdG9zaG9wPSJodHRwOi8vbnMuYWRvYmUuY29tL3Bob3Rvc2hvcC8xLjAvIiB4bWxuczpJcHRjNHhtcENvcmU9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBDb3JlLzEuMC94bWxucy8iICAgeG1sbnM6R2V0dHlJbWFnZXNHSUZUPSJodHRwOi8veG1wLmdldHR5aW1hZ2VzLmNvbS9naWZ0LzEuMC8iIHhtbG5zOmRjPSJodHRwOi8vcHVybC5vcmcvZGMvZWxlbWVudHMvMS4xLyIgeG1sbnM6cGx1cz0iaHR0cDovL25zLnVzZXBsdXMub3JnL2xkZi94bXAvMS4wLyIgIHhtbG5zOmlwdGNFeHQ9Imh0dHA6Ly9pcHRjLm9yZy9zdGQvSXB0YzR4bXBFeHQvMjAwOC0wMi0yOS8iIHhtbG5zOnhtcFJpZ2h0cz0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL3JpZ2h0cy8iIHBob3Rvc2hvcDpDcmVkaXQ9IkdldHR5IEltYWdlcyIgR2V0dHlJbWFnZXNHSUZUOkFzc2V0SUQ9IjEzNDEwNDY2NjIiIHhtcFJpZ2h0czpXZWJTdGF0ZW1lbnQ9Imh0dHBzOi8vd3d3LmlzdG9ja3Bob3RvLmNvbS9sZWdhbC9saWNlbnNlLWFncmVlbWVudD91dG1fbWVkaXVtPW9yZ2FuaWMmYW1wO3V0bV9zb3VyY2U9Z29vZ2xlJmFtcDt1dG1fY2FtcGFpZ249aXB0Y3VybCIgcGx1czpEYXRhTWluaW5nPSJodHRwOi8vbnMudXNlcGx1cy5vcmcvbGRmL3ZvY2FiL0RNSS1QUk9ISUJJVEVELUVYQ0VQVFNFQVJDSEVOR0lORUlOREVYSU5HIiA+CjxkYzpjcmVhdG9yPjxyZGY6U2VxPjxyZGY6bGk+UHJhZXdwYWlsaW48L3JkZjpsaT48L3JkZjpTZXE+PC9kYzpjcmVhdG9yPjxkYzpkZXNjcmlwdGlvbj48cmRmOkFsdD48cmRmOmxpIHhtbDpsYW5nPSJ4LWRlZmF1bHQiPlBpY3R1cmUgcHJvZmlsZSBpY29uLiBIdW1hbiBvciBwZW9wbGUgc2lnbiBhbmQgc3ltYm9sIGZvciB0ZW1wbGF0ZSBkZXNpZ24uIFZlY3RvciBpbGx1c3RyYXRpb24uPC9yZGY6bGk+PC9yZGY6QWx0PjwvZGM6ZGVzY3JpcHRpb24+CjxwbHVzOkxpY2Vuc29yPjxyZGY6U2VxPjxyZGY6bGkgcmRmOnBhcnNlVHlwZT0nUmVzb3VyY2UnPjxwbHVzOkxpY2Vuc29yVVJMPmh0dHBzOi8vd3d3LmlzdG9ja3Bob3RvLmNvbS9waG90by9saWNlbnNlLWdtMTM0MTA0NjY2Mi0/dXRtX21lZGl1bT1vcmdhbmljJmFtcDt1dG1fc291cmNlPWdvb2dsZSZhbXA7dXRtX2NhbXBhaWduPWlwdGN1cmw8L3BsdXM6TGljZW5zb3JVUkw+PC9yZGY6bGk+PC9yZGY6U2VxPjwvcGx1czpMaWNlbnNvcj4KCQk8L3JkZjpEZXNjcmlwdGlvbj4KCTwvcmRmOlJERj4KPC94OnhtcG1ldGE+Cjw/eHBhY2tldCBlbmQ9InciPz4K/+0AolBob3Rvc2hvcCAzLjAAOEJJTQQEAAAAAACFHAJQAAtQcmFld3BhaWxpbhwCeABfUGljdHVyZSBwcm9maWxlIGljb24uIEh1bWFuIG9yIHBlb3BsZSBzaWduIGFuZCBzeW1ib2wgZm9yIHRlbXBsYXRl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/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/df/AP8A/wD/AP8A/wD/APxP1/8A/wD/AP8A/wD/AP8A+/8An/8A/wD/AP8A/wD/AP3/AP8Af/8A/wD/AP8A/wD/AD//AP8A/wD/AP8A/wD/AP8A+/8A/wD7/wD/AP8A/wD/AP8Av/8A/wDP/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A7/8A/wD7/wD/AP8A/wD/AP8Af/8A/wDf/wD/AP8A/wD/AP8A/wD/AP8Af/8A/wD/AP8A/wD/AP8A/wD7/wD/AP8A/wD/AP8A/wD/AP8A3/8A/wD/AP8A/wDv/wD/AP8A/wD/AP8A/wD/AP8Av/8A/wD3/wD/AP8A/wD/AP3/AP8A/wB//wD/AP8A/wD/AO//AP8A+/8A/wD/AP8A/wD/AD//AP8A/wD/AP8A/wD/AP8A9f8A/wD9/wD/AP8A/wD/AP8Av/8A/wD/AP8A/wD/AP8A/wD9/wD/AP8A/wD/AP8A/wD/AP8A7/8A/wD/AP8A/wD/AP8A/wD/AH//AP8A7/8A/wD/AP8A/wD7/wD/AP8A/wD/AP8A/wD/AP8A7/8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/APj/AP8A/f8A/wD/AP8A/wD/AP8A/wD/AO//AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AH//AP8A/wD/AP8A/wD/AP8A9/8A/wD/AP8A/wD/AP8A/wD/AL//AP8A/wD/AP8A/wD/AP8A+/8A/wD/AP8A/wD/AP8A3/8A/wD/AP8A/wD/AP8A/wD9/wD/AP8A/wD/AP8A/wD/AP8A/wD/APf/AP8A/wD/AP8A/wD/AH//AL//AP8A/wD/AP8A/wD/AP8A/v8A/wD/AP8A/wD/AP8Av/8A9/8A/wD/AP8A/wD/APP/AP8A1/8A/wD/AP8A/wD+/wD/AP8A/wD/AP8A/wD/AP8Ax/8A/wD/AL//AP8A/wD/APn/AP8A/wD/AD//AP8A/wD/AH//AP8A/wD/AN//AP8A/wB//wD/AP8A/wD+v/8A/wD3/wD/AP8A/wD/AP4//wD9/wD/AP8A/wD/AP8A/f8A/wDv/wD/AP8A/wD/AP8A+f8A/P8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wC//wD/AP8A/wD/AP8A/wD/AP3/APf/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8Av/8A/wD/AP8A/wD/AP8A/wD9/wD/AP8A/wD/AP8A/wD/AP8A7/7/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/f8Af/8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wB//wD/AP8A/wD/AP8A/wD/AP3+/wD/AP8A/wD/AP8A/wD/AP8A9/8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD/AP8A/wD7/wD/AP8A/wD/AP8A/wD/AP8A39//AP8A/wD/AP8A/wD/AP8AfgAAAAAAAAAAB/8A/8QAKxABAAEEAQMEAQQCAwAAAAAAAREAITFAUSBBYTBQYHGBEJGhwbHR4fDx/9oACAEBAAE/EPhmHsoC7wAzYj+b0MosQEXWYg/akYClJwDNOwBKouHNMKAQQcjP7Y9kw9kcUAyxMOYKC0JsQEc27m+L0jGI2CSX/kpw8gHLxA4xE2/NC7LAy5bvsmHssBywi7NqXFpCCUwfn6oNcLUbuHP1NQ8cjQSKsJl/7z7Jh8Nw+G4fDcPhuHw3D4bh7KhAV4KuMD5zQf8ASUF3v5aW7j6a76Psq82ObvYsPYMufQr/AM6lYT7H6QknB3ahRHXzzyWauyjx3pCAjw7+G9GJuXsVcP61ACAA8fogIQThqECPHb0wYJq+MOHNIqEROzu4bsbPwd2jQAHY0gbL9kzSMt+wbmG2CCVwUcf0pqoBEke1Xwz3HG3ht2g4PBsSYb8nG1htSpjL9bIMEiQ0ivZ2cNr7kwbV4u9nZw2vspO156X2cNrxABteZCKxsYbIgcpuCDwtjDZEh4bgjYmGyJ+73QMNmRfTcUp5bGGyMImSiMYSdpYFe1KR5djDaizuo2pX3bGzhtTDwf52v807OG0xjIzRGcJOzM+wwbOGyCCVwUa4+BtRw1QxPbY8JlZ2cNkYXODayJOOad2DHmsMbGGzg+3cMEctjDZkLjcKR5bGGzHzydtSPBNLKvOxhsyViYdue8kbOG1ErhZ2oBd7uzhtXfi387UMYLGzhtJAZGSiZS5fxSMqCba8inAbWG5KezZ1jMQYJoowIwG1huDDJRi9zUFHsTSqq5dvDdkDKk+tSNGVL9bmG7PHDZ08Etca4Prcw3pw5LOlfT+k3cN64W3+eiiaxTDM7uG8KMmSkocoToXTwmN7DfMF49urDeEg5dE3vDvYb0A8NH6kxvYb052DRtzJfew3r2ncdFJIaRuzI+N3DcCWCmEDGP8AbTwUTDxUCNuyYdzDbVsg5Yq+n2NVEAj2aQn9xSKAidnaw2UIZ81FrLjtQAQEGwZH5u5V0/tUkMOxhr4mLmyr6p8YKAQADsbnNXJmr1E4bNNQ32NbDTehl4K7IPm9Z7/BWIzzHsKCQgnmufvwr/SCriscl9PDRvpA5bVfEvjBQsAHj2jtC8lmr4B4bNKwy86GHrIwi8FXxB4y1fZHlv7abBDzV+RcNyuIeS562HqZrjlxRLzODFFQQ8e5Xlk5sq92/wCaRUIjw+nh6VtIO6q8p5mse7CwTThRfKkRhIfRw9FsQcvNAggO3vQuA/zSIiEyehh6CGcZXgo0EBj3y3fJ5PQw9CNU7j79MhyHXh1xxwXff5wZv68OuBHdB7+giOGnV7MdWHX4XL/AI3sHVh1eVm/wGN5OrDql4L4D52OrDqneT4F42bdOHV4WPgUZwdOHT5ST4HLzUdOHTdeE/A4f4npw6Y+ex8DSSHvU2dmOjDp++3+Cfux0YdASgd6gB2I+Cfxvow6I7wz8FlXkPRh0S8Uj4L5aHow6IeY+DeHl+uFQqFQogTj4MH2BUKhQRX//2Q==",
                onUpdateWeight = {},
                onUpdateHeight = {},
                onUpdateProfilePicture = {},
                onUpdateCalorieGoal = {}
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun decodeBase64ToImage(base64: String): ImageBitmap? {
    return try {
        val decodedBytes = Base64.getDecoder().decode(base64)
        val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        bitmap?.asImageBitmap()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun encodeImageToBase64(context: Context, uri: Uri): String? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)

        // Проверка ориентации и исправление
        val correctedBitmap = fixImageOrientation(context, uri, bitmap)

        val byteArrayOutputStream = ByteArrayOutputStream()
        correctedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val bytes = byteArrayOutputStream.toByteArray()

        Base64.getEncoder().encodeToString(bytes)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

private fun fixImageOrientation(context: Context, uri: Uri, bitmap: Bitmap): Bitmap {
    val inputStream = context.contentResolver.openInputStream(uri)
    val exif = inputStream?.let { ExifInterface(it) }
    inputStream?.close()

    val orientation = exif?.getAttributeInt(
        ExifInterface.TAG_ORIENTATION,
        ExifInterface.ORIENTATION_NORMAL
    ) ?: ExifInterface.ORIENTATION_NORMAL

    val rotationDegrees = when (orientation) {
        ExifInterface.ORIENTATION_ROTATE_90 -> 90
        ExifInterface.ORIENTATION_ROTATE_180 -> 180
        ExifInterface.ORIENTATION_ROTATE_270 -> 270
        else -> 0
    }

    return if (rotationDegrees != 0) {
        val matrix = Matrix()
        matrix.postRotate(rotationDegrees.toFloat())
        Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    } else {
        bitmap
    }
}

