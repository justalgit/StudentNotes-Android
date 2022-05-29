package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.getLoggedInUserId
import com.example.studentnotes.utils.getUserSharedPreferences

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RequestSearchScreenBody(
    navController: NavController
) {
    val context = LocalContext.current
    val sharedPrefs = context.getUserSharedPreferences()
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )

    val currentUserId = sharedPrefs?.getLoggedInUserId() ?: ""
    val requestsList = databaseRepo.getAllRequests().observeAsState().value ?: emptyList()
    var requestSearchString by rememberSaveable { mutableStateOf("") }
    var filteredRequests by rememberSaveable { mutableStateOf(requestsList) }

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
                Text(stringResource(R.string.request_searching).uppercase())
            }
        )
        UiTextField(
            value = requestSearchString,
            onValueChange = {
                requestSearchString = it
                filteredRequests = requestsList
                // TODO: найти рациональный способ фильтрации
            },
            label = stringResource(R.string.search_string)
        )
        RequestsList(
            requests = if (requestSearchString.isEmpty()) requestsList else filteredRequests,
            currentUserId = currentUserId
        )
        if (filteredRequests.isEmpty() && requestSearchString.isNotEmpty() || requestsList.isEmpty()) {
            Text(
                text = context.getString(R.string.not_found),
                style = Typography.body1,
                textAlign = TextAlign.Center,
                color = Color.Gray,
                modifier = Modifier
                    .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}