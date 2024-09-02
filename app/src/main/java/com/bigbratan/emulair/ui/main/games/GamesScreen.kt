package com.bigbratan.emulair.ui.main.games

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.bigbratan.emulair.R
import com.bigbratan.emulair.activities.LocalFocusProvider
import com.bigbratan.emulair.ui.components.FadingScrimBackground
import com.bigbratan.emulair.ui.components.GamesListItem
import com.bigbratan.emulair.ui.components.LocalTopNavHeight
import com.bigbratan.emulair.ui.components.SolidScrimBackground
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

    val focusRequester = LocalFocusProvider.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
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
                    focusRequester = focusRequester,
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
    focusRequester: FocusRequester,
    onGameClick: (gameId: Int) -> Unit,
    onAchievementsClick: (gameId: Int) -> Unit,
    onGameOptionsClick: (gameId: Int) -> Unit,
) {
    val localConfiguration = LocalConfiguration.current
    val screenWidth by remember { mutableStateOf(localConfiguration.screenWidthDp.dp) }
    val itemCount by remember { mutableIntStateOf(games.size) }
    val itemWidth by remember { mutableStateOf(100.dp) }
    val currentFocusedItem = remember { mutableIntStateOf(0) }

    val coroutineScope = rememberCoroutineScope()
    val pagerState = rememberPagerState(pageCount = { itemCount })

    LaunchedEffect(pagerState.currentPage) {
        currentFocusedItem.intValue = pagerState.currentPage
    }

    // TODO: IMPROVE FOCUS MANAGEMENT
    /*
    - I need to put `.focusable()` on an item because I noticed otherwise the first element of the screen (the profile icon) comes into focus, for some reason
    - you can better notice this behaviour if you remove `.focusable()` from the Box inside SystemsScreen, OnlineScreen or SearchScreen
    - also, for some reason, focusing the top app bar doesn't allow me to re-focus on any item on the screen again
    */

    // TODO: CREATE CUSTOM HOVER MANAGEMENT
    // I just realized, hover management is what I need to do; all I wanted was to remove the ugly default hover decoration

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        GameBannerBackground(
            imageBanner = games[currentFocusedItem.intValue].imageBanner,
            imageBannerUri = games[currentFocusedItem.intValue].imageBannerUri,
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = LocalTopNavHeight.current),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            GameDetails(details = games[currentFocusedItem.intValue].details)

            Box(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
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
                    pageSpacing = 16.dp,
                    beyondViewportPageCount = 10,
                ) { page ->
                    val pageOffSet =
                        ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue

                    GamesListItem(
                        icon = games[page].icon,
                        iconUri = games[page].iconUri,
                        title = games[page].placeholderTitle,
                        fraction = 1f - pageOffSet.coerceIn(0f, 1f),
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

            GameActions(
                screenWidth = screenWidth,
                displayTitle = games[currentFocusedItem.intValue].displayTitle,
                systemName = games[currentFocusedItem.intValue].systemName,
                onAchievementsClick = { onAchievementsClick(games[pagerState.currentPage].id) },
                onGameOptionsClick = { onGameOptionsClick(games[pagerState.currentPage].id) },
            )
        }
    }
}

@Composable
private fun GameBannerBackground(
    imageBanner: String?,
    imageBannerUri: String?,
) {
    when {
        imageBanner != null -> {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.60f),
                model = imageBanner,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            SolidScrimBackground()
        }

        imageBannerUri != null -> {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.60f),
                model = imageBannerUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            SolidScrimBackground()
        }

        else -> {
            Image(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.60f),
                painter = painterResource(id = R.drawable.ill_deleteme),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            FadingScrimBackground(
                topColor = MaterialTheme.colorScheme.surface,
                middleColor = Color.Black.copy(alpha = 0.40f),
                bottomColor = MaterialTheme.colorScheme.surface,
            )
        }
    }
}

@Composable
private fun GameDetails(
    details: String,
) {
    Row(
        modifier = Modifier
            .padding(top = 20.dp)
            .padding(horizontal = 32.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Text(
            text = details,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontFamily = plusJakartaSans,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            style = TextStyle(platformStyle = removeFontPadding),
            maxLines = 1,
        )
    }
}

@Composable
private fun GameActions(
    screenWidth: Dp,
    displayTitle: String,
    systemName: String,
    onAchievementsClick: () -> Unit,
    onGameOptionsClick: () -> Unit,
) {
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
                text = displayTitle,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = plusJakartaSans,
                fontSize = 28.sp,
                fontWeight = FontWeight.SemiBold,
                style = TextStyle(platformStyle = removeFontPadding),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = systemName,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                .background(MaterialTheme.colorScheme.surfaceVariant)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(),
                    enabled = true,
                    onClick = onAchievementsClick,
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(64.dp),
                imageVector = Icons.Filled.Image,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                contentDescription = null,
            )

            Text(
                modifier = Modifier.padding(top = 12.dp),
                text = stringResource(id = R.string.games_achievement_title),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
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
                onClick = onGameOptionsClick,
            )
        }
    }
}
