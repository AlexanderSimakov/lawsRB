package com.team.lawsrb.basic

import android.util.Log
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress

object NetConnection {

    private const val TAG = "NetworkAvailable"
    fun isOnline(): Boolean {
        return try {
            val timeoutMs = 1500
            val socket = Socket()
            val socketAddr: SocketAddress = InetSocketAddress("8.8.8.8", 53)
            socket.connect(socketAddr, timeoutMs)
            socket.close()
            Log.i(TAG, "The device has internet access")
            true
        }
        catch (e: IOException) {
            Log.d(TAG, "No internet access")
            false
        }
    }
}

