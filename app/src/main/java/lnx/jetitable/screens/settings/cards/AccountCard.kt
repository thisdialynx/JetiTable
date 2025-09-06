package lnx.jetitable.screens.settings.cards

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.ui.icons.Snu
import lnx.jetitable.ui.icons.google.Logout
import lnx.jetitable.viewmodel.UserDataUiState

@Composable
fun AccountCard(
    userDataUiState: UserDataUiState?,
    onSignOut: () -> Unit
) {
    val activityContext = LocalActivity.current
    val title = stringResource(id = R.string.timetable_account, stringResource(id = R.string.timetable))
    val description = userDataUiState?.let {
        "${userDataUiState.fullName.first} (${userDataUiState.group.first}).\n" +
                "${stringResource(id = R.string.status)}: ${userDataUiState.status}. ${userDataUiState.academicYears}. " +
                "${stringResource(id = userDataUiState.semesterResId)}. ${stringResource(id = userDataUiState.formOfEducationResId)}."
    } ?: ""
    val icon = Snu

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .animateContentSize()
        ) {
            if (description.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(vertical = 16.dp),
                        strokeCap = StrokeCap.Round
                    )
                }
            } else {
                Column(
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
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onSignOut()
                            activityContext?.finish()
                        }
                    ) {
                        Icon(
                            imageVector = Logout,
                            contentDescription = stringResource(id = R.string.sign_out),
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(ButtonDefaults.IconSize)
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
    }
}