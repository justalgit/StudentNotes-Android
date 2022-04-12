package com.example.studentnotes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import com.example.studentnotes.ui.theme.StudentNotesTheme
import com.google.accompanist.pager.ExperimentalPagerApi

@ExperimentalPagerApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StudentNotesApp()
        }
    }
}

@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun StudentNotesApp() {
    StudentNotesTheme {
        Navigation()
    }
}