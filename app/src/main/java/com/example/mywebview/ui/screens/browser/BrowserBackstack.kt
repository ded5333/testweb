package com.example.mywebview.ui.screens.browser

import android.webkit.WebBackForwardList
import com.example.mywebview.data.model.HistoryItem

interface BrowserBackstack {
    fun copyBackstack(): WebBackForwardList
    fun restoreBackstack(history: List<HistoryItem>)
}