package com.bigbratan.emulair.services

import com.bigbratan.emulair.models.Game
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamesService @Inject constructor(
) {
    fun fetchGames(): List<Game> {
        return dummyGames
    }

    private val dummyGames = listOf(
        Game(
            id = "1",
            systemId = "100",
            fullTitle = "Castlevania: Symphony of the Night (Europe) (En,Fr,It)",
            displayTitle = "Castlevania: Symphony of the Night",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "2",
            systemId = "100",
            fullTitle = "Need for Speed: Most Wanted (2005) (USA) (En,Es)",
            displayTitle = "Need for Speed: Most Wanted (2005)",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "3",
            systemId = "100",
            fullTitle = "Metal Gear Solid (Asia) (En,Jp,Kr,Cn)",
            displayTitle = "Metal Gear Solid",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "4",
            systemId = "100",
            fullTitle = "God of War (USA) (En,Fr,It)",
            displayTitle = "God of War",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "5",
            systemId = "100",
            fullTitle = "Assassin's Creed (USA) (En,Fr,It)",
            displayTitle = "Assassin's Creed",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "6",
            systemId = "100",
            fullTitle = "Final Fantasy VII (USA) (En,Fr,It)",
            displayTitle = "Final Fantasy VII",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "7",
            systemId = "100",
            fullTitle = "The Legend of Zelda: Ocarina of Time (USA) (En,Fr,It)",
            displayTitle = "The Legend of Zelda: Ocarina of Time",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "8",
            systemId = "100",
            fullTitle = "Super Mario Bros. (USA) (En,Fr,It)",
            displayTitle = "Super Mario Bros.",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "9",
            systemId = "100",
            fullTitle = "Silent Hill (USA) (En,Fr,It)",
            displayTitle = "Silent Hill",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "10",
            systemId = "100",
            fullTitle = "Resident Evil 2 (USA) (En,Fr,It)",
            displayTitle = "Resident Evil 2",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "11",
            systemId = "100",
            fullTitle = "The Elder Scrolls V: Skyrim (USA) (En,Fr,It)",
            displayTitle = "The Elder Scrolls V: Skyrim",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "12",
            systemId = "100",
            fullTitle = "Doom (USA) (En,Fr,It)",
            displayTitle = "Doom",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "13",
            systemId = "100",
            fullTitle = "Half-Life (USA) (En,Fr,It)",
            displayTitle = "Half-Life",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "14",
            systemId = "100",
            fullTitle = "Grand Theft Auto III (USA) (En,Fr,It)",
            displayTitle = "Grand Theft Auto III",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "15",
            systemId = "100",
            fullTitle = "Minecraft (USA) (En,Fr,It)",
            displayTitle = "Minecraft",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "16",
            systemId = "100",
            fullTitle = "Terraria (USA) (En,Fr,It)",
            displayTitle = "Terraria",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        ),
        Game(
            id = "17",
            systemId = "100",
            fullTitle = "Hollowknight (USA) (En,Fr,It)",
            displayTitle = "Hollowknight",
            lastIndexedAt = 99999,
            fileName = "file",
            fileUri = "file.iso",
        )
    )
}