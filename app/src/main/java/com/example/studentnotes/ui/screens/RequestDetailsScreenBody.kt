package com.example.studentnotes.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
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
import com.example.studentnotes.data.entities.*
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.UiBackButton
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.components.UiProgressDialog
import com.example.studentnotes.ui.theme.LightGreen
import com.example.studentnotes.ui.theme.LightRed
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class RequestDetailsViewModel : ViewModel() {

    private val serverRepo = ServerRepository()

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus


    fun deleteRequest(requestId: String, isAccept: Boolean) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.deleteRequest(requestId, isAccept)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("deleteRequest", "success, is_accept = $isAccept")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("deleteRequest", "error: ${e.message}")
            }
        }
    }

}

@Composable
fun RequestDetailsScreenBody(
    navController: NavController,
    request: Request,
    viewModel: RequestDetailsViewModel = viewModel()
) {

    val context = LocalContext.current
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val sharedPrefs = context.getUserSharedPreferences()
    val currentUserId = sharedPrefs?.getLoggedInUserId() ?: ""
    val requestStatus by viewModel.requestStatus.observeAsState()

    val requestAuthor = databaseRepo.getUserById(request.authorId)
    val requestGroup = databaseRepo.getGroupById(request.groupId)
    val requestIncomingUser = databaseRepo.getUserById(request.incomingUserId)

    val coroutineScope = rememberCoroutineScope()

    if (requestStatus == ApiRequestStatus.LOADING) {
        UiProgressDialog()
    }
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
                Text(request.requestTypeStringValue(context, currentUserId).uppercase())
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
        ) {
            Text(
                text = request.requestTypeStringValue(context, currentUserId),
                style = Typography.h5,
                color = Color.Black
            )
            Text(
                text = context.getString(
                    R.string.author,
                    "${requestAuthor.name} ${requestAuthor.surname}"
                ),
                style = Typography.body2,
                color = Color.Black
            )
            Text(
                text = context.getString(
                    R.string.group_with_parameter,
                    requestGroup.title
                ),
                style = Typography.body2,
                color = Color.Black
            )
            Text(
                text = if (!request.message.isNullOrEmpty()) "\"${request.message}\"" else stringResource(R.string.no_message),
                style = Typography.body1,
                color = Color.Black
            )
            Text(
                text = context.getString(
                    R.string.user_can_join_after_request_acceptance,
                    "${requestIncomingUser.name} ${requestIncomingUser.surname}"
                ),
                style = Typography.body1,
                color = Color.Gray
            )
            Text(
                text = context.getString(
                    R.string.request_date,
                    getFormattedDateFromTimestamp(request.requestDate)
                ),
                style = Typography.caption,
                color = Color.Gray
            )
        }
        if (request.requestType(currentUserId) == RequestType.INCOMING_REQUEST) {
            if (!requestGroup.isPrivate || (requestGroup.isPrivate && requestGroup.creatorId == currentUserId))
            UiBigButton(
                text = stringResource(R.string.accept),
                color = LightGreen,
                isEnabled = true,
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteRequest(request.id, isAccept = true)
                    }
                    showToast(
                        context,
                        context.getString(
                            R.string.request_accepted_successfully
                        )
                    )
                    navController.popBackStack(
                        Screen.MainPagerScreen.route,
                        inclusive = false
                    )
                }
            )
        }
        UiBigButton(
            text = stringResource(R.string.delete),
            color = LightRed,
            isEnabled = true,
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteRequest(request.id, isAccept = false)
                }
                showToast(
                    context,
                    context.getString(
                        R.string.request_deleted_successfully
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