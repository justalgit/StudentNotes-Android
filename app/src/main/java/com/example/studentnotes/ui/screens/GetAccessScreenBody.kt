package com.example.studentnotes.ui.screens

import android.util.Log
import android.view.ViewGroup
import android.webkit.ValueCallback
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.data.datasources.server.ApiRequestStatus
import com.example.studentnotes.data.datasources.server.json.UserToCreate
import com.example.studentnotes.data.repositories.ServerRepository
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.ui.theme.Blue200
import com.example.studentnotes.utils.showToast
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.util.*

class GetAccessViewModel : ViewModel() {

    private val serverRepo = ServerRepository()
    private val _requestStatus = MutableLiveData<ApiRequestStatus>()
    val requestStatus: LiveData<ApiRequestStatus>
        get() = _requestStatus

    suspend fun createUser(userToCreate: UserToCreate) {
        try {
            _requestStatus.value = ApiRequestStatus.LOADING
            serverRepo.createUser(userToCreate)
            _requestStatus.value = ApiRequestStatus.DONE
        }
        catch (e: Exception) {
            _requestStatus.value = when (e) {
                is HttpException -> ApiRequestStatus.HTTP_ERROR
                is SocketTimeoutException -> ApiRequestStatus.TIMEOUT_ERROR
                else -> ApiRequestStatus.UNKNOWN_ERROR
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun GetAccessScreenBody(
    navController: NavController,
    viewModel: GetAccessViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var isLoginAndPasswordShown by remember { mutableStateOf(false) }
    val maiGetpassUrl = "https://mai.ru/getpass/"
    var currentHtmlContent = ""

    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var surname by rememberSaveable { mutableStateOf("") }

    val requestStatus by viewModel.requestStatus.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .wrapContentHeight(),
        horizontalAlignment = Alignment.Start
    ) {
        UiHeader(
            modifier = Modifier.padding(horizontal = 12.dp),
            leftContent = {
                UiBackButton(
                    onClick = { navController.popBackStack() }
                )
            },
            rightRowContent = {
                Text(stringResource(R.string.getting_access).uppercase())
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            if (!isLoginAndPasswordShown) {
                AndroidView(factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        settings.javaScriptEnabled = true
                        webViewClient = object : WebViewClient() {
                            override fun onPageFinished(view: WebView?, url: String?) {
                                evaluateJavascript(
                                    "(function() { return ('<html>'+document.getElementsByTagName('main')[0].innerHTML+'</html>'); })();",
                                    object : ValueCallback<String?> {
                                        override fun onReceiveValue(html: String?) {
                                            Log.d("html", html ?: "no value")
                                            currentHtmlContent = html ?: ""
                                            with (currentHtmlContent) {
                                                if (contains("Имя пользователя") && contains("Ваш пароль")) {
                                                    val loginPattern = Regex("Имя пользователя: (.*?)003C")
                                                    val passwordPattern = Regex("Ваш пароль: (.*?)003C")
                                                    login = loginPattern.find(currentHtmlContent)?.value ?: ""
                                                    login = login.substring(
                                                        login.indexOf(":") + 2,
                                                        login.indexOf("\\")
                                                    )
                                                    password = passwordPattern.find(currentHtmlContent)?.value ?: ""
                                                    password = password.substring(
                                                        password.indexOf(":") + 2,
                                                        password.indexOf("\\")
                                                    )
                                                    isLoginAndPasswordShown = true
                                                }
                                            }
                                        }
                                    })
                                super.onPageFinished(view, url)
                            }
                        }
                        loadUrl(maiGetpassUrl)
                    }
                }, update = {})
            } else {
                if (requestStatus == ApiRequestStatus.LOADING) {
                    UiProgressDialog()
                }
                Column(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Text(
                        text = "Логин: $login, пароль: $password",
                        color = Blue200,
                        textAlign = TextAlign.Center
                    )

                    Spacer(Modifier.height(16.dp))

                    Text(
                        text = "Сохраните эти данные, чтобы войти в приложение после добавления пользователя",
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    UiTextField(
                        value = name,
                        onValueChange = {
                            name = it
                        },
                        label = stringResource(R.string.name)
                    )

                    Spacer(modifier = Modifier.size(12.dp))

                    UiTextField(
                        value = surname,
                        onValueChange = {
                            surname = it
                        },
                        label = stringResource(R.string.surname)
                    )

                    Spacer(modifier = Modifier.size(24.dp))

                    UiBigButton(
                        text = stringResource(R.string.sign_up),
                        isEnabled = name.isNotBlank() && surname.isNotBlank()
                    ) {
                        coroutineScope.launch {
                            viewModel.createUser(
                                UserToCreate(
                                    id = UUID.randomUUID().toString(),
                                    name = name.trim(),
                                    surname = surname.trim(),
                                    login = login,
                                    password = password
                                )
                            )
                            showToast(
                                context,
                                context.getString(R.string.user_successfully_added)
                            )
                            navController.popBackStack()
                        }
                    }
                }
            }
        }
    }
}
