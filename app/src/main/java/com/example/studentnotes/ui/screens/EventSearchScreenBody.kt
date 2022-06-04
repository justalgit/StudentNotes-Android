package com.example.studentnotes.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.entities.*
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.components.EventsListComposable
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Typography
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class EventSearchViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = EventSearchViewModel(context) as T
}

class EventSearchViewModel(context: Context) : ViewModel() {

    private val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )

    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>>
        get() = _events

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val _groups = MutableLiveData<List<Group>>()
    val groups: LiveData<List<Group>>
        get() = _groups

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    init {
        getEventSearchDataFromDb()
    }

    private fun getEventSearchDataFromDb() {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                _groups.value = databaseRepo.getAllGroups()
                _users.value = databaseRepo.getAllUsers()
                _events.value = databaseRepo.getAllEvents()
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("getEventSearchDataFromDb", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("getEventSearchDataFromDb", "error: ${e.message}")
                _groups.value = emptyList()
                _users.value = emptyList()
                _events.value = emptyList()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun EventSearchScreenBody(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel by remember { mutableStateOf(EventSearchViewModel(context)) }

    val eventsList by viewModel.events.observeAsState()
    val groupsList by viewModel.groups.observeAsState()
    val usersList by viewModel.users.observeAsState()

    var eventTitle by rememberSaveable { mutableStateOf("") }
    var filteredEvents by rememberSaveable { mutableStateOf(eventsList) }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {
        UiHeader(
            leftContent = {
                UiBackButton(
                    onClick = { navController.popBackStack() }
                )
            },
            rightRowContent = {
                Text(stringResource(R.string.event_searching).uppercase())
            }
        )
        UiTextField(
            value = eventTitle,
            onValueChange = {
                eventTitle = it
                filteredEvents = eventsList?.filter {
                    it.title.contains(eventTitle, ignoreCase = true)
                }
            },
            label = stringResource(R.string.event_title)
        )
        EventsListComposable(
            events = if (eventTitle.isEmpty()) eventsList!! else filteredEvents!!,
            groups = groupsList!!,
            users = usersList!!,
            onEventClick = { event ->
                navController.navigate(
                    Screen.EventDetailsScreen.withArgs(event.id)
                )
            }
        )
        if (filteredEvents.isNullOrEmpty() && eventTitle.isNotEmpty() || eventsList.isNullOrEmpty()) {
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