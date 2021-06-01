package com.gatheringandyou.gandyoudemo.shared

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.gatheringandyou.gandyoudemo.bulletinboards.ExtensionActivity
import com.gatheringandyou.gandyoudemo.login.loginResponse
import retrofit2.Callback

class PreferenceManger(context: Context) {

    private val prefsFilename = "prefs"
    private val prefsKeyEdt = "myEditText"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    private val DEFAULT_VALUE_STRING = "Default"

    private val DEFAULT_VALUE_BOOLEAN = false

    private val DEFAULT_VALUE_INT = -1

    private val DEFAULT_VALUE_LONG = -1L

    private val DEFAULT_VALUE_FLOAT = -1f

//    var myEditText : String?
//        get() = prefs.getString(prefsKeyEdt, "")
//        set(value) = prefs.edit().putString(prefsKeyEdt, value).apply()

//    private fun getPreferences(context: Context): android.content.SharedPreferences? {
//        return context.getSharedPreferences(
//            PREFERENCES_NAME,
//            Context.MODE_PRIVATE
//        )
//    }

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