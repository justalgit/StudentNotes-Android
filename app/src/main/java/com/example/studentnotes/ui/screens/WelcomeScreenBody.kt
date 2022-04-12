package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.Screen
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiPasswordField
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Blue200
import com.example.studentnotes.ui.theme.Blue700
import com.example.studentnotes.ui.theme.Typography

@Composable
fun WelcomeScreenBody(
    navController: NavController
) {

    var login by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Добро пожаловать в StudentNotes!",
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
            label = "Логин"
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
            text = "Войти",
            onClick = {
                if (login.isEmpty() || password.isEmpty())
                    Toast.makeText(context, "Необходимо заполнить поля!", Toast.LENGTH_SHORT).show()
                else
                    navController.navigate(Screen.MainPagerScreen.withArgs(login))
            }
        )
        Spacer(modifier = Modifier.size(24.dp))
        Text(
            text = "Получить доступ",
            color = Blue200,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .clickable { navController.navigate(Screen.GetAccessScreen.route) }
        )
    }
}