package com.example.mywebview.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Base64
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.appsflyer.AppsFlyerLib
import com.example.mywebview.App
import com.example.mywebview.R
import com.example.mywebview.domain.NetworkStateManager
import com.example.mywebview.ui.screens.browser.BrowserFragment
import com.example.mywebview.ui.screens.nonetwork.NoNetworkFragment
import com.example.mywebview.ui.screens.start.PolicyFragment
import com.onesignal.OneSignal
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class MainActivity : AppCompatActivity() {
    var router = Router()
    var isAgree = false
    private val ONESIGNAL_APP_ID = "asdf"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        AppsFlyerLib.getInstance().init("<AF_DEV_KEY>", null, this);
        AppsFlyerLib.getInstance().start(this);

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);

        try {
            val info = packageManager.getPackageInfo(
                "com.facebook.samples.hellofacebook",
                PackageManager.GET_SIGNATURES)
            for (signature in info.signatures) {
                val md: MessageDigest = MessageDigest.getInstance("SHA")
                md.update(signature.toByteArray())
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {
        } catch (e: NoSuchAlgorithmException) {
        }

        isAgree = App.preferencesManager.isSave()

        val activeNetworkStateObserver: Observer<Boolean> =
            Observer { isConnected ->
                isAgree = App.preferencesManager.isSave()

                if (!isAgree) {
                    return@Observer
                }
                if (isConnected) {
                    Log.d("TAG", "onCreate:  internet")
                    var webViewFragment = BrowserFragment()
                    router.openNextFragmentWithoutBackStack(this, webViewFragment)
                } else {
                    Log.d("TAG", "onCreate:  NO internet")

                    var noInternetFragment = NoNetworkFragment()
                    router.openNextFragmentWithoutBackStack(this, noInternetFragment)
                }

            }


        NetworkStateManager.getNetworkConnectivityStatus()
            .observe(this, activeNetworkStateObserver)


        if (isAgree) {

            var isInternet = checkForInternet(this)
            checkInternetAndRealisationNavFrag(isInternet)
        } else {

            var startFragment = PolicyFragment()
            router.openNextFragmentWithoutBackStack(this, startFragment)
        }


    }


    fun checkInternetAndRealisationNavFrag(isInternet: Boolean) {
        if (isInternet) {
            var webViewFragment = BrowserFragment()
            router.openNextFragmentWithoutBackStack(this, webViewFragment)
        } else {
            var noInternetFragment = NoNetworkFragment()
            router.openNextFragmentWithoutBackStack(this, noInternetFragment)
        }
    }

    @SuppressLint("MissingPermission")
    fun checkForInternet(context: Context): Boolean {

        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }


}