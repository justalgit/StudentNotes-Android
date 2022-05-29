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
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.entities.Group
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
import java.util.*

class GroupCreationViewModel : ViewModel() {

    private val serverRepo = ServerRepository()

    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    fun createGroup(group: Group) {
        viewModelScope.launch {
            try {
                _requestStatus.value = ApiRequestStatus.LOADING
                serverRepo.createGroup(group)
                _requestStatus.value = ApiRequestStatus.DONE
                Log.d("createGroup", "success")
            }
            catch (e: Exception) {
                _requestStatus.value = when (e) {
                    is HttpException -> ApiRequestStatus.HTTP_ERROR
                    is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                    else -> ApiRequestStatus.UNKNOWN_ERROR
                }
                Log.d("createGroup", "error: ${e.message}")
            }
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun GroupCreationScreenBody(
    navController: NavController,
    viewModel: GroupCreationViewModel = viewModel()
) {

    val options = listOf(
        stringResource(R.string.open_group),
        stringResource(R.string.closed_group)
    )
    var selectedOption by remember {
        mutableStateOf(options[0])
    }
    val onSelectionChange = { text: String ->
        selectedOption = text
    }
    val selectedOptionColor by animateColorAsState(
        if (selectedOption == stringResource(R.string.open_group)) LightGreen else LightRed
    )

    var groupTitle by rememberSaveable { mutableStateOf("") }
    var groupDescription by rememberSaveable { mutableStateOf("") }
    var isDescriptionAbsent by rememberSaveable { mutableStateOf(false) }
    var isGroupEditable by rememberSaveable { mutableStateOf(true) }

    val context = LocalContext.current
    val sharedPrefs = context.getUserSharedPreferences()
    val coroutineScope = rememberCoroutineScope()
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
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.Top)
        ) {
            UiHeader(
                leftContent = {
                    UiBackButton(
                        onClick = { navController.popBackStack() }
                    )
                },
                rightRowContent = {
                    Text(stringResource(R.string.group_creation).uppercase())
                }
            )
            UiTextField(
                value = groupTitle,
                onValueChange = {
                    groupTitle = it
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
                        onClick = { isDescriptionAbsent = !isDescriptionAbsent }
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
                        onClick = { isGroupEditable = !isGroupEditable }
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

        UiBigButton(
            text = stringResource(R.string.create_group),
            isEnabled = groupTitle.isNotBlank() && (isDescriptionAbsent || !isDescriptionAbsent && groupDescription.isNotBlank()),
            onClick = {
                coroutineScope.launch {
                    viewModel.createGroup(
                        Group(
                            id = UUID.randomUUID().toString(),
                            title = groupTitle,
                            description = if (!isDescriptionAbsent) groupDescription else null,
                            creatorId = sharedPrefs?.getLoggedInUserId() ?: "",
                            lastModifiedDate = getCurrentTimestamp(),
                            lastModifiedUserId = sharedPrefs?.getLoggedInUserId() ?: "",
                            isPrivate = selectedOption == context.getString(R.string.closed_group),
                            isEditable = isGroupEditable
                        )
                    )
                }
            }
        )
    }
    if (requestStatus == ApiRequestStatus.DONE) {
        Toast.makeText(
            context,
            context.getString(
                R.string.group_created_successfully,
                selectedOption,
                groupTitle
            ),
            Toast.LENGTH_SHORT
        ).show()
        navController.popBackStack(
            Screen.MainPagerScreen.route,
            inclusive = false
        )
    }
}