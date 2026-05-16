package lnx.jetitable.features.settings.presentation.cards

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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.api.timetable.domain.models.EducationForm
import lnx.jetitable.api.timetable.domain.models.SemesterType
import lnx.jetitable.features.settings.domain.model.ProfileError
import lnx.jetitable.features.settings.domain.model.UserProfileState
import lnx.jetitable.ui.components.StateStatus
import lnx.jetitable.ui.icons.Snu
import lnx.jetitable.ui.icons.google.Info
import lnx.jetitable.ui.icons.google.Logout

@Composable
fun AccountCard(
    userProfileState: UserProfileState,
    onLogOut: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        AnimatedContent(
            targetState = userProfileState
        ) { state ->
            when (state) {
                is UserProfileState.Loading -> {
                    StateStatus(
                        description = stringResource(R.string.loading),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }

                is UserProfileState.Success -> {
                    val semester = when (state.data.semesterType) {
                        SemesterType.AUTUMN -> R.string.autumn_semester
                        SemesterType.SPRING -> R.string.spring_semester
                        SemesterType.UNKNOWN -> R.string.unknown_error
                    }

                    val educationForm = when (state.data.educationForm) {
                        EducationForm.FULL_TIME -> R.string.full_time
                        EducationForm.PART_TIME -> R.string.part_time
                        EducationForm.UNKNOWN -> R.string.unknown_error
                    }

                    val title = stringResource(R.string.timetable_account, stringResource(R.string.timetable))
                    val description = "${state.data.fullName} (${state.data.group}).\n" +
                            "${stringResource(R.string.status, state.data.status)}. ${state.data.academicYears}. " +
                            "${stringResource(semester)}. ${stringResource(educationForm)}."

                    AccountContent(title, description) { onLogOut() }
                }

                is UserProfileState.Failure -> {
                    val reason = when (state.error) {
                        ProfileError.NO_DATA -> R.string.failed_to_fetch_data
                        ProfileError.UNKNOWN_ERROR -> R.string.unknown_error
                    }

                    StateStatus(
                        icon = Info,
                        modifier = Modifier.rotate(180f),
                        description = stringResource(reason),
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                    )
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
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
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
            val signOutStr = stringResource(id = R.string.sign_out)

            Icon(
                imageVector = Logout,
                contentDescription = signOutStr,
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Text(
                text = signOutStr, modifier = Modifier.padding(start = 4.dp),
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}