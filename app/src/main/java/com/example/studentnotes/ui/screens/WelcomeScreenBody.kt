package com.example.studentnotes.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiPasswordField
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Blue200
import com.example.studentnotes.ui.theme.Blue700
import com.example.studentnotes.ui.theme.Typography

@ExperimentalComposeUiApi
@Composable
fun WelcomeScreenBody(
    navController: NavController
) {

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
            color = Blue700,
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
            isEnabled = login.isNotBlank() && password.isNotBlank()
        ) {
            navController.navigate(Screen.MainPagerScreen.withArgs(login))
        }
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = stringResource(R.string.get_access),
            color = Blue200,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { navController.navigate(Screen.GetAccessScreen.route) }
        )
    }
}