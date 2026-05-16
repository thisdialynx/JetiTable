@file:OptIn(ExperimentalMaterial3Api::class)

package lnx.jetitable.features.home.presentation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.delay
import lnx.jetitable.R
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.features.home.domain.models.ScheduleResult
import lnx.jetitable.features.home.presentation.elements.HomeScreenState
import lnx.jetitable.features.home.presentation.elements.datepicker.DateState

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onSettingsNavigate: () -> Unit,
    onNotificationsNavigate: () -> Unit
) {
    val screenState by viewModel.screenState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle(false)
    val snackbarHostState = remember { SnackbarHostState() }

    var titleResId by rememberSaveable { mutableIntStateOf(R.string.welcome_title) }
    val notifTipDescResId = stringResource(R.string.notification_tip_description)
    val answerYesResId = stringResource(R.string.answer_yes)

    LaunchedEffect(Unit) {
        delay(3000)
        titleResId = R.string.home_screen
    }

    LaunchedEffect(screenState.notifTipState) {
        if (screenState.notifTipState) {
            val result = snackbarHostState
                .showSnackbar(
                    message = notifTipDescResId,
                    actionLabel = answerYesResId,
                    withDismissAction = true
                )
            when (result) {
                SnackbarResult.Dismissed -> {
                    viewModel.disableNotificationTip()
                }

                SnackbarResult.ActionPerformed -> {
                    viewModel.disableNotificationTip()
                    onNotificationsNavigate()
                }
            }
        }
    }

    HomeLayout(
        screenState = screenState,
        snackbarHostState = snackbarHostState,
        titleResId = titleResId,
        isRefreshing = isRefreshing,
        onDateUpdate = viewModel::onDateUpdate,
        onForwardDateShift = viewModel::onForwardDayShift,
        onBackwardDateShift = viewModel::onBackwardDayShift,
        onPresenceVerify = viewModel::onPresenceVerify,
        onAttendanceListRequest = viewModel::loadAttendanceLog,
        onSettingsNavigate = onSettingsNavigate,
        onRefresh = viewModel::refreshSchedules
    )
}

@Preview
@Composable
private fun HomeScreenPreview() {
    HomeLayout(
        screenState = HomeScreenState(
            classList = ScheduleResult.Success(
                listOf(
                    ClassUiData(
                        id = "",
                        group = "IPZ-22d",
                        number = "1",
                        educator = "John Doe",
                        name = "Programming for beginners",
                        educatorId = "123",
                        date = "01.01.2001",
                        start = "9:30",
                        end = "10:30",
                        items = "",
                        weeks = listOf(
                            "1",
                            "3",
                            "5"
                        ),
                        meetingLink = "zoom.us",
                        moodleLink = "moodle",
                        type = "LC",
                        room = "432",
                        isNow = true
                    ),
                    ClassUiData(
                        id = "",
                        group = "IPZ-22d",
                        number = "2",
                        educator = "Novikova Maryna Fedorivna",
                        name = "Ukrainian language",
                        educatorId = "123",
                        date = "01.01.2001",
                        start = "10:40",
                        end = "11:40",
                        items = "",
                        weeks = listOf(
                            "2",
                            "4",
                            "6"
                        ),
                        meetingLink = "teams.microsoft.com",
                        moodleLink = "",
                        type = "PT",
                        room = "432",
                        isNow = false
                    ),
                    ClassUiData(
                        id = "",
                        group = "IPZ-22d",
                        number = "3",
                        educator = "Jonathan White",
                        name = "Software engineering",
                        educatorId = "123",
                        date = "01.01.2001",
                        start = "12:00",
                        end = "13:00",
                        items = "",
                        weeks = listOf(
                            "1",
                            "3",
                            "5"
                        ),
                        meetingLink = "meet.google.com",
                        moodleLink = "",
                        type = "LB",
                        room = "432",
                        isNow = false
                    ),
                    ClassUiData(
                        id = "",
                        group = "IPZ-22d",
                        number = "4",
                        educator = "Camellia Anderson",
                        name = "American literature",
                        educatorId = "123",
                        date = "01.01.2001",
                        start = "13:10",
                        end = "14:10",
                        items = "",
                        weeks = listOf(
                            "2",
                            "4",
                            "6"
                        ),
                        meetingLink = "unknown.link",
                        moodleLink = "",
                        type = "PT",
                        room = "432",
                        isNow = false
                    )
                )
            ),
            examList = ScheduleResult.Success(
                listOf(
                    ExamNetworkData(
                        date = "02.03.2001",
                        time = "9:30",
                        number = "1",
                        name = "Programming for beginners",
                        educator = "John Doe",
                        url = "meet.google.com"
                    ),
                    ExamNetworkData(
                        date = "05.03.2001",
                        time = "10:40",
                        number = "2",
                        name = "Ukrainian language",
                        educator = "Novikova Maryna Fedorivna",
                        url = "zoom.us"
                    ),
                    ExamNetworkData(
                        date = "09.03.2001",
                        time = "12:00",
                        number = "3",
                        name = "Software engineering",
                        educator = "Jonathan White",
                        url = "teams.microsoft.com"
                    ),
                    ExamNetworkData(
                        date = "09.03.2001",
                        time = "13:10",
                        number = "4",
                        name = "American literature",
                        educator = "Camellia Anderson",
                        url = "unknown.link"
                    )
                )
            ),
            studentFullName = "Billy Harrington",
            dateState = DateState(
                formattedDate = "01.01.2001"
            )
        ),
        isRefreshing = false,
        onDateUpdate = {},
        onForwardDateShift = {},
        onBackwardDateShift = {},
        onPresenceVerify = {},
        onAttendanceListRequest = {},
        onRefresh = {},
        snackbarHostState = SnackbarHostState(),
        titleResId = R.string.welcome_title,
        onSettingsNavigate = {},
    )
}

@Preview
@Composable
private fun EmptyHomeScreenPreview() {
    HomeLayout(
        screenState = HomeScreenState(
            dateState = DateState(
                formattedDate = "02.01.2001"
            )
        ),
        isRefreshing = false,
        onAttendanceListRequest = {},
        onDateUpdate = {},
        onForwardDateShift = {},
        onBackwardDateShift = {},
        onPresenceVerify = {},
        onRefresh = {},
        snackbarHostState = SnackbarHostState(),
        titleResId = R.string.home_screen,
        onSettingsNavigate = {},
    )
}