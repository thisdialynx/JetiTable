@file:OptIn(ExperimentalMaterial3Api::class)

package lnx.jetitable.features.home.presentation

import android.icu.util.Calendar
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.features.home.presentation.elements.HomeScreenState
import lnx.jetitable.features.home.presentation.elements.schedule.ClassScheduleCard
import lnx.jetitable.features.home.presentation.elements.schedule.ExamScheduleCard
import lnx.jetitable.ui.components.ThemedSnackbar
import lnx.jetitable.ui.icons.google.Settings

@Composable
fun HomeLayout(
    screenState: HomeScreenState,
    snackbarHostState: SnackbarHostState,
    titleResId: Int,
    isRefreshing: Boolean,
    onDateUpdate: (Calendar) -> Unit,
    onForwardDateShift: () -> Unit,
    onBackwardDateShift: () -> Unit,
    onPresenceVerify: (ClassUiData) -> Unit,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onSettingsNavigate: () -> Unit,
    onRefresh: () -> Unit
) {
    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                ThemedSnackbar(it)
            }
        },
        topBar = {
            LargeTopAppBar(
                title = {
                    Crossfade(targetState = titleResId, label = "") { currentTitle ->
                        Text(text = stringResource(id = currentTitle))
                    }
                },
                actions = {
                    IconButton(onClick = onSettingsNavigate) {
                        Icon(
                            imageVector = Settings,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
                    rememberTopAppBarState()
                ),
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = onRefresh,
            modifier = Modifier.padding(innerPadding)
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
                item {
                    ClassScheduleCard(
                        classList = screenState.classList,
                        attendanceList = screenState.attendanceListState,
                        studentFullName = screenState.studentFullName,
                        dateState = screenState.dateState,
                        onAttendanceListRequest = onAttendanceListRequest,
                        onPresenceVerify = onPresenceVerify,
                        onDateUpdate = onDateUpdate,
                        onBackwardDateShift = onBackwardDateShift,
                        onForwardDateShift = onForwardDateShift
                    )
                }
                item {
                    ExamScheduleCard(screenState.examList)
                }
            }
        }
    }
}
