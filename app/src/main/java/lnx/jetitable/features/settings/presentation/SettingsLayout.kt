@file:OptIn(ExperimentalMaterial3Api::class)

package lnx.jetitable.features.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import lnx.jetitable.R
import lnx.jetitable.features.settings.presentation.cards.AccountCard
import lnx.jetitable.features.settings.presentation.cards.SettingsCard
import lnx.jetitable.features.settings.presentation.cards.UpdateCard

@Composable
fun SettingsLayout(
    screenState: SettingsScreenState,
    onBack: () -> Unit,
    onDestinationNavigate: (String) -> Unit,
    onLogOut: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(R.string.settings))
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                AccountCard(screenState.userInfo, onLogOut)
            }

            item {
                UpdateCard(screenState.updateInfo)
            }

            item {
                SettingsCard(screenState.settings, onDestinationNavigate)
            }
        }
    }
}