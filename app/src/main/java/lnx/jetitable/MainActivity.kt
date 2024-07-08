package lnx.jetitable

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lnx.jetitable.core.About
import lnx.jetitable.core.Auth
import lnx.jetitable.core.Home
import lnx.jetitable.core.Loading
import lnx.jetitable.core.Settings
import lnx.jetitable.ui.screens.AboutScreen
import lnx.jetitable.ui.screens.AuthScreen
import lnx.jetitable.ui.screens.HomeScreen
import lnx.jetitable.ui.screens.LoadingScreen
import lnx.jetitable.ui.screens.SettingsScreen
import lnx.jetitable.ui.theme.JetiTableTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetiTableTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController,
        startDestination = Loading.route) {
        composable(Auth.route) {
            AuthScreen()
        }
        composable(Home.route) {
            HomeScreen(navController)
        }
        composable(About.route) {
            AboutScreen(navController)
        }
        composable(Settings.route) {
            SettingsScreen()
        }
        composable(Loading.route) {
            LoadingScreen(navController)
        }
    }
}

@Composable
@Preview(showBackground = true)
fun DefaultPreview() {
    JetiTableTheme {
        AppNavigation()
    }
}