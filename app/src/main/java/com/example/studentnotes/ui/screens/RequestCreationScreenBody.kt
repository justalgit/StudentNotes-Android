package com.example.studentnotes.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.studentnotes.data.entities.GroupsList
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.ui.theme.Blue200
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.getCurrentTimestamp
import com.example.studentnotes.utils.getLoggedInUserId
import com.example.studentnotes.utils.getUserSharedPreferences
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.*

class RequestCreationViewModel : ViewModel() {

    private val serverRepo = ServerRepository()

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    fun createRequest(request: Request) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.createRequest(request)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("createRequest", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("createRequest", "error: ${e.message}")
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@Composable
fun RequestCreationScreenBody(
    navController: NavController,
    viewModel: RequestCreationViewModel = viewModel()
) {
    val options = listOf(
        stringResource(R.string.request),
        stringResource(R.string.invitation)
    )
    var selectedOption by remember {
        mutableStateOf(options[0])
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }

    var requestMessage by rememberSaveable { mutableStateOf("") }
    var isMessageAbsent by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val currentUserId = context.getUserSharedPreferences()?.getLoggedInUserId() ?: ""
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )

    val availableGroups = databaseRepo.getAllGroups().observeAsState().value ?: emptyList()
    var selectedGroupTitle = remember {
        mutableStateOf(
            if (availableGroups.isEmpty()) "" else availableGroups[0].title
        )
    }

    val availableUsers = databaseRepo.getAllUsers().observeAsState().value ?: emptyList()
    var selectedUser = remember {
        mutableStateOf(
            if (availableUsers.isEmpty()) "" else "${availableUsers[0].name} ${availableUsers[0].surname}"
        )
    }

    val requestStatus by viewModel.requestStatus.observeAsState()

    if (requestStatus == ApiRequestStatus.LOADING) {
        UiProgressDialog()
    }
    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.Top)
        ) {
            UiHeader(
                leftContent = {
                    UiBackButton(
                        onClick = { navController.popBackStack() }
                    )
                },
                rightRowContent = {
                    Text(stringResource(R.string.request_creation).uppercase())
                }
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                options.forEach { text ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .weight(1f)
                            .padding(horizontal = 12.dp)
                            .clip(shape = RoundedCornerShape(size = 12.dp))
                            .clickable { onSelectionChange(text) }
                            .background(if (text == selectedOption) Blue200 else Color.LightGray),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = text,
                            style = Typography.body1,
                            color = Color.White,
                            modifier = Modifier
                                .padding(vertical = 12.dp, horizontal = 16.dp)
                        )
                    }
                }
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .padding(8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { isMessageAbsent = !isMessageAbsent }
                    )
            ) {
                Checkbox(
                    checked = isMessageAbsent,
                    onCheckedChange = null
                )
                Text(
                    text = stringResource(R.string.no_message),
                    color = Color.Black
                )
            }
            AnimatedVisibility(!isMessageAbsent) {
                UiTextArea(
                    value = requestMessage,
                    onValueChange = {
                        requestMessage = it
                    },
                    label = stringResource(R.string.description)
                )
            }
            UiDropdownList(
                label = stringResource(R.string.group),
                selectedOption = selectedGroupTitle,
                suggestions = availableGroups.map { it.title }.distinct()
            )
            AnimatedVisibility(selectedOption == stringResource(R.string.invitation)) {
                UiDropdownList(
                    label = stringResource(R.string.user),
                    selectedOption = selectedUser,
                    suggestions = availableUsers.map { "${it.name} ${it.surname}" }.distinct()
                )
            }
        }

        UiBigButton(
            text = stringResource(R.string.create_request),
            isEnabled = isMessageAbsent || !isMessageAbsent && requestMessage.isNotBlank(),
            onClick = {
                val nameAndSurname = selectedUser.value.split("\\s".toRegex()).toTypedArray()
                // TODO: пофиксить запрос на приглашение
                viewModel.createRequest(
                    Request(
                        id = UUID.randomUUID().toString(),
                        authorId = currentUserId,
                        incomingUserId = if (selectedOption == context.getString(R.string.invitation)) {
                            databaseRepo.getUserByNameAndSurname(
                                nameAndSurname[0],
                                nameAndSurname[1]
                            ).value?.id ?: ""
                        } else {
                            currentUserId
                        },
                        groupId = databaseRepo.getGroupByTitle(selectedGroupTitle.value).id,
                        requestDate = getCurrentTimestamp(),
                        message = if (requestMessage.isNotBlank()) requestMessage else null
                    )
                )
                if (selectedOption == context.getString(R.string.invitation)) {
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.invitation_was_sent_to_user,
                            selectedUser.value
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.your_request_was_sent,
                            selectedGroupTitle.value
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                }
                navController.popBackStack(
                    Screen.MainPagerScreen.route,
                    inclusive = false
                )
            }
        )
    }
}