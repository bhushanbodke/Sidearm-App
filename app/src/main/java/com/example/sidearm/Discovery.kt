package com.example.sidearm
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.time.LocalTime

fun receiveUdpBroadcast() {
    Thread {
        val port = 5000
        val socket = DatagramSocket(port)
        val buffer = ByteArray(1024)

        val packet = DatagramPacket(buffer, buffer.size)
        socket.receive(packet)
        val receivedMessage = String(packet.data, 0, packet.length)
        AvailableDevice.addDevice(Device( receivedMessage.split(",")[1],  receivedMessage.split(",")[0]))
    }.start()
}