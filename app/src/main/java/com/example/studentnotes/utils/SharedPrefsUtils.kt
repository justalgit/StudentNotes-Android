package com.example.studentnotes.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.example.studentnotes.R

private const val PREFERENCES_USER_ID = "PREFERENCES_USER_ID"
private const val PREFERENCES_USER_NAME = "PREFERENCES_USER_NAME"
private const val PREFERENCES_USER_SURNAME = "PREFERENCES_USER_SURNAME"

fun Context.getUserSharedPreferences(): SharedPreferences? {
    return getSharedPreferences(
        getString(R.string.shared_prefs_filename),
        MODE_PRIVATE
    )
}

fun SharedPreferences.getLoggedInUserId() = getString(PREFERENCES_USER_ID, null)

fun SharedPreferences.getLoggedInUserName() = getString(PREFERENCES_USER_NAME, null)

fun SharedPreferences.getLoggedInUserSurname() = getString(PREFERENCES_USER_SURNAME, null)

fun SharedPreferences.saveLoggedInUser(
    id: String?,
    name: String?,
    surname: String?
) {
    edit()
        .putString(PREFERENCES_USER_ID, id?.replace("-", ""))
        .putString(PREFERENCES_USER_NAME, name)
        .putString(PREFERENCES_USER_SURNAME, surname)
        .apply()
}

fun SharedPreferences.logOut() {
    edit()
        .remove(PREFERENCES_USER_NAME)
        .remove(PREFERENCES_USER_SURNAME)
        .apply()
}