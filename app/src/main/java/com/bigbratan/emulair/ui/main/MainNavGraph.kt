package com.bigbratan.emulair.ui.main

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bigbratan.emulair.navigation.Destination
import com.bigbratan.emulair.ui.main.apps.AppsScreen
import com.bigbratan.emulair.ui.main.coreOptions.CoreOptionsScreen
import com.bigbratan.emulair.ui.main.games.GamesScreen
import com.bigbratan.emulair.ui.main.online.OnlineScreen
import com.bigbratan.emulair.ui.main.profile.ProfileScreen
import com.bigbratan.emulair.ui.main.search.SearchScreen
import com.bigbratan.emulair.ui.main.settings.SettingsScreen
import com.bigbratan.emulair.ui.main.systemGames.SystemGamesScreen
import com.bigbratan.emulair.ui.main.systems.SystemsScreen

fun NavGraphBuilder.mainNavGraph(
    onBackClick: () -> Unit,
    onGameClick: (gameId: Int) -> Unit,
    onAchievementsClick: (gameId: Int) -> Unit,
    onCoreOptionsClick: (gameId: Int?, systemId: Int?) -> Unit,
) {
    navigation(
        route = Destination.Main.route,
        startDestination = Destination.Main.GamesDestination.route
    ) {
        composable(route = Destination.Main.GamesDestination.route) {
            GamesScreen(
                onGameClick = onGameClick,
                onAchievementsClick = onAchievementsClick,
                onGameOptionsClick = onCoreOptionsClick,
            )
        }

        composable(route = Destination.Main.SystemsDestination.route) {
            SystemsScreen()
        }

        composable(route = Destination.Main.SystemGamesDestination.route) {
            SystemGamesScreen(
                onGameClick = onGameClick,
                onAchievementsClick = onAchievementsClick,
                onSystemOptionsClick = onCoreOptionsClick,
                onBackClick = onBackClick,
            )
        }

        composable(route = Destination.Main.OnlineDestination.route) {
            OnlineScreen()
        }

        composable(route = Destination.Main.SearchDestination.route) {
            SearchScreen()
        }

        composable(route = Destination.Main.ProfileDestination.route) {
            ProfileScreen(
                onBackClick = onBackClick,
            )
        }

        composable(route = Destination.Main.AppsDestination.route) {
            AppsScreen()
        }

        composable(route = Destination.Main.SettingsDestination.route) {
            SettingsScreen(
                onBackClick = onBackClick,
            )
        }

        composable(
            route = Destination.Main.CoreOptionsDestination.routeWithArgs(
                "{gameId}",
                "{systemId}"
            ),
            arguments = listOf(
                navArgument("gameId") { type = NavType.StringType },
                navArgument("systemId") { type = NavType.StringType },
            )
        ) { backStackEntry ->
            CoreOptionsScreen(
                gameId = backStackEntry.arguments?.getString("gameId"),
                systemId = backStackEntry.arguments?.getString("systemId"),
                onBackClick = onBackClick,
            )
        }
    }
}
