package com.rsps.studios.io

import io.netty.buffer.ByteBuf
import kotlin.text.toByteArray


fun ByteBuf.writeByte(value: Boolean) = writeByte(if (value) 1 else 0)

fun ByteBuf.writeByteAdd(value: Boolean) = writeByteAdd(if (value) 1 else 0)

fun ByteBuf.writeByteInverse(value: Boolean) = writeByte(if (value) 1 else 0)

fun ByteBuf.writeByteAdd(value: Int) = writeByte(value + 128)

fun ByteBuf.writeByteInverse(value: Int) = writeByte(-value)

fun ByteBuf.writeByteSubtract(value: Int) = writeByte(-value + 128)

fun ByteBuf.writeShortAdd(value: Int) {
    writeByte(value shr 8)
    writeByteAdd(value)
}

fun ByteBuf.writeShortAddLittle(value: Int) {
    writeByteAdd(value)
    writeByte(value shr 8)
}

fun ByteBuf.writeIntMiddle(value: Int) {
    writeByte(value shr 8)
    writeByte(value)
    writeByte(value shr 24)
    writeByte(value shr 16)
}

fun ByteBuf.writeIntInverse(value: Int) {
    writeByte(value shr 8)
    writeByte(value shr 24)
    writeByte(value shr 16)
    writeByteInverse(value)
}

fun ByteBuf.writeIntInverseMiddle(value: Int) {
    writeByte(value shr 16)
    writeByte(value shr 24)
    writeByte(value)
    writeByte(value shr 8)
}

fun ByteBuf.writeMedium(value: Int) {
    writeByte(value shr 16)
    writeByte(value shr 8)
    writeByte(value)
}

fun ByteBuf.writeSmart(value: Int) {
    if (value >= 128) {
        writeShort(value + 32768)
    } else {
        writeByte(value)
    }
}

fun ByteBuf.writeString(value: String?) {
    if (value != null) {
        writeBytes(value.toByteArray())
    }
    writeByte(0)
}

class BitAccessor {
    private var bitIndex = 0
    private val data = ByteArray(4096 * 2)

    fun writeBit(value: Boolean) = writeBits(1, if (value) 1 else 0)

    fun writeBits(count: Int, value: Int) {
        var numBits = count

        var byteIndex = bitIndex shr 3
        var bitOffset = 8 - (bitIndex and 7)
        bitIndex += numBits

        var tmp: Int
        var max: Int
        while (numBits > bitOffset) {
            tmp = data[byteIndex].toInt()
            max = BIT_MASKS[bitOffset]
            tmp = tmp and max.inv() or (value shr numBits - bitOffset and max)
            data[byteIndex++] = tmp.toByte()
            numBits -= bitOffset
            bitOffset = 8
        }

        tmp = data[byteIndex].toInt()
        max = BIT_MASKS[numBits]
        if (numBits == bitOffset) {
            tmp = tmp and max.inv() or (value and max)
        } else {
            tmp = tmp and (max shl bitOffset - numBits).inv()
            tmp = tmp or (value and max shl bitOffset - numBits)
        }
        data[byteIndex] = tmp.toByte()
    }

    suspend fun write(buffer: ByteBuf) {
        buffer.writeBytes(data, 0, (bitIndex + 7) / 8)
    }
}

suspend fun ByteBuf.bitAccess(block: BitAccessor.() -> Unit) {
    val accessor = BitAccessor()
    block.invoke(accessor)
    accessor.write(this)
}

fun ByteBuf.readString(): String {
    val sb = StringBuilder()
    var b: Int
    while (readableBytes() > 0) {
        b = readByte().toInt()
        if (b == 0) {
            break
        }
        sb.append(b.toChar())
    }
    return sb.toString()
}

fun ByteBuf.readBoolean(): Boolean = readByte().toInt() == 1

fun ByteBuf.readBooleanInverse() = readByteInverse() == 1

fun ByteBuf.readBooleanSubtract() = readByteSubtract() == 1

fun ByteBuf.readBooleanAdd() = readByteAdd() == 1

fun ByteBuf.readByteAdd(): Int = (readByte() - 128).toByte().toInt()

fun ByteBuf.readByteInverse(): Int = -readByte()

fun ByteBuf.readByteSubtract(): Int = (readByteInverse() + 128).toByte().toInt()

fun ByteBuf.readShortAdd(): Int = (readByte().toInt() shl 8) or readByteAdd()

fun ByteBuf.readShortAddLittle(): Int = ((readByte().toInt() - 128) and 0xff) or ((readByte().toInt() shl 8) and 0xff00)

fun ByteBuf.readUnsignedShortAdd(): Int = (readByte().toInt() shl 8) or ((readByte() - 128) and 0xff)

fun ByteBuf.readUnsignedShortLittle(): Int = readUnsignedByte().toInt() or (readUnsignedByte().toInt() shl 8)

fun ByteBuf.readUnsignedShortAddLittle(): Int = (readByte() - 128 and 0xff) + (readByte().toInt() shl 8 and 0xff00)

fun ByteBuf.readUnsignedIntMiddle(): Int = (readUnsignedByte().toInt() shl 8) or readUnsignedByte().toInt() or (readUnsignedByte().toInt() shl 24) or (readUnsignedByte().toInt() shl 16)

fun ByteBuf.readIntInverseMiddle(): Int = (readByte().toInt() shl 16) or (readByte().toInt() shl 24) or readUnsignedByte().toInt() or (readByte().toInt() shl 8)

fun ByteBuf.readSmart(): Int {
    val peek = readUnsignedByte().toInt()
    return if (peek < 128) {
        peek and 0xFF
    } else {
        (peek shl 8 or readUnsignedByte().toInt()) - 32768
    }
}

val BIT_MASKS = IntArray(32).apply {
    for (i in this.indices) {
        this[i] = (1 shl i) - 1
    }
}