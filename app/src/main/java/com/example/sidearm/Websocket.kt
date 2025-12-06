package com.example.sidearm
import android.widget.Toast
import okhttp3.WebSocketListener
import okhttp3.WebSocket
import okio.ByteString


class MyWebSocketListener : WebSocketListener() {
    override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
        println("WebSocket opened!")
        is_connected.postValue(1);
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        println("Received text: $text")
    }

    override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
        println("Received bytes: $bytes")
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        println("WebSocket closing: $code / $reason")
        is_connected.postValue(-1);
        webSocket.close(1000, null)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
        println("WebSocket error: ${t.message}")
        }
    }