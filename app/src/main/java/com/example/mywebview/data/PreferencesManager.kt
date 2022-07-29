package com.example.mywebview.data

import android.content.SharedPreferences

class PreferencesManager(var sharedPreferences: SharedPreferences) {

    var isRemember = false


    fun saveUserData(isAgree: Boolean) {

        sharedPreferences.edit().putBoolean("isAgree", isAgree).apply()
    }


    fun isSave(): Boolean {
        return sharedPreferences.getBoolean("isAgree", isRemember)
    }


}