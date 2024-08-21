package com.bigbratan.emulair.navigation

sealed class Destination(val route: String) {
    object Onboarding : Destination("onboarding") {
        object WelcomeDestination : Destination("welcome")

        object PickGamesFolderDestination : Destination("pick_games_folder")

        object CreateSystemFolderDestination : Destination("create_system_folder")
    }

    object Main : Destination("main") {
        object GamesDestination : Destination("games")

        object SystemsDestination : Destination("systems")

        object SystemGamesDestination : Destination("system_games")

        object OnlineDestination : Destination("online")

        object SearchDestination : Destination("search")

        object AppsDestination : Destination("apps")

        object ProfileDestination : Destination("profile")

        object SettingsDestination : Destination("settings")

        object CoreOptionsDestination : Destination("core_options")
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