package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiIconButton
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.entities.*
import com.example.studentnotes.ui.components.EventsList
import com.example.studentnotes.ui.theme.Typography

@Composable
fun EventsScreenBody(
    navController: NavController,
    eventsList: List<Event>,
    groupsList: List<Group>
) {

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
                if (groupsList.isNotEmpty()) {
                    UiIconButton(
                        painter = painterResource(R.drawable.ic_plus_24),
                        onClick = { navController.navigate(Screen.EventCreationScreen.route) }
                    )
                    UiIconButton(
                        painter = painterResource(R.drawable.ic_search_24),
                        onClick = { navController.navigate(Screen.EventSearchScreen.route) }
                    )
                } else {
                    null
                }
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
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (groupsList.isNotEmpty()) {
                if (eventsList.isEmpty()) {
                    Text(
                        text = stringResource(R.string.you_dont_have_events_yet),
                        style = Typography.body1,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                            .fillMaxWidth()
                    )
                }
            } else {
                Text(
                    text = stringResource(R.string.you_dont_have_groups_and_events_yet),
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}