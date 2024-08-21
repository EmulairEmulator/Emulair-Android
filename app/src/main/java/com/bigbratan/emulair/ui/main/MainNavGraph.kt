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
    onGameOptionsClick: (gameId: Int?) -> Unit,
    onSystemOptionsClick: (systemId: Int?) -> Unit,
) {
    navigation(
        route = Destination.Main.route,
        startDestination = Destination.Main.GamesDestination.route
    ) {
        composable(route = Destination.Main.GamesDestination.route) {
            GamesScreen(
                onGameClick = onGameClick,
                onAchievementsClick = onAchievementsClick,
                onGameOptionsClick = onGameOptionsClick,
            )
        }

        composable(route = Destination.Main.SystemsDestination.route) {
            SystemsScreen()
        }

        composable(
            route = Destination.Main.SystemGamesDestination.routeWithArgs("{systemId}"),
            arguments = listOf(
                navArgument("systemId") { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            SystemGamesScreen(
                systemId = backStackEntry.arguments?.getInt("systemId") ?: 0,
                onGameClick = onGameClick,
                onAchievementsClick = onAchievementsClick,
                onSystemOptionsClick = onSystemOptionsClick,
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
            route = Destination.Main.GameOptionsDestination.routeWithArgs("{gameId}"),
            arguments = listOf(
                navArgument("gameId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            CoreOptionsScreen(
                gameId = backStackEntry.arguments?.getString("gameId")?.toInt(),
                systemId = null,
                onBackClick = onBackClick,
            )
        }

        composable(
            route = Destination.Main.SystemOptionsDestination.routeWithArgs("{systemId}"),
            arguments = listOf(
                navArgument("systemId") { type = NavType.IntType },
            )
        ) { backStackEntry ->
            CoreOptionsScreen(
                gameId = null,
                systemId = backStackEntry.arguments?.getString("systemId")?.toInt(),
                onBackClick = onBackClick,
            )
        }
    }
}
