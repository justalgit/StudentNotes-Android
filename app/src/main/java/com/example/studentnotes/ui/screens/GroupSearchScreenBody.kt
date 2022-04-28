package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.api.getGroupsList
import com.example.studentnotes.data.entities.toJson
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Typography

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GroupSearchScreenBody(
    navController: NavController
) {

    var groupTitle by rememberSaveable { mutableStateOf("") }
    var filteredGroups by rememberSaveable { mutableStateOf(getGroupsList()) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
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
                Text(stringResource(R.string.group_searching).uppercase())
            }
        )
        UiTextField(
            value = groupTitle,
            onValueChange = {
                groupTitle = it
                filteredGroups = getGroupsList().filter {
                    it.title.contains(groupTitle, ignoreCase = true)
                }
            },
            label = stringResource(R.string.event_title)
        )
        if (filteredGroups.isEmpty()) {
            Text(
                text = context.getString(R.string.not_found),
                style = Typography.body1,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } else {
            GroupsList(
                groups = filteredGroups,
                onGroupClick = { group ->
                    navController.navigate(
                        Screen.GroupDetailsScreen.withArgs(group.toJson())
                    )
                }
            )
        }
    }
}