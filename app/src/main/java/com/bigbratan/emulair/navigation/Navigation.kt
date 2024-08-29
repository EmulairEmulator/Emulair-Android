package com.bigbratan.emulair.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bigbratan.emulair.ui.components.TopNavDestination
import com.bigbratan.emulair.ui.components.TopNavigationBar
import com.bigbratan.emulair.ui.main.mainNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    viewModel: NavigationViewModel = hiltViewModel(),
    isGamePadConnected: MutableState<Boolean>,
) {
    val startDestinationState = viewModel.startDestinationFlow.collectAsState()
    val navController = rememberNavController()
    val currentBackstackEntry by navController.currentBackStackEntryAsState()

    val topBarItems = remember {
        listOf(
            TopNavDestination.SYSTEMS.route,
            TopNavDestination.GAMES.route,
            TopNavDestination.ONLINE.route,
            TopNavDestination.SEARCH.route,
        )
    }
    val shouldShowTopBar = remember {
        derivedStateOf {
            currentBackstackEntry?.destination?.route in topBarItems
        }
    }

    startDestinationState.value?.let { startDestination ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
        ) {
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.TopCenter),
                visible = shouldShowTopBar.value,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                TopNavigationBar(
                    currentDestination = currentBackstackEntry?.destination,
                    isGamePadConnected = isGamePadConnected.value,
                    onTabSwitch = { navDestination ->
                        navController.navigate(navDestination.route) {
                            popUpTo(navDestination.route) { inclusive = true }
                        }
                    },
                    onProfileClick = {
                        navController.navigate(Destination.Main.ProfileDestination.route)
                    },
                    onSettingsClick = {
                        navController.navigate(Destination.Main.SettingsDestination.route)
                    },
                )
            }

            NavHost(
                navController = navController,
                startDestination = startDestination,
                enterTransition = { fadeIn(animationSpec = tween(400)) },
                exitTransition = { fadeOut(animationSpec = tween(400)) },
            ) {
                mainNavGraph(
                    navigateBack = { navController.popBackStack() },
                    navigateToGame = { gameId -> },
                    navigateToAchievements = { gameId -> },
                    navigateToGameOptions = { gameId ->
                        navController.navigate(
                            Destination.Main.GameOptionsDestination.routeWithArgs(
                                gameId.toString(),
                            )
                        )
                    },
                    navigateToSystemOptions = { systemId ->
                        navController.navigate(
                            Destination.Main.SystemOptionsDestination.routeWithArgs(
                                systemId.toString(),
                            )
                        )
                    },
                    navigateToTab = { navDestination ->
                        navController.navigate(navDestination.route) {
                            popUpTo(navDestination.route) { inclusive = true }
                        }
                    },
                )
            }
        }
    }
}