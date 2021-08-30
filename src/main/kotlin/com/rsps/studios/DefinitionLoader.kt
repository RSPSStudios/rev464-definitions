package com.rsps.studios

import io.netty.buffer.ByteBuf

interface DefinitionLoader<D> {

    fun load(definitionId: Int, data: ByteArray) : D

    fun D.decodeOpcode(opcode: Int, stream: ByteBuf)

}