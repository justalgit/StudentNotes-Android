package com.example.studentnotes.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.ui.theme.LightGreen
import com.example.studentnotes.ui.theme.LightRed
import com.example.studentnotes.ui.theme.Shapes
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.R
import com.example.studentnotes.utils.CARD_DESCRIPTION_LENGTH
import com.example.studentnotes.utils.cutOff

@Composable
fun GroupCard(
    group: Group,
    onClick: () -> Unit = {}
) {
    Card(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(bounded = true),
                onClick = onClick
            ),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = group.title,
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = group.description?.cutOff(CARD_DESCRIPTION_LENGTH) ?: stringResource(R.string.no_description),
                    style = Typography.caption,
                    color = Color.Gray
                )
                Text(
                    text = "Автор: ${group.creatorId}",
                    style = Typography.caption,
                    color = Color.Gray
                )
            }
            GroupPrivacyLabel(
                isPrivate = group.isPrivate
            )
        }
    }
}

@Composable
fun GroupPrivacyLabel(isPrivate: Boolean) {
    Box(
        modifier = Modifier
            .background(
                color = if (isPrivate) LightRed else LightGreen,
                shape = Shapes.medium
            )
            .clip(Shapes.medium)
    ) {
        Text(
            text = if (isPrivate) stringResource(R.string.closed_group) else stringResource(R.string.open_group),
            color = Color.White,
            style = Typography.caption,
            modifier = Modifier
                .padding(4.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GroupCardPreview() {
    GroupCard(
        group = Group(
            id = "123",
            title = "М8О-203М-20",
            description = "Самые крутые ребята",
            creatorId = "Алексей Воробьев",
            lastModifiedDate = 12345L,
            lastModifiedUserId = "Алексей Воробьев",
            isPrivate = true,
            isEditable = true
        )
    )
}