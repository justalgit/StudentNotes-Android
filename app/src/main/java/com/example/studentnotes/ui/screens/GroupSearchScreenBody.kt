package com.example.studentnotes.ui.screens

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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.theme.Typography
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.studentnotes.data.entities.GroupsList
import com.example.studentnotes.data.entities.UsersList
import com.example.studentnotes.ui.components.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class GroupSearchViewModel : ViewModel() {

    private val serverRepo = ServerRepository()

    private val _groupsFromServer = MutableLiveData<GroupsList>()
    val groupsFromServer: LiveData<GroupsList>
        get() = _groupsFromServer

    private val _usersFromServer = MutableLiveData<UsersList>()
    val usersFromServer: LiveData<UsersList>
        get() = _usersFromServer

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    init {
        getGroupsAndUsersFromServer()
    }

    private fun getGroupsAndUsersFromServer() {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                _groupsFromServer.value = serverRepo.getAllGroups()
                _usersFromServer.value = serverRepo.getAllUsers()
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("groupsFromServer", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("groupsFromServer", "error: ${e.message}")
                _groupsFromServer.value = GroupsList()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun GroupSearchScreenBody(
    navController: NavController,
    viewModel: GroupSearchViewModel = viewModel()
) {

    val context = LocalContext.current

    val groupsList by viewModel.groupsFromServer.observeAsState()
    val usersList by viewModel.usersFromServer.observeAsState()

    var groupTitle by rememberSaveable { mutableStateOf("") }
    var filteredGroups by rememberSaveable { mutableStateOf(groupsList?.groups ?: emptyList()) }

    val requestStatus by viewModel.requestStatus.observeAsState()
    if (requestStatus == ApiRequestStatus.LOADING) {
        UiProgressDialog()
    }
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
                Text(stringResource(R.string.group_searching).uppercase())
            }
        )
        UiTextField(
            value = groupTitle,
            onValueChange = {
                groupTitle = it
                filteredGroups = groupsList?.groups?.filter {
                    it.title.contains(groupTitle, ignoreCase = true)
                } ?: emptyList()
            },
            label = stringResource(R.string.group_title)
        )
        GroupsListComposable(
            groups = if (groupTitle.isEmpty()) groupsList?.groups ?: emptyList() else filteredGroups,
            users = usersList?.users ?: emptyList(),
            onGroupClick = { group ->
                navController.navigate(
                    Screen.GroupDetailsScreen.withArgs(group.id)
                )
            }
        )
        if (filteredGroups.isNullOrEmpty() && groupTitle.isNotEmpty() || groupsList?.groups.isNullOrEmpty()) {
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
