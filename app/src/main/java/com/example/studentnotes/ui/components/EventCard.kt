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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.ui.theme.Typography
import java.util.*
import com.example.studentnotes.R

@Composable
fun EventCard(
    event: Event
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
                onClick = {}
            ),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.weight(9f)
            ) {
                Text(
                    text = event.title,
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = "${event.eventDate}",
                    style = Typography.body1,
                    color = Color.Black
                )
                Text(
                    text = sliceString(event.description, 75),
                    style = Typography.caption,
                    color = Color.Gray
                )
                Text(
                    text = "Автор: ${event.author}",
                    style = Typography.caption,
                    color = Color.Gray
                )
            }
            Image(
                painter = painterResource(R.drawable.ic_arrow_forward_24),
                contentDescription = null,
                modifier = Modifier
                    .weight(1f)
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
                eventDate = Date(2022, 4, 7, 14, 45),
                creationDate = Date(2022, 4, 3, 21, 30),
                group = "М8О-203М-20"
            )
        )
        EventCard(
            event = Event(
                title = "Сделать ДЗ по английскому",
                description = "Новый тест по старому модулю Новый тест по старому модулю Новый тест по старому модулю Новый тест по старому модулю",
                author = "Алексей Воробьев",
                eventDate = Date(2022, 4, 7, 14, 45),
                creationDate = Date(2022, 4, 3, 21, 30),
                group = "М8О-203М-20"
            )
        )
    }
}

fun sliceString(value: String, limit: Int): String {
    if (value.length > limit)
        return value.slice(0..limit) + "..."
    return value
}