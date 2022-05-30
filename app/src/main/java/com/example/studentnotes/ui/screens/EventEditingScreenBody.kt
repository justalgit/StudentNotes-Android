package com.example.studentnotes.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.entities.Event
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.ui.theme.LightRed
import com.example.studentnotes.utils.*
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.time.format.DateTimeFormatter

class EventEditingViewModel : ViewModel() {

    private val serverRepo = ServerRepository()

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    fun updateEvent(event: Event) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.updateEvent(event)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("updateEvent", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("updateEvent", "error: ${e.message}")
            }
        }
    }

    fun deleteEvent(eventId: String) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.deleteEvent(eventId)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("deleteEvent", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("deleteEvent", "error: ${e.message}")
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun EventEditingScreenBody(
    navController: NavController,
    event: Event,
    viewModel: EventEditingViewModel = viewModel()
) {

    var eventTitle by rememberSaveable { mutableStateOf(event.title) }
    var eventDescription by rememberSaveable { mutableStateOf(event.description ?: "") }
    var isDescriptionAbsent by rememberSaveable { mutableStateOf(event.description == null) }
    var isEventEditable by rememberSaveable { mutableStateOf(event.isEditable) }
    var eventDate by rememberSaveable { mutableStateOf(getLocalDateFromTimestamp(event.eventDate)) }
    var eventTime by rememberSaveable { mutableStateOf(getLocalTimeFromTimestamp(event.eventDate)) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val sharedPrefs = context?.getUserSharedPreferences()
    val currentUserId = sharedPrefs?.getLoggedInUserId() ?: ""
    val requestStatus by viewModel.requestStatus.observeAsState()

    var isEventModified by rememberSaveable { mutableStateOf(false) }

    val dateDialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.close))
        }
    ) {
        datepicker { date ->
            eventDate = date
        }
    }

    val timeDialogState = rememberMaterialDialogState()
    MaterialDialog(
        dialogState = timeDialogState,
        buttons = {
            positiveButton(stringResource(R.string.ok))
            negativeButton(stringResource(R.string.close))
        }
    ) {
        timepicker { time ->
            eventTime = time
        }
    }

    if (requestStatus == ApiRequestStatus.LOADING) {
        UiProgressDialog()
    }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
        ) {
            UiHeader(
                leftContent = {
                    UiBackButton(
                        onClick = { navController.popBackStack() }
                    )
                },
                rightRowContent = {
                    Text(stringResource(R.string.event_editing).uppercase())
                }
            )
            UiTextField(
                value = eventTitle,
                onValueChange = {
                    eventTitle = it
                    isEventModified = true
                },
                label = stringResource(R.string.title),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isDescriptionAbsent = !isDescriptionAbsent
                            isEventModified = true
                        }
                    )
            ) {
                Checkbox(
                    checked = isDescriptionAbsent,
                    onCheckedChange = { isDescriptionAbsent = !isDescriptionAbsent },
                )
                Text(
                    text = stringResource(R.string.no_description),
                    color = Color.Black
                )
            }
            AnimatedVisibility(!isDescriptionAbsent) {
                UiTextArea(
                    value = eventDescription,
                    onValueChange = {
                        eventDescription = it
                        isEventModified = true
                    },
                    label = stringResource(R.string.description)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = {
                            isEventEditable = !isEventEditable
                            isEventModified = true
                        }
                    )
            ) {
                Checkbox(
                    checked = isEventEditable,
                    onCheckedChange = { isEventEditable = !isEventEditable },
                )
                Text(
                    text = stringResource(R.string.editable_event),
                    color = Color.Black
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                UiTextField(
                    value = eventDate.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    label = stringResource(R.string.date),
                    isEnabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                dateDialogState.show()
                                isEventModified = true
                            }
                        )
                )
                UiTextField(
                    value = eventTime.format(DateTimeFormatter.ISO_LOCAL_TIME),
                    label = stringResource(R.string.time),
                    isEnabled = false,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() },
                            onClick = {
                                timeDialogState.show()
                                isEventModified = true
                            }
                        )
                )
            }
        }

        UiBigButton(
            text = stringResource(R.string.save_changes),
            isEnabled = isEventModified
                    && eventTitle.isNotBlank()
                    && (isDescriptionAbsent || !isDescriptionAbsent && eventDescription.isNotBlank())
        ) {
            coroutineScope.launch {
                with(event) {
                    title = eventTitle
                    description = if (isDescriptionAbsent) null else eventDescription
                    isEditable = isEventEditable
                    lastModifiedUserId = currentUserId
                    event.eventDate = getTimestampFromDateAndTime(eventDate, eventTime)
                    lastModifiedDate = getCurrentTimestamp()
                }
                viewModel.updateEvent(event)
            }
            showToast(
                context,
                context.getString(
                    R.string.event_updated_successfully,
                    event.title
                )
            )
            navController.popBackStack(
                Screen.MainPagerScreen.route,
                inclusive = false
            )
        }

        if (currentUserId == event.authorId) {
            UiBigButton(
                text = stringResource(R.string.delete),
                color = LightRed,
                isEnabled = true,
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteEvent(event.id)
                    }
                    showToast(
                        context,
                        context.getString(
                            R.string.event_deleted_successfully,
                            event.title
                        )
                    )
                    navController.popBackStack(
                        Screen.MainPagerScreen.route,
                        inclusive = false
                    )
                }
            )
        }
    }
}