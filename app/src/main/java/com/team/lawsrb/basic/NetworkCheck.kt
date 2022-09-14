package com.team.lawsrb.basic

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

class NetworkCheck(context: Context) {
    private val TAG = "NetworkCheck"

    private val connectivityManager = getSystemService(context, ConnectivityManager::class.java) as ConnectivityManager
    private val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        // network is available for use
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            _isAvailable = checkInternetConnection()
            Log.d(TAG, "$_isAvailable")
            Log.d(TAG, "Network turned on")
        }

        // Network capabilities have changed for the network
        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            super.onCapabilitiesChanged(network, networkCapabilities)
            val isChanges = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED)
            Log.d(TAG, "$isChanges")
        }

        // lost network connection
        override fun onLost(network: Network) {
            super.onLost(network)
            _isAvailable = false
            Log.d(TAG, "$_isAvailable")
            Log.d(TAG, "Network turned off")
        }
    }

    fun subscribeForUpdates() {
        try {
            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        }
        catch (e: IOException) {
            Log.e(TAG, "${e.message}")
        }
    }

    fun unsubscribeForUpdates() {
        try {
            connectivityManager.unregisterNetworkCallback(networkCallback)
        }
        catch (e: IOException) {
            Log.e(TAG, "${e.message}")
        }
    }

    private fun checkInternetConnection(): Boolean {
        return try {
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            socket.connect(socketAddr, timeoutMs)
            socket.close()
            true
        }
        catch (e: IOException) {
            false
        }
    }

    companion object {
        private var _isAvailable: Boolean = false

        val isAvailable get() = _isAvailable
        val isNotAvailable get() = !_isAvailable
    }
}

