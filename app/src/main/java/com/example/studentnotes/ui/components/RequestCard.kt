package com.example.studentnotes.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentnotes.R
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.ui.theme.Typography

@Composable
fun RequestCard(
    request: Request
) {

    val context = LocalContext.current

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
                    text = context.getString(
                        R.string.user_wants_to_join_group,
                        request.authorId,
                        request.groupId
                    ),
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = request.message ?: stringResource(R.string.no_message),
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
            id = "123",
            authorId = "Алексей Воробьев",
            incomingUserId = "Макар Павлов",
            groupId = "М8О-203М-20",
            requestDate = 123L,
            message = "Го к нам"
        )
    )
}