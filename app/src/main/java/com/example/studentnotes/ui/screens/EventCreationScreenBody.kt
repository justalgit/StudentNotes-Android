package com.example.studentnotes.ui.screens

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.utils.*
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun EventCreationScreenBody(
    navController: NavController
) {

    var eventTitle by rememberSaveable { mutableStateOf("") }
    var eventDescription by rememberSaveable { mutableStateOf("") }
    var isDescriptionAbsent by rememberSaveable { mutableStateOf(false) }
    var isEventEditable by rememberSaveable { mutableStateOf(true) }
    var eventDate by rememberSaveable { mutableStateOf(getCurrentLocalDate()) }
    var eventTime by rememberSaveable { mutableStateOf(getCurrentLocalTime()) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )

    val availableGroups = databaseRepo.getAllGroups().observeAsState().value ?: emptyList()
    var selectedGroupTitle = remember {
        mutableStateOf(
            if (availableGroups.isEmpty()) "" else availableGroups[0].title
        )
    }

    val dateDialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.close))
        }
    ) {
        datepicker { date ->
            eventDate = date
        }
    }

    val timeDialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.close))
        }
    ) {
        timepicker { time ->
            eventTime = time
        }
    }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f),
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
                    Text(stringResource(R.string.event_creation).uppercase())
                }
            )
            UiTextField(
                value = eventTitle,
                onValueChange = {
                    eventTitle = it
                },
                label = stringResource(R.string.title),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isDescriptionAbsent = !isDescriptionAbsent }
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
                    },
                    label = stringResource(R.string.description)
                )
            }
            UiDropdownList(
                label = stringResource(R.string.group),
                selectedOption = selectedGroupTitle,
                suggestions = availableGroups.map { it.title }.distinct()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isEventEditable = !isEventEditable }
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UiTextField(
                    value = eventDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    label = stringResource(R.string.date),
                    isEnabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { dateDialogState.show() }
                        )
                )
                UiTextField(
                    value = eventTime.format(DateTimeFormatter.ISO_LOCAL_TIME),
                    label = stringResource(R.string.time),
                    isEnabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = { timeDialogState.show() }
                        )
                )
            }
        }

        UiBigButton(
            text = stringResource(R.string.create_event),
            isEnabled = eventTitle.isNotBlank() && (
                    isDescriptionAbsent || !isDescriptionAbsent && eventDescription.isNotBlank())
        ) {
            coroutineScope.launch {
                val groupId = databaseRepo.getGroupByTitle(selectedGroupTitle.value).id
                databaseRepo.insertEvent(
                    Event(
                        id = UUID.randomUUID().toString(),
                        title = eventTitle,
                        description = if (!isDescriptionAbsent) eventDescription else null,
                        eventDate = getTimestampFromDateAndTime(eventDate, eventTime),
                        authorId = CURRENT_USER_PLACEHOLDER_ID,
                        lastModifiedDate = getCurrentTimestamp(),
                        lastModifiedUserId = CURRENT_USER_PLACEHOLDER_ID,
                        groupId = groupId,
                        isEditable = isEventEditable
                    )
                )
            }
            Toast.makeText(
                context,
                context.getString(
                    R.string.event_created_successfully,
                    eventTitle
                ),
                Toast.LENGTH_SHORT
            ).show()
            navController.popBackStack()
        }
    }
}