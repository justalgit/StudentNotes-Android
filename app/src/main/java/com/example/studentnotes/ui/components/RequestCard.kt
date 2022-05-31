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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.studentnotes.R
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.data.entities.RequestType
import com.example.studentnotes.data.entities.requestType
import com.example.studentnotes.ui.theme.*
import com.example.studentnotes.utils.getLoggedInUserId
import com.example.studentnotes.utils.getUserSharedPreferences

@Composable
fun RequestCard(
    request: Request,
    requestType: RequestType,
    author: String,
    group: String,
    incomingUser: String,
    onRequestClick: () -> Unit = {}
) {

    val context = LocalContext.current
    val sharedPrefs = context.getUserSharedPreferences()
    val currentUserId = sharedPrefs?.getLoggedInUserId() ?: ""

    Card(
        backgroundColor = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(vertical = 8.dp),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = onRequestClick
            )
        ) {
            RequestTypeLabel(
                requestType = requestType
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = when(request.requestType(currentUserId)) {
                        RequestType.INCOMING_REQUEST -> context.getString(
                            R.string.user_wants_to_join_group,
                            author,
                            group
                        )
                        RequestType.OUTCOMING_REQUEST -> context.getString(
                            R.string.you_sent_request_to_join_group,
                            group
                        )
                        RequestType.INVITATION -> context.getString(
                            R.string.you_invited_user_to_join_group,
                            incomingUser,
                            group
                        )
                    },
                    style = Typography.body2,
                    color = Color.Black
                )
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