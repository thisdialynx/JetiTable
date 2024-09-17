package lnx.jetitable.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import lnx.jetitable.R
import lnx.jetitable.navigation.About
import lnx.jetitable.navigation.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController) {
    val homeViewModel: HomeViewModel = viewModel()
    val appBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(appBarState)
    val lessonList = homeViewModel.dailyLessonList
    val selectedDate = homeViewModel.selectedDate

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = { Text(text = stringResource(id = R.string.welcome_title)) },
                actions = {
                    IconButton(onClick = { navController.navigate(Settings.route) }) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.google.Settings,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = { navController.navigate(About.route) }) {
                        Icon(
                            imageVector = lnx.jetitable.ui.icons.google.Info,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(horizontal = 16.dp)) {
            ScheduleCard(lessonList = lessonList, selectedDate = selectedDate, homeViewModel = homeViewModel)
        }
    }
}