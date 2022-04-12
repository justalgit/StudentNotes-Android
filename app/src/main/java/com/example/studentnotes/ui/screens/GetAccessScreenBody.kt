package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.studentnotes.R
import com.example.studentnotes.ui.components.UiBigButton
import com.example.studentnotes.ui.components.UiTextField
import com.example.studentnotes.ui.theme.Blue700
import com.example.studentnotes.ui.theme.Typography

@Composable
fun GetAccessScreenBody(
    navController: NavController
) {
    var fullName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.mai_getpass),
            style = Typography.h5,
            color = Blue700,
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
        UiTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            label = stringResource(R.string.phone_number)
        )
        Spacer(modifier = Modifier.size(24.dp))
        UiBigButton(
            text = stringResource(R.string.continue_string),
            onClick = {
                if (fullName.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(context, context.getString(R.string.need_to_fill_all_fields), Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, context.getString(R.string.data_was_sent), Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        )
    }
}