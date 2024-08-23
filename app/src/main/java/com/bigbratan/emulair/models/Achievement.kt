package com.bigbratan.emulair.models

import androidx.compose.runtime.Immutable

@Immutable
data class Achievement(
    val id: Int,
    val gameId: Int,
    val icon: String? = null,
    val title: String,
    val description: String,
    val points: Int,
    val number: Int,
    val status: Int,
)