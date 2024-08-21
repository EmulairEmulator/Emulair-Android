package com.bigbratan.emulair.navigation

sealed class Destination(val route: String) {
    data object Onboarding : Destination("onboarding") {
        data object WelcomeDestination : Destination("welcome")

        data object PickGamesFolderDestination : Destination("pick_games_folder")

        data object CreateSystemFolderDestination : Destination("create_system_folder")
    }

    data object Main : Destination("main") {
        data object GamesDestination : Destination("games")

        data object SystemsDestination : Destination("systems")

        data object SystemGamesDestination : Destination("system_games")

        data object OnlineDestination : Destination("online")

        data object SearchDestination : Destination("search")

        data object AppsDestination : Destination("apps")

        data object ProfileDestination : Destination("profile")

        data object SettingsDestination : Destination("settings")

        data object SystemOptionsDestination : Destination("system_options")

        data object GameOptionsDestination : Destination("game_options")
    }

    fun routeWithArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.filter { it.isNotEmpty() }.forEach { arg ->
                append("/$arg")
            }
        }
    }
}