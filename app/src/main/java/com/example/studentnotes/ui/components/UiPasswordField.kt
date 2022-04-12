package com.example.studentnotes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.studentnotes.ui.theme.Orange200

@Composable
fun UiPasswordField(
    password: String,
    isPasswordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onIconClick: () -> Unit
) {
    TextField(
        value = password,
        onValueChange = onPasswordChange,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Orange200,
            focusedIndicatorColor = Orange200,
            backgroundColor = Color.White
        ),
        label = { Text("Пароль") },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            val image =
                if (isPasswordVisible) Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff
            IconButton(onClick = onIconClick) {
                Icon(
                    imageVector  = image,
                    contentDescription = null
                )
            }
        },
        modifier = Modifier
            .fillMaxWidth()
    )
}