package com.bigbratan.emulair.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Immutable
data class Game(
    val id: Int,
    val systemId: Int,
    val fullTitle: String,
    val displayTitle: String,
    val icon: String? = null,
    val imageBanner: String? = null,
    val videoBanner: String? = null,
    val audioBgm: String? = null,
    val developer: String? = null,
    val publisher: String? = null,
    val genre: String? = null,
    val releaseDate: Long? = null,
    val region: String? = null,
    val lastPlayedAt: Long? = null,
    val lastIndexedAt: Long,
    val fileName: String,
    val fileUri: String,
    val iconUri: String? = null,
    val imageBannerUri: String? = null,
    val videoBannerUri: String? = null,
    val audioBgmUri: String? = null,
    val achievements: List<Achievement>? = null,
)