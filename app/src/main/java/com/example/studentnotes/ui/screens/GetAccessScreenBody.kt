package com.example.studentnotes.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
            text = "MAI Getpass",
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
            label = "ФИО"
        )
        Spacer(modifier = Modifier.size(12.dp))
        UiTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            label = "Номер телефона"
        )
        Spacer(modifier = Modifier.size(24.dp))
        UiBigButton(
            text = "Продолжить",
            onClick = {
                if (fullName.isEmpty() || phoneNumber.isEmpty()) {
                    Toast.makeText(context, "Необходимо заполнить поля!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Данные отправлены", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            }
        )
    }
}