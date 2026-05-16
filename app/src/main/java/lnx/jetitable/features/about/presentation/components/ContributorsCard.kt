package lnx.jetitable.features.about.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import lnx.jetitable.features.about.presentation.ContributorUi
import lnx.jetitable.ui.components.getColumnCardShape

@Composable
fun ContributorsCard(contributors: List<ContributorUi>) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        contributors.forEachIndexed { index, item ->
            val isLastItem = index == contributors.size - 1
            val shape = getColumnCardShape(index, isLastItem)
            val role = stringResource(item.roleResId)

            Surface(
                color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                modifier = Modifier.fillMaxWidth(),
                shape = shape
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp), Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(2.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.name.first().toString(),
                            style = MaterialTheme.typography.displaySmall
                        )
                        if (!item.profilePictureUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = item.profilePictureUrl,
                                contentDescription = "${item.name}, $role",
                            )
                        }
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(2.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = item.name,
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium,
                            fontSize = 18.sp
                        )
                        Text(
                            text = role,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}