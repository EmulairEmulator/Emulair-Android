package com.bigbratan.emulair.ui.main.games

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bigbratan.emulair.models.Game
import com.bigbratan.emulair.services.GamesService
import com.bigbratan.emulair.utils.DataState
import com.bigbratan.emulair.utils.formatCompany
import com.bigbratan.emulair.utils.formatDate
import com.bigbratan.emulair.utils.formatTitlePlaceholder
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
    val systemName = when (game.systemId) {
        1 -> "PlayStation"
        2 -> "PlayStation 2"
        3 -> "PlayStation Portable"
        4 -> "Nintendo Entertainment System"
        5 -> "Super Nintendo Entertainment System"
        6 -> "Game Boy Advance"
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
    val details = concatDetails(this)
    val lastPlayedAt = game.lastPlayedAt
    val lastIndexedAt = game.lastIndexedAt
    val fileName = game.fileName
    val fileUri = game.fileUri
    val iconUri = game.iconUri
    val imageBannerUri = game.imageBannerUri
    val videoBannerUri = game.videoBannerUri
    val audioBgmUri = game.audioBgmUri
}

private fun concatDetails(game: GameItemViewModel): String {
    val details = mutableListOf<String>()

    game.developer?.let { developer -> details.add(developer) }
    if (game.developer != game.publisher) {
        game.publisher?.let { publisher -> details.add(publisher) }
    }
    game.genre?.let { genre -> details.add(genre) }
    game.releaseDate?.let { releaseDate -> details.add(releaseDate) }
    game.region?.let { region -> details.add(region) }

    return details.joinToString(" · ")
}
