package com.bigbratan.emulair.ui.main.games

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bigbratan.emulair.R
import com.bigbratan.emulair.ui.components.GamesListItem
import com.bigbratan.emulair.ui.components.LocalTopNavHeight
import com.bigbratan.emulair.ui.components.TonalIconButton
import com.bigbratan.emulair.ui.components.TopNavDestination
import com.bigbratan.emulair.ui.theme.plusJakartaSans
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.utils.next
import com.bigbratan.emulair.utils.onListScroll
import com.bigbratan.emulair.utils.onShoulderButtonPress
import com.bigbratan.emulair.utils.perform
import com.bigbratan.emulair.utils.previous
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
            .focusable()
            .padding(top = LocalTopNavHeight.current)
            .background(Color.Transparent)
            .onShoulderButtonPress(
                nextDestination = TopNavDestination.GAMES.next(),
                previousDestination = TopNavDestination.GAMES.previous(),
                onNext = { onTabSwitch(TopNavDestination.GAMES.next()) },
                onPrevious = { onTabSwitch(TopNavDestination.GAMES.previous()) },
            ),
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

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
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

        // TODO: IMPROVE FOCUS MANAGEMENT
        /*
        - when any UI element is focused in general, a default decoration is applied to it, making its colors brighter
        - applying `.focusable()` removes this default decoration
        - this carousel has `.focusable()` applied to it, so the default decoration of every item is removed
        - and for some reason, this also means items aren't actually into focus, they simply appear to be so (I don't really know what `.focusable()` actually does)
        - I mean, I guess the default decoration appears only when the item is actually in focus, and because it doesn't appear on the items of the carousel, it means the items aren't actually in focus
        - focus is regained once the user clicks an item (try pressing "Enter" on the Android Studio emulator when an item is selected)
        - however, this isn't good, because no other action is performed when the clicking occurs, only the focus is regained
        - the user has to click ONCE MORE in order to perform whatever action happens on `onGameClick()`
        - I need to learn proper focus management in Compose

        - I also need to put `.focusable()` on an item because I noticed otherwise the first element of the screen (the profile icon) comes into focus, for some reason
        - you can better notice this behaviour if you remove `.focusable()` from the Box inside SystemsScreen, OnlineScreen or SearchScreen
        */
        Box(
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .focusable()
                .onListScroll(
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
                            start = 0.40f,
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
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column(
                modifier = Modifier
                    .width(180.dp)
                    .height(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.40f))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(),
                        enabled = true,
                        onClick = { onAchievementsClick(games[pagerState.currentPage].id) }
                    ),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Icon(
                    modifier = Modifier.size(64.dp),
                    imageVector = Icons.Filled.Image,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
                    contentDescription = null,
                )

                Text(
                    modifier = Modifier.padding(top = 12.dp),
                    text = stringResource(id = R.string.games_achievement_title),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.60f),
                    fontFamily = plusJakartaSans,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    style = TextStyle(platformStyle = removeFontPadding),
                )
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
}
