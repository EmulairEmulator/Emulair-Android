package com.bigbratan.emulair.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Person3
import androidx.compose.material.icons.filled.Person4
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.bigbratan.emulair.R
import com.bigbratan.emulair.navigation.Destination
import com.bigbratan.emulair.ui.theme.plusJakartaSans

enum class TopNavDestination(val route: String) {
    GAMES(Destination.Main.GamesDestination.route),
    SYSTEMS(Destination.Main.SystemsDestination.route),
    ONLINE(Destination.Main.OnlineDestination.route),
    SETTINGS(Destination.Main.SettingsDestination.route),
}

@Composable
fun TopNavigationBar(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TonalIconButton(
            modifier = Modifier.padding(start = 32.dp),
            imageVector = Icons.Filled.Person,
            onClick = {},
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp),
                painter = painterResource(id = R.drawable.icon_lb),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null,
            )

            TopNavDestination.entries.forEachIndexed { index, navDestination ->
                Button(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = {
                        navController.navigate(navDestination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor =
                        if (currentRoute == navDestination.route) MaterialTheme.colorScheme.surfaceVariant
                        else Color.Transparent,
                        contentColor =
                        if (currentRoute == navDestination.route) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    ),
                ) {
                    Text(
                        text = when (navDestination) {
                            TopNavDestination.GAMES -> stringResource(id = R.string.nav_games_title)
                            TopNavDestination.SYSTEMS -> stringResource(id = R.string.nav_systems_title)
                            TopNavDestination.ONLINE -> stringResource(id = R.string.nav_online_title)
                            TopNavDestination.SETTINGS -> stringResource(id = R.string.nav_settings_title)
                        },
                        fontFamily = plusJakartaSans,
                        fontWeight = FontWeight.Normal,
                    )
                }
            }

            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp),
                painter = painterResource(id = R.drawable.icon_rb),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null,
            )
        }

        TonalIconButton(
            modifier = Modifier.padding(end = 32.dp),
            imageVector = Icons.Filled.QuestionMark,
            onClick = {},
        )
    }
}

@Preview(widthDp = 800)
@Composable
fun TopNavigationBarPreview() {
    TopNavigationBar(
        navController = rememberNavController()
    )
}