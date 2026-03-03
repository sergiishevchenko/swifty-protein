package com.music42.swiftyprotein.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.music42.swiftyprotein.ui.login.LoginScreen
import com.music42.swiftyprotein.ui.proteinlist.ProteinListScreen
import com.music42.swiftyprotein.ui.proteinview.ProteinViewScreen

@Composable
fun SwiftyProteinNavHost(
    shouldShowLogin: Boolean,
    onLoginShown: () -> Unit,
    navController: NavHostController = rememberNavController()
) {
    LaunchedEffect(shouldShowLogin) {
        if (shouldShowLogin) {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
            }
            onLoginShown()
        }
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.ProteinList.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ProteinList.route) {
            ProteinListScreen(
                onLigandSelected = { ligandId ->
                    navController.navigate(Screen.ProteinView.createRoute(ligandId))
                }
            )
        }

        composable(
            route = Screen.ProteinView.route,
            arguments = listOf(navArgument("ligandId") { type = NavType.StringType })
        ) { backStackEntry ->
            val ligandId = backStackEntry.arguments?.getString("ligandId") ?: return@composable
            ProteinViewScreen(
                ligandId = ligandId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
