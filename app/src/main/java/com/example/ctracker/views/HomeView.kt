import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ctracker.entity.Meal
import com.example.ctracker.ui.theme.CTrackerTheme
import com.example.ctracker.viewmodel.HomeViewModel
import com.example.ctracker.viewmodel.MealModel
import com.example.ctracker.views.NavigationBottomBar
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: HomeViewModel, navController: NavController) {
    HomeContent(
        calorie = viewModel.calorie.value,
        maxCalories = viewModel.maxCalories.value,
        mealList = viewModel.mealList,
        onNavigateToSearch = { index -> navController.navigate("search/$index") },
        onNavigateToEditMeal = { mealId -> navController.navigate("editmeal/$mealId") },
        getReccomendations = { viewModel.getReccomendations() },
        recommendations = viewModel.recommendations.value
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    calorie: Int,
    maxCalories: Int,
    mealList: List<MealModel>,
    onNavigateToSearch: (Int) -> Unit,
    onNavigateToEditMeal: (Int) -> Unit,
    getReccomendations: () -> Unit,
    recommendations : String// Добавляем обработчик для перехода на экран редактирования
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                title = {
                    Text("CTracker", fontFamily = FontFamily.Serif, color = MaterialTheme.colorScheme.onPrimary)
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Калории: $calorie / $maxCalories")

            Spacer(modifier = Modifier.height(8.dp))
            val progressColor = if (calorie > maxCalories) Color(0xFFFFD1A4) else Color(0xFF4CAF50)
            val progress = calorie.toFloat() / maxCalories.toFloat()
            LinearProgressIndicator(
                progress = progress.coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = progressColor,
                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f),
            )

            Spacer(modifier = Modifier.height(16.dp))

            mealList.forEachIndexed { index, mealModel ->
                MealOfDay(
                    mealModel = mealModel,
                    onAddProductClick = { onNavigateToSearch(index) },
                    onEditProductClick = onNavigateToEditMeal // Передаём обработчик для редактирования
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            Button(
                onClick = {
                    getReccomendations()
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(text = "Создать рекомендации", color = MaterialTheme.colorScheme.onPrimary)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = recommendations,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(16.dp),
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MealOfDay(
    mealModel: MealModel,
    onAddProductClick: () -> Unit,
    onEditProductClick: (Int) -> Unit // Добавляем обработчик для редактирования
) {
    val icon: ImageVector = if (mealModel.isProductListVisible.value) {
        Icons.Filled.KeyboardArrowUp
    } else {
        Icons.Filled.KeyboardArrowDown
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = mealModel.toggleProductList)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "${mealModel.name} (${mealModel.totalCalories} ккал)", // Отображение имени и калорий
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
        Button(
            onClick = onAddProductClick,
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary)
        ) {
            Text(text = "+", color = MaterialTheme.colorScheme.onPrimaryContainer)
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (mealModel.isProductListVisible.value) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .verticalScroll(rememberScrollState())
        ) {
            mealModel.productList.forEach { product ->
                ProductItem(product = product, onEditProductClick = onEditProductClick)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun ProductItem(product: Meal, onEditProductClick: (Int) -> Unit) {
    Button(
        onClick = { onEditProductClick(product.id) }, // Передаём ID продукта
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                var multiplier by remember { mutableStateOf(1f) }
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f),
                    onTextLayout = {
                        if (it.hasVisualOverflow) {
                            multiplier *= 0.99f // you can tune this constant
                        }
                    },
                    maxLines = 2,
                )

                Text(
                    text = "${product.calories.toInt()} ккал",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Вес: ${product.quantity} г | Б: ${product.protein.format(1)} г | Ж: ${product.fats.format(1)} г | У: ${product.carbs.format(1)} г",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Extension function for formatting floats
private fun Float.format(digits: Int) = "%.${digits}f".format(this)

@SuppressLint("UnrememberedMutableState", "UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun PreviewHomeContent() {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 0)
    val date: Date = calendar.time

    val breakfastProducts = listOf(
        Meal(1, 0, "Яичница", 70F, 6.0F, 5.0F, 1.0F, 10, date.toString()),
        Meal(2, 0, "Тост", 120F, 4.0F, 2.0F, 20.0F, 10, date.toString())
    )

    val lunchProducts = listOf(
        Meal(3, 1, "Суп", 200F, 10.0F, 8.0F, 30.0F, 15, date.toString()),
        Meal(4, 1, "Салат", 150F, 5.0F, 7.0F, 12.0F, 10, date.toString())
    )

    val dinnerProducts = listOf(
        Meal(5, 2, "Паста", 400F, 12.0F, 10.0F, 50.0F, 20, date.toString()),
        Meal(6, 2, "Курица", 250F, 30.0F, 5.0F, 2.0F, 10, date.toString())
    )

    val additionalProducts = listOf(
        Meal(7, 3, "Шоколад", 120F, 2.0F, 8.0F, 10.0F, 5, date.toString()),
        Meal(8, 3, "Орехи", 150F, 4.0F, 10.0F, 5.0F, 7, date.toString())
    )

    val mealList = listOf(
        MealModel("Завтрак", breakfastProducts, mutableStateOf(true)) { },
        //MealModel("Обед", lunchProducts, mutableStateOf(false)) { },
        //MealModel("Ужин", dinnerProducts, mutableStateOf(false)) { },
        //MealModel("Другое", additionalProducts, mutableStateOf(false)) { }
    )
    CTrackerTheme {
        Scaffold(
            bottomBar = {
                NavigationBottomBar(navController = rememberNavController())
            }) {
            HomeContent(
                calorie = 2200,
                maxCalories = 2000,
                mealList = mealList,
                onNavigateToSearch = {},
                onNavigateToEditMeal = {},
                getReccomendations = {},
                recommendations = "bebra please eat more\n bebra\nasdfpasdokfopasdkfopasdkfopfopkasdksdapfok asdf oaskdf "
            )
        }
    }
}
