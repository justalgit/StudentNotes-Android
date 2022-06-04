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
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.User
import com.example.studentnotes.ui.components.GroupsListComposable
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiIconButton
import com.example.studentnotes.ui.theme.Typography

@Composable
fun GroupsScreenBody(
    navController: NavController,
    groupsList: List<Group>,
    usersList: List<User>
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        UiHeader(
            leftContent = {
                Text(
                    text = stringResource(R.string.groups),
                    style = Typography.h5,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            },
            rightRowContent = {
                UiIconButton(
                    painter = painterResource(R.drawable.ic_plus_24),
                    onClick = { navController.navigate(Screen.GroupCreationScreen.route) }
                )
                UiIconButton(
                    painter = painterResource(R.drawable.ic_search_24),
                    onClick = { navController.navigate(Screen.GroupSearchScreen.route) }
                )
            }
        )
        GroupsListComposable(
            groups = groupsList,
            users = usersList,
            onGroupClick = { group ->
                navController.navigate(
                    Screen.GroupDetailsScreen.withArgs(group.id)
                )
            }
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (groupsList.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.you_dont_have_groups_yet),
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
