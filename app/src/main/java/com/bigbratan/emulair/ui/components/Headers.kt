package com.bigbratan.emulair.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbratan.emulair.R
import com.bigbratan.emulair.navigation.Destination
import com.bigbratan.emulair.ui.theme.plusJakartaSans
import com.bigbratan.emulair.ui.theme.removeFontPadding

enum class TopNavDestination(val route: String, val title: Int) {
    GAMES(Destination.Main.GamesDestination.route, R.string.games_title),
    SYSTEMS(Destination.Main.SystemsDestination.route, R.string.systems_title),
    ONLINE(Destination.Main.OnlineDestination.route, R.string.online_title),
    SEARCH(Destination.Main.SearchDestination.route, R.string.search_title),
}

val topNavHeight = 48.dp
val LocalTopNavHeight = compositionLocalOf { topNavHeight }

@Composable
fun TopNavigationBar(
    modifier: Modifier = Modifier,
    currentRoute: String? = null,
    onTabSwitch: (TopNavDestination) -> Unit,
    onProfileClick: () -> Unit,
    onSettingsClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(topNavHeight)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TonalIconButton(
            modifier = Modifier.padding(start = 32.dp),
            imageVector = Icons.Filled.Person,
            onClick = { onProfileClick() },
        )

        Row(
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // TODO: SHOW LB/RB ICONS ONLY WHEN GAME PAD IS CONNECTED
            /* Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp),
                painter = painterResource(id = R.drawable.icon_lb),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null,
            ) */

            TopNavDestination.entries.forEachIndexed { index, navDestination ->
                Button(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    onClick = { onTabSwitch(navDestination) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentRoute == navDestination.route) {
                            MaterialTheme.colorScheme.surfaceVariant
                        } else {
                            Color.Transparent
                        },
                        contentColor = if (currentRoute == navDestination.route) {
                            MaterialTheme.colorScheme.onSurface
                        } else {
                            MaterialTheme.colorScheme.onSurfaceVariant
                        },
                    ),
                ) {
                    Text(
                        text = stringResource(id = navDestination.title),
                        fontFamily = plusJakartaSans,
                        fontWeight = FontWeight.Normal,
                        fontSize = 16.sp,
                        style = TextStyle(platformStyle = removeFontPadding),
                    )
                }
            }

            /* Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .size(36.dp),
                painter = painterResource(id = R.drawable.icon_rb),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null,
            ) */
        }

        TonalIconButton(
            modifier = Modifier.padding(end = 32.dp),
            imageVector = Icons.Filled.Settings,
            onClick = { onSettingsClick() },
        )
    }
}

@Composable
fun TopTitleBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackClick: () -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(topNavHeight)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TonalIconButton(
            modifier = Modifier.padding(start = 32.dp),
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            onClick = { onBackClick() },
        )

        Text(
            modifier = Modifier.padding(start = 16.dp),
            text = title,
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            style = TextStyle(platformStyle = removeFontPadding),
        )
    }
}

@Preview(widthDp = 800)
@Composable
fun TopNavigationBarPreview() {
    TopNavigationBar(
        onTabSwitch = {},
        onProfileClick = {},
        onSettingsClick = {},
    )
}

@Preview(widthDp = 800)
@Composable
fun TopTitleBarPreview() {
    TopTitleBar(
        title = "Title",
        onBackClick = {},
    )
}