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
import com.example.studentnotes.data.datasources.database.StudentNotesDatabase
import com.example.studentnotes.data.entities.User
import com.example.studentnotes.data.repositories.DatabaseRepository
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiPasswordField
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Blue200
import com.example.studentnotes.ui.theme.Blue500
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.CURRENT_USER_PLACEHOLDER_ID
import com.example.studentnotes.utils.PREFERENCES_USER_NAME
import com.example.studentnotes.utils.PREFERENCES_USER_SURNAME
import com.example.studentnotes.utils.getSharedPreferences
import kotlinx.coroutines.launch
import java.util.*

@ExperimentalComposeUiApi
@Composable
fun WelcomeScreenBody(
    navController: NavController
) {

    val context = LocalContext.current
    val databaseRepo = DatabaseRepository(
        database = StudentNotesDatabase.getInstance(context.applicationContext)
    )
    val coroutineScope = rememberCoroutineScope()

    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.welcome_to_studentnotes),
            style = Typography.h5,
            color = Blue500,
            textAlign = TextAlign.Center,
            modifier = Modifier.align(Alignment.CenterHorizontally)
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
            //isEnabled = login.isNotBlank() && password.isNotBlank()
            isEnabled = true
        ) {
            val sharedPrefs = context.getSharedPreferences()
            sharedPrefs?.let {
                it
                    .edit()
                    .putString(PREFERENCES_USER_NAME, login)
                    .putString(PREFERENCES_USER_SURNAME, password)
                    .apply()
            }
            CURRENT_USER_PLACEHOLDER_ID = UUID.randomUUID().toString()
            coroutineScope.launch {
                databaseRepo.createInitUser(
                    User(
                        id = CURRENT_USER_PLACEHOLDER_ID,
                        name = "Антон",
                        surname = "Ларин"
                    )
                )
            }
            navController.navigate(Screen.MainPagerScreen.route)
        }
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = stringResource(R.string.get_access),
            color = Blue200,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable {
                    navController.navigate(Screen.GetAccessScreen.route)
                }
        )
    }
}