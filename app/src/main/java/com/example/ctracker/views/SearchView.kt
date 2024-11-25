package com.example.ctracker.views

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.ctracker.entity.Food
import com.example.ctracker.viewmodel.SearchViewModel
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.ctracker.ui.theme.CTrackerTheme

@Composable
fun SearchView(viewModel: SearchViewModel, navController: NavController) {
    SearchContent(
        query = viewModel.query.value,
        results = viewModel.results.value,
        hasSearched = viewModel.hasSearched.value,
        onQueryChange = viewModel::onQuerySearch,
        onSearch = viewModel::search,
        onItemClick = { index ->
            print("here")
            val mealType: Int = viewModel.mealType // mealType берется из ViewModel
            navController.navigate("additem/${mealType}/$index")
        },
        isLoading = viewModel.isLoading.value
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchContent(
    query: String,
    results: List<Food>,
    hasSearched: Boolean,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onItemClick: (Int) -> Unit,
    isLoading: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
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
            Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp), Arrangement.Top, Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = { Text("Введите название продукта") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        onSearch()
                    }
                ),
                modifier = Modifier.fillMaxWidth(),
                isError = hasSearched && results.isEmpty()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (hasSearched) {
                if (isLoading) {
                    // Показываем индикатор загрузки
                    CircularProgressIndicator(
                        modifier = Modifier.size(100.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else if (results.isEmpty()) {
                    // Показываем текст, если продукты не найдены
                    Text(
                        text = "Продуктов нет",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Top,

                    ) {
                        results.take(10).forEach { product ->
                            ProductItem(product) { onItemClick(product.id) }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Food,
    onAddProductClick: () -> Unit
) {
    Button(
        onClick = { onAddProductClick() },
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
                    text = "${product.calories} ккал",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .weight(1f)
                        .wrapContentWidth(Alignment.End)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Б: ${product.protein} г | Ж: ${product.fat} г | У: ${product.carb} г",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(showBackground = true)
@Composable
fun SearchContentPreview() {
    CTrackerTheme {
        Scaffold(
            bottomBar = {
                NavigationBottomBar(navController = rememberNavController())
            }) {
            SearchContent(
                query = "",
                results = listOf(
                    Food(1, "Яблоко ASdoi asiodj adjioasdj ioasdjo idjoia jdoiajs disi osjadoi jasiodja", 52F, 0.2f, 14f, 0.3f),
                    Food(2, "Банан", 89F, 0.3f, 23f, 1.1f),
                    Food(3, "Куриное филе", 165F, 3.6f, 0f, 31f),
                    Food(4, "Овсянка", 68F, 1.4f, 12f, 2.4f),
                    Food(5, "Яйцо вареное", 155F, 11f, 1.1f, 13f),
                    Food(6, "Молоко", 42F, 1f, 5f, 3.4f),
                    Food(7, "Рис", 130F, 0.3f, 28f, 2.7f),
                    Food(8, "Гречка", 110F, 1.6f, 20f, 4.2f),
                    Food(9, "Помидор", 18F, 0.2f, 3.9f, 0.9f),
                    Food(10, "Огурец", 15F, 0.1f, 3.6f, 0.7f)
                ),
                hasSearched = true,
                onQueryChange = {},
                onSearch = {},
                onItemClick = {},
                isLoading = false
            )
        }
    }
}
