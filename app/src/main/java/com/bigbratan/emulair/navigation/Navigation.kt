package com.bigbratan.emulair.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.bigbratan.emulair.ui.components.TopNavigationBar
import com.bigbratan.emulair.ui.main.mainApp

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation() {
    val viewModel: NavigationViewModel = hiltViewModel()
    val navController = rememberNavController()

    val startDestination = viewModel.startDestination.collectAsState()
    val shouldShowTopBar =
        navController.currentBackStackEntryAsState().value?.destination?.route in listOf(
            Destination.Main.GamesDestination.route,
            Destination.Main.SystemsDestination.route,
            Destination.Main.OnlineDestination.route,
            Destination.Main.SettingsDestination.route,
        )

    startDestination.value?.let { startDestinationValue ->
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = { if (shouldShowTopBar) TopNavigationBar(navController = navController) }
        ) { paddingValues ->
            val navModifier = if (shouldShowTopBar) {
                Modifier.padding(
                    top = paddingValues.calculateTopPadding(),
                    bottom = paddingValues.calculateBottomPadding(),
                )
            } else {
                Modifier
            }

            NavHost(
                modifier = navModifier,
                navController = navController,
                startDestination = startDestinationValue,
            ) {
                /*navigation(
                    route = Destination.Onboarding.route,
                    startDestination = Destination.Onboarding.FirstScreen.route
                ) { onboardingApp(navController = navController) }*/

                navigation(
                    route = Destination.Main.route,
                    startDestination = Destination.Main.GamesDestination.route
                ) { mainApp(navController = navController) }
            }
        }
    }
}