package com.example.studentnotes.ui.screens

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import androidx.lifecycle.*
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
import com.example.studentnotes.utils.*
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.datetime.time.timepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.time.format.DateTimeFormatter
import java.util.*

class EventCreationViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = EventCreationViewModel(context) as T
}

class EventCreationViewModel(context: Context) : ViewModel() {

    private val serverRepo = ServerRepository()
    private val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    private val currentUserId = context.getUserSharedPreferences()?.getLoggedInUserId() ?: ""

    private val _groupsForUser = MutableLiveData<List<Group>>()
    val groupsForUser: LiveData<List<Group>>
        get() = _groupsForUser

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    init {
        getGroupsForUser(currentUserId)
    }

    private fun getGroupsForUser(userId: String) {
        try {
            _requestStatus.value = ApiRequestStatus.LOADING
            _groupsForUser.value = databaseRepo.getUserGroups(userId)
            _requestStatus.value = ApiRequestStatus.DONE
            Log.d("groupsForUser", "success")
        }
        catch (e: Exception) {
            _requestStatus.value = when (e) {
                is HttpException -> ApiRequestStatus.HTTP_ERROR
                is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                else -> ApiRequestStatus.UNKNOWN_ERROR
            }
            Log.d("groupsForUser", "error: ${e.message}")
            _groupsForUser.value = emptyList()
        }
    }

    fun createEvent(event: Event) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.createEvent(event)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("createEvent", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("createEvent", "error: ${e.message}")
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun EventCreationScreenBody(
    navController: NavController
) {
    var eventTitle by rememberSaveable { mutableStateOf("") }
    var eventDescription by rememberSaveable { mutableStateOf("") }
    var isDescriptionAbsent by rememberSaveable { mutableStateOf(false) }
    var isEventEditable by rememberSaveable { mutableStateOf(true) }
    var eventDate by rememberSaveable { mutableStateOf(getCurrentLocalDate()) }
    var eventTime by rememberSaveable { mutableStateOf(getCurrentLocalTime()) }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val sharedPrefs = context.getUserSharedPreferences()
    val currentUserId = sharedPrefs?.getLoggedInUserId() ?: ""
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val viewModel by remember { mutableStateOf(EventCreationViewModel(context)) }
    val availableGroups by viewModel.groupsForUser.observeAsState()
    val requestStatus by viewModel.requestStatus.observeAsState()

    var selectedGroupTitle = remember {
        mutableStateOf(
            if (availableGroups!!.isEmpty()) "" else availableGroups!![0].title
        )
    }

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
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp)
    ) {

        Column(
            modifier = Modifier.weight(1f),
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
                    Text(stringResource(R.string.event_creation).uppercase())
                }
            )
            UiTextField(
                value = eventTitle,
                onValueChange = {
                    eventTitle = it
                },
                label = stringResource(R.string.title),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isDescriptionAbsent = !isDescriptionAbsent }
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
                    },
                    label = stringResource(R.string.description)
                )
            }
            UiDropdownList(
                label = stringResource(R.string.group),
                selectedOption = selectedGroupTitle,
                suggestions = availableGroups!!.map { it.title }.distinct()
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isEventEditable = !isEventEditable }
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
                            onClick = { dateDialogState.show() }
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
                            onClick = { timeDialogState.show() }
                        )
                )
            }
        }

        UiBigButton(
            text = stringResource(R.string.create_event),
            isEnabled = eventTitle.isNotBlank() && (
                    isDescriptionAbsent || !isDescriptionAbsent && eventDescription.isNotBlank())
        ) {
            coroutineScope.launch {
                val groupId = databaseRepo.getGroupByTitle(selectedGroupTitle.value).id
                viewModel.createEvent(
                    Event(
                        id = UUID.randomUUID().toString(),
                        title = eventTitle,
                        description = if (!isDescriptionAbsent) eventDescription else null,
                        eventDate = getTimestampFromDateAndTime(eventDate, eventTime),
                        authorId = currentUserId,
                        lastModifiedDate = getCurrentTimestamp(),
                        lastModifiedUserId = currentUserId,
                        groupId = groupId,
                        isEditable = isEventEditable
                    )
                )
                showToast(
                    context,
                    context.getString(
                        R.string.event_created_successfully,
                        eventTitle
                    )
                )
                navController.popBackStack(
                    Screen.MainPagerScreen.route,
                    inclusive = false
                )
            }
        }
    }
}