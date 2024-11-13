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
    }

    // Метод для сброса состояния выхода
    fun resetLogoutState() {
        _logoutClicked.value = false
    }
}
