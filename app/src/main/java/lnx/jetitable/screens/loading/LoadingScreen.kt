package lnx.jetitable.screens.loading

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.navigation.Auth
import lnx.jetitable.navigation.Home
import lnx.jetitable.navigation.Loading
import lnx.jetitable.viewmodel.LoadingViewModel

@Composable
fun LoadingScreen(navController: NavHostController) {
    val loadingViewModel: LoadingViewModel = viewModel()
    val isAuthorized = loadingViewModel.isAuthorized

    LaunchedEffect(isAuthorized) {
        when(isAuthorized) {
            true -> {
                navController.navigate(Home.route) {
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

    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { CircularProgressIndicator(strokeCap = StrokeCap.Round) }
    }
}