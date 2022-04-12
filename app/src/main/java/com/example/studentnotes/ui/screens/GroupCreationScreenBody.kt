package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
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
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.ui.theme.LightGreen
import com.example.studentnotes.ui.theme.LightRed
import com.example.studentnotes.ui.theme.Typography

@ExperimentalAnimationApi
@Composable
fun GroupCreationScreenBody(
    navController: NavController
) {

    var groupTitle by rememberSaveable { mutableStateOf("") }
    var groupDescription by rememberSaveable { mutableStateOf("") }
    var isDescriptionAbsent by rememberSaveable { mutableStateOf(false) }
    val options = listOf(
        stringResource(R.string.open_group),
        stringResource(R.string.private_group)
    )
    var selectedOption by remember {
        mutableStateOf(options[0])
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }
    val selectedOptionColor by animateColorAsState(
        if (selectedOption == stringResource(R.string.open_group)) LightGreen else LightRed
    )

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
                Text(stringResource(R.string.group_creation).uppercase())
            }
        )
        Spacer(modifier = Modifier.size(12.dp))
        UiTextField(
            value = groupTitle,
            onValueChange = {
                groupTitle = it
            },
            label = stringResource(R.string.title)
        )
        Spacer(modifier = Modifier.size(12.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            options.forEach { text ->
                Box(
                    modifier = Modifier
                        .padding(
                            all = 12.dp,
                        ),
                ) {
                    Text(
                        text = text,
                        style = Typography.body1,
                        color = Color.Black,
                        modifier = Modifier
                            .clip(
                                shape = RoundedCornerShape(
                                    size = 12.dp,
                                ),
                            )
                            .clickable {
                                onSelectionChange(text)
                            }
                            .background(
                                if (text == selectedOption) {
                                    selectedOptionColor
                                } else {
                                    Color.LightGray
                                }
                            )
                            .padding(
                                vertical = 12.dp,
                                horizontal = 16.dp,
                            )
                    )
                }
            }
        }
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
                onCheckedChange = null
            )
            Text(
                text = stringResource(R.string.no_description),
                color = Color.Black
            )
        }
        AnimatedVisibility(!isDescriptionAbsent) {
            Spacer(modifier = Modifier.size(12.dp))
            UiTextField(
                value = groupDescription,
                onValueChange = {
                    groupDescription = it
                },
                label = stringResource(R.string.description)
            )
        }
        Spacer(modifier = Modifier.size(24.dp))
        UiBigButton(
            text = stringResource(R.string.create_group),
            onClick = {
                if (groupTitle.isEmpty() || (groupDescription.isEmpty() && !isDescriptionAbsent)) {
                    Toast.makeText(context, context.getString(R.string.need_to_fill_all_fields), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.group_created_successfully,
                            selectedOption,
                            groupTitle
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
            }
        )
    }
}