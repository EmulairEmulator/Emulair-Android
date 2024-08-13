package com.bigbratan.emulair.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "games",
    indices = [
        Index("id", unique = true),
        Index("fileUri", unique = true), // Same as below
        Index("title"),
        Index("systemId"),
        Index("developer"),
        Index("genre"),
        Index("publishDate"),
        Index("lastIndexedAt"), // TODO: [NOTICE] Am I adding lastIndexedAt in the SQLite database? Why not use shared prefs?
        Index("lastPlayedAt"), // Same as above
    ]
)

@Immutable
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val systemId: String,
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
    val lastPlayedAt: Long? = null,
    val lastIndexedAt: Long,
    val fileName: String,
    val fileUri: String,
    val iconUri: String? = null,
    val imageBannerUri: String? = null,
    val videoBannerUri: String? = null,
    val audioBgmUri: String? = null,
)