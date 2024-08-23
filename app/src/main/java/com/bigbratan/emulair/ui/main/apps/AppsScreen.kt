package com.bigbratan.emulair.ui.main.apps

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.bigbratan.emulair.ui.components.LocalTopNavHeight

@Composable
fun AppsScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = LocalTopNavHeight.current)
            .background(Color.Transparent)
    ) {

    }
}