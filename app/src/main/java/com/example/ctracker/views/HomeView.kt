import android.annotation.SuppressLint
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ctracker.entity.Meal
import com.example.ctracker.viewmodel.HomeViewModel
import com.example.ctracker.viewmodel.MealModel
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: HomeViewModel, navController: NavController) {
    HomeContent(
        calorie = viewModel.calorie.value,
        maxCalories = viewModel.maxCalories.value,
        progress = viewModel.calorie.value / viewModel.maxCalories.value.toFloat(),
        progressColor = if (viewModel.calorie.value > viewModel.maxCalories.value) Color(0xFFFF9800) else Color(0xFF4CAF50),
        mealList = viewModel.mealList,
        onNavigateToSearch = { index -> navController.navigate("search/$index") }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    calorie: Int,
    maxCalories: Int,
    progress: Float,
    progressColor: Color,
    mealList: List<MealModel>,
    onNavigateToSearch: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                ),
                title = {
                    Text("CTracker")
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
                    onAddProductClick = { onNavigateToSearch(index) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}
@Composable
fun MealOfDay(
    mealModel: MealModel,
    onAddProductClick: () -> Unit
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
                color = MaterialTheme.colorScheme.secondaryContainer,
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
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "${mealModel.name} (${mealModel.totalCalories} ккал)", // Отображение имени и калорий
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                textAlign = TextAlign.Start,
                modifier = Modifier.weight(1f)
            )
        }
        Button(onClick = onAddProductClick) {
            Text(text = "+")
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
                ProductItem(product = product)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@Composable
fun ProductItem(product: Meal) {
    Button(
        onClick = { /* TODO: обработка нажатия на продукт */ },
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${product.calories.toInt()} ккал",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                        .wrapContentWidth(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Вес: ${product.quantity.toInt()} г | Б: ${product.protein} г | Ж: ${product.fats} г | У: ${product.carbs} г",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Preview(showBackground = true)
@Composable
fun PreviewHomeContent() {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DAY_OF_YEAR, 0)
    val date: Date = calendar.time

    val breakfastProducts = listOf(
        Meal(1, 0, "Яичница", 70F, 6.0F, 5.0F, 1.0F, 10.0F, date),
        Meal(2, 0, "Тост", 120F, 4.0F, 2.0F, 20.0F, 10.0F, date)
    )

    val lunchProducts = listOf(
        Meal(3, 1, "Суп", 200F, 10.0F, 8.0F, 30.0F, 15.0F, date),
        Meal(4, 1, "Салат", 150F, 5.0F, 7.0F, 12.0F, 10.0F, date)
    )

    val dinnerProducts = listOf(
        Meal(5, 2, "Паста", 400F, 12.0F, 10.0F, 50.0F, 20.0F, date),
        Meal(6, 2, "Курица", 250F, 30.0F, 5.0F, 2.0F, 15.0F, date)
    )

    val additionalProducts = listOf(
        Meal(7, 3, "Шоколад", 120F, 2.0F, 8.0F, 10.0F, 5.0F, date),
        Meal(8, 3, "Орехи", 150F, 4.0F, 10.0F, 5.0F, 7.0F, date)
    )

    val mealList = listOf(
        MealModel("Завтрак", breakfastProducts, mutableStateOf(true)) { },
        MealModel("Обед", lunchProducts, mutableStateOf(false)) { },
        MealModel("Ужин", dinnerProducts, mutableStateOf(false)) { },
        MealModel("Другое", additionalProducts, mutableStateOf(false)) { }
    )

    HomeContent(
        calorie = 1200,
        maxCalories = 2000,
        progress = 0.6f,
        progressColor = Color.Green,
        mealList = mealList,
        onNavigateToSearch = {}
    )
}
