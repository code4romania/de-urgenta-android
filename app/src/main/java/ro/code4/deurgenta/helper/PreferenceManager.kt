package ro.code4.deurgenta.helper

import android.content.SharedPreferences

const val PREFS_TOKEN = "PREFS_TOKEN"
const val ONBOARDING_COMPLETED = "ONBOARDING_COMPLETED"

fun SharedPreferences.getString(key: String): String? = getString(key, null)
fun SharedPreferences.getInt(key: String): Int = getInt(key, 0)
fun SharedPreferences.getLong(key: String): Long = getLong(key, 0)

fun SharedPreferences.putString(key: String, value: String?) {
    val editor = edit()
    editor.putString(key, value)
    editor.apply()
}

fun SharedPreferences.putInt(key: String, value: Int) {
    val editor = edit()
    editor.putInt(key, value)
    editor.apply()
}

fun SharedPreferences.putLong(key: String, value: Long) {
    val editor = edit()
    editor.putLong(key, value)
    editor.apply()
}

fun SharedPreferences.putBoolean(key: String, value: Boolean = true) {
    val editor = edit()
    editor.putBoolean(key, value)
    editor.apply()
}

fun SharedPreferences.getToken(): String? = getString(PREFS_TOKEN)
fun SharedPreferences.saveToken(token: String) = putString(PREFS_TOKEN, token)
fun SharedPreferences.deleteToken() = putString(PREFS_TOKEN, null)


fun SharedPreferences.hasCompletedOnboarding() = getBoolean(ONBOARDING_COMPLETED, false)
fun SharedPreferences.completedOnboarding() = putBoolean(ONBOARDING_COMPLETED)
