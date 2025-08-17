package lnx.jetitable.screens.home

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.misc.ConnectionState
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.screens.home.elements.datepicker.DateState
import lnx.jetitable.viewmodel.HomeViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    onSettingsNavigate: () -> Unit,
    onNotificationsNavigate: () -> Unit
) {
    val viewModel = viewModel<HomeViewModel>()
    val dateState by viewModel.dateState.collectAsStateWithLifecycle(DateState())
    val connectionState by viewModel.connectivityState.collectAsStateWithLifecycle()
    val classList by viewModel.classesFlow.collectAsStateWithLifecycle()
    val examList by viewModel.examsFlow.collectAsStateWithLifecycle()

    HomeUI(
        dateState, classList, examList, connectionState,
        onDateUpdate = { viewModel.updateDate(it) },
        onForwardDateShift = { viewModel.shiftDayForward() },
        onBackwardDateShift = { viewModel.shiftDayBackward() },
        onPresenceVerify = { viewModel.verifyPresence(it) },
        onSettingsNavigate = onSettingsNavigate,
        onNotificationsNavigate = onNotificationsNavigate
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Preview
@Composable
private fun HomeScreenPreview() {
    HomeUI(
        dateState = DateState(
            formattedDate = "01.01.2001"
        ),
        classList = DataState.Success(
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
                    meetingLink = "unknown.link",
                    moodleLink = "",
                    type = "PT",
                    room = "432",
                    isNow = false
                )
            )
        ),
        examList = DataState.Success(
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
        connectionState = ConnectionState.Available,
        onDateUpdate = {},
        onForwardDateShift = {},
        onBackwardDateShift = {},
        onPresenceVerify = {},
        onSettingsNavigate = {},
        onNotificationsNavigate = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Preview
@Composable()
private fun EmptyHomeScreenPreview() {
    HomeUI(
        dateState = DateState(
            formattedDate = "02.01.2001"
        ),
        classList = DataState.Empty,
        examList = DataState.Empty,
        connectionState = ConnectionState.Available,
        onDateUpdate = {},
        onForwardDateShift = {},
        onBackwardDateShift = {},
        onPresenceVerify = {},
        onSettingsNavigate = {},
        onNotificationsNavigate = {}
    )
}