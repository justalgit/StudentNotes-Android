package com.example.studentnotes.utils

import android.content.Context
import android.widget.Toast

fun showToast(
    context: Context,
    string: String
) {
    Toast.makeText(
        context,
        string,
        Toast.LENGTH_SHORT
    ).show()
}