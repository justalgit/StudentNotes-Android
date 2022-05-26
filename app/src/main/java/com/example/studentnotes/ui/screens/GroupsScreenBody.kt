package com.example.studentnotes.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.entities.toJson
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.GroupCard
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiIconButton
import com.example.studentnotes.ui.components.UiProgressDialog
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.getLoggedInUserId
import com.example.studentnotes.utils.getUserSharedPreferences
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class GroupsScreenViewModel : ViewModel() {

    private val serverRepo = ServerRepository()

    private val _userGroups = MutableLiveData<List<Group>>()
    val userGroups: LiveData<List<Group>>
        get() = _userGroups

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    fun getUserGroups(userId: String) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                _userGroups.value = serverRepo.getUserGroups(userId).groups ?: emptyList()

                if (userGroups.value == null || userGroups.value?.isEmpty() == true)
                    _requestStatus.value = ApiRequestStatus.EMPTY
                else {
                    _requestStatus.value = ApiRequestStatus.DONE
                }
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                _userGroups.value = emptyList()
            }
        }
    }
}

@Composable
fun GroupsScreenBody(
    navController: NavController,
    viewModel: GroupsScreenViewModel = viewModel()
) {
    val context = LocalContext.current
    val sharedPrefs = context.getUserSharedPreferences()
    val requestStatus by viewModel.requestStatus.observeAsState()
    val groupsList by viewModel.userGroups.observeAsState()
    LaunchedEffect(Unit) {
        viewModel.getUserGroups(sharedPrefs?.getLoggedInUserId() ?: "")
    }

    if (requestStatus == ApiRequestStatus.LOADING) {
        UiProgressDialog()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 12.dp, end = 12.dp)
    ) {
        UiHeader(
            leftContent = {
                Text(
                    text = stringResource(R.string.groups),
                    style = Typography.h5,
                    color = Color.Black,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            },
            rightRowContent = {
                UiIconButton(
                    painter = painterResource(R.drawable.ic_plus_24),
                    onClick = { navController.navigate(Screen.GroupCreationScreen.route) }
                )
                UiIconButton(
                    painter = painterResource(R.drawable.ic_search_24),
                    onClick = { navController.navigate(Screen.GroupSearchScreen.route) }
                )
            }
        )
        GroupsList(
            groups = groupsList ?: emptyList(),
            onGroupClick = { group ->
                navController.navigate(
                    Screen.GroupDetailsScreen.withArgs(group.toJson())
                )
            }
        )
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (groupsList.isNullOrEmpty()) {
                Text(
                    text = stringResource(R.string.you_dont_have_groups_yet),
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
fun GroupsList(
    modifier: Modifier = Modifier,
    groups: List<Group>,
    onGroupClick: (Group) -> Unit
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        items(groups) { group ->
            GroupCard(
                group = group,
                onClick = { onGroupClick(group) }
            )
        }
    }
}