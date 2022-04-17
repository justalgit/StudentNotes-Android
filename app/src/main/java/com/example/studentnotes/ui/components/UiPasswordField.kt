package com.example.studentnotes.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.studentnotes.R
import com.example.studentnotes.ui.theme.Orange200

@ExperimentalComposeUiApi
@Composable
fun UiPasswordField(
    password: String,
    isPasswordVisible: Boolean,
    onPasswordChange: (String) -> Unit,
    onIconClick: () -> Unit
) {

    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = password,
        onValueChange = onPasswordChange,
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            cursorColor = Orange200,
            focusedIndicatorColor = Orange200,
            backgroundColor = Color.White
        ),
        label = { Text(stringResource(R.string.password)) },
        singleLine = true,
        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        keyboardActions = KeyboardActions(
            onNext = { focusManager.clearFocus() }
        ),
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
            .onPreviewKeyEvent {
                if (it.key == Key.Tab){
                    focusManager.moveFocus(FocusDirection.Down)
                    true
                } else {
                    false
                }
            },
    )
}