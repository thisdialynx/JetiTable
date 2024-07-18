package lnx.jetitable.screens.loading

import android.app.Application
import android.util.Log
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import lnx.jetitable.navigation.Auth
import lnx.jetitable.navigation.Loading
import lnx.jetitable.prefdatastore.DataStoreManager

@Composable
fun LoadingScreen(navController: NavHostController) {
    val loadingViewModel: LoadingViewModel = viewModel()
    val isAuthorized = loadingViewModel.isAuthorized

    LaunchedEffect(isAuthorized) {
        when(isAuthorized) {
            true -> {
                navController.navigate(Auth.route) {
                    popUpTo(Loading.route) { inclusive = true }
                }
            }
            false -> {
                navController.navigate(Auth.route) {
                    popUpTo(Loading.route) { inclusive = true }
                }
            }
            null -> loadingViewModel.checkToken()
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
    private val dataStore = DataStoreManager(context)
    var isAuthorized by mutableStateOf<Boolean?>(null)
        private set

    fun checkToken() {
        viewModelScope.launch {
            val token = dataStore.getToken().first()
            isAuthorized = !token.isNullOrEmpty()
            Log.d("LoadingViewModel", "isAuthorized: $isAuthorized")
        }
    }

}


@Preview(showBackground = true)
@Composable
private fun LoadingScreenPreview() {
    LoadingScreen(navController = NavHostController(LocalContext.current))
}