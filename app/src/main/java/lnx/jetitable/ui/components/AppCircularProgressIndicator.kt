package lnx.jetitable.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp

@Composable
fun AppCircularProgressIndicator(modifier: Modifier = Modifier) {
    CircularProgressIndicator(
        strokeWidth = 3.dp,
        strokeCap = StrokeCap.Round,
        modifier = modifier.size(24.dp)
    )
}