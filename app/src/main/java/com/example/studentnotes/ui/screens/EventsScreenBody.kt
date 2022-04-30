package com.example.studentnotes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiIconButton
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.entities.toJson
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.components.EventsList
import com.example.studentnotes.ui.theme.Typography

@Composable
fun EventsScreenBody(
    navController: NavController
) {

    val context = LocalContext.current
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val eventsList = databaseRepo.getAllEvents().value ?: emptyList()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        UiHeader(
            leftContent = {
                Text(
                    text = stringResource(R.string.events),
                    style = Typography.h5,
                    color = Color.Black
                )
            },
            rightRowContent = {
                UiIconButton(
                    painter = painterResource(R.drawable.ic_plus_24),
                    onClick = { navController.navigate(Screen.EventCreationScreen.route) }
                )
                UiIconButton(
                    painter = painterResource(R.drawable.ic_search_24),
                    onClick = { navController.navigate(Screen.EventSearchScreen.route) }
                )
            }
        )
        EventsList(
            events = eventsList,
            onEventClick = { event ->
                navController.navigate(
                    Screen.EventDetailsScreen.withArgs(event.toJson())
                )
            }
        )
    }
}