package com.bigbratan.emulair.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.ui.theme.plusJakartaSans
import java.util.Locale

private fun computeTitlePlaceholder(title: String): String {
    val romanNumeralRegex = "^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$".toRegex()

    val sanitizedName = title.replace(Regex("\\(.*\\)"), "")

    if (!sanitizedName.contains(" ")) {
        return sanitizedName
    }

    return sanitizedName.split(Regex("\\s|(?=\\p{Punct})")).asSequence()
        .map { word ->
            if (romanNumeralRegex.matches(word.uppercase(Locale.ROOT))) {
                word.uppercase(Locale.ROOT)
            } else {
                word.firstOrNull()?.uppercaseChar().toString()
            }
        }
        .filter {
            it.firstOrNull() != null && (it.first().isDigit() or it.first()
                .isUpperCase() or (it.first() == '&') or (it.first() == ':') or (it.first() == '.') or (it.first() == '-'))
        }
        .joinToString("")
        .ifBlank { title.first().toString() }
        .replaceFirstChar(Char::titlecase)
}

@Composable
fun GamesListItem(
    modifier: Modifier = Modifier,
    icon: String? = null,
    iconUri: String? = null,
    title: String,
    itemWidth: Dp,
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
                text = computeTitlePlaceholder(title),
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = plusJakartaSans,
                fontSize = (itemWidth / 6).value.sp,
                fontWeight = FontWeight.Bold,
                style = TextStyle(platformStyle = removeFontPadding),
            )
        }
    }
}