package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.data.api.getEventsList
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.ui.components.EventCard
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiIconButton
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.ui.theme.Typography

@Composable
fun EventsScreenBody(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        UiHeader(
            leftContent = {
                Text(
                    text = "События",
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
            events = getEventsList()
        )
    }
}

@Composable
fun EventsList(
    modifier: Modifier = Modifier,
    events: List<Event>
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(events) { event ->
            EventCard(event = event)
        }
    }
}