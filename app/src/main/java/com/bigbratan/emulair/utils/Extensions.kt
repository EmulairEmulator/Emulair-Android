package com.bigbratan.emulair.utils

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.navigation.NavDestination
import com.bigbratan.emulair.navigation.Destination
import com.bigbratan.emulair.ui.components.TopNavDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val timeUntilScrollStarts = 360L

fun Modifier.onListScroll(
    coroutineScope: CoroutineScope,
    pagerState: PagerState,
    itemCount: Int,
    currentFocusedItem: MutableState<Int>,
    onKeyEvent: () -> Unit,
): Modifier {
    return this.onKeyEvent { event ->
        when {
            (event.key == Key.DirectionRight || event.key == Key.DirectionLeft || event.key == Key.A || event.key == Key.D) && event.type == KeyEventType.KeyDown -> {
                val direction = if (event.key == Key.DirectionRight || event.key == Key.D) 1 else -1

                coroutineScope.launch {
                    val newPage = (pagerState.currentPage + direction)
                        .coerceIn(0, itemCount - 1)

                    pagerState.animateScrollToPage(
                        page = newPage,
                        animationSpec = spring(stiffness = Spring.StiffnessHigh),
                    )
                    currentFocusedItem.value = newPage
                    onKeyEvent()
                    delay(timeUntilScrollStarts)
                }
                true
            }

            (event.key == Key.DirectionRight || event.key == Key.DirectionLeft || event.key == Key.A || event.key == Key.D) && event.type == KeyEventType.KeyUp -> {
                true
            }

            else -> false
        }
    }
}

fun Modifier.onShoulderButtonPress(
    nextDestination: TopNavDestination,
    previousDestination: TopNavDestination,
    onNext: (nextDestination: TopNavDestination) -> Unit,
    onPrevious: (previousDestination: TopNavDestination) -> Unit,
): Modifier {
    return this.onKeyEvent { event ->
        when {
            (event.key == Key.ButtonL1 || event.key == Key.Q) && event.type == KeyEventType.KeyDown -> {
                onPrevious(previousDestination)
                true
            }

            (event.key == Key.ButtonR1 || event.key == Key.E) && event.type == KeyEventType.KeyDown -> {
                onNext(nextDestination)
                true
            }

            else -> false
        }
    }
}

fun NavDestination?.toTopNavDestination(): TopNavDestination {
    return when (this?.route) {
        Destination.Main.GamesDestination.route -> TopNavDestination.GAMES
        Destination.Main.SystemsDestination.route -> TopNavDestination.SYSTEMS
        Destination.Main.OnlineDestination.route -> TopNavDestination.ONLINE
        Destination.Main.SearchDestination.route -> TopNavDestination.SEARCH
        else -> TopNavDestination.GAMES
    }
}

fun TopNavDestination.next(): TopNavDestination {
    val currentRouteIndex =
        TopNavDestination.entries.toTypedArray().indexOfFirst { it == this }
    val nextRouteIndex = (currentRouteIndex + 1) % TopNavDestination.entries.size
    return TopNavDestination.entries[nextRouteIndex]
}

fun TopNavDestination.previous(): TopNavDestination {
    val currentRouteIndex =
        TopNavDestination.entries.toTypedArray().indexOfFirst { it == this }
    val previousRouteIndex =
        (currentRouteIndex - 1 + TopNavDestination.entries.size) % TopNavDestination.entries.size
    return TopNavDestination.entries[previousRouteIndex]
}