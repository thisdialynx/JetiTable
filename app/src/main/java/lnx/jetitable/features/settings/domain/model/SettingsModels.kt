package lnx.jetitable.features.settings.domain.model

import androidx.compose.ui.graphics.vector.ImageVector
import lnx.jetitable.R
import lnx.jetitable.api.timetable.domain.models.EducationForm
import lnx.jetitable.api.timetable.domain.models.SemesterType
import lnx.jetitable.navigation.About
import lnx.jetitable.navigation.Notifications
import lnx.jetitable.ui.icons.google.Info
import lnx.jetitable.ui.icons.google.Notifications as NotificationsIcon

sealed class UpdateResult {
    object Loading : UpdateResult()
    data class Available(val data: AppUpdateState) : UpdateResult()
    object Latest : UpdateResult()
    data class Failure(val error: UpdateCheckError) : UpdateResult()
}

sealed class UserProfileState {
    data object Loading : UserProfileState()
    data class Success(val data: UserInfoData) : UserProfileState()
    data class Failure(val error: ProfileError) : UserProfileState()
}

sealed class SettingsEvent {
    object NavigateToAuth : SettingsEvent()
}

enum class UpdateCheckError {
    NETWORK_ERROR,
    UNKNOWN_ERROR
}

enum class ProfileError {
    NO_DATA,
    UNKNOWN_ERROR
}

enum class SettingsEntries(
    val titleResId: Int,
    val descResId: Int,
    val icon: ImageVector,
    val route: String
) {
    NOTIFICATIONS(
        R.string.notifications_settings_entry_title,
        R.string.notifications_settings_entry_description,
        NotificationsIcon,
        Notifications.route
    ),
    ABOUT(
        R.string.about_screen_title,
        R.string.about_screen_description,
        Info,
        About.route
    )
}

data class UserInfoData(
    val fullName: String,
    val group: String,
    val academicYears: String,
    val status: String,
    val educationForm: EducationForm,
    val semesterType: SemesterType
)

data class AppUpdateState(
    val currentVersion: String,
    val latestVersion: String,
    val updateAvailable: Boolean,
    val downloadUrl: String,
    val releaseNotes: String
)