package com.bigbratan.emulair.ui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.bigbratan.emulair.navigation.Destination
import com.bigbratan.emulair.ui.main.games.GamesScreen
import com.bigbratan.emulair.ui.main.online.OnlineScreen
import com.bigbratan.emulair.ui.main.settings.SettingsScreen
import com.bigbratan.emulair.ui.main.systems.SystemsScreen

fun NavGraphBuilder.mainApp(
    navController: NavHostController,
) {
    composable(route = Destination.Main.GamesDestination.route) {
        GamesScreen(
            onGameClick = {}
        )
    }

    composable(route = Destination.Main.SystemsDestination.route) {
        SystemsScreen()
    }

    composable(route = Destination.Main.OnlineDestination.route) {
        OnlineScreen()
    }

    composable(route = Destination.Main.SettingsDestination.route) {
        SettingsScreen()
    }
}
