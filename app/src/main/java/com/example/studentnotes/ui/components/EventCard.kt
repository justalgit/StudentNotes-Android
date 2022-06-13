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
import androidx.compose.ui.unit.dp
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.R
import com.example.studentnotes.utils.getFormattedDateFromTimestamp

@Composable
fun EventCard(
    event: Event,
    author: String,
    group: String,
    isPassed: Boolean,
    onClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val textColor = if (isPassed) Color.Gray else Color.Black
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
            when (event.user_priority) {
                2 -> Image(
                    painter = painterResource(R.drawable.ic_star_full_24),
                    contentDescription = null
                )
                1 -> Image(
                    painter = painterResource(R.drawable.ic_star_half_24),
                    contentDescription = null
                )
                0 -> Image(
                    painter = painterResource(R.drawable.ic_star_empty_24),
                    contentDescription = null
                )
            }
            Spacer(Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = event.title,
                    style = Typography.body2,
                    color = textColor
                )
                Text(
                    text = getFormattedDateFromTimestamp(event.eventDate),
                    style = Typography.body1,
                    color = textColor
                )
                Text(
                    text = group,
                    style = Typography.caption,
                    color = textColor
                )
                Text(
                    text = context.getString(R.string.author, author),
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
