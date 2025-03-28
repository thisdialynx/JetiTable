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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import lnx.jetitable.R
import lnx.jetitable.api.timetable.data.query.ExamNetworkData
import lnx.jetitable.misc.DateState
import lnx.jetitable.misc.DataState
import lnx.jetitable.screens.home.card.ClassScheduleCard
import lnx.jetitable.screens.home.card.ExamScheduleCard
import lnx.jetitable.screens.home.data.ClassUiData
import lnx.jetitable.ui.icons.google.Settings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeUI(
    dateState: DateState,
    classList: DataState<out List<ClassUiData>>,
    examList: DataState<out List<ExamNetworkData>>,
    onDateUpdate: (Calendar) -> Unit,
    onForwardDateShift: () -> Unit,
    onBackwardDateShift: () -> Unit,
    onPresenceVerify: (ClassUiData) -> Unit,
    onSettingsNavigate: () -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(rememberTopAppBarState())
    var title by rememberSaveable { mutableIntStateOf(R.string.welcome_title) }

    LaunchedEffect(Unit) {
        delay(3000)
        title = R.string.home_screen
    }

    Scaffold(
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
                            imageVector = Settings,
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
                    dateState = dateState,
                    onPresenceVerify = onPresenceVerify,
                    onDateUpdate = onDateUpdate,
                    onBackwardDateShift = onBackwardDateShift,
                    onForwardDateShift = onForwardDateShift
                )
            }
            item {
                ExamScheduleCard(examList)
            }
        }
    }
}