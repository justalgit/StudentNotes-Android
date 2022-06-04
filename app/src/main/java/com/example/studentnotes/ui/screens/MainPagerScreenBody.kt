package com.example.studentnotes.ui.screens

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.*
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.datasources.server.json.InitialDataResponse
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.UiProgressDialog
import com.example.studentnotes.ui.components.UiTabItem
import com.example.studentnotes.utils.getLoggedInUserId
import com.example.studentnotes.utils.getUserSharedPreferences
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class MainPagerViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = MainPagerViewModel(context) as T
}

class MainPagerViewModel(context: Context) : ViewModel() {

    private val serverRepo = ServerRepository()
    private val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    private val currentUserId = context.getUserSharedPreferences()?.getLoggedInUserId() ?: ""

    private val _userInitialData = MutableLiveData<InitialDataResponse>()
    val userInitialData: LiveData<InitialDataResponse>
        get() = _userInitialData

    private val _groupsForUser = MutableLiveData<List<Group>>()
    val groupsForUser: LiveData<List<Group>>
        get() = _groupsForUser

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    init {
        viewModelScope.launch {
            getInitialData(currentUserId)
            getGroupsForUser(currentUserId)
        }
    }

    private suspend fun getInitialData(userId: String) {
        try {
            _requestStatus.value = ApiRequestStatus.LOADING
            _userInitialData.value = serverRepo.getInitialData(userId)
            databaseRepo.clearData()
            databaseRepo.insertInitialData(_userInitialData.value!!)
            _requestStatus.value = ApiRequestStatus.DONE
            Log.d("initialData", "success")
        }
        catch (e: Exception) {
            _requestStatus.value = when (e) {
                is HttpException -> ApiRequestStatus.HTTP_ERROR
                is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                else -> ApiRequestStatus.UNKNOWN_ERROR
            }
            Log.d("initialData", "error: ${e.message}")
            _userInitialData.value?.apply {
                groupsList = emptyList()
                eventsList = emptyList()
                requestsList = emptyList()
                userGroupRelationsList = emptyList()
            }
        }
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
}

@Composable
fun MainPagerScreenBody(
    navController: NavController
) {
    val context = LocalContext.current
    val viewModel by remember { mutableStateOf(MainPagerViewModel(context)) }
    val requestStatus by viewModel.requestStatus.observeAsState()
    val initialData by viewModel.userInitialData.observeAsState()
    val groupsForUser by viewModel.groupsForUser.observeAsState()

    val menus = listOf(
        TabData(stringResource(R.string.events), painterResource(R.drawable.ic_events_24)),
        TabData(stringResource(R.string.groups), painterResource(R.drawable.ic_groups_24)),
        TabData(stringResource(R.string.requests), painterResource(R.drawable.ic_requests_24)),
        TabData(stringResource(R.string.settings), painterResource(R.drawable.ic_settings_24))
    )
    var selectedMenu by remember {
        mutableStateOf(menus[0])
    }

    Scaffold {
        Column {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                if (requestStatus == ApiRequestStatus.LOADING) {
                    UiProgressDialog()
                }
                when (selectedMenu) {
                    menus[0] -> EventsScreenBody(
                        navController = navController,
                        eventsList = initialData?.eventsList ?: emptyList(),
                        groupsList = initialData?.groupsList ?: emptyList(),
                        usersList = initialData?.usersList ?: emptyList()
                    )
                    menus[1] -> {
                        GroupsScreenBody(
                            navController = navController,
                            groupsList = groupsForUser ?: emptyList(),
                            usersList = initialData?.usersList ?: emptyList()
                        )
                    }
                    menus[2] -> RequestsScreenBody(
                        navController = navController,
                        requestsList = initialData?.requestsList ?: emptyList(),
                        groupsList = initialData?.groupsList ?: emptyList(),
                        usersList = initialData?.usersList ?: emptyList()
                    )
                    menus[3] -> SettingsScreenBody(
                        navController = navController
                    )
                }
            }
            Row(
                modifier = Modifier
                    .padding(vertical = 12.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                menus.forEach { menu ->
                    UiTabItem(
                        modifier = Modifier
                            .weight(1f),
                        textLabel = menu.textLabel,
                        iconPainter = menu.painter,
                        isSelected = menu == selectedMenu,
                        onClick = {
                            selectedMenu = menu
                        }
                    )
                }
            }
        }
    }
}

data class TabData(
    var textLabel: String,
    var painter: Painter
)