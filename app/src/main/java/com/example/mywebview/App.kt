package com.example.mywebview

import android.app.Application
import androidx.room.Room
import com.example.mywebview.data.AppDataBase
import com.example.mywebview.data.PreferencesManager
import com.example.mywebview.domain.NetworkMonitoringUtil

class App : Application() {

    companion object {
        lateinit var preferencesManager: PreferencesManager
        lateinit var mNetworkMonitoringUtil: NetworkMonitoringUtil
        lateinit var database: AppDataBase


        var instance: App? = null

        const val APP_PREFERENCES = "remember"

    }

    override fun onCreate() {
        super.onCreate()

        if (instance == null) {
            instance = this
        }

        val sharedPreferences = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        preferencesManager = PreferencesManager(sharedPreferences)

        mNetworkMonitoringUtil = NetworkMonitoringUtil(this);
        // Check the network state before registering for the 'networkCallbackEvents'
        mNetworkMonitoringUtil.checkNetworkState();
        mNetworkMonitoringUtil.registerNetworkCallbackEvents();

        database = Room.databaseBuilder(this, AppDataBase::class.java, "DataBase.db")
            .fallbackToDestructiveMigration()
            .build()

//        mNetworkMonitoringUtil.networkMonitoringUtil(this)


    }

    fun getDatabase(): AppDataBase? {
        return database
    }

}