package com.example.mywebview.domain

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.mywebview.data.model.HistoryItem
import com.example.mywebview.ui.screens.browser.BrowserBackstack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class BrowserClient(
    private val historyManager: HistoryManager,
    private val browserBackstack: BrowserBackstack
) : WebViewClient() {

    private val scope = CoroutineScope(Dispatchers.Default)
    private val mutex = Mutex()


    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Log.e("DED", "onPageStarted $url")
        scope.launch {
            saveBackstack()
        }
    }


    private suspend fun saveBackstack() = mutex.withLock {
        val backstack = withContext(Dispatchers.Main) {
            browserBackstack.copyBackstack()
        }
        Log.e("DED", "backstack size ${backstack.size}")
        val currentIndex = backstack.currentIndex

        val history = ArrayList<HistoryItem>()

        for (index in 0..currentIndex) {
            val item = backstack.getItemAtIndex(index)
            history.add(HistoryItem(item.url, index))
        }

        historyManager.invalidate(history)
    }

    fun syncHistory() {
        scope.launch {
            mutex.withLock {
                val history = historyManager.loadHistory()
                withContext(Dispatchers.Main) {
                    browserBackstack.restoreBackstack(history)
                }
            }
        }
    }
}