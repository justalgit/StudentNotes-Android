package com.example.studentnotes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiProgressDialog
import com.example.studentnotes.ui.components.UiPasswordField
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Blue200
import com.example.studentnotes.ui.theme.Blue500
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import java.lang.Exception
import java.net.SocketTimeoutException

@ExperimentalComposeUiApi
@Composable
fun WelcomeScreenBody(
    navController: NavController
) {

    val context = LocalContext.current
    val serverRepo = ServerRepository()
    val coroutineScope = rememberCoroutineScope()
    var requestStatus by rememberSaveable { mutableStateOf(ApiRequestStatus.WAITING_TO_SEND) }
    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    if (requestStatus == ApiRequestStatus.LOADING) {
        UiProgressDialog()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.welcome_to_studentnotes),
                    style = Typography.h5,
                    color = Blue500,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.size(24.dp))

                UiTextField(
                    value = login,
                    onValueChange = {
                        login = it
                    },
                    label = stringResource(R.string.login)
                )

                Spacer(modifier = Modifier.size(12.dp))

                UiPasswordField(
                    password = password,
                    onPasswordChange = {
                        password = it
                    },
                    onIconClick = {
                        isPasswordVisible = !isPasswordVisible
                    },
                    isPasswordVisible = isPasswordVisible
                )

                Spacer(modifier = Modifier.size(24.dp))

                UiBigButton(
                    text = stringResource(R.string.sign_in),
                    isEnabled = login.isNotBlank() && password.isNotBlank()
                ) {
                    val sharedPrefs = context.getSharedPreferences()
                    coroutineScope.launch {
                        try {
                            requestStatus = ApiRequestStatus.LOADING
                            val loginResponse = serverRepo.login(
                                login,
                                password
                            )
                            sharedPrefs?.let {
                                it.saveLoggedInUser(
                                    loginResponse.id,
                                    loginResponse.name,
                                    loginResponse.surname
                                )
                            }
                            requestStatus = ApiRequestStatus.DONE
                            navController.navigate(Screen.MainPagerScreen.route)
                            showToast(
                                context,
                                context.getString(
                                    R.string.hello_user,
                                    loginResponse.name,
                                    loginResponse.surname
                                )
                            )
                        }
                        catch (e: Exception) {
                            when (e) {
                                is HttpException -> {
                                    showToast(
                                        context,
                                        context.getString(R.string.invalid_login_or_password)
                                    )
                                }
                                is SocketTimeoutException -> {
                                    showToast(
                                        context,
                                        context.getString(R.string.timeout_error)
                                    )
                                }
                                else -> {
                                    showToast(
                                        context,
                                        context.getString(R.string.unknown_error)
                                    )
                                }
                            }
                            requestStatus = ApiRequestStatus.ERROR
                        }
                    }
                }

                Spacer(modifier = Modifier.size(24.dp))

                Text(
                    text = stringResource(R.string.get_access),
                    color = Blue200,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.GetAccessScreen.route)
                        }
                )
            }
        }
    }
}