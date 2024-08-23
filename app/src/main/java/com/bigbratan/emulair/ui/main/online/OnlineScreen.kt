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
import com.bigbratan.emulair.utils.next
import com.bigbratan.emulair.utils.onShoulderButtonPress
import com.bigbratan.emulair.utils.previous

@Composable
fun OnlineScreen(
    onTabSwitch: (TopNavDestination) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .focusable()
            .padding(top = LocalTopNavHeight.current)
            .background(Color.Transparent)
            .onShoulderButtonPress(
                nextDestination = TopNavDestination.ONLINE.next(),
                previousDestination = TopNavDestination.ONLINE.previous(),
                onNext = { onTabSwitch(TopNavDestination.ONLINE.next()) },
                onPrevious = { onTabSwitch(TopNavDestination.ONLINE.previous()) },
            ),
    ) {
        OnlineView()
    }
}

@Composable
private fun OnlineView() {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .focusRequester(focusRequester),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {

    }
}