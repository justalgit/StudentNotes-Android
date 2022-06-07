package com.example.studentnotes

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.ui.screens.*
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@OptIn(ExperimentalAnimationApi::class)
@ExperimentalMaterialApi
@Composable
fun Navigation(
    entryScreen: Screen
) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = entryScreen.route
    ) {
        composable(
            route = Screen.WelcomeScreen.route
        ) {
            WelcomeScreenBody(
                navController = navController
            )
        }

        composable(
            route = Screen.MainPagerScreen.route
        ) {
            MainPagerScreenBody(
                navController = navController
            )
        }

        composable(
            route = Screen.EventCreationScreen.route
        ) {
            EventCreationScreenBody(
                navController = navController
            )
        }

        composable(
            route = Screen.EventSearchScreen.route
        ) {
            EventSearchScreenBody(
                navController = navController
            )
        }

        composable(
            route = Screen.GroupCreationScreen.route
        ) {
            GroupCreationScreenBody(
                navController = navController
            )
        }

        composable(
            route = Screen.GroupSearchScreen.route
        ) {
            GroupSearchScreenBody(
                navController = navController
            )
        }

        composable(
            route = Screen.RequestCreationScreen.route
        ) {
            RequestCreationScreenBody(
                navController = navController
            )
        }

        composable(
            route = Screen.SettingsScreen.route
        ) {
            SettingsScreenBody(
                navController = navController
            )
        }

        composable(
            route = Screen.EventDetailsScreen.route + "/{eventId}",
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val eventJson =  entry.arguments?.getString("eventId")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(String::class.java).lenient()
            EventDetailsScreenBody(
                navController = navController,
                eventId = jsonAdapter.fromJson(eventJson)!!
            )
        }

        composable(
            route = Screen.EventEditingScreen.route + "/{eventId}",
            arguments = listOf(
                navArgument("eventId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val eventJson = entry.arguments?.getString("eventId")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(String::class.java).lenient()
            EventEditingScreenBody(
                navController = navController,
                eventId = jsonAdapter.fromJson(eventJson)!!
            )
        }

        composable(
            route = Screen.GroupDetailsScreen.route + "/{groupId}",
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val groupJson =  entry.arguments?.getString("groupId")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(String::class.java).lenient()
            GroupDetailsScreenBody(
                navController = navController,
                groupId = jsonAdapter.fromJson(groupJson)!!
            )
        }

        composable(
            route = Screen.GroupEditingScreen.route + "/{groupId}",
            arguments = listOf(
                navArgument("groupId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val groupJson = entry.arguments?.getString("groupId")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(String::class.java).lenient()
            GroupEditingScreenBody(
                navController = navController,
                groupId = jsonAdapter.fromJson(groupJson)!!
            )
        }

        composable(
            route = Screen.RequestDetailsScreen.route + "/{requestId}",
            arguments = listOf(
                navArgument("requestId") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val requestJson = entry.arguments?.getString("requestId")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(String::class.java).lenient()
            RequestDetailsScreenBody(
                navController = navController,
                requestId = jsonAdapter.fromJson(requestJson)!!
            )
        }
    }
}