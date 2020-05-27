package com.abdoul.smsreader

interface MessageListener {

    fun onMessageReceived(message: String)
}