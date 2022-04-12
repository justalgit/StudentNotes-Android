package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Shapes

@ExperimentalAnimationApi
@Composable
fun EventCreationScreenBody(
    navController: NavController
) {

    var eventTitle by rememberSaveable { mutableStateOf("") }
    var eventDescription by rememberSaveable { mutableStateOf("") }
    var isDescriptionAbsent by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.Start
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
        Spacer(modifier = Modifier.size(12.dp))
        UiTextField(
            value = eventTitle,
            onValueChange = {
                eventTitle = it
            },
            label = stringResource(R.string.title)
        )
        Spacer(modifier = Modifier.size(12.dp))
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
            Spacer(modifier = Modifier.size(12.dp))
            UiTextField(
                value = eventDescription,
                onValueChange = {
                    eventDescription = it
                },
                label = stringResource(R.string.description)
            )
        }
        Spacer(modifier = Modifier.size(24.dp))
        UiBigButton(
            text = stringResource(R.string.create_event),
            onClick = {
                if (eventTitle.isEmpty() || (eventDescription.isEmpty() && !isDescriptionAbsent)) {
                    Toast.makeText(context, context.getString(R.string.need_to_fill_all_fields), Toast.LENGTH_SHORT).show()
                } else {
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
        )
    }
}