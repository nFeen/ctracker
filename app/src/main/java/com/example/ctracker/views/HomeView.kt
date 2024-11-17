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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(viewModel: HomeViewModel, navController: NavController) {
    val calorie = viewModel.calorie
    val maxCalories = viewModel.maxCalories

    val progress = calorie.value / maxCalories.value.toFloat()

    val progressColor =
        if (calorie.value > maxCalories.value) Color(0xFFFF9800)
        else Color(0xFF4CAF50)

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
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Калории: ${calorie.value} / ${maxCalories.value}")

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

            viewModel.mealList.forEachIndexed { index, mealModel ->
                MealOfDay(
                    isVisibleItems = mealModel.isProductListVisible.value,
                    toggleProductList = mealModel.toggleProductList,
                    productList = mealModel.productList,
                    name = mealModel.name,
                    onAddProductClick = {
                        navController.navigate("search/$index")
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MealOfDay(
    isVisibleItems: Boolean,
    toggleProductList: () -> Unit,
    productList: List<Meal>,
    name: String,
    onAddProductClick: () -> Unit
) {
    val icon: ImageVector = if (isVisibleItems) {
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
            .clickable(onClick = toggleProductList)
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
                text = name,
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

    if (isVisibleItems) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 200.dp)
                .verticalScroll(rememberScrollState())
        ) {
            productList.forEach { product ->
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
                modifier = Modifier.fillMaxWidth(), // Растягиваем Row на всю ширину
                horizontalArrangement = Arrangement.SpaceBetween // Распределяем элементы по краям
            ) {
                Text(
                    text = product.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurface, // Текстовый цвет
                    modifier = Modifier.weight(1f) // Левый текст занимает свою часть пространства
                )
                Text(
                    text = "${product.calories.toInt()} ккал",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface, // Текстовый цвет
                    modifier = Modifier.weight(1f) // Правый текст занимает свою часть пространства
                        .wrapContentWidth(Alignment.End) // Выравниваем текст по правому краю
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Вес: ${product.quantity.toInt()} г | Б: ${product.protein} г | Ж: ${product.fats} г | У: ${product.carbs} г",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant // Более мягкий цвет текста
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeView() {
    val viewModel = HomeViewModel()
    val navController = rememberNavController()
    HomeView(viewModel = viewModel, navController = navController)
}
