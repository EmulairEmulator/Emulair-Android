package com.bigbratan.emulair.ui.main.games

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bigbratan.emulair.models.Game
import com.bigbratan.emulair.ui.components.GamesListItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun GamesScreen(
    viewModel: GamesViewModel = hiltViewModel(),
    onGameClick: (Game) -> Unit,
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val visibleItems = 6
    val itemCount = previewGames.size
    val itemSpacing = 16.dp
    val itemWidth = (screenWidth - itemSpacing * (visibleItems - 1)) / visibleItems
    val contentPaddingStart = 32.dp
    val contentPaddingEnd = screenWidth - itemWidth - contentPaddingStart

    val pagerState = rememberPagerState(pageCount = { itemCount })
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val currentFocusedItem = remember { mutableIntStateOf(0) }
    val isLongPressing = remember { mutableStateOf(false) }
    val timeUntilScrollStarts = 400L

    val games = previewGames

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(pagerState.currentPage) {
        currentFocusedItem.intValue = pagerState.currentPage
    }

    Box(
        modifier = Modifier
            .padding(top = 24.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { event ->
                when {
                    (event.key == Key.DirectionRight || event.key == Key.DirectionLeft) && event.type == KeyEventType.KeyDown -> {
                        val direction = if (event.key == Key.DirectionRight) 1 else -1
                        isLongPressing.value = true

                        coroutineScope.launch {
                            while (isLongPressing.value) {
                                val newPage = (pagerState.currentPage + direction)
                                    .coerceIn(0, itemCount - 1)

                                pagerState.animateScrollToPage(
                                    page = newPage,
                                    animationSpec = spring(stiffness = Spring.StiffnessHigh),
                                )
                                currentFocusedItem.intValue = newPage
                                focusRequester.requestFocus()
                                delay(timeUntilScrollStarts)
                            }
                        }
                        true
                    }

                    (event.key == Key.DirectionRight || event.key == Key.DirectionLeft) && event.type == KeyEventType.KeyUp -> {
                        isLongPressing.value = false
                        true
                    }

                    else -> false
                }
            },
    ) {
        HorizontalPager(
            modifier = Modifier,
            state = pagerState,
            flingBehavior = PagerDefaults.flingBehavior(
                state = pagerState,
                pagerSnapDistance = PagerSnapDistance.atMost(itemCount),
            ),
            contentPadding = PaddingValues(
                start = contentPaddingStart,
                end = contentPaddingEnd
            ),
            pageSpacing = itemSpacing,
            beyondViewportPageCount = visibleItems,
        ) { page ->
            val pageOffSet =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

            GamesListItem(
                modifier = Modifier.graphicsLayer {
                    alpha = lerp(
                        start = 0.4f,
                        stop = 1f,
                        fraction = 1f - pageOffSet.coerceIn(0f, 1f),
                    )
                },
                icon = games[page].icon,
                iconUri = games[page].iconUri,
                title = games[page].title,
                itemWidth = itemWidth,
                onGameClick = {
                    currentFocusedItem.intValue = page

                    if (pagerState.currentPage == page) {
                        onGameClick(games[page])
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page)
                        }
                    }
                },
            )
        }
    }
}

val previewGames = listOf(
    Game(
        id = "1",
        systemId = "100",
        title = "Castlevania: Symphony of the Night",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "2",
        systemId = "100",
        title = "Need for Speed: Most Wanted (2005)",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "3",
        systemId = "100",
        title = "Metal Gear Solid",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "4",
        systemId = "100",
        title = "God of War",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "5",
        systemId = "100",
        title = "Assassin's Creed",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "6",
        systemId = "100",
        title = "Final Fantasy VII",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "7",
        systemId = "100",
        title = "The Legend of Zelda: Ocarina of Time",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "8",
        systemId = "100",
        title = "Super Mario Bros.",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "9",
        systemId = "100",
        title = "Silent Hill",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "10",
        systemId = "100",
        title = "Resident Evil 2",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "11",
        systemId = "100",
        title = "The Elder Scrolls V: Skyrim",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "12",
        systemId = "100",
        title = "Doom",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "13",
        systemId = "100",
        title = "Half-Life",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "14",
        systemId = "100",
        title = "Grand Theft Auto III",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "15",
        systemId = "100",
        title = "Minecraft",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "16",
        systemId = "100",
        title = "Terraria",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
)