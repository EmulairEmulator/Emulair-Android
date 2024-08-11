package com.bigbratan.emulair.ui.components

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bigbratan.emulair.models.Game
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.ui.theme.plusJakartaSans

private fun computeTitlePlaceholder(title: String): String {
    val sanitizedName = title
        .replace(Regex("\\(.*\\)"), "")

    return sanitizedName.asSequence()
        .filter { it.isDigit() or it.isUpperCase() or (it == '&') }
        .take(3)
        .joinToString("")
        .ifBlank { title.first().toString() }
        .replaceFirstChar(Char::titlecase)
}

@Composable
fun GameCarouselItem(
    modifier: Modifier = Modifier,
    icon: String? = null,
    iconUri: String? = null,
    title: String,
    isFocused: Boolean,
    onFocusChanged: (Boolean) -> Unit,
    focusRequester: FocusRequester,
    onGameClick: () -> Unit,
) {
    val width by animateDpAsState(targetValue = if (isFocused) 188.dp else 108.dp, label = "")
    val scale by animateFloatAsState(targetValue = if (isFocused) 1.8f else 1f, label = "")
    val padding by animateDpAsState(targetValue = if (isFocused) 0.dp else 16.dp, label = "")

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                onFocusChanged(focusState.isFocused)
            }
            .padding(top = padding)
            .width(width)
            .clip(RoundedCornerShape(16.dp))
            .scale(scale)
            .clickable(onClick = onGameClick)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .aspectRatio(1f)
            .focusRequester(focusRequester)
            .focusable(),
        contentAlignment = Alignment.Center,
    ) {
        when {
            !iconUri.isNullOrEmpty() -> Image(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .aspectRatio(1f),
                painter = painterResource(id = iconUri.toInt()),
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )

            !icon.isNullOrEmpty() -> AsyncImage(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .aspectRatio(1f),
                model = ImageRequest.Builder(LocalContext.current)
                    .data(icon)
                    .crossfade(true)
                    .build(),
                contentScale = ContentScale.Crop,
                // placeholder = painterResource(id = R.drawable.ic_placeholder_game),
                contentDescription = null,
            )

            else -> Text(
                modifier = Modifier.align(Alignment.Center),
                text = computeTitlePlaceholder(title),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = plusJakartaSans,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = removeFontPadding),
            )
        }
    }
}

@Composable
fun GameCarousel(
    modifier: Modifier = Modifier,
    games: List<Game>,
    onGameClick: () -> Unit,
) {
    val listState = rememberLazyListState()
    var lastFocusedIndex by rememberSaveable { mutableIntStateOf(0) }
    val focusRequesters = remember { List(games.size) { FocusRequester() } }

    LazyRow(
        modifier = modifier.fillMaxWidth(),
        state = listState,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 32.dp)
    ) {
        items(games.size) { index ->
            GameCarouselItem(
                icon = games[index].icon,
                iconUri = games[index].iconUri,
                title = games[index].title,
                isFocused = index == lastFocusedIndex,
                onFocusChanged = { isFocused ->
                    if (isFocused) {
                        lastFocusedIndex = index
                    }
                },
                focusRequester = focusRequesters[index],
                onGameClick = { onGameClick() },
            )
        }
    }

    LaunchedEffect(listState) {
        listState.scrollToItem(lastFocusedIndex)
        focusRequesters[lastFocusedIndex].requestFocus()

        snapshotFlow { listState.firstVisibleItemIndex }
            .collect { index ->
                if (index == 0) {
                    listState.animateScrollToItem(0)
                }

                if (index == games.size - 1) {
                    listState.animateScrollToItem(games.size - 1)
                }
            }
    }
}

@Preview
@Composable
fun GameCarouselPreview() {
    GameCarousel(
        games = listOf(
            Game(
                id = "1",
                systemId = "10",
                title = "Castlevania: Symphony of the Night",
                lastIndexedAt = 99999,
                fileName = "castlevania_sotn",
                fileUri = "castlevania_sotn.iso",
            ),
            Game(
                id = "2",
                systemId = "10",
                title = "Need for Speed: Most Wanted (2005)",
                lastIndexedAt = 99999,
                fileName = "castlevania_sotn",
                fileUri = "castlevania_sotn.iso",
            ),
            Game(
                id = "3",
                systemId = "10",
                title = "Metal Gear Solid",
                lastIndexedAt = 99999,
                fileName = "castlevania_sotn",
                fileUri = "castlevania_sotn.iso",
            ),
            Game(
                id = "4",
                systemId = "10",
                title = "God of War",
                lastIndexedAt = 99999,
                fileName = "castlevania_sotn",
                fileUri = "castlevania_sotn.iso",
            ),
        ),
        onGameClick = {}
    )
}