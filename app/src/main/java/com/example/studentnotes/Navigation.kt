package com.example.studentnotes

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.studentnotes.ui.screens.*
import com.google.accompanist.pager.ExperimentalPagerApi

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
            route = Screen.MainPagerScreen.route + "/{login}",
            arguments = listOf(
                navArgument("login") {
                    type = NavType.StringType
                    defaultValue = "Anton"
                    nullable = false
                }
            )
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
    }
}