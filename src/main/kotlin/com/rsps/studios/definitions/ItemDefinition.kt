package com.rsps.studios.definitions

class ItemDefinition {
    var name = "null"

    var resizeX = 128
    var resizeY = 128
    var resizeZ = 128

    var xan2d = 0
    var yan2d = 0
    var zan2d = 0

    var cost = 1
    var isTradeable: Boolean = false
    var stackable = 0
    var inventoryModel: Int = 0
    var members = false

    var colorFind: ShortArray? = null
    var colorReplace: ShortArray? = null
    var textureFind: ShortArray? = null
    var textureReplace: ShortArray? = null

    var zoom2d = 2000
    var xOffset2d = 0
    var yOffset2d = 0

    var ambient: Int = 0
    var contrast: Int = 0

    var countCo: IntArray? = null
    var countObj: IntArray? = null

    var options = arrayOf(null, null, "Take", null, null)

    var interfaceOptions = arrayOf(null, null, null, null, "Drop")

    var maleModel0 = -1
    var maleModel1 = -1
    var maleModel2 = -1
    var maleOffset: Int = 0
    var maleHeadModel = -1
    var maleHeadModel2 = -1

    var femaleModel0 = -1
    var femaleModel1 = -1
    var femaleModel2 = -1
    var femaleOffset: Int = 0
    var femaleHeadModel = -1
    var femaleHeadModel2 = -1

    var notedID = -1
    var notedTemplate = -1

    var team: Int = 0

    var itemId = -1
}