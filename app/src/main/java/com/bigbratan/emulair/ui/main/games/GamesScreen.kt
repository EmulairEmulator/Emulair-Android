package com.bigbratan.emulair.ui.main.games

import android.util.Log
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.PagerSnapDistance
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.ui.theme.plusJakartaSans
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
fun GamesScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp
        val visibleItems = 6
        val itemCount = 10
        val itemSpacing = 16.dp
        val itemWidth = (screenWidth - itemSpacing * (visibleItems - 1)) / visibleItems
        val contentPaddingStart = 32.dp
        val contentPaddingEnd = screenWidth - itemWidth - contentPaddingStart

        val pagerState = rememberPagerState(pageCount = { itemCount })
        val coroutineScope = rememberCoroutineScope()
        val focusRequesters = remember { List(itemCount) { FocusRequester() } }
        val currentFocusedItem = remember { mutableIntStateOf(0) }

        LaunchedEffect(pagerState.currentPage) {
            currentFocusedItem.intValue = pagerState.currentPage
            focusRequesters[pagerState.currentPage].requestFocus()
        }

        HorizontalPager(
            modifier = Modifier
                .padding(top = 24.dp)
                .onKeyEvent { event ->
                    when (event.key) {
                        Key.DirectionRight, Key.DirectionLeft -> {
                            val direction = if (event.key == Key.DirectionRight) 1 else -1
                            val newPage = (pagerState.currentPage + direction)
                                .coerceIn(0, itemCount - 1)

                            coroutineScope.launch {
                                pagerState.animateScrollToPage(newPage)
                            }
                            currentFocusedItem.intValue = newPage
                            focusRequesters[newPage].requestFocus()
                            true
                        }

                        else -> false
                    }
                },
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
            val alphaFactor = lerp(
                start = 0.5f,
                stop = 1f,
                fraction = 1f - pageOffSet.coerceIn(0f, 1f),
            )
            val scaleFactor = lerp(
                start = 1f,
                stop = 1.2f,
                fraction = 1f - pageOffSet.coerceIn(0f, 1f),
            )

            Box(
                modifier = Modifier
                    .graphicsLayer { alpha = alphaFactor }
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .focusRequester(focusRequesters[page])
                    .onFocusChanged { focusState ->
                        if (focusState.isFocused) {
                            currentFocusedItem.intValue = page
                        }
                    }
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(),
                        enabled = true,
                    ) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page)
                        }
                    },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    modifier = Modifier.align(Alignment.Center),
                    text = "Game $page",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = plusJakartaSans,
                    fontSize = (itemWidth / 6).value.sp,
                    fontWeight = FontWeight.Bold,
                    style = TextStyle(platformStyle = removeFontPadding),
                )
            }
        }
    }
}

@Composable
fun SelectedItemIndicator(
    visibleItems: Int,
    selectedItemOffset: Dp = 0.dp,
    itemSpacing: Dp = 0.dp,
) {
    val itemWidth =
        LocalConfiguration.current.screenWidthDp.dp / visibleItems

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = Color.Red,
            start = Offset(x = (selectedItemOffset + itemWidth / 2).toPx(), y = 0f),
            end = Offset(x = (selectedItemOffset + itemWidth / 2).toPx(), y = size.height),
            strokeWidth = 2.dp.toPx()
        )
    }
}