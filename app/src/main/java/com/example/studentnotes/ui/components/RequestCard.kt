package com.example.studentnotes.ui.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.studentnotes.R
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.theme.*
import kotlinx.coroutines.launch

enum class RequestType {
    OUTCOMING_REQUEST,
    INCOMING_REQUEST,
    INVITATION
}

@Composable
fun RequestCard(
    request: Request,
    requestType: RequestType
) {

    val context = LocalContext.current
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val coroutineScope = rememberCoroutineScope()

    Card(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column {
            RequestTypeLabel(
                requestType = requestType
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = when(requestType) {
                        RequestType.INCOMING_REQUEST -> context.getString(
                            R.string.user_wants_to_join_group,
                            request.authorId,
                            request.groupId
                        )
                        RequestType.OUTCOMING_REQUEST -> context.getString(
                            R.string.you_sent_request_to_join_group,
                            request.groupId
                        )
                        RequestType.INVITATION -> context.getString(
                            R.string.you_invited_user_to_join_group,
                            request.incomingUserId,
                            request.groupId
                        )
                    },
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = request.message ?: stringResource(R.string.no_message),
                    style = Typography.body1,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .wrapContentHeight()
                        .padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    UiChoiceButton(
                        modifier = Modifier
                            .weight(1f),
                        isAcceptButton = false,
                        onClick = {
                            coroutineScope.launch {
                                databaseRepo.deleteRequest(request)
                            }
                            Toast.makeText(
                                context,
                                context.getString(R.string.outcoming_request_deleted),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )
                    if (requestType == RequestType.INCOMING_REQUEST) {
                        UiChoiceButton(
                            modifier = Modifier
                                .weight(1f),
                            isAcceptButton = true,
                            onClick = {
                                coroutineScope.launch {
                                    // TODO: принять в группу перед удалением
                                    databaseRepo.deleteRequest(request)
                                }
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.incoming_request_accepted),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RequestTypeLabel(requestType: RequestType) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Blue200
            )
    ) {
        Text(
            text = when(requestType) {
                RequestType.INVITATION -> stringResource(R.string.invitation)
                else -> stringResource(R.string.request_to_join)
            },
            color = Color.White,
            style = Typography.body1,
            modifier = Modifier
                .padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun RequestCardPreview() {
    Column(
        modifier = Modifier.padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        RequestCard(
            request = Request(
                id = "123",
                authorId = "Макар Павлов",
                incomingUserId = "Макар Павлов",
                groupId = "М8О-203М-20",
                requestDate = 123L,
                message = "Хочу к вам"
            ),
            requestType = RequestType.INCOMING_REQUEST
        )
        RequestCard(
            request = Request(
                id = "123",
                authorId = "Макар Павлов",
                incomingUserId = "Макар Павлов",
                groupId = "М8О-203М-20",
                requestDate = 123L,
                message = "Хочу к вам"
            ),
            requestType = RequestType.OUTCOMING_REQUEST
        )
        RequestCard(
            request = Request(
                id = "123",
                authorId = "Алексей Воробьев",
                incomingUserId = "Макар Павлов",
                groupId = "М8О-203М-20",
                requestDate = 123L,
                message = "Го к нам"
            ),
            requestType = RequestType.INVITATION
        )
    }
}