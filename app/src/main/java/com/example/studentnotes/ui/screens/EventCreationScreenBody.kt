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
import com.example.studentnotes.data.api.getGroupsList
import com.example.studentnotes.ui.components.*

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
    val context = LocalContext.current

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
                    .padding(8.dp)
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
                suggestions = getGroupsList().map { it.title }.distinct()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
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
        }

        UiBigButton(
            text = stringResource(R.string.create_event),
            isEnabled = eventTitle.isNotBlank() && (
                    isDescriptionAbsent || !isDescriptionAbsent && eventDescription.isNotBlank())
        ) {
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