package com.rinoindraw.storybismillah.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREF_NAME = "Session"
        private const val PRIVATE_MODE = 0
        private const val KEY_AUTH_TOKEN = "auth_token"
    }

    fun saveAuthToken(token: String) {
        val editor = pref.edit()
        editor.putString(KEY_AUTH_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return pref.getString(KEY_AUTH_TOKEN, null)
    }

    fun clearAuthToken() {
        val editor = pref.edit()
        editor.remove(KEY_AUTH_TOKEN)
        editor.apply()
    }

    fun clearPreferences() {
        val editor = pref.edit()
        editor.clear().apply()
    }

    fun setStringPreference(prefKey: String, value: String) {
        val editor = pref.edit()
        editor.putString(prefKey, value)
        editor.apply()
    }

    fun setBooleanPreference(prefKey: String, value: Boolean) {
        val editor = pref.edit()
        editor.putBoolean(prefKey, value)
        editor.apply()
    }

    val getUserName = pref.getString(ConstVal.KEY_USER_NAME, "")
    val getEmail = pref.getString(ConstVal.KEY_EMAIL, "")
    val isLogin = pref.getBoolean(ConstVal.KEY_IS_LOGIN, false)

}