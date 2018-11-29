package mygame.editor.parser

import mygame.editor.repository.NodeRepository
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
private const val SPRITE = "sprite"
private const val IMAGE = "image"
private const val TYPE = "type"

fun createNodesFromSting(repository: NodeRepository, json: String) {
    repository.nodes.clear()


    val jsonObject = JSONObject(json)
    val nodes = jsonObject.getJSONArray(NODES)
    for (i in 0 until nodes.length()) {
        val jsonNode = nodes.getJSONObject(i)
        val type = jsonNode.getString(TYPE)
        when (type) {
            SPRITE -> {
                val sprite = createSprite(jsonNode)
                repository.save(sprite)
            }
            else -> print("do nothing")
        }
    }


}

fun createSprite(jsonObject: JSONObject): CcSprite {
    val image = jsonObject.getString(IMAGE)
    val x = jsonObject.getDouble(X)
    val y = jsonObject.getDouble(Y)
    val width = jsonObject.getDouble(WIDTH)
    val height = jsonObject.getDouble(HEIGHT)
    val name = jsonObject.getString(NAME)
    val angle = jsonObject.getDouble(ANGLE)
    val sprite = CcSprite(image, width, height)
    if (jsonObject.has("nodes")) {
        val nodes = jsonObject.getJSONArray("nodes")
        for ( index in 0 until nodes.length()){
            val jsonNode = nodes.getJSONObject(index)
            val parsedNode = createSprite(jsonNode)
            sprite.children.add(parsedNode)
        }
    }
    sprite.setX(x)
    sprite.setY(y)
    sprite.setAngle(angle)
    sprite.setName(name)
    return sprite
}

fun createJsonFromNodes(repository: NodeRepository): String {
    val jsonObject = JSONObject()
    val nodes = JSONArray()
    jsonObject.put(NODES, nodes)
    for (ccNode in repository.nodes) {

        val jsonNode = createJsonNode(ccNode)

        nodes.put(jsonNode)
    }
    return jsonObject.toString()
}

fun createJsonNode(node: CcNode): JSONObject? {

    if (node is CcSprite) {
        val json = createJsonSprite(node)
        val children = JSONArray()
        for (child in node.children) {
            val jsonChild = createJsonNode(child)
            children.put(jsonChild)
        }
        json.put("nodes",children)
        return json
    } else {
        return null
    }
}

fun createJsonSprite(node: CcSprite): JSONObject {
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

