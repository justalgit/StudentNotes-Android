package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.entities.stringifiedName
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.*

@Composable
fun EventDetailsScreenBody(
    navController: NavController,
    eventId: String?
) {

    val context = LocalContext.current
    val sharedPrefs = context.getUserSharedPreferences()
    val currentUserId = sharedPrefs?.getLoggedInUserId() ?: ""
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val event = databaseRepo.getEventById(eventId ?: "")
    val eventAuthor = databaseRepo.getUserById(event.authorId)
    val eventGroup = databaseRepo.getGroupById(event.groupId)
    val eventLastModifiedUser = databaseRepo.getUserById(event.lastModifiedUserId)

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        UiHeader(
            leftContent = {
                UiBackButton(
                    onClick = { navController.popBackStack() }
                )
            },
            rightRowContent = {
                Text(eventGroup.title.cutOff(HEADER_TITLE_LENGTH).uppercase())
            }
        )
        if (event != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
            ) {
                Text(
                    text = event.title,
                    style = Typography.h5,
                    color = Color.Black
                )
                Text(
                    text = getFormattedDateFromTimestamp(event.eventDate),
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = context.getString(R.string.author, eventAuthor.stringifiedName()),
                    style = Typography.body1,
                    color = Color.Gray
                )
                Text(
                    text = event.description ?: stringResource(R.string.no_description),
                    style = Typography.body1,
                    color = Color.Black
                )
                Text(
                    text = context.getString(
                        R.string.last_modified_by,
                        eventLastModifiedUser.stringifiedName(),
                        getFormattedDateFromTimestamp(event.lastModifiedDate)
                    ),
                    style = Typography.caption,
                    color = Color.Gray
                )
                if (!event.isEditable && currentUserId != event.authorId) {
                    Text(
                        text = context.getString(R.string.author_made_event_uneditable),
                        style = Typography.body1,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
            }
            if (event.isEditable || !event.isEditable && event.authorId == currentUserId) {
                UiBigButton(
                    text = stringResource(R.string.edit),
                    onClick = {
                        navController.navigate(
                            Screen.EventEditingScreen.withArgs(event.id)
                        )
                    }
                )
            }
        }
    }
}