package com.music42.swiftyprotein.ui.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Onboarding : Screen("onboarding")
    data object ProteinList : Screen("protein_list")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")
    data object ProteinView : Screen("protein_view/{ligandId}") {
        fun createRoute(ligandId: String) = "protein_view/$ligandId"
    }
    data object Compare : Screen("compare/{ligandA}/{ligandB}") {
        fun createRoute(ligandA: String, ligandB: String) = "compare/$ligandA/$ligandB"
    }
}
