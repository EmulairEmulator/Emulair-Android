package com.bigbratan.emulair.ui.main

import androidx.navigation.NavDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.bigbratan.emulair.navigation.Destination
import com.bigbratan.emulair.ui.components.TopNavDestination
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
    navigateBack: () -> Unit,
    navigateToGame: (gameId: Int) -> Unit,
    navigateToAchievements: (gameId: Int) -> Unit,
    navigateToGameOptions: (gameId: Int?) -> Unit,
    navigateToSystemOptions: (systemId: Int?) -> Unit,
    navigateToTab: (TopNavDestination) -> Unit,
) {
    navigation(
        route = Destination.Main.route,
        startDestination = Destination.Main.GamesDestination.route
    ) {
        composable(route = Destination.Main.GamesDestination.route) {
            GamesScreen(
                onGameClick = navigateToGame,
                onAchievementsClick = navigateToAchievements,
                onGameOptionsClick = navigateToGameOptions,
                onTabSwitch = navigateToTab,
            )
        }

        composable(route = Destination.Main.SystemsDestination.route) {
            SystemsScreen(
                onTabSwitch = navigateToTab,
            )
        }

        composable(
            route = Destination.Main.SystemGamesDestination.routeWithArgs("{systemId}"),
            arguments = listOf(
                navArgument("systemId") { type = NavType.IntType },
            ),
        ) { backStackEntry ->
            SystemGamesScreen(
                systemId = backStackEntry.arguments?.getInt("systemId") ?: 0,
                onGameClick = navigateToGame,
                onAchievementsClick = navigateToAchievements,
                onSystemOptionsClick = navigateToSystemOptions,
                onBackClick = navigateBack,
            )
        }

        composable(route = Destination.Main.OnlineDestination.route) {
            OnlineScreen(
                onTabSwitch = navigateToTab,
            )
        }

        composable(route = Destination.Main.SearchDestination.route) {
            SearchScreen(
                onTabSwitch = navigateToTab,
            )
        }

        composable(route = Destination.Main.ProfileDestination.route) {
            ProfileScreen(
                onBackClick = navigateBack,
            )
        }

        composable(route = Destination.Main.AppsDestination.route) {
            AppsScreen()
        }

        composable(route = Destination.Main.SettingsDestination.route) {
            SettingsScreen(
                onBackClick = navigateBack,
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
                onBackClick = navigateBack,
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
                onBackClick = navigateBack,
            )
        }
    }
}
