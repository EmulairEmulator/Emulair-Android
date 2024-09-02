package com.bigbratan.emulair.ui.main.search

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
import com.bigbratan.emulair.activities.LocalFocusProvider
import com.bigbratan.emulair.ui.components.LocalTopNavHeight
import com.bigbratan.emulair.ui.components.TopNavDestination
import com.bigbratan.emulair.utils.next
import com.bigbratan.emulair.utils.onShoulderButtonPress
import com.bigbratan.emulair.utils.previous

@Composable
fun SearchScreen(
    onTabSwitch: (TopNavDestination) -> Unit,
) {
    val focusRequester = LocalFocusProvider.current

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = LocalTopNavHeight.current)
            .background(Color.Transparent)
            .onShoulderButtonPress(
                nextDestination = TopNavDestination.SEARCH.next(),
                previousDestination = TopNavDestination.SEARCH.previous(),
                onNext = { onTabSwitch(TopNavDestination.SEARCH.next()) },
                onPrevious = { onTabSwitch(TopNavDestination.SEARCH.previous()) },
            ),
    ) {
        SearchView(
            focusRequester = focusRequester,
        )
    }
}

@Composable
private fun SearchView(
    focusRequester: FocusRequester,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start,
    ) {
        Box(
            modifier = Modifier.focusRequester(focusRequester),
        )
    }
}