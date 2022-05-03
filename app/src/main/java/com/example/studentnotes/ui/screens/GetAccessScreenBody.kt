package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.ui.components.*
import com.example.studentnotes.ui.theme.Blue500
import com.example.studentnotes.ui.theme.Typography

@ExperimentalComposeUiApi
@Composable
fun GetAccessScreenBody(
    navController: NavController
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .wrapContentHeight()
            .padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.Start
    ) {
        UiHeader(
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
            Text(
                text = stringResource(R.string.mai_getpass),
                style = Typography.h5,
                color = Blue500,
                textAlign = TextAlign.Center,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.size(24.dp))
            UiTextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                },
                label = stringResource(R.string.full_name)
            )
            Spacer(modifier = Modifier.size(12.dp))
            UiPhoneField(
                value = phoneNumber,
                onValueChange = {
                    phoneNumber = it
                },
                label = stringResource(R.string.phone_number)
            )
            Spacer(modifier = Modifier.size(24.dp))
            UiBigButton(
                text = stringResource(R.string.continue_string),
                isEnabled = fullName.isNotBlank() && phoneNumber.isNotBlank(),
                onClick = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.data_was_sent),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
            )
        }
    }
}