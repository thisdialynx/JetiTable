package lnx.jetitable.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import lnx.jetitable.navigation.About
import lnx.jetitable.navigation.Auth
import lnx.jetitable.navigation.Home
import lnx.jetitable.navigation.Loading
import lnx.jetitable.navigation.Settings
import lnx.jetitable.screens.about.AboutScreen
import lnx.jetitable.screens.auth.AuthScreen
import lnx.jetitable.screens.home.HomeScreen
import lnx.jetitable.screens.loading.LoadingScreen
import lnx.jetitable.screens.settings.SettingsScreen
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