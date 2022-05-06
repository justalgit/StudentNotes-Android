package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.components.RequestCard
import com.example.studentnotes.ui.components.RequestType
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiIconButton
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.CURRENT_USER_PLACEHOLDER_ID

@Composable
fun RequestsScreenBody(
    navController: NavController
) {

    val context = LocalContext.current
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )

    val requestsList = databaseRepo.getAllRequests().observeAsState().value ?: emptyList()
    val outcomingRequestsList = requestsList.filter {
        it.authorId == CURRENT_USER_PLACEHOLDER_ID
    }
    val incomingRequestsList = requestsList.filter {
        !outcomingRequestsList.contains(it)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        UiHeader(
            leftContent = {
                Text(
                    text = stringResource(R.string.requests),
                    style = Typography.h5,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            },
            rightRowContent = {
                UiIconButton(
                    painter = painterResource(R.drawable.ic_plus_24),
                    onClick = { navController.navigate(Screen.RequestCreationScreen.route) }
                )
                UiIconButton(
                    painter = painterResource(R.drawable.ic_search_24),
                    onClick = { navController.navigate(Screen.RequestSearchScreen.route) }
                )
            }
        )
        if (outcomingRequestsList.isNotEmpty()) {
            Text(
                text = stringResource(R.string.requests),
                style = Typography.h6,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            RequestsList(
                requests = outcomingRequestsList
            )
        }
        if (incomingRequestsList.isNotEmpty()) {
            Text(
                text = stringResource(R.string.requests),
                style = Typography.h6,
                color = Color.Black,
                modifier = Modifier.padding(vertical = 12.dp)
            )
            RequestsList(
                requests = incomingRequestsList
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (requestsList.isEmpty()) {
                Text(
                    text = stringResource(R.string.you_dont_have_requests_yet),
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
fun RequestsList(
    modifier: Modifier = Modifier,
    requests: List<Request>
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(requests) { request ->
            RequestCard(
                request,
                getRequestType(request)
            )
        }
    }
}

fun getRequestType(
    request: Request
) = if (request.authorId != CURRENT_USER_PLACEHOLDER_ID)
        RequestType.INCOMING_REQUEST
    else if (request.authorId == CURRENT_USER_PLACEHOLDER_ID && request.incomingUserId == CURRENT_USER_PLACEHOLDER_ID)
        RequestType.OUTCOMING_REQUEST
    else
        RequestType.INVITATION