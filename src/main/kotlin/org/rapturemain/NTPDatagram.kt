package org.rapturemain

import kotlin.experimental.and
import kotlin.math.pow

data class NTPDatagram (
    val li: Long,
    val vn: Long,
    val mode: Long,
    val poll: Long,
    val stratum: Long,
    val precision: Long,
    val rootDelay: Long,
    val rootDispersion: Long,
    val referenceId: Long,
    val refTimestamp: Long,
    val originTimestamp: Long,
    val receiveTimestamp: Long,
    val transmitTimestamp: Long
    ) {

    companion object {
        fun NTPtimeToUnix(time: Long): Long {
            val seconds70Years = (70 * 365L + 17)* 24 * 60 * 60
            val seconds = (time ushr 32) - seconds70Years
            val parts = time and 0xFFFFFFFFL
            val secondsParts = parts / 2.0.pow(32.0)
            return ((seconds + secondsParts) * 1000).toLong()
        }

        fun unixTimeToNTP(time: Long): Long {
            val seconds70Years = (70 * 365L + 17) * 24 * 60 * 60;
            val seconds = ((time / 1000) shl 32) + seconds70Years
            val parts = (time % 1000 / 1000.0 * 2.0.pow(32.0)).toLong()
            return seconds and parts
        }

        fun fromBytes(bytes: ByteArray): NTPDatagram {
            var ptr = 0
            return NTPDatagram(
                li = (bytes[ptr] ushr 6 and 0b11).toLong(),
                vn = (bytes[ptr] ushr 3 and 0b111).toLong(),
                mode = (bytes[ptr++] and 0b111).toLong(),

                poll = bytes[ptr++].toL(),

                stratum = bytes[ptr++].toL(),

                precision = bytes[ptr++].toL(),

                rootDelay = (bytes[ptr++].toL() shl 24) or
                        (bytes[ptr++].toL() shl 16) or
                        (bytes[ptr++].toL() shl 8) or
                        (bytes[ptr++].toL()),

                rootDispersion = (bytes[ptr++].toL() shl 24) or
                        (bytes[ptr++].toL() shl 16) or
                        (bytes[ptr++].toL() shl 8) or
                        (bytes[ptr++].toL()),

                referenceId = (bytes[ptr++].toL() shl 24) or
                        (bytes[ptr++].toL() shl 16) or
                        (bytes[ptr++].toL() shl 8) or
                        (bytes[ptr++].toL()),

                refTimestamp = (bytes[ptr++].toL() shl 56) or
                        (bytes[ptr++].toL() shl 48) or
                        (bytes[ptr++].toL() shl 40) or
                        (bytes[ptr++].toL() shl 32) or
                        (bytes[ptr++].toL() shl 24) or
                        (bytes[ptr++].toL() shl 16) or
                        (bytes[ptr++].toL() shl 8) or
                        (bytes[ptr++].toL()),

                originTimestamp = (bytes[ptr++].toL() shl 56) or
                        (bytes[ptr++].toL() shl 48) or
                        (bytes[ptr++].toL() shl 40) or
                        (bytes[ptr++].toL() shl 32) or
                        (bytes[ptr++].toL() shl 24) or
                        (bytes[ptr++].toL() shl 16) or
                        (bytes[ptr++].toL() shl 8) or
                        (bytes[ptr++].toL()),

                receiveTimestamp = (bytes[ptr++].toL() shl 56) or
                        (bytes[ptr++].toL() shl 48) or
                        (bytes[ptr++].toL() shl 40) or
                        (bytes[ptr++].toL() shl 32) or
                        (bytes[ptr++].toL() shl 24) or
                        (bytes[ptr++].toL() shl 16) or
                        (bytes[ptr++].toL() shl 8) or
                        (bytes[ptr++].toL()),

                transmitTimestamp = (bytes[ptr].toL() shl 56) or
                        (bytes[ptr + 1].toL() shl 48) or
                        (bytes[ptr + 2].toL() shl 40) or
                        (bytes[ptr + 3].toL() shl 32) or
                        (bytes[ptr + 4].toL() shl 24) or
                        (bytes[ptr + 5].toL() shl 16) or
                        (bytes[ptr + 6].toL() shl 8) or
                        (bytes[ptr + 7].toL())
            )
        }

        fun toBytes(ntpDatagram: NTPDatagram): ByteArray {
            val bytes = ByteArray(12 * 4)
            var ptr = 0
            with(ntpDatagram) {
                bytes[ptr++] = ((li shl 6) or (vn shl 3) or mode).toByte()

                bytes[ptr++] = poll.toByte()

                bytes[ptr++] = stratum.toByte()

                bytes[ptr++] = precision.toByte()

                bytes[ptr++] = (rootDelay ushr 24).toByte()
                bytes[ptr++] = (rootDelay ushr 16).toByte()
                bytes[ptr++] = (rootDelay ushr 8).toByte()
                bytes[ptr++] = (rootDelay and 0xFF).toByte()

                bytes[ptr++] = (rootDispersion ushr 24).toByte()
                bytes[ptr++] = (rootDispersion ushr 16).toByte()
                bytes[ptr++] = (rootDispersion ushr 8).toByte()
                bytes[ptr++] = (rootDispersion and 0xFF).toByte()

                bytes[ptr++] = (referenceId ushr 24).toByte()
                bytes[ptr++] = (referenceId ushr 16).toByte()
                bytes[ptr++] = (referenceId ushr 8).toByte()
                bytes[ptr++] = (referenceId and 0xFF).toByte()

                bytes[ptr++] = (refTimestamp ushr 56).toByte()
                bytes[ptr++] = (refTimestamp ushr 48).toByte()
                bytes[ptr++] = (refTimestamp ushr 40).toByte()
                bytes[ptr++] = (refTimestamp ushr 32).toByte()
                bytes[ptr++] = (refTimestamp ushr 24).toByte()
                bytes[ptr++] = (refTimestamp ushr 16).toByte()
                bytes[ptr++] = (refTimestamp ushr 8).toByte()
                bytes[ptr++] = (refTimestamp and 0xFF).toByte()

                bytes[ptr++] = (originTimestamp ushr 56).toByte()
                bytes[ptr++] = (originTimestamp ushr 48).toByte()
                bytes[ptr++] = (originTimestamp ushr 40).toByte()
                bytes[ptr++] = (originTimestamp ushr 32).toByte()
                bytes[ptr++] = (originTimestamp ushr 24).toByte()
                bytes[ptr++] = (originTimestamp ushr 16).toByte()
                bytes[ptr++] = (originTimestamp ushr 8).toByte()
                bytes[ptr++] = (originTimestamp and 0xFF).toByte()

                bytes[ptr++] = (receiveTimestamp ushr 56).toByte()
                bytes[ptr++] = (receiveTimestamp ushr 48).toByte()
                bytes[ptr++] = (receiveTimestamp ushr 40).toByte()
                bytes[ptr++] = (receiveTimestamp ushr 32).toByte()
                bytes[ptr++] = (receiveTimestamp ushr 24).toByte()
                bytes[ptr++] = (receiveTimestamp ushr 16).toByte()
                bytes[ptr++] = (receiveTimestamp ushr 8).toByte()
                bytes[ptr++] = (receiveTimestamp and 0xFF).toByte()

                bytes[ptr++] = (transmitTimestamp ushr 56).toByte()
                bytes[ptr++] = (transmitTimestamp ushr 48).toByte()
                bytes[ptr++] = (transmitTimestamp ushr 40).toByte()
                bytes[ptr++] = (transmitTimestamp ushr 32).toByte()
                bytes[ptr++] = (transmitTimestamp ushr 24).toByte()
                bytes[ptr++] = (transmitTimestamp ushr 16).toByte()
                bytes[ptr++] = (transmitTimestamp ushr 8).toByte()
                bytes[ptr] = (transmitTimestamp and 0xFF).toByte()
            }

            return bytes
        }
    }
}

private infix fun Byte.shl(i: Int): Byte = (this.toUInt() shl i).toByte()

private infix fun Byte.ushr(i: Int): Byte = (this.toUInt() shr i).toByte()

private infix fun Byte.and(i: Int): Byte = this and i.toByte()

fun Byte.toL(): Long {
    val sign = this and 0x80
    var l = this.toLong() and 0x0000000000007FL
    if (sign != 0.toByte()) {
        l = 0x80L or l
    }
    return l
}
