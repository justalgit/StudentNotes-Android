package com.example.studentnotes

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.example.studentnotes.ui.theme.StudentNotesTheme
import com.example.studentnotes.utils.PREFERENCES_USER_NAME
import com.example.studentnotes.utils.PREFERENCES_USER_SURNAME
import com.example.studentnotes.utils.getSharedPreferences

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentNotesApp()
        }
    }
}

@ExperimentalComposeUiApi
@ExperimentalMaterialApi
@Composable
fun StudentNotesApp() {
    val context = LocalContext.current
    val sharedPrefs = context.getSharedPreferences()
    val userName = sharedPrefs?.getString(PREFERENCES_USER_NAME, null)
    val userSurname = sharedPrefs?.getString(PREFERENCES_USER_SURNAME, null)
    lateinit var entryScreen: Screen
    if (userName == null || userSurname == null) {
        entryScreen = Screen.WelcomeScreen
        Toast.makeText(
            context,
            stringResource(
                R.string.log_in_using_getpass
            ),
            Toast.LENGTH_SHORT
        ).show()
    } else {
        entryScreen = Screen.MainPagerScreen
        Toast.makeText(
            context,
            stringResource(
                R.string.hello_user,
                userName,
                userSurname
            ),
            Toast.LENGTH_SHORT
        ).show()
    }

    StudentNotesTheme {
        Navigation(entryScreen)
    }
}