package com.bigbratan.emulair.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.ui.theme.plusJakartaSans

@Composable
internal fun TonalIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    size: Dp = 16.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.60f))
            .padding(size / 2),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(size),
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
            contentDescription = null,
        )
    }
}

@Composable
internal fun TransparentIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    size: Dp = 16.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
            .padding(size / 2),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(size),
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
            contentDescription = null,
        )
    }
}

@Composable
internal fun OutlinedIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    size: Dp = 16.dp,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    bounded = true,
                    color = MaterialTheme.colorScheme.primary,
                ),
            )
            .border(
                BorderStroke(
                    width = size / 8,
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.60f),
                ),
                shape = CircleShape
            )
            .padding(size / 2),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(size),
            imageVector = imageVector,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.60f),
            contentDescription = null,
        )
    }
}

@Composable
internal fun TonalTextButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val backgroundColor = if (enabled) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    }

    val textColor = if (enabled) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
    }

    Text(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(56.dp))
            .run {
                if (enabled) clickable(onClick = onClick) else this
            }
            .background(backgroundColor)
            .padding(vertical = 12.dp),
        text = label,
        fontFamily = plusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        color = textColor,
        textAlign = TextAlign.Center,
        style = TextStyle(platformStyle = removeFontPadding),
    )
}

@Composable
internal fun TransparentTextButton(
    modifier: Modifier = Modifier,
    label: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    val textColor = if (enabled) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
    }

    Text(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(56.dp))
            .run {
                if (enabled) clickable(onClick = onClick) else this
            }
            .padding(vertical = 12.dp),
        text = label,
        fontFamily = plusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        color = textColor,
        textAlign = TextAlign.Center,
        style = TextStyle(platformStyle = removeFontPadding),
    )
}

@Composable
internal fun OutlinedTextButton(
    modifier: Modifier = Modifier,
    label: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
) {
    val borderColor = if (enabled) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
    }

    val textColor = if (enabled) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
    }

    Text(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(56.dp))
            .run {
                if (enabled) clickable(onClick = onClick) else this
            }
            .border(
                BorderStroke(
                    width = 2.dp,
                    color = borderColor
                ),
                shape = RoundedCornerShape(56.dp)
            )
            .padding(vertical = 12.dp),
        text = label,
        fontFamily = plusJakartaSans,
        fontWeight = FontWeight.Medium,
        fontSize = 20.sp,
        color = textColor,
        textAlign = TextAlign.Center,
        style = TextStyle(platformStyle = removeFontPadding),
    )
}

@Preview
@Composable
private fun TonalIconButtonPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        TonalIconButton(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun TransparentIconButtonPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        TransparentIconButton(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun OutlinedIconButtonPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        OutlinedIconButton(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun TonalTextButtonPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        TonalTextButton(
            label = "Click me",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun TransparentTextButtonPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        TransparentTextButton(
            label = "Click me",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun OutlinedTextButtonPreview() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        OutlinedTextButton(
            label = "Click me",
            onClick = {},
        )
    }
}