package com.bigbratan.emulair.ui.main.games

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbratan.emulair.models.Game
import com.bigbratan.emulair.ui.components.ImmersiveGameCarousel
import com.bigbratan.emulair.ui.components.ImmersiveGameCarouselItem
import com.bigbratan.emulair.ui.components.immersive.ImmersiveRow
import com.bigbratan.emulair.ui.theme.noFontPadding
import com.bigbratan.emulair.ui.theme.plusJakartaSans

@Composable
fun GamesScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        SelectedItemIndicator(
            visibleItems = 6,
            selectedItemOffset = 32.dp,
            itemSpacing = 8.dp,
        )

        /*ImmersiveGameCarousel(
            modifier = Modifier.padding(top = 24.dp),
            games = listOf(
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
                    title = "GTA III",
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
            ),
            visibleItems = 6,
            selectedItemOffset = 32.dp,
            itemSpacing = 8.dp,
            onGameClick = { index ->
                Log.d("GamesScreen", "Game clicked: $index")
            }
        )*/

        Column {
            ImmersiveRow(
                modifier = Modifier,
                visibleItems = 6,
                selectedItemOffset = 32.dp,
                itemSpacing = 8.dp,
            ) {
                for (index in 0..100) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .aspectRatio(1f)
                            .focusable(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Game $index",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = plusJakartaSans,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(platformStyle = noFontPadding),
                        )
                    }
                }
            }

            /*LazyRow(
                modifier = Modifier.padding(top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(16.dp),
            ) {
                items(10000) { index ->
                    Box(
                        modifier = Modifier
                            .width(150.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                            .aspectRatio(1f),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.Center),
                            text = "Game $index",
                            color = MaterialTheme.colorScheme.onSurface,
                            fontFamily = plusJakartaSans,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold,
                            style = TextStyle(platformStyle = noFontPadding),
                        )
                    }
                }
            }*/
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