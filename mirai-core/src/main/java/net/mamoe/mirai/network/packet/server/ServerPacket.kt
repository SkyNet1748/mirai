package net.mamoe.mirai.network.packet.server

import net.mamoe.mirai.network.packet.Packet

import java.io.DataInputStream

/**
 * @author Him188moe @ Mirai Project
 */
abstract class ServerPacket(val input: DataInputStream) : Packet {

    abstract fun decode()

    companion object {

        fun ofByteArray(bytes: ByteArray): ServerPacket {

            val stream = DataInputStream(bytes.inputStream())

            stream.skipUntil(10)
            val idBytes = stream.readUntil(11)
            val id = idBytes.map { it.toString(16) }.joinToString("")

            return when (id) {
                "08 25 31 01" -> Server0825Packet(Server0825Packet.Type.TYPE_08_25_31_01, stream);
                "08 25 31 02" -> Server0825Packet(Server0825Packet.Type.TYPE_08_25_31_02, stream);

                else -> throw UnsupportedOperationException();
            }
        }
    }
}

fun DataInputStream.skipUntil(byte: Byte) {
    while (readByte() != byte);
}

fun DataInputStream.readUntil(byte: Byte): ByteArray {
    var buff = byteArrayOf()
    var b: Byte
    b = readByte()
    while (b != byte) {
        buff += b
        b = readByte()
    }
    return buff
}

fun DataInputStream.readIP(): String {
    var buff = "";
    for (i in 0..12) {//todo: check that
        buff += readByte().toInt();
    }
    return buff;
}
