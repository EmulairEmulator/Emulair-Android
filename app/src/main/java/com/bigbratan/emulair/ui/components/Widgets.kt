package com.bigbratan.emulair.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.ui.theme.plusJakartaSans
import java.util.Locale

@Composable
fun GamesListItem(
    modifier: Modifier = Modifier,
    icon: String? = null,
    iconUri: String? = null,
    title: String,
    onGameClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                enabled = true,
                onClick = onGameClick
            )
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .aspectRatio(1f),
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
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = plusJakartaSans,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = removeFontPadding),
            )
        }
    }
}

@Preview
@Composable
fun GamesListItemPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .width(150.dp)
            .padding(16.dp),
    ) {
        GamesListItem(
            title = "Game",
            onGameClick = {},
        )
    }
}