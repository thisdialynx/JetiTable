package lnx.jetitable.screens.settings

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.BuildConfig
import lnx.jetitable.viewmodel.SettingsViewModel

@Composable
fun TTAccountCard(title: String, description: String, icon: ImageVector, context: Context, toastText: String, settingsViewModel: SettingsViewModel) {
    val activityContext = LocalContext.current as? Activity

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        onClick = {
            if (BuildConfig.DEBUG) {
                Toast.makeText(context, toastText, Toast.LENGTH_SHORT ).show()
            }
        }
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Icon(
                    imageVector = icon,
                    contentDescription = "$title $description",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(24.dp)
                )
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                    Text(
                        text = description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
            OutlinedButton(
                onClick = {
                    settingsViewModel.signOut()
                    activityContext?.finish()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = lnx.jetitable.ui.icons.google.Logout,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = stringResource(id = R.string.sign_out), modifier = Modifier.padding(start = 4.dp),
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    style = MaterialTheme.typography.titleSmall
                )
            }
        }
    }
}