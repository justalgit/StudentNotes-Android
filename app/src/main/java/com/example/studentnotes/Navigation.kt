package com.example.studentnotes

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
import com.example.studentnotes.ui.screens.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

@ExperimentalComposeUiApi
@OptIn(ExperimentalAnimationApi::class)
@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.WelcomeScreen.route
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
            route = Screen.GetAccessScreen.route
        ) {
            GetAccessScreenBody(
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
            route = Screen.RequestSearchScreen.route
        ) {
            RequestSearchScreenBody(
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
            route = Screen.EventDetailsScreen.route + "/{event}",
            arguments = listOf(
                navArgument("event") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val eventJson =  entry.arguments?.getString("event")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(Event::class.java).lenient()
            EventDetailsScreenBody(
                navController = navController,
                event = jsonAdapter.fromJson(eventJson)!!
            )
        }

        composable(
            route = Screen.EventEditingScreen.route + "/{event}",
            arguments = listOf(
                navArgument("event") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val eventJson = entry.arguments?.getString("event")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(Event::class.java).lenient()
            EventEditingScreenBody(
                navController = navController,
                event = jsonAdapter.fromJson(eventJson)!!
            )
        }

        composable(
            route = Screen.GroupDetailsScreen.route + "/{group}",
            arguments = listOf(
                navArgument("group") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val groupJson =  entry.arguments?.getString("group")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(Group::class.java).lenient()
            GroupDetailsScreenBody(
                navController = navController,
                group = jsonAdapter.fromJson(groupJson)!!
            )
        }

        composable(
            route = Screen.GroupEditingScreen.route + "/{group}",
            arguments = listOf(
                navArgument("group") {
                    type = NavType.StringType
                    nullable = false
                }
            )
        ) { entry ->
            val groupJson = entry.arguments?.getString("group")
            val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
            val jsonAdapter = moshi.adapter(Group::class.java).lenient()
            GroupEditingScreenBody(
                navController = navController,
                group = jsonAdapter.fromJson(groupJson)!!
            )
        }
    }
}