package lnx.jetitable.screens.settings.cards

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.misc.DataState
import lnx.jetitable.ui.icons.Snu
import lnx.jetitable.ui.icons.google.Info
import lnx.jetitable.ui.icons.google.Logout
import lnx.jetitable.viewmodel.UserDataUiState

@Composable
fun AccountCard(
    userDataUiState: DataState<out UserDataUiState>,
    onSignOut: () -> Unit
) {
    val activityContext = LocalActivity.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        AnimatedContent(
            targetState = userDataUiState
        ) { state ->
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                when (state) {
                    is DataState.Loading -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(ButtonDefaults.IconSize),
                                strokeCap = StrokeCap.Round,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = stringResource(R.string.loading),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    is DataState.Success -> {
                        val title = stringResource(R.string.timetable_account, stringResource(R.string.timetable))
                        val description = "${state.data.fullName.first} (${state.data.group.first}).\n" +
                                "${stringResource(R.string.status, state.data.status)}. ${state.data.academicYears}. " +
                                "${stringResource(state.data.semesterResId)}. ${stringResource(state.data.formOfEducationResId)}."

                        AccountContent(title, description) {
                            onSignOut()
                            activityContext?.finish()
                        }
                    }
                    is DataState.Error -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Icon(
                                imageVector = Info,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.rotate(180f)
                            )
                            Text(
                                text = stringResource(R.string.account_data_fetch_failure),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                    }
                    else -> ""
                }
            }
        }
    }
}

@Composable
fun AccountContent(
    title: String,
    description: String,
    onSignOut: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Icon(
                imageVector = Snu,
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
            onClick = onSignOut,
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            Icon(
                imageVector = Logout,
                contentDescription = stringResource(id = R.string.sign_out),
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Text(
                text = stringResource(id = R.string.sign_out), modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}