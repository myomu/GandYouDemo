package com.gatheringandyou.gandyoudemo.shared

import android.content.Context
import android.content.SharedPreferences

class PreferenceManger(context: Context) {

    private val prefsFilename = "prefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    private val DEFAULT_VALUE_STRING = "Default"

    private val DEFAULT_VALUE_BOOLEAN = false

    private val DEFAULT_VALUE_INT = -1

    fun setString(key: String, value: String) {

        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String): String? {
        return prefs.getString(key, DEFAULT_VALUE_STRING)
    }

    fun setInt(key: String, value: Int) {
        //val editor = prefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String): Int {
        return prefs.getInt(key, DEFAULT_VALUE_INT)
    }

    fun setBoolean(key: String, value: Boolean) {
        //val editor = prefs.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String): Boolean {
        return prefs.getBoolean(key, DEFAULT_VALUE_BOOLEAN)
    }


    fun clearData() {
        editor.clear()
        editor.commit()
    }

}