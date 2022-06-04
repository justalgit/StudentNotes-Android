package com.example.studentnotes.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
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
import com.example.studentnotes.data.entities.Group
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.ui.theme.LightGreen
import com.example.studentnotes.ui.theme.LightRed
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.getCurrentTimestamp
import com.example.studentnotes.utils.getLoggedInUserId
import com.example.studentnotes.utils.getUserSharedPreferences
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException

class GroupEditingViewModel : ViewModel() {

    private val serverRepo = ServerRepository()

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    fun updateGroup(group: Group) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.updateGroup(group)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("updateGroup", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("updateGroup", "error: ${e.message}")
            }
        }
    }

    fun deleteGroup(groupId: String) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.deleteGroup(groupId)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("deleteGroup", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("deleteGroup", "error: ${e.message}")
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun GroupEditingScreenBody(
    navController: NavController,
    groupId: String,
    viewModel: GroupEditingViewModel = viewModel()
) {
    val context = LocalContext.current
    val currentUserId = context.getUserSharedPreferences()?.getLoggedInUserId() ?: ""
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val group = databaseRepo.getGroupById(groupId)
    val coroutineScope = rememberCoroutineScope()

    val requestStatus by viewModel.requestStatus.observeAsState()

    val options = listOf(
        stringResource(R.string.open_group),
        stringResource(R.string.closed_group)
    )
    var selectedOption by remember {
        mutableStateOf(
            if (group.isPrivate) options[1] else options[0]
        )
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }
    val selectedOptionColor by animateColorAsState(
        if (selectedOption == stringResource(R.string.open_group)) LightGreen else LightRed
    )

    var groupTitle by rememberSaveable { mutableStateOf(group.title) }
    var groupDescription by rememberSaveable { mutableStateOf(group.description ?: "") }
    var isDescriptionAbsent by rememberSaveable { mutableStateOf(group.description.isNullOrEmpty()) }
    var isGroupEditable by rememberSaveable { mutableStateOf(group.isEditable) }
    var isGroupModified by rememberSaveable { mutableStateOf(false) }

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
                    Text(stringResource(R.string.group_editing).uppercase())
                }
            )
            UiTextField(
                value = groupTitle,
                onValueChange = {
                    groupTitle = it
                    isGroupModified = true
                },
                label = stringResource(R.string.title)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
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
                            .background(if (text == selectedOption) selectedOptionColor else Color.LightGray),
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
                        onClick = {
                            isDescriptionAbsent = !isDescriptionAbsent
                            isGroupModified = true
                        }
                    )
            ) {
                Checkbox(
                    checked = isDescriptionAbsent,
                    onCheckedChange = null
                )
                Text(
                    text = stringResource(R.string.no_description),
                    color = Color.Black
                )
            }
            AnimatedVisibility(!isDescriptionAbsent) {
                UiTextArea(
                    value = groupDescription,
                    onValueChange = {
                        groupDescription = it
                        isGroupModified = true
                    },
                    label = stringResource(R.string.description)
                )
            }
            if (currentUserId == group.creatorId) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                isGroupEditable = !isGroupEditable
                                isGroupModified = true
                            }
                        )
                ) {
                    Checkbox(
                        checked = isGroupEditable,
                        onCheckedChange = null
                    )
                    Text(
                        text = stringResource(R.string.editable_group),
                        color = Color.Black
                    )
                }
            }
        }

        UiBigButton(
            text = stringResource(R.string.save_changes),
            isEnabled = isGroupModified
                    && groupTitle.isNotBlank()
                    && (isDescriptionAbsent || !isDescriptionAbsent && groupDescription.isNotBlank()),
            onClick = {
                coroutineScope.launch {
                    group.apply {
                        title = groupTitle
                        description = if (!isDescriptionAbsent) groupDescription else null
                        lastModifiedUserId = currentUserId
                        isPrivate = selectedOption == context.getString(R.string.closed_group)
                        lastModifiedDate = getCurrentTimestamp()
                        isEditable = isGroupEditable
                    }
                    viewModel.updateGroup(group)
                    Toast.makeText(
                        context,
                        context.getString(
                            R.string.group_updated_successfully,
                            group.title
                        ),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack(
                        Screen.MainPagerScreen.route,
                        inclusive = false
                    )
                }
            }
        )

        if (currentUserId == group.creatorId) {
            UiBigButton(
                text = stringResource(R.string.delete),
                color = LightRed,
                isEnabled = true,
                onClick = {
                    coroutineScope.launch {
                        viewModel.deleteGroup(group.id)
                        Toast.makeText(
                            context,
                            context.getString(
                                R.string.group_deleted_successfully,
                                group.title
                            ),
                            Toast.LENGTH_SHORT
                        ).show()
                        navController.popBackStack(
                            Screen.MainPagerScreen.route,
                            inclusive = false
                        )
                    }
                }
            )
        }
    }
}