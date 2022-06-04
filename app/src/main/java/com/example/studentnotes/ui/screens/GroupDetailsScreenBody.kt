package com.example.studentnotes.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.entities.*
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.theme.LightRed
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.*

class GroupDetailsViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = GroupDetailsViewModel(context) as T
}

class GroupDetailsViewModel(context: Context) : ViewModel() {

    private val serverRepo = ServerRepository()
    private val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    private val currentUserId = context.getUserSharedPreferences()?.getLoggedInUserId() ?: ""

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val _groupsForUser = MutableLiveData<List<Group>>()
    val groupsForUser: LiveData<List<Group>>
        get() = _groupsForUser

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    init {
        getGroupsForUser(currentUserId)
        getAllUsersFromDb()
    }

    fun leaveGroup(groupId: String, userId: String) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.leaveGroup(groupId, userId)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("leaveGroup", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("leaveGroup", "error: ${e.message}")
            }
        }
    }

    private fun getGroupsForUser(userId: String) {
        viewModelScope.launch {
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
    }

    private fun getAllUsersFromDb() {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                _users.value = databaseRepo.getAllUsers()
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("allUsersFromDb", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("allUsersFromDb", "error: ${e.message}")
                _users.value = emptyList()
            }
        }
    }
}

@Composable
fun GroupDetailsScreenBody(
    navController: NavController,
    groupId: String
) {
    val context = LocalContext.current
    val currentUserId = context.getUserSharedPreferences()?.getLoggedInUserId() ?: ""
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val group = databaseRepo.getGroupById(groupId)
    val groupDetailsViewModel by remember { mutableStateOf(GroupDetailsViewModel(context)) }
    val requestCreationViewModel by remember { mutableStateOf(RequestCreationViewModel(context)) }

    val groupsForUser by groupDetailsViewModel.groupsForUser.observeAsState()
    val allUsers by groupDetailsViewModel.users.observeAsState()

    val groupCreator = allUsers?.find { it.id == group.creatorId }
    val groupLastModifiedUser = allUsers?.find { it.id == group.lastModifiedUserId }

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
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
                Text(group.title.cutOff(HEADER_TITLE_LENGTH).uppercase())
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
        ) {
            Text(
                text = group.title,
                style = Typography.h5,
                color = Color.Black
            )
            Text(
                text = context.getString(R.string.author, groupCreator?.stringifiedName() ?: ""),
                style = Typography.body2,
                color = Color.Black
            )
            Text(
                text = group.description ?: stringResource(R.string.no_description),
                style = Typography.body1,
                color = Color.Black
            )
            Text(
                text = context.getString(
                    R.string.last_modified_by,
                    groupLastModifiedUser?.stringifiedName() ?: "",
                    getFormattedDateFromTimestamp(group.lastModifiedDate)
                ),
                style = Typography.caption,
                color = Color.Gray
            )
            if (!group.isEditable && currentUserId != group.creatorId) {
                Text(
                    text = context.getString(R.string.creator_made_group_uneditable),
                    style = Typography.body1,
                    textAlign = TextAlign.Center,
                    color = Color.Gray,
                    modifier = Modifier
                        .padding(start = 24.dp, top = 24.dp, end = 24.dp)
                        .fillMaxWidth()
                )
            }
        }
        if (groupsForUser!!.find { it.title == group.title && it.lastModifiedDate == group.lastModifiedDate } == null) {
            UiBigButton(
                text = stringResource(R.string.to_join),
                onClick = {
                    requestCreationViewModel.createRequest(
                        Request(
                            id = UUID.randomUUID().toString(),
                            authorId = currentUserId,
                            groupId = group.id,
                            message = null,
                            requestDate = getCurrentTimestamp()
                        )
                    )
                    showToast(
                        context = context,
                        string = when (group.isPrivate) {
                            true -> context.getString(
                                R.string.request_sent_to_private_group,
                                group.title
                            )
                            else -> context.getString(
                                R.string.you_joined_group,
                                group.title
                            )
                        }
                    )
                    navController.popBackStack(
                        Screen.MainPagerScreen.route,
                        inclusive = false
                    )
                }
            )
        } else {
            if (group.isEditable || !group.isEditable && currentUserId == group.creatorId) {
                UiBigButton(
                    text = stringResource(R.string.edit),
                    onClick = {
                        navController.navigate(
                            Screen.GroupEditingScreen.withArgs(group.id)
                        )
                    }
                )
            }
            UiBigButton(
                text = stringResource(R.string.leave_group),
                color = LightRed,
                isEnabled = true,
                onClick = {
                    groupDetailsViewModel.leaveGroup(group.id, currentUserId)
                    showToast(
                        context,
                        context.getString(
                            R.string.you_left_group,
                            group.title
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