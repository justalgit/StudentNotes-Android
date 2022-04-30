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
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.HEADER_TITLE_LENGTH
import com.example.studentnotes.utils.cutOff

@Composable
fun GroupDetailsScreenBody(
    navController: NavController,
    group: Group?
) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
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
                Text(group!!.title.cutOff(HEADER_TITLE_LENGTH).uppercase())
            }
        )
        if (group != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
            ) {
                Text(
                    text = group.title,
                    style = Typography.h5,
                    color = Color.Black
                )
                Text(
                    text = context.getString(R.string.author, group.creatorId),
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = group?.description ?: stringResource(R.string.no_description),
                    style = Typography.body1,
                    color = Color.Black
                )
                if (!group.isEditable) {
                    Text(
                        text = context.getString(R.string.creator_made_group_uneditable),
                        style = Typography.body1,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                            .fillMaxWidth()
                    )
                }
            }
            if (!group.isPrivate) {
                UiBigButton(
                    text = stringResource(R.string.invite)
                )
            }
            if (group.isEditable) {
                UiBigButton(
                    text = stringResource(R.string.edit)
                )
            }
        }
    }
}