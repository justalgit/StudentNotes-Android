package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.Screen
import com.example.studentnotes.ui.components.UiHeader
import com.example.studentnotes.ui.theme.Typography
import com.example.studentnotes.utils.getLoggedInUserName
import com.example.studentnotes.utils.getLoggedInUserSurname
import com.example.studentnotes.utils.getSharedPreferences
import com.example.studentnotes.utils.logOut

@Composable
fun SettingsScreenBody(
    navController: NavController
) {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences()
    val userName = sharedPrefs?.getLoggedInUserName() ?: ""
    val userSurname = sharedPrefs?.getLoggedInUserSurname() ?: ""
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        UiHeader(
            leftContent = {
                Text(
                    text = stringResource(R.string.settings),
                    style = Typography.h5,
                    color = Color.Black,
                    modifier = Modifier.padding(all = 12.dp)
                )
            }
        )

        Text(
            text = "$userName $userSurname",
            style = Typography.body1,
            color = Color.Gray,
            modifier = Modifier.padding(all = 12.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = rememberRipple(bounded = true),
                    onClick = {
                        val sharedPrefs = context.getSharedPreferences()
                        sharedPrefs?.logOut()
                        navController.navigate(Screen.WelcomeScreen.route) {
                            popUpTo(0)
                        }
                    }
                )
        ) {
            Icon(
                modifier = Modifier.padding(start = 12.dp),
                painter = painterResource(R.drawable.ic_logout_24),
                contentDescription = null,
                tint = Color.Black
            )

            Text(
                text = stringResource(R.string.logout_from_account),
                color = Color.Black,
                style = Typography.body1
            )
        }
    }
}