package com.example.stateaffordability


import WelcomeScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.stateaffordability.screens.DetailScreen
import com.example.stateaffordability.screens.HomeScreen
import com.example.stateaffordability.ui.theme.StateAffordabilityTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StateAffordabilityTheme {
                AppNavHost()
            }
        }
    }
}

@Composable
fun AppNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome",
        modifier = modifier
    ) {
        composable("welcome") {
            WelcomeScreen(
                onEnterClick = {
                    navController.navigate("home") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
            )
        }

        composable("home") {
            HomeScreen(
                onStateClicked = { stateName ->
                    navController.navigate("detail/$stateName")
                }
            )
        }

        composable(
            route = "detail/{stateName}",
            arguments = listOf(navArgument("stateName") { type = NavType.StringType })
        ) { backStackEntry ->
            DetailScreen(
                stateName = backStackEntry.arguments?.getString("stateName") ?: "",
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}
