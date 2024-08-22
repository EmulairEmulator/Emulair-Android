package com.bigbratan.emulair.ui.main.online

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bigbratan.emulair.ui.components.LocalTopNavHeight
import com.bigbratan.emulair.ui.components.TopNavDestination
import com.bigbratan.emulair.utils.onShoulderButtonPress

@Composable
fun OnlineScreen(
    onTabSwitch: (TopNavDestination) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .onShoulderButtonPress(
                nextDestination = TopNavDestination.SEARCH,
                previousDestination = TopNavDestination.SYSTEMS,
                onNext = { onTabSwitch(TopNavDestination.SEARCH) },
                onPrevious = { onTabSwitch(TopNavDestination.SYSTEMS) },
            ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = LocalTopNavHeight.current),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start,
        ) {
            OnlineView()
        }
    }
}

@Composable
private fun OnlineView() {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .padding(top = 12.dp)
            .fillMaxWidth()
            .focusRequester(focusRequester)
            .focusable()
    )
}