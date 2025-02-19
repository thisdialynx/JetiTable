package lnx.jetitable.screens.home.card

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun ScheduleCard(
    expanded: Boolean = true,
    title: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        title()
        AnimatedVisibility(
            visible = expanded
        ) { content() }
    }
}