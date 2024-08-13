package com.bigbratan.emulair.ui.main.games

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bigbratan.emulair.models.Game
import com.bigbratan.emulair.ui.components.GamesListItem
import com.bigbratan.emulair.ui.theme.plusJakartaSans
import com.bigbratan.emulair.ui.theme.removeFontPadding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

const val visibleItems = 6
const val timeUntilScrollStarts = 400L

@Composable
fun GamesScreen(
    viewModel: GamesViewModel = hiltViewModel(),
    onGameClick: (Game) -> Unit,
) {
    val games = previewGames
    val localConfiguration = LocalConfiguration.current
    val screenWidth by remember {
        mutableStateOf(localConfiguration.screenWidthDp.dp)
    }
    val itemCount = previewGames.size
    val itemSpacing = 16.dp
    val contentPaddingStart = 32.dp
    val itemWidth = 150.dp /* (screenWidth - itemSpacing * (visibleItems - 1)) / visibleItems */
    val contentPaddingEnd = screenWidth - itemWidth - contentPaddingStart

    val pagerState = rememberPagerState(pageCount = { itemCount })
    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val currentFocusedItem = remember { mutableIntStateOf(0) }
    val isLongPressing = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(pagerState.currentPage) {
        currentFocusedItem.intValue = pagerState.currentPage
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
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
                // pageSize = PageSize.Fixed(1000.dp),
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
                    title = games[page].displayTitle,
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

        Text(
            modifier = Modifier
                .width((itemWidth + itemSpacing) * 2)
                .padding(top = 24.dp)
                .padding(horizontal = contentPaddingStart),
            text = games[currentFocusedItem.intValue].displayTitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = plusJakartaSans,
            fontSize = 28.sp,
            fontWeight = FontWeight.SemiBold,
            style = TextStyle(platformStyle = removeFontPadding),
        )
    }
}

val previewGames = listOf(
    Game(
        id = "1",
        systemId = "100",
        fullTitle = "Castlevania: Symphony of the Night (Europe) (En,Fr,It)",
        displayTitle = "Castlevania: Symphony of the Night",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "2",
        systemId = "100",
        fullTitle = "Need for Speed: Most Wanted (2005) (USA) (En,Es)",
        displayTitle = "Need for Speed: Most Wanted (2005)",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "3",
        systemId = "100",
        fullTitle = "Metal Gear Solid (Asia) (En,Jp,Kr,Cn)",
        displayTitle = "Metal Gear Solid",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "4",
        systemId = "100",
        fullTitle = "God of War (USA) (En,Fr,It)",
        displayTitle = "God of War",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "5",
        systemId = "100",
        fullTitle = "Assassin's Creed (USA) (En,Fr,It)",
        displayTitle = "Assassin's Creed",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "6",
        systemId = "100",
        fullTitle = "Final Fantasy VII (USA) (En,Fr,It)",
        displayTitle = "Final Fantasy VII",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "7",
        systemId = "100",
        fullTitle = "The Legend of Zelda: Ocarina of Time (USA) (En,Fr,It)",
        displayTitle = "The Legend of Zelda: Ocarina of Time",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "8",
        systemId = "100",
        fullTitle = "Super Mario Bros. (USA) (En,Fr,It)",
        displayTitle = "Super Mario Bros.",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "9",
        systemId = "100",
        fullTitle = "Silent Hill (USA) (En,Fr,It)",
        displayTitle = "Silent Hill",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "10",
        systemId = "100",
        fullTitle = "Resident Evil 2 (USA) (En,Fr,It)",
        displayTitle = "Resident Evil 2",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "11",
        systemId = "100",
        fullTitle = "The Elder Scrolls V: Skyrim (USA) (En,Fr,It)",
        displayTitle = "The Elder Scrolls V: Skyrim",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "12",
        systemId = "100",
        fullTitle = "Doom (USA) (En,Fr,It)",
        displayTitle = "Doom",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "13",
        systemId = "100",
        fullTitle = "Half-Life (USA) (En,Fr,It)",
        displayTitle = "Half-Life",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "14",
        systemId = "100",
        fullTitle = "Grand Theft Auto III (USA) (En,Fr,It)",
        displayTitle = "Grand Theft Auto III",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "15",
        systemId = "100",
        fullTitle = "Minecraft (USA) (En,Fr,It)",
        displayTitle = "Minecraft",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
    Game(
        id = "16",
        systemId = "100",
        fullTitle = "Terraria (USA) (En,Fr,It)",
        displayTitle = "Terraria",
        lastIndexedAt = 99999,
        fileName = "file",
        fileUri = "file.iso",
    ),
)