package com.example.studentnotes.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.R

@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit = {}
) {

    val context = LocalContext.current

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
                    text = event.title,
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = event.eventDate.toString(),
                    style = Typography.body1,
                    color = Color.Black
                )
                Text(
                    text = event.group,
                    style = Typography.caption,
                    color = Color.Black
                )
                Text(
                    text = context.getString(R.string.author, event.author),
                    style = Typography.caption,
                    color = Color.Gray
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_arrow_forward_24),
                contentDescription = null
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EventCardPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        EventCard(
            event = Event(
                title = "Сделать ДЗ по английскому",
                description = "Новый тест по старому модулю",
                author = "Алексей Воробьев",
                eventDate = 12345L,
                lastModifiedDate = 12345L,
                group = "М8О-203М-20"
            )
        )
        EventCard(
            event = Event(
                title = "Сделать ДЗ по английскому",
                description = "Новый тест по старому модулю Новый тест по старому модулю Новый тест по старому модулю Новый тест по старому модулю",
                author = "Алексей Воробьев",
                eventDate = 12345L,
                lastModifiedDate = 12345L,
                group = "М8О-203М-20"
            )
        )
    }
}