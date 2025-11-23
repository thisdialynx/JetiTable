@file:OptIn(ExperimentalMaterial3Api::class)

package lnx.jetitable.screens.home

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
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lnx.jetitable.R
import lnx.jetitable.api.timetable.data.query.AttendanceListData
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.screens.home.elements.datepicker.DateState
import lnx.jetitable.screens.home.elements.schedule.ClassScheduleCard
import lnx.jetitable.screens.home.elements.schedule.ExamScheduleCard
import lnx.jetitable.ui.components.AppSnackbar
import lnx.jetitable.ui.icons.google.Settings as SettingsIcon

@Composable
fun HomeUI(
    dateState: DateState,
    classList: DataState<out List<ClassUiData>>,
    examList: DataState<out List<ExamNetworkData>>,
    attendanceList: DataState<out List<AttendanceListData>>,
    studentFullName: String,
    connectionState: DataState<out Boolean>,
    notificationTipState: Boolean,
    onDateUpdate: (Calendar) -> Unit,
    onForwardDateShift: () -> Unit,
    onBackwardDateShift: () -> Unit,
    onPresenceVerify: (ClassUiData) -> Unit,
    onAttendanceListRequest: (ClassUiData) -> Unit,
    onSettingsNavigate: () -> Unit,
    onNotificationsNavigate: () -> Unit,
    onSnackbarDismiss: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var title by rememberSaveable { mutableIntStateOf(R.string.welcome_title) }
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        delay(3000)
        title = R.string.home_screen
    }

    LaunchedEffect(notificationTipState) {
        if (notificationTipState) {
            val result = snackbarHostState
                .showSnackbar(
                    message = context.getString(R.string.notification_tip_description),
                    actionLabel = context.getString(R.string.answer_yes),
                    withDismissAction = true
                )
            when (result) {
                SnackbarResult.Dismissed -> {
                    onSnackbarDismiss()
                }
                SnackbarResult.ActionPerformed -> {
                    onNotificationsNavigate()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                AppSnackbar(it)
            }
        },
        topBar = {
            LargeTopAppBar(
                title = {
                    Crossfade(targetState = title, label = "") { currentTitle ->
                        Text(text = stringResource(id = currentTitle))
                    }
                },
                actions = {
                    IconButton(onClick = { onSettingsNavigate() }) {
                        Icon(
                            imageVector = SettingsIcon,
                            contentDescription = null
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection)
        ) {
            item {
                ClassScheduleCard(
                    classList = classList,
                    attendanceList = attendanceList,
                    studentFullName = studentFullName,
                    dateState = dateState,
                    onAttendanceListRequest = onAttendanceListRequest,
                    onPresenceVerify = onPresenceVerify,
                    onDateUpdate = onDateUpdate,
                    onBackwardDateShift = onBackwardDateShift,
                    onForwardDateShift = onForwardDateShift,
                    connectionState = connectionState
                )
            }
            item {
                ExamScheduleCard(examList)
            }
        }
    }
}
