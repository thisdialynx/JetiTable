package lnx.jetitable.features.about.presentation

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import lnx.jetitable.BuildConfig
import lnx.jetitable.R
import lnx.jetitable.ui.icons.Snu
import lnx.jetitable.ui.icons.google.CalendarMonth

@Composable
fun AboutScreen(
    viewModel: AboutViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val aboutState by viewModel.aboutState.collectAsStateWithLifecycle()
    val packageManager = LocalContext.current.packageManager
    val drawable = packageManager.getApplicationIcon(BuildConfig.APPLICATION_ID)
    val appIcon = drawable.toBitmap(config = Bitmap.Config.ARGB_8888).asImageBitmap()

    AboutLayout(
        aboutState = aboutState,
        onBack = onBack
    ) {
        Image(
            bitmap = appIcon,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .padding(8.dp)
        )
    }
}

@Preview
@Composable
private fun AboutScreenPreview() {
    val fakeScreenState = AboutScreenState(
        contributors = listOf(
            ContributorUi(
                name = "Moonwalker",
                profilePictureUrl = null,
                roleResId = R.string.developer
            ),
            ContributorUi(
                name = "Malyi Tokmach",
                profilePictureUrl = null,
                roleResId = R.string.timetable_developer
            )
        ),
        links = listOf(
            ProjectLinkUi(
                url = "",
                platformIcon = Snu,
                titleResId = R.string.university
            ),
            ProjectLinkUi(
                url = "",
                platformIcon = CalendarMonth,
                titleResId = R.string.timetable
            ),
        )
    )

    AboutLayout(
        aboutState = fakeScreenState,
        onBack = {},
        appIcon = {}
    )
}