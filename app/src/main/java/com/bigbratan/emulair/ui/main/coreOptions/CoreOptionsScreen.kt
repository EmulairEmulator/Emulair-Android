package com.bigbratan.emulair.ui.main.coreOptions

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.bigbratan.emulair.R
import com.bigbratan.emulair.ui.components.LocalTopNavHeight
import com.bigbratan.emulair.ui.components.TopTitleBar

@Composable
fun CoreOptionsScreen(
    gameId: String? = "#",
    systemId: String? = "#",
    onBackClick: () -> Unit,
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        TopTitleBar(
            title = stringResource(id = R.string.core_options_title),
            onBackClick = onBackClick
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = LocalTopNavHeight.current),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {

        }
    }
}