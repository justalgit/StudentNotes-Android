package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.ui.components.UiBackButton
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
                Text(group!!.title.cutOff(HEADER_TITLE_LENGTH).uppercase())
            }
        )
        Text(
            text = "${group?.title}",
            style = Typography.h5,
            color = Color.Black
        )
        Text(
            text = context.getString(R.string.author, group?.creator),
            style = Typography.body2,
            color = Color.Black
        )
        Text(
            text = "${group?.description}",
            style = Typography.body1,
            color = Color.Black
        )
    }
}