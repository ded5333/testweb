package com.example.mywebview.domain

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest


class NetworkMonitoringUtil(context: Context) : ConnectivityManager.NetworkCallback() {
    // private var mNetworkRequest: NetworkRequest? = null


    private var mNetworkRequest: NetworkRequest = NetworkRequest.Builder()
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private var mConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager



    override fun onAvailable(network: Network) {
        super.onAvailable(network)
        NetworkStateManager.setNetworkConnectivityStatus(true);

    }

    override fun onLost(network: Network) {
        super.onLost(network)
        NetworkStateManager.setNetworkConnectivityStatus(false);

    }

    fun registerNetworkCallbackEvents() {
        mConnectivityManager.registerNetworkCallback(mNetworkRequest, this)
    }

    fun checkNetworkState() {
        try {
            val networkInfo = mConnectivityManager.activeNetworkInfo
            // Set the initial value for the live-data
            NetworkStateManager.setNetworkConnectivityStatus(networkInfo != null
                    && networkInfo.isConnected)
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }
}