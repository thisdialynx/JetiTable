package lnx.jetitable.ui.components

import androidx.compose.ui.graphics.vector.ImageVector
import lnx.jetitable.R
import lnx.jetitable.navigation.About
import lnx.jetitable.navigation.Notifications

enum class SettingsItems(
    val titleResId: Int,
    val descriptionResId: Int,
    val icon: ImageVector,
    val destination: String
) {
    NOTIFICATIONS(
        R.string.notifications_settings_entry_title,
        R.string.notifications_settings_entry_description,
        lnx.jetitable.ui.icons.google.Notifications,
        Notifications.route
    ),
    ABOUT(
        R.string.about_screen_title,
        R.string.about_screen_description,
        lnx.jetitable.ui.icons.google.Info,
        About.route
    )
}