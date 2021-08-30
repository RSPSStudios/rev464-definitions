package com.rsps.studios.definitions.loaders

import com.rsps.studios.DefinitionLoader
import com.rsps.studios.definitions.ItemDefinition
import com.rsps.studios.io.readString
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled

class ItemLoader : DefinitionLoader<ItemDefinition> {
    override fun load(definitionId: Int, data: ByteArray): ItemDefinition {
        val buffer = Unpooled.wrappedBuffer(data)
        val definition = ItemDefinition()
        while(true) {
            val opcode = buffer.readUnsignedByte().toInt()
            if(opcode == 0)
                break
            definition.decodeOpcode(opcode, buffer)
        }
        return definition
    }

    override fun ItemDefinition.decodeOpcode(opcode: Int, stream: ByteBuf) {
        if (opcode == 1) {
            inventoryModel = stream.readUnsignedShort()
        } else if (opcode == 2) {
            name = stream.readString()
        } else if (opcode.inv() == -5) {
            zoom2d = stream.readUnsignedShort()
        } else if (opcode == 5) {
            xan2d = stream.readUnsignedShort()
        } else if (opcode == 6) {
            yan2d = stream.readUnsignedShort()
        } else if (opcode == 7) {
            xOffset2d = stream.readUnsignedShort()
            if (xOffset2d.inv() < -32768)
                xOffset2d -= 65536
        } else if (opcode == 8) {
            yOffset2d = stream.readUnsignedShort()
            if (yOffset2d.inv() < -32768)
                yOffset2d -= 65536
        } else if (opcode == 11) {
            stackable = 1
        } else if (opcode == 12) {
            cost = stream.readInt()
        } else if (opcode.inv() == -17) {
            members = true
        } else if (opcode == 23) {
            maleModel0 = stream.readUnsignedShort()
            maleOffset = stream.readUnsignedByte().toInt()
        } else if (opcode == 24) {
            maleModel1 = stream.readUnsignedShort()
        } else if (opcode == 25) {
            femaleModel0 = stream.readUnsignedShort()
            femaleOffset = stream.readUnsignedByte().toInt()
        } else if (opcode == 26) {
            femaleModel1 = stream.readUnsignedShort()
        } else if (opcode in 30..34) {
            options[opcode - 30] = stream.readString()
            if (options[opcode + -30].equals("null"))
                options[opcode - 30] = null
        } else if (opcode in 35..39) {
            interfaceOptions[opcode - 35] = stream.readString()
        } else if (opcode == 40) {
            val i = stream.readUnsignedByte().toInt()
            colorFind = ShortArray(i)
            colorReplace = ShortArray(i)
            var i_4_ = 0
            while (i > i_4_) {
                colorFind!![i_4_] = stream.readUnsignedShort().toShort()
                colorReplace!![i_4_] = stream.readUnsignedShort().toShort()
                i_4_++
            }
        } else if (opcode == 78) {
            maleModel2 = stream.readUnsignedShort()
        } else if (opcode == 79) {
            femaleModel2 = stream.readUnsignedShort()
        } else if (opcode == 90) {
            maleHeadModel = stream.readUnsignedShort()
        } else if (opcode == 91) {
            femaleHeadModel = stream.readUnsignedShort()
        } else if (opcode == 92) {
            maleHeadModel2 = stream.readUnsignedShort()
        } else if (opcode == 93) {
            femaleHeadModel2 = stream.readUnsignedShort()
        } else if (opcode == 95) {
            zan2d = stream.readUnsignedShort()
        } else if (opcode == 97) {
            notedID = stream.readUnsignedShort()
        } else if (opcode == 98) {
            notedTemplate = stream.readUnsignedShort()
        } else if (opcode.inv() <= -101 && opcode < 110) {
            if (countObj == null) {
                countObj = IntArray(10)
                countCo = IntArray(10)
            }
            countObj!![opcode + -100] = stream.readUnsignedShort()
            countCo!![opcode - 100] = stream.readUnsignedShort()
        } else if (opcode == 110) {
            resizeX = stream.readUnsignedShort()
        } else if (opcode == 111) {
            resizeY = stream.readUnsignedShort()
        } else if (opcode == 112) {
            resizeZ = stream.readUnsignedShort()
        } else if (opcode == 113) {
            ambient = stream.readByte().toInt()
        } else if (opcode == 114) {
            contrast = stream.readByte() * 5
        } else if (opcode == 115) {
            team = stream.readUnsignedByte().toInt()
        }
    }
}