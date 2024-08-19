package com.bigbratan.emulair.navigation

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bigbratan.emulair.ui.components.TopNavigationBar
import com.bigbratan.emulair.ui.main.mainNavGraph

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(
    viewModel: NavigationViewModel = hiltViewModel(),
) {
    val startDestinationState = viewModel.startDestinationFlow.collectAsState()
    val navController = rememberNavController()
    val currentBackstackEntry by navController.currentBackStackEntryAsState()

    val topBarItems = remember {
        listOf(
            Destination.Main.GamesDestination,
            Destination.Main.SystemsDestination,
            Destination.Main.OnlineDestination,
            Destination.Main.SearchDestination,
        )
    }
    val shouldShowTopBar = remember {
        derivedStateOf {
            currentBackstackEntry?.destination?.route in topBarItems.map { it.route }
        }
    }

    startDestinationState.value?.let { startDestination ->
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            AnimatedVisibility(
                modifier = Modifier.align(Alignment.TopCenter),
                visible = shouldShowTopBar.value,
                enter = fadeIn(animationSpec = tween(400)),
                exit = fadeOut(animationSpec = tween(400))
            ) {
                TopNavigationBar(
                    currentRoute = currentBackstackEntry?.destination?.route,
                    onTabSwitch = { navDestination ->
                        navController.navigate(navDestination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                                inclusive = true
                            }
                            launchSingleTop = true
                            restoreState = true
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
                    onBackClick = { navController.popBackStack() },
                )
            }
        }
    }
}