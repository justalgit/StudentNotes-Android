package com.example.studentnotes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.entities.stringifiedName
import com.example.studentnotes.data.entities.stringifiedPriority
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiDropdownList
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime

class EventDetailsViewModel : ViewModel() {

    private val serverRepo = ServerRepository()

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    fun checkEvent(eventId: String, userId: String) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.checkEvent(eventId, userId)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("checkEvent", "success")
            } catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("checkEvent", "error: ${e.message}")
            }
        }
    }

    fun changeEventPriority(eventId: String, userId: String, priority: Int) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.changeEventPriority(eventId, userId, priority)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("changeEventPriority", "success")
            } catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("changeEventPriority", "error: ${e.message}")
            }
        }
    }
}

@OptIn(ExperimentalTime::class)
@Composable
fun EventDetailsScreenBody(
    navController: NavController,
    eventId: String?,
    viewModel: EventDetailsViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    val context = LocalContext.current
    val sharedPrefs = context.getUserSharedPreferences()
    val currentUserId = sharedPrefs?.getLoggedInUserId() ?: ""
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val event = databaseRepo.getEventById(eventId ?: "")
    val eventAuthor = databaseRepo.getUserById(event.authorId)
    val eventGroup = databaseRepo.getGroupById(event.groupId)
    val eventLastModifiedUser = databaseRepo.getUserById(event.lastModifiedUserId)

    val priorities = listOf("Высокий", "Средний", "Низкий")
    var selectedPriority = remember { mutableStateOf(event.stringifiedPriority()) }

    var isEventCheckedByUser by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while(true) {
            delay(3.seconds)
            if (!isEventCheckedByUser) {
                viewModel.checkEvent(
                    eventId = eventId ?: "",
                    userId = currentUserId
                )
                isEventCheckedByUser = true
            }
        }
    }
    if (selectedPriority.value != event.stringifiedPriority()) {
        viewModel.changeEventPriority(
            eventId = eventId ?: "",
            userId = currentUserId,
            priority = eventPriorityToInt(selectedPriority.value)
        )
        showToast(
            context = context,
            string = stringResource(R.string.priority_changed_successfully)
        )
        event.user_priority = eventPriorityToInt(selectedPriority.value)
    }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
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
                Text(eventGroup.title.cutOff(HEADER_TITLE_LENGTH).uppercase())
            }
        )
        if (event != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
            ) {
                Text(
                    text = event.title,
                    style = Typography.h5,
                    color = Color.Black
                )
                Text(
                    text = getFormattedDateFromTimestamp(event.eventDate),
                    style = Typography.body2,
                    color = Color.Black
                )
                Text(
                    text = context.getString(R.string.author, eventAuthor.stringifiedName()),
                    style = Typography.body1,
                    color = Color.Gray
                )
                Text(
                    text = event.description ?: stringResource(R.string.no_description),
                    style = Typography.body1,
                    color = Color.Black
                )
                Text(
                    text = "Приоритет: ${event.stringifiedPriority()}",
                    style = Typography.body1,
                    color = Color.Black
                )
                Text(
                    text = context.getString(
                        R.string.last_modified_by,
                        eventLastModifiedUser.stringifiedName(),
                        getFormattedDateFromTimestamp(event.lastModifiedDate)
                    ),
                    style = Typography.caption,
                    color = Color.Gray
                )
                if (!event.isEditable && currentUserId != event.authorId) {
                    Text(
                        text = context.getString(R.string.author_made_event_uneditable),
                        style = Typography.body1,
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                            .align(Alignment.CenterHorizontally)
                    )
                }
                UiDropdownList(
                    label = stringResource(R.string.priority),
                    selectedOption = selectedPriority,
                    suggestions = priorities
                )
                Text(
                    text = stringResource(R.string.this_priority_will_be_set_for_you),
                    style = Typography.caption,
                    textAlign = TextAlign.Center,
                    color = Color.Gray
                )
            }
            if (event.isEditable || !event.isEditable && event.authorId == currentUserId) {
                UiBigButton(
                    text = stringResource(R.string.edit),
                    onClick = {
                        navController.navigate(
                            Screen.EventEditingScreen.withArgs(event.id)
                        )
                    }
                )
            }
        }
    }
}