package com.example.mywebview.ui.screens.browser

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.*
import android.webkit.WebBackForwardList
import android.webkit.WebView
import androidx.fragment.app.Fragment
import com.example.mywebview.App
import com.example.mywebview.IOnBackPressed
import com.example.mywebview.R
import com.example.mywebview.data.model.HistoryItem
import com.example.mywebview.domain.BrowserClient
import com.example.mywebview.domain.HistoryManager

class BrowserFragment : Fragment(), IOnBackPressed {

    private lateinit var webView: WebView
    private val handler = Handler(Looper.getMainLooper())

    private val browserClient: BrowserClient by lazy {
        BrowserClient(
            historyManager = HistoryManager(App.database),
            browserBackstack = object : BrowserBackstack {
                override fun copyBackstack(): WebBackForwardList {
                    return webView.copyBackForwardList()
                }

                override fun restoreBackstack(history: List<HistoryItem>) {
                    if (history.isEmpty()) {
                        webView.loadUrl("https://www.mil.gov.ua/")
                    } else {
                        Log.e("DED", "restore history ${history.joinToString()}")
                        history.forEachIndexed { index, historyItem ->
                            handler.postDelayed({
                                webView.loadUrl(historyItem.url)
                            }, 1000L * index)
                        }
                    }
                }
            }
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webView = view.findViewById(R.id.webView)
        webView.webViewClient = browserClient
        browserClient.syncHistory()

        webView.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_DOWN) {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    } else {
                        requireActivity().finish()
                    }
                    return true
                }
                return false
            }
        })
    }

    override fun onBackPressed(): Boolean {
        if (webView.canGoBack()) {
            webView.goBack();
            return true;
        } else {
            return false;
        }
    }

}