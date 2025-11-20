package lnx.jetitable.screens.home.elements.schedule

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import lnx.jetitable.ui.icons.GoogleMeet
import lnx.jetitable.ui.icons.MsTeams
import lnx.jetitable.ui.icons.ZoomMeeting
import lnx.jetitable.ui.icons.google.Link

fun getCardShape(index: Int, isLastElement: Boolean): RoundedCornerShape {
    val roundedCorner = 12.dp
    val semiRounded = 4.dp
    return when {
        index == 0 && !isLastElement -> {
            RoundedCornerShape(
                topStart = roundedCorner,
                topEnd = roundedCorner,
                bottomStart = semiRounded,
                bottomEnd = semiRounded
            )
        }
        index != 0 && isLastElement -> {
            RoundedCornerShape(
                topStart = semiRounded,
                topEnd = semiRounded,
                bottomStart = roundedCorner,
                bottomEnd = roundedCorner
            )
        }
        index != 0 && !isLastElement -> {
            RoundedCornerShape(
                topStart = semiRounded,
                topEnd = semiRounded,
                bottomStart = semiRounded,
                bottomEnd = semiRounded
            )
        }
        else -> {
            RoundedCornerShape(
                topStart = roundedCorner,
                topEnd = roundedCorner,
                bottomStart = roundedCorner,
                bottomEnd = roundedCorner
            )
        }
    }
}

fun getMeetingIcon(url: String): ImageVector {
    return when {
        url.contains("zoom.us") -> ZoomMeeting
        url.contains("meet.google.com") -> GoogleMeet
        url.contains("teams.microsoft.com") -> MsTeams
        else -> Link
    }
}