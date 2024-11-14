import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeView(viewModel: MainViewModel) {
    val items = listOf("Task 1", "Task 2", "Task 3", "Task 4","Task 1", "Task 2", "Task 3", "Task 4","Task 1", "Task 2", "Task 3", "Task 4","Task 1", "Task 2", "Task 3", "Task 4","Task 1", "Task 2", "Task 3", "Task 4")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end= 16.dp, bottom= 0.dp)
    ) {
        // Заголовок и отступ в начале LazyColumn
        item {
            Text(
                text = "Home",
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Элементы списка
        items(items) { item ->
            Text(
                text = item,
                fontSize = 40.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp)
            )
        }
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewHomeView() {
    val viewModel = MainViewModel()
    HomeView(viewModel=viewModel)
}