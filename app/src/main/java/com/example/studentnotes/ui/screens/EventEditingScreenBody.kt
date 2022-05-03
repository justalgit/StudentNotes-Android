package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.ui.theme.LightRed
import com.example.studentnotes.utils.CURRENT_USER_PLACEHOLDER_ID
import kotlinx.coroutines.launch

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun EventEditingScreenBody(
    navController: NavController,
    event: Event
) {

    var eventTitle by rememberSaveable { mutableStateOf(event.title) }
    var eventDescription by rememberSaveable { mutableStateOf(event.description ?: "") }
    var isDescriptionAbsent by rememberSaveable { mutableStateOf(event.description == null) }
    var isEventEditable by rememberSaveable { mutableStateOf(event.isEditable) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )

    val availableGroups = databaseRepo.getAllGroups().observeAsState().value ?: emptyList()
    var selectedGroupTitle = if (availableGroups.isEmpty()) "" else availableGroups[0].title
    var isEventModified by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
        ) {
            UiHeader(
                leftContent = {
                    UiBackButton(
                        onClick = { navController.popBackStack() }
                    )
                },
                rightRowContent = {
                    Text(stringResource(R.string.event_editing).uppercase())
                }
            )
            UiTextField(
                value = eventTitle,
                onValueChange = {
                    eventTitle = it
                    isEventModified = true
                },
                label = stringResource(R.string.title),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isDescriptionAbsent = !isDescriptionAbsent
                            isEventModified = true
                        }
                    )
            ) {
                Checkbox(
                    checked = isDescriptionAbsent,
                    onCheckedChange = { isDescriptionAbsent = !isDescriptionAbsent },
                )
                Text(
                    text = stringResource(R.string.no_description),
                    color = Color.Black
                )
            }
            AnimatedVisibility(!isDescriptionAbsent) {
                UiTextArea(
                    value = eventDescription,
                    onValueChange = {
                        eventDescription = it
                        isEventModified = true
                    },
                    label = stringResource(R.string.description)
                )
            }
            UiDropdownGroupList(
                label = stringResource(R.string.group),
                selectedOption = mutableStateOf(selectedGroupTitle),
                suggestions = availableGroups.map { it.title }.distinct(),
                onValueChange = {
                    event.groupId = databaseRepo.getGroupByTitle(selectedGroupTitle).id
                    isEventModified = true
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isEventEditable = !isEventEditable
                            isEventModified = true
                        }
                    )
            ) {
                Checkbox(
                    checked = isEventEditable,
                    onCheckedChange = { isEventEditable = !isEventEditable },
                )
                Text(
                    text = stringResource(R.string.editable_event),
                    color = Color.Black
                )
            }
        }

        UiBigButton(
            text = stringResource(R.string.save_changes),
            isEnabled = isEventModified
                    && eventTitle.isNotBlank()
                    && (isDescriptionAbsent || !isDescriptionAbsent && eventDescription.isNotBlank())
        ) {
            coroutineScope.launch {
                with(event) {
                    title = eventTitle
                    description = if (isDescriptionAbsent) null else eventDescription
                    groupId = databaseRepo.getGroupByTitle(selectedGroupTitle).id
                    isEditable = isEventEditable
                    lastModifiedUserId = CURRENT_USER_PLACEHOLDER_ID
                    // TODO: lastModifiedDate and eventDate
                }
                databaseRepo.updateEvent(event)
            }
            Toast.makeText(
                context,
                context.getString(
                    R.string.event_updated_successfully,
                    event.title
                ),
                Toast.LENGTH_SHORT
            ).show()
            navController.popBackStack(
                Screen.MainPagerScreen.route,
                inclusive = false
            )
        }

        if (CURRENT_USER_PLACEHOLDER_ID == event.authorId) {
            UiBigButton(
                text = stringResource(R.string.delete),
                color = LightRed,
                isEnabled = true,
                onClick = {
                    coroutineScope.launch {
                        databaseRepo.deleteEvent(event)
                    }
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.event_deleted_successfully,
                            event.title
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack(
                        Screen.MainPagerScreen.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}