package lnx.jetitable.screens.home.card

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.ConnectionState

@Composable
fun ScheduleTable(
    connectivityState: ConnectionState,
    emptyContent: @Composable () -> Unit,
    data: List<Any>?,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .animateContentSize(animationSpec = spring(stiffness = Spring.StiffnessMedium)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when {
                data == null && connectivityState == ConnectionState.Available -> {
                    ScheduleStatus(text = R.string.getting_list)
                }
                data.isNullOrEmpty() && connectivityState == ConnectionState.Unavailable -> {
                    ScheduleStatus(
                        text = R.string.no_internet_connection,
                        icon = lnx.jetitable.ui.icons.google.WifiOff
                    )
                }
                data?.isEmpty() == true && connectivityState == ConnectionState.Available -> {
                    emptyContent()
                }
                !data.isNullOrEmpty() -> {
                    content()
                }
            }
        }
    }
}