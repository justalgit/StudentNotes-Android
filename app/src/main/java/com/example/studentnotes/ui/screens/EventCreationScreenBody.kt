package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiTextField

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
                Text("Создание события".uppercase())
            }
        )
        Spacer(modifier = Modifier.size(12.dp))
        UiTextField(
            value = eventTitle,
            onValueChange = {
                eventTitle = it
            },
            label = "Название"
        )
        Spacer(modifier = Modifier.size(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Checkbox(
                checked = isDescriptionAbsent,
                onCheckedChange = { isDescriptionAbsent = !isDescriptionAbsent },
            )
            Text(
                text = "Без описания",
                color = Color.Black
            )
        }
        Spacer(modifier = Modifier.size(12.dp))
        if (!isDescriptionAbsent) {
            UiTextField(
                value = eventDescription,
                onValueChange = {
                    eventDescription = it
                },
                label = "Описание"
            )
        }
        Spacer(modifier = Modifier.size(24.dp))
        UiBigButton(
            text = "Создать событие",
            onClick = {
                if (eventTitle.isEmpty() || (eventDescription.isEmpty() && !isDescriptionAbsent)) {
                    Toast.makeText(context, "Необходимо заполнить поля!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Событие \"$eventTitle\" успешно создано", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        )
    }
}