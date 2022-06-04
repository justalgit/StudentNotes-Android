package com.example.studentnotes.ui.screens

import android.content.Context
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
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.Request
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.utils.getCurrentTimestamp
import com.example.studentnotes.utils.getLoggedInUserId
import com.example.studentnotes.utils.getUserSharedPreferences
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.*

class RequestCreationViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = RequestCreationViewModel(context) as T
}

class RequestCreationViewModel(context: Context) : ViewModel() {

    private val serverRepo = ServerRepository()
    private val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    private val currentUserId = context.getUserSharedPreferences()?.getLoggedInUserId() ?: ""

    private val _groupsWithoutUser = MutableLiveData<List<Group>>()
    val groupsWithoutUser: LiveData<List<Group>>
        get() = _groupsWithoutUser

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    init {
        getGroupsWithoutUser(currentUserId)
    }

    private fun getGroupsWithoutUser(userId: String) {
        try {
            _requestStatus.value = ApiRequestStatus.LOADING
            _groupsWithoutUser.value = databaseRepo.getGroupsWithoutUser(userId)
            _requestStatus.value = ApiRequestStatus.DONE
            Log.d("groupsWithoutUser", "success")
        }
        catch (e: Exception) {
            _requestStatus.value = when (e) {
                is HttpException -> ApiRequestStatus.HTTP_ERROR
                is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                else -> ApiRequestStatus.UNKNOWN_ERROR
            }
            Log.d("groupsWithoutUser", "error: ${e.message}")
            _groupsWithoutUser.value = emptyList()
        }
    }

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
    navController: NavController
) {
    var requestMessage by rememberSaveable { mutableStateOf("") }
    var isMessageAbsent by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val currentUserId = context.getUserSharedPreferences()?.getLoggedInUserId() ?: ""
    val viewModel by remember { mutableStateOf(RequestCreationViewModel(context)) }
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )

    val availableGroups = viewModel.groupsWithoutUser.observeAsState().value ?: emptyList()
    var selectedGroupTitle = remember {
        mutableStateOf(
            if (availableGroups.isEmpty()) "" else availableGroups[0].title
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
                    label = stringResource(R.string.message)
                )
            }
            UiDropdownList(
                label = stringResource(R.string.group),
                selectedOption = selectedGroupTitle,
                suggestions = availableGroups.map { it.title }.distinct()
            )
        }

        UiBigButton(
            text = stringResource(R.string.create_request),
            isEnabled = isMessageAbsent || !isMessageAbsent && requestMessage.isNotBlank(),
            onClick = {
                viewModel.createRequest(
                    Request(
                        id = UUID.randomUUID().toString(),
                        authorId = currentUserId,
                        groupId = databaseRepo.getGroupByTitle(selectedGroupTitle.value).id,
                        requestDate = getCurrentTimestamp(),
                        message = if (requestMessage.isNotBlank()) requestMessage else null
                    )
                )
                Toast.makeText(
                    context,
                    context.getString(
                            R.string.your_request_was_sent,
                            selectedGroupTitle.value
                        ),
                    Toast.LENGTH_SHORT
                ).show()
                navController.popBackStack(
                    Screen.MainPagerScreen.route,
                    inclusive = false
                )
            }
        )
    }
}
