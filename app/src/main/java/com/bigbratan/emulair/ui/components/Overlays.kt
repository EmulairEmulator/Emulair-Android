package com.bigbratan.emulair.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bigbratan.emulair.R
import com.bigbratan.emulair.ui.theme.removeFontPadding
import com.bigbratan.emulair.ui.theme.plusJakartaSans

@Composable
internal fun FadingScrimBackground(
    topColor: Color = Color.Transparent,
    middleColor: Color,
    bottomColor: Color,
    shape: Shape = RectangleShape,
    aspectRatio: Float = 1f / 1f,
) {
    val gradient = Brush.verticalGradient(
        colorStops = arrayOf(
            0.0f to topColor,
            0.3f to topColor,
            0.5f to middleColor,
            0.7f to bottomColor,
            1.0f to bottomColor
        )
    )

    Box(
        modifier = Modifier
            .clip(shape)
            .fillMaxSize()
            .background(brush = gradient)
            .aspectRatio(aspectRatio)
    )
}

@Composable
internal fun SolidScrimBackground(
    shape: Shape = RectangleShape,
    aspectRatio: Float = 1f / 1f,
) {
    Box(
        modifier = Modifier
            .clip(shape)
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.60f))
            .aspectRatio(aspectRatio)
    )
}

@Composable
internal fun LoadingAnimation(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
internal fun ErrorMessage(
    message: String,
    action: String? = null,
    onClick: (() -> Unit)? = null,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = message,
            fontFamily = plusJakartaSans,
            fontWeight = FontWeight.Medium,
            fontSize = 24.sp,
            color = MaterialTheme.colorScheme.onSurface,
            style = TextStyle(platformStyle = removeFontPadding),
        )

        if (action != null && onClick != null) {
            TonalTextButton(
                modifier = Modifier.padding(top = 24.dp),
                label = action,
                onClick = onClick,
            )
        }
    }
}

@Composable
internal fun Popup(
    title: String,
    message: String,
    hasNegativeAction: Boolean = true,
    isPopupVisible: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (isPopupVisible) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            title = { Text(text = title) },
            text = { Text(text = message) },
            confirmButton = {
                Button(onClick = { onConfirm() }) {
                    Text(text = stringResource(id = R.string.action_positive_title))
                }
            },
            dismissButton = {
                if (hasNegativeAction) {
                    Button(onClick = { onDismiss() }) {
                        Text(text = stringResource(id = R.string.action_negative_title))
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun ErrorMessagePreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        ErrorMessage(
            message = "An error occurred. Please retry this action.",
            action = "Retry",
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun PopupPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        Popup(
            title = "Are you sure?",
            message = "The action you are trying to perform is irreversible. Do you really want to proceed?",
            isPopupVisible = true,
            onConfirm = {},
            onDismiss = {},
        )
    }
}