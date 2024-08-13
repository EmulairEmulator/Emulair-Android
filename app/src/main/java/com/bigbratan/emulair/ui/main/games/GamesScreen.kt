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
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.bigbratan.emulair.ui.components.GamesListItem
import com.bigbratan.emulair.ui.theme.plusJakartaSans
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.utils.perform
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

const val timeUntilScrollStarts = 360L

@Composable
fun GamesScreen(
    viewModel: GamesViewModel = hiltViewModel(),
    onGameClick: (GameItemViewModel) -> Unit,
) {
    val gamesState by viewModel.gamesFlow.collectAsState()

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        gamesState.perform(
            onLoading = {
                // TODO: ADD LOADER (SHIMMER WOULD BE NICE)
            },
            onSuccess = { games ->
                GamesView(
                    games = games,
                    onGameClick = onGameClick,
                )
            },
            onError = {
                // TODO: ADD ERROR MESSAGE IN SCREEN (WITH RELOAD BUTTON)
            }
        )
    }
}

@Composable
private fun GamesView(
    games: List<GameItemViewModel>,
    onGameClick: (GameItemViewModel) -> Unit,
) {
    val localConfiguration = LocalConfiguration.current
    val screenWidth by remember { mutableStateOf(localConfiguration.screenWidthDp.dp) }
    val itemSpacing by remember { mutableStateOf(16.dp) }
    val itemWidth by remember { mutableStateOf(100.dp) }
    val itemCount by remember { mutableIntStateOf(games.size) }

    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val currentFocusedItem = remember { mutableIntStateOf(0) }
    val isLongPressing = remember { mutableStateOf(false) }
    val pagerState = rememberPagerState(pageCount = { itemCount })

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(pagerState.currentPage) {
        currentFocusedItem.intValue = pagerState.currentPage
    }

    Box(
        modifier = Modifier
            .padding(top = 20.dp)
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
                start = 32.dp,
                end = screenWidth - itemWidth - 32.dp,
            ),
            pageSpacing = itemSpacing,
            beyondViewportPageCount = 10,
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
                title = games[page].placeholderTitle,
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
            .width(screenWidth / 2)
            .padding(top = 24.dp)
            .padding(horizontal = 32.dp),
        text = games[currentFocusedItem.intValue].displayTitle,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontFamily = plusJakartaSans,
        fontSize = 28.sp,
        fontWeight = FontWeight.SemiBold,
        style = TextStyle(platformStyle = removeFontPadding),
    )
}