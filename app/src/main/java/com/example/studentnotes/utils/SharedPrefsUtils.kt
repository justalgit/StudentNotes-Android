package com.example.studentnotes.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.studentnotes.R

fun Context.getSharedPreferences(): SharedPreferences? {
    return getSharedPreferences(
        getString(R.string.shared_prefs_filename),
        MODE_PRIVATE
    )
}