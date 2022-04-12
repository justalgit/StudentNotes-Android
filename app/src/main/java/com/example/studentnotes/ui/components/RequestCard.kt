package com.example.studentnotes.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.ui.theme.Typography

@Composable
fun RequestCard(
    request: Request
) {
    Card(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "${request.author} хочет вступить в группу \"${request.group}\"",
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = request.message ?: "Без сообщения",
                    style = Typography.caption,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    UiChoiceButton(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .weight(1f),
                        isAcceptButton = false
                    )
                    UiChoiceButton(
                        modifier = Modifier
                            .weight(1f),
                        isAcceptButton = true
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RequestCardPreview() {
    RequestCard(
        request = Request(
            author = "Макар Павлов",
            group = "М8О-203М-20",
            message = "Я передумал, пустите меня обратно..."
        )
    )
}