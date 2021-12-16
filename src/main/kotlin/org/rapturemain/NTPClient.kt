package org.rapturemain

import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.util.concurrent.TimeUnit

private const val PORT = 123

fun main(args: Array<String>) {
    val socket = DatagramSocket(123).apply {
        soTimeout = TimeUnit.SECONDS.toMillis(60).toInt()
    }

    var ntpDatagram = NTPDatagram(
        li = 0,
        vn = 4,
        mode = 3,
        poll = 0,
        stratum = 0,
        precision = 0,
        rootDelay = 0,
        rootDispersion = 0,
        referenceId = 0,
        refTimestamp = 0,
        originTimestamp = 0,
        receiveTimestamp = 0,
        transmitTimestamp = 0
    )

    val bytes = NTPDatagram.toBytes(ntpDatagram)

    var sentTimestamp = System.currentTimeMillis()
    while (true) {
        socket.send(
            DatagramPacket(
                bytes,
                48,
                InetAddress.getByName("0.europe.pool.ntp.org"),
                PORT
            )
        )

        val datagram = DatagramPacket(bytes, 48)
        try {
            socket.receive(datagram)
            sentTimestamp = System.currentTimeMillis() - sentTimestamp
            ntpDatagram = NTPDatagram.fromBytes(datagram.data)
        } catch (e: Exception) {
            continue
        }

        break
    }

    val sentTime = NTPDatagram.NTPtimeToUnix(ntpDatagram.transmitTimestamp)
    val receivedTime = NTPDatagram.NTPtimeToUnix(ntpDatagram.receiveTimestamp)
    val correction = sentTimestamp - (sentTime - receivedTime)

    println(
        LocalDateTime.ofInstant(
            Calendar.getInstance().apply {
                timeInMillis = sentTime - correction / 2
            }.toInstant(), ZoneId.systemDefault()
        )
    )
}

