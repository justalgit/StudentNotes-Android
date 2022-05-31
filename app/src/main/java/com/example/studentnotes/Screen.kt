package com.example.studentnotes

sealed class Screen(
    val route: String
) {

    object WelcomeScreen: Screen("welcome_screen")
    object GetAccessScreen: Screen("get_access_screen")

    object MainPagerScreen: Screen("main_pager_screen")
    object EventCreationScreen: Screen("event_creation_screen")
    object EventSearchScreen: Screen("event_search_screen")
    object EventDetailsScreen: Screen("event_details_screen")
    object EventEditingScreen: Screen("event_editing_screen")

    object GroupCreationScreen: Screen("group_creation_screen")
    object GroupSearchScreen: Screen("group_search_screen")
    object GroupDetailsScreen: Screen("group_details_screen")
    object GroupEditingScreen: Screen("group_editing_screen")

    object RequestCreationScreen: Screen("request_creation_screen")
    object RequestDetailsScreen: Screen("request_details_screen")

    object SettingsScreen: Screen("settings_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}