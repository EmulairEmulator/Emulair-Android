package com.bigbratan.emulair.models

import androidx.compose.runtime.Immutable
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.io.Serializable

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
        Index("lastIndexedAt"), // NOTICEME: Am I adding lastIndexedAt in the SQLite database? Why not use shared prefs?
        Index("lastPlayedAt"), // Same as above
    ]
)

@Immutable
data class Game(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val systemId: String,
    val title: String,
    val icon: String? = null,
    val banner: String? = null,
    val developer: String? = null,
    val genre: String? = null,
    val publishDate: Long? = null,
    val lastPlayedAt: Long? = null,
    val lastIndexedAt: Long,
    val fileName: String,
    val fileUri: String,
    val iconUri: String? = null,
    val bannerUri: String? = null,
)