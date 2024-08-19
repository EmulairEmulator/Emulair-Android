package com.bigbratan.emulair.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.bigbratan.emulair.navigation.Destination
import com.bigbratan.emulair.ui.main.apps.AppsScreen
import com.bigbratan.emulair.ui.main.games.GamesScreen
import com.bigbratan.emulair.ui.main.online.OnlineScreen
import com.bigbratan.emulair.ui.main.profile.ProfileScreen
import com.bigbratan.emulair.ui.main.search.SearchScreen
import com.bigbratan.emulair.ui.main.settings.SettingsScreen
import com.bigbratan.emulair.ui.main.systemGames.SystemGamesScreen
import com.bigbratan.emulair.ui.main.systems.SystemsScreen

fun NavGraphBuilder.mainNavGraph(
    onBackClick: () -> Unit,
) {
    navigation(
        route = Destination.Main.route,
        startDestination = Destination.Main.GamesDestination.route
    ) {
        composable(route = Destination.Main.GamesDestination.route) {
            GamesScreen(
                onGameClick = {},
                onAchievementsClick = {},
            )
        }

        composable(route = Destination.Main.SystemsDestination.route) {
            SystemsScreen()
        }

        composable(route = Destination.Main.SystemGamesDestination.route) {
            SystemGamesScreen()
        }

        composable(route = Destination.Main.OnlineDestination.route) {
            OnlineScreen()
        }

        composable(route = Destination.Main.SearchDestination.route) {
            SearchScreen()
        }

        composable(route = Destination.Main.ProfileDestination.route) {
            ProfileScreen(
                onBackClick = onBackClick
            )
        }

        composable(route = Destination.Main.AppsDestination.route) {
            AppsScreen()
        }

        composable(route = Destination.Main.SettingsDestination.route) {
            SettingsScreen(
                onBackClick = onBackClick
            )
        }
    }
}
