package com.bigbratan.emulair.ui.main.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbratan.emulair.models.Game
import com.bigbratan.emulair.services.GamesService
import com.bigbratan.emulair.utils.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class GamesViewModel @Inject constructor(
    private val gamesService: GamesService,
) : ViewModel() {
    val gamesFlow = MutableStateFlow<DataState<List<GameItemViewModel>>>(DataState.Idle)

    init {
        getGames()
    }

    private fun getGames() {
        viewModelScope.launch {
            gamesFlow.value = DataState.Loading

            try {
                val games = gamesService.fetchGames().map(::GameItemViewModel)
                gamesFlow.value = DataState.Success(games)
            } catch (e: Exception) {
                gamesFlow.value = DataState.Error
            }
        }
    }
}

data class GameItemViewModel(
    private val game: Game,
) {
    val id = game.id
    val systemId = game.systemId
    val systemName = when (game.systemId) {
        "1" -> "PlayStation"
        "2" -> "PlayStation 2"
        "3" -> "PlayStation Portable"
        "4" -> "Nintendo Entertainment System"
        "5" -> "Super Nintendo Entertainment System"
        "6" -> "Game Boy Advance"
        else -> "Unknown"
    }
    val fullTitle = game.fullTitle
    val displayTitle = game.displayTitle
    val placeholderTitle = formatTitlePlaceholder(game.displayTitle)
    val icon = game.icon
    val imageBanner = game.imageBanner
    val videoBanner = game.videoBanner
    val audioBgm = game.audioBgm
    val developer = game.developer?.let { formatCompany(it) }
    val publisher = game.publisher?.let { formatCompany(it) }
    val genre = game.genre
    val releaseDate = game.releaseDate?.let { formatDate(it) }
    val region = game.region
    val lastPlayedAt = game.lastPlayedAt
    val lastIndexedAt = game.lastIndexedAt
    val fileName = game.fileName
    val fileUri = game.fileUri
    val iconUri = game.iconUri
    val imageBannerUri = game.imageBannerUri
    val videoBannerUri = game.videoBannerUri
    val audioBgmUri = game.audioBgmUri
}

private fun formatTitlePlaceholder(title: String): String {
    val romanNumeralRegex = "^(M{0,3})(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$".toRegex()

    val sanitizedName = title.replace(Regex("\\(.*\\)"), "").trim()

    if (!sanitizedName.contains(" ")) {
        return when {
            sanitizedName.length > 10 -> {
                val consonants = sanitizedName.filter { it.lowercaseChar() !in "aeiou" }
                if (consonants.length > 10) {
                    "${sanitizedName.take(10)}."
                } else {
                    consonants
                }
            }

            else -> sanitizedName
        }
    }

    return sanitizedName.split(Regex("\\s|(?=\\p{Punct})")).asSequence()
        .map { word ->
            if (romanNumeralRegex.matches(word.uppercase(Locale.ROOT))) {
                word.uppercase(Locale.ROOT)
            } else {
                word.firstOrNull()?.uppercaseChar().toString()
            }
        }
        .filter {
            it.firstOrNull() != null && (it.first().isDigit() or it.first()
                .isUpperCase() or (it.first() == '&') or (it.first() == ':') or (it.first() == '.') or (it.first() == '-'))
        }
        .joinToString("")
        .ifBlank { title.first().toString() }
        .replaceFirstChar(Char::titlecase)
}

private fun formatDate(dateInMilliseconds: Long): String {
    val date = Date(dateInMilliseconds)
    val format = SimpleDateFormat("yyyy", Locale.getDefault())

    return format.format(date)
}

private fun formatCompany(input: String): String {
    val words = input.split(" ")

    return if (words.size > 2) {
        words.joinToString("") { it.first().toString() }
    } else {
        input
    }
}