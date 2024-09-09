package lnx.jetitable.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JetiTableTheme {
                Surface { AppNavigation() }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Loading.route
    ) {
        composable(
            Auth.route,
            exitTransition = { slideOutHorizontally(targetOffsetX = { -200 }) + fadeOut(animationSpec = tween(150)) }
        ) {
            AuthScreen(
                onAuthComplete = {
                    navController.navigate(Home.route) {
                        popUpTo(Auth.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            Home.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { -200 }) + fadeIn(animationSpec = tween(200)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { -200 }) + fadeOut(animationSpec = tween(200)) }
        ) { HomeScreen(navController) }

        composable(
            About.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { 200 }) + fadeIn(animationSpec = tween(200)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { 200 }) + fadeOut(animationSpec = tween(200)) }
        ) { AboutScreen(navController) }

        composable(
            Settings.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { 200 }) + fadeIn(animationSpec = tween(200)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { 200 }) + fadeOut(animationSpec = tween(200)) }
        ) { SettingsScreen(navController) }

        composable(
            Loading.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { 200 }) + fadeIn(animationSpec = tween(200)) },
            exitTransition = { slideOutHorizontally(targetOffsetX = { 200 }) + fadeOut(animationSpec = tween(200)) }
        ) { LoadingScreen(navController) }
    }
}