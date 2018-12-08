package mygame.editor.parser

import mygame.editor.views.CcNode
import mygame.editor.views.CcSprite
import org.json.JSONArray
import org.json.JSONObject


private const val NODES = "nodes"
private const val X = "x"
private const val Y = "y"
private const val NAME = "name"
private const val WIDTH = "width"
private const val HEIGHT = "height"
private const val ANGLE = "angle"
private const val IMAGE = "image"
private const val TYPE = "type"
private const val NODE = "node"
private const val SPRITE = "sprite"

fun createNodesFromString(json: String): List<CcNode> {

    val layers = arrayListOf<CcNode>()


    val jsonObject = JSONObject(json)
    val nodes = jsonObject.getJSONArray(NODES)
    for (i in 0 until nodes.length()) {
        val jsonNode = nodes.getJSONObject(i)
        val node = parseNode(jsonNode)
        layers.add(node)
    }

    return layers
}

fun createJsonFromNodes(nodes: List<CcNode>): String {
    val jsonObject = JSONObject()
    val jsonArray = JSONArray()
    jsonObject.put(NODES, jsonArray)
    for (ccNode in nodes) {
        val jsonNode = createJsonNode(ccNode)
        jsonArray.put(jsonNode)
    }
    return jsonObject.toString()
}

private fun parseNode(jsonObject: JSONObject): CcNode {
    val type = jsonObject.getString(TYPE)
    return when (type) {
        SPRITE -> createSprite(jsonObject)
        else -> createNode(jsonObject)
    }
}

private fun createNode(jsonObject: JSONObject): CcNode {
    val node = CcNode()
    fillNode(node,jsonObject)
    return node
}

private fun createSprite(jsonObject: JSONObject): CcSprite {
    val image = jsonObject.getString(IMAGE)
    val sprite = CcSprite(image)
    fillNode(sprite,jsonObject)
    return sprite
}

private fun fillNode(node: CcNode,jsonObject:JSONObject){
    val x = jsonObject.getDouble(X)
    val y = jsonObject.getDouble(Y)
    val width = jsonObject.getDouble(WIDTH)
    val height = jsonObject.getDouble(HEIGHT)
    val name = jsonObject.getString(NAME)
    val angle = jsonObject.getDouble(ANGLE)

    node.x.set(x)
    node.y.set(y)
    node.width.set(width)
    node.height.set(height)
    node.name.set(name)
    node.angle.set(angle)


    if (jsonObject.has(NODES)) {
        val nodes = jsonObject.getJSONArray(NODES)
        for (index in 0 until nodes.length()) {
            val jsonNode = nodes.getJSONObject(index)
            val parsedNode = parseNode(jsonNode)
            node.children.add(parsedNode)
        }
    }
}

private fun createJsonNode(node: CcNode): JSONObject? {

    val json: JSONObject
    if (node is CcSprite) {
        json = createJsonSprite(node)
    } else {
        json = createJsonDefaultNode(node)

    }


    val children = JSONArray()
    for (child in node.children) {
        val jsonChild = createJsonNode(child)
        children.put(jsonChild)
    }
    json.put(NODES, children)
    return json


}

private fun createJsonSprite(node: CcSprite): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put(TYPE, SPRITE)
    jsonObject.put(X, node.x.doubleValue())
    jsonObject.put(Y, node.y.doubleValue())
    jsonObject.put(NAME, node.name.value)
    jsonObject.put(WIDTH, node.width.doubleValue())
    jsonObject.put(HEIGHT, node.height.doubleValue())
    jsonObject.put(ANGLE, node.angle.doubleValue())
    jsonObject.put(IMAGE, node.imagePath)
    return jsonObject
}

private fun createJsonDefaultNode(node: CcNode): JSONObject {
    val jsonObject = JSONObject()
    jsonObject.put(TYPE, NODE)
    jsonObject.put(X, node.x.doubleValue())
    jsonObject.put(Y, node.y.doubleValue())
    jsonObject.put(NAME, node.name.value)
    jsonObject.put(WIDTH, node.width.doubleValue())
    jsonObject.put(HEIGHT, node.height.doubleValue())
    jsonObject.put(ANGLE, node.angle.doubleValue())
    return jsonObject
}

