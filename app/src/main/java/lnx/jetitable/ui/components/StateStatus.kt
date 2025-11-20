package lnx.jetitable.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

@Composable
fun StateStatus(
    icon: ImageVector? = null,
    description: String,
    color: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(16.dp)
    ) {
        if (icon == null) {
            AppCircularProgressIndicator()
        } else {
            Icon(
                imageVector = icon,
                contentDescription = description,
                tint = color
            )
        }
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = color
        )
    }
}