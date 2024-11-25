import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _logoutClicked = MutableLiveData(false)
    val logoutClicked: LiveData<Boolean> = _logoutClicked


    // Метод для установки состояния выхода
    fun onLogoutClick() {
        _logoutClicked.value = true
        SharedPreferencesManager.saveString("UserID", "-1")
        SharedPreferencesManager.saveInt("fats", 0)        // Обнуляем жиры
        SharedPreferencesManager.saveInt("carbs", 0)       // Обнуляем углеводы
        SharedPreferencesManager.saveInt("protein", 0)     // Обнуляем белки
        SharedPreferencesManager.saveInt("maxCalorie", 2500) // Устанавливаем стандартное значение калорий
        SharedPreferencesManager.saveInt("calorie", 0)     // Обнуляем текущие калории

        SharedPreferencesManager.saveString("userName", "Unknown User") // Сбрасываем имя пользователя
        SharedPreferencesManager.saveInt("userHeight", 0)             // Обнуляем рост
        SharedPreferencesManager.saveInt("userWeight", 0)             // Обнуляем вес
        SharedPreferencesManager.saveString("profilePic", "")         // Удаляем ссылку на изображение

        // Дополнительно сбросить данные графиков
        SharedPreferencesManager.saveString("chartData", "")
    }

    // Метод для сброса состояния выхода
    fun resetLogoutState() {
        _logoutClicked.value = false


    }
}
