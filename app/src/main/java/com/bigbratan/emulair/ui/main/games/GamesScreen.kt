package com.bigbratan.emulair.ui.main.games

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.ripple.rememberRipple
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bigbratan.emulair.ui.components.GamesListItem
import com.bigbratan.emulair.ui.components.LocalTopNavHeight
import com.bigbratan.emulair.ui.components.TonalIconButton
import com.bigbratan.emulair.ui.components.TopNavDestination
import com.bigbratan.emulair.ui.theme.plusJakartaSans
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.utils.onListScroll
import com.bigbratan.emulair.utils.onShoulderButtonPress
import com.bigbratan.emulair.utils.perform
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun GamesScreen(
    viewModel: GamesViewModel = hiltViewModel(),
    onGameClick: (gameId: Int) -> Unit,
    onAchievementsClick: (gameId: Int) -> Unit,
    onGameOptionsClick: (gameId: Int) -> Unit,
    onTabSwitch: (TopNavDestination) -> Unit,
) {
    val gamesState by viewModel.gamesFlow.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .onShoulderButtonPress(
                nextDestination = TopNavDestination.SYSTEMS,
                previousDestination = TopNavDestination.SEARCH,
                onNext = { onTabSwitch(TopNavDestination.SYSTEMS) },
                onPrevious = { onTabSwitch(TopNavDestination.SEARCH) },
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = LocalTopNavHeight.current),
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
                        onAchievementsClick = onAchievementsClick,
                        onGameOptionsClick = onGameOptionsClick,
                    )
                },
                onError = {
                    // TODO: ADD ERROR MESSAGE IN SCREEN (WITH RELOAD BUTTON)
                }
            )
        }
    }
}

@Composable
private fun GamesView(
    games: List<GameItemViewModel>,
    onGameClick: (gameId: Int) -> Unit,
    onAchievementsClick: (gameId: Int) -> Unit,
    onGameOptionsClick: (gameId: Int) -> Unit,
) {
    val localConfiguration = LocalConfiguration.current
    val screenWidth by remember { mutableStateOf(localConfiguration.screenWidthDp.dp) }
    val itemSpacing by remember { mutableStateOf(16.dp) }
    val itemCount by remember { mutableIntStateOf(games.size) }
    val itemWidth by remember { mutableStateOf(100.dp) }

    val coroutineScope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    val currentFocusedItem = remember { mutableIntStateOf(0) }
    val pagerState = rememberPagerState(pageCount = { itemCount })

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    LaunchedEffect(pagerState.currentPage) {
        currentFocusedItem.intValue = pagerState.currentPage
    }

    Row(
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = games[currentFocusedItem.intValue].details,
            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f),
            fontFamily = plusJakartaSans,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            style = TextStyle(platformStyle = removeFontPadding),
            maxLines = 1,
        )
    }

    Box(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusable()
            .onListScroll(
                coroutineScope = coroutineScope,
                pagerState = pagerState,
                itemCount = itemCount,
                currentFocusedItem = currentFocusedItem,
                onKeyEvent = {
                    focusRequester.requestFocus()
                },
            ),
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
                        onGameClick(games[page].id)
                    } else {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page)
                        }
                    }
                },
            )
        }
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                horizontal = 32.dp,
                vertical = 24.dp,
            ),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.Start,
    ) {
        Column(
            modifier = Modifier.width(screenWidth / 2.4f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = games[currentFocusedItem.intValue].displayTitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = plusJakartaSans,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(platformStyle = removeFontPadding),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = games[currentFocusedItem.intValue].systemName,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f),
                fontFamily = plusJakartaSans,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal,
                style = TextStyle(platformStyle = removeFontPadding),
                maxLines = 1,
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .width(180.dp)
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                    enabled = true,
                    onClick = { onAchievementsClick(games[pagerState.currentPage].id) }
                ),
        ) {

        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            TonalIconButton(
                imageVector = Icons.Filled.FavoriteBorder,
                size = 24.dp,
                onClick = {
                    // TODO: IMPLEMENT FAVORITE FUNCTIONALITY
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            TonalIconButton(
                imageVector = Icons.Filled.Tune,
                size = 24.dp,
                onClick = { onGameOptionsClick(games[pagerState.currentPage].id) }
            )
        }
    }
}
