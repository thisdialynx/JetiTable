package lnx.jetitable.ui.screens

import android.app.Application
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import lnx.jetitable.core.UserSettings
import lnx.jetitable.timetable.api.login.AuthViewModel

@Composable
fun LoadingScreen(navController: NavHostController) {
    val loadingViewModel: LoadingViewModel = viewModel()
    val isAuthorized = loadingViewModel.isAuthorized

    LaunchedEffect(isAuthorized) {
        when(isAuthorized) {
            true -> {
                navController.navigate("Home") {
                    popUpTo("Loading") { inclusive = true }
                }
            }
            false -> {
                navController.navigate("Auth") {
                    popUpTo("Loading") { inclusive = true }
                }
            }
            null -> {
                loadingViewModel.checkToken()
            }
        }
    }
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                strokeCap = StrokeCap.Round
            )
        }
    }
}

class LoadingViewModel(application: Application) : AndroidViewModel(application) {
    private val context
        get() = getApplication<Application>().applicationContext
    private val userSettings = UserSettings(context)
    var isAuthorized by mutableStateOf<Boolean?>(null)
        private set

    fun checkToken() {
        val token = userSettings.getAuthToken()
        isAuthorized = !token.isNullOrEmpty()
    }

}


@Preview(showBackground = true)
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen(navController = NavHostController(LocalContext.current))
}