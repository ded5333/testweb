package com.example.mywebview.ui

import android.webkit.WebView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mywebview.R
import com.example.mywebview.data.model.HistoryEntity

class Router {

    fun openNextFragmentWithoutBackStack(mainActivity: MainActivity, fragment: Fragment) {
        var fragmentManager: FragmentManager = mainActivity.supportFragmentManager

        fragmentManager.beginTransaction()
            .replace(R.id.navContainer, fragment)
            .commit()
    }

}