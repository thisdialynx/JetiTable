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
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import lnx.jetitable.viewmodel.LoadingViewModel

@Composable
fun LoadingScreen(
    viewModel: LoadingViewModel = hiltViewModel(),
    onAuthNavigate: () -> Unit, onHomeNavigate: () -> Unit
) {
    val isAuthorized = viewModel.isAuthorized

    LaunchedEffect(isAuthorized) {
        when(isAuthorized) {
            true -> {
                viewModel.startSyncService()
                onHomeNavigate()
            }
            false -> { onAuthNavigate() }
            null -> viewModel.checkToken()
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