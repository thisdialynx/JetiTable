package lnx.jetitable.screens.settings.cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.ui.components.SettingsItems
import lnx.jetitable.ui.components.getCardShape

@Composable
fun SettingsCard(onDestinationNavigate: (String) -> Unit) {
    SettingsItems.entries.forEachIndexed { index, item ->
        val isLastItem = index == SettingsItems.entries.size - 1
        val shape = getCardShape(index, isLastItem)

        if (index > 0) Spacer(modifier = Modifier.height(2.dp))

        Surface(
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
            onClick = { onDestinationNavigate(item.destination) },
            modifier = Modifier.fillMaxWidth(),
            shape = shape
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = "${stringResource(id = item.titleResId)}. ${stringResource(id = item.descriptionResId)}",
                    tint = MaterialTheme.colorScheme.primary
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResource(id = item.titleResId),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = stringResource(id = item.descriptionResId),
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }
        }
    }
}