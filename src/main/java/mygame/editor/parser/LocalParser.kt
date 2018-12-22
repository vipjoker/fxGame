package mygame.editor.parser

import com.badlogic.gdx.math.Vector2
import mygame.editor.model.box2d.B2Body
import mygame.editor.model.box2d.B2Fixture
import mygame.editor.model.box2d.B2FixtureType
import mygame.editor.model.box2d.B2Type
import mygame.editor.views.*
import org.json.JSONArray
import org.json.JSONObject


private const val NODES = "nodes"
private const val ID = "id"
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
private const val PHYSICS = "physics"
private const val FIXTURES = "fixtures"
private const val RESTITUTION = "restitution"
private const val DENSITY = "density"
private const val FRICTION = "friction"
private const val POINTS = "points"

private const val RECT = "rect"
private const val CIRCLE = "circle"
private const val CHAIN = "chain"
private const val EDGE = "edge"
private const val POLYGON = "polygon"

private const val DYNAMIC = "dynamic"
private const val KINEMATIC = "kinematic"
private const val STATIC = "static"


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
    fillNode(node, jsonObject)
    return node
}

private fun createSprite(jsonObject: JSONObject): CcSprite {
    val image = jsonObject.getString(IMAGE)
    val sprite = CcSprite(image)
    fillNode(sprite, jsonObject)
    return sprite
}

private fun fillNode(node: CcNode, jsonObject: JSONObject) {
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
    if (jsonObject.has(PHYSICS)) {

        val physicsObject = jsonObject.getJSONObject(PHYSICS)
        val physicsType = physicsObject.getString(TYPE)
        val bodyType = parseBodyType(physicsType)
        val body = B2Body(bodyType)

        val fixturesArray = physicsObject.getJSONArray(FIXTURES)
        for (index in 0 until fixturesArray.length()) {
            val fixtureJson = fixturesArray.getJSONObject(index)
            val fixtureType = fixtureJson.getString(TYPE)


            val fixtureTypeEnum = parseFixtureType(fixtureType)


            val friction = fixtureJson.getFloat(FRICTION)
            val restitution = fixtureJson.getFloat(RESTITUTION)
            val density = fixtureJson.getFloat(DENSITY)

            val pointsJsonArray = fixtureJson.getJSONArray(POINTS)

            val list = arrayListOf<Vector2>()
            for (pointIndex in 0 until pointsJsonArray.length()) {
                val jsonPoint = pointsJsonArray.getJSONObject(pointIndex)
                val pointX = jsonPoint.getFloat(X)
                val pointY = jsonPoint.getFloat(Y)
                list.add(Vector2(pointX, pointY))

            }
            val fixture = B2Fixture(fixtureTypeEnum, list)
            fixture.density = density
            fixture.restitution = restitution
            fixture.friction = friction

            body.addFixture(fixture)
        }
        node.editBody = CcEditBodyNode(body)

    }

    if (jsonObject.has(NODES)) {
        val nodes = jsonObject.getJSONArray(NODES)
        for (index in 0 until nodes.length()) {
            val jsonNode = nodes.getJSONObject(index)
            val parsedNode = parseNode(jsonNode)
            node.addChild(parsedNode)
        }
    }
}

private fun parseBodyType(type:String):B2Type{
    return when(type){
        STATIC ->B2Type.STATIC
        DYNAMIC ->B2Type.DYNAMIC
        else->B2Type.KINEMATIC
    }
}

private fun parseFixtureType(type: String): B2FixtureType {
    return when (type) {
        RECT -> B2FixtureType.RECT
        CIRCLE -> B2FixtureType.CIRCLE
        CHAIN -> B2FixtureType.CHAIN
        EDGE -> B2FixtureType.CHAIN
        else -> B2FixtureType.POLYGON
    }
}

private fun enumToBodyType(enum :B2Type):String{
    return when(enum){
        B2Type.STATIC -> STATIC
        B2Type.DYNAMIC -> DYNAMIC
        else -> KINEMATIC
    }
}

private fun enumToFixtureType(enum: B2FixtureType):String{
    return when(enum){
        B2FixtureType.CHAIN-> CHAIN
        B2FixtureType.CIRCLE -> CIRCLE
        B2FixtureType.RECT -> RECT
        B2FixtureType.EDGE -> EDGE
        else -> POLYGON
    }
}
private fun createJsonNode(node: CcNode): JSONObject? {

    val json: JSONObject
    if (node is CcSprite) {
        json = createJsonSprite(node)
    } else {
        json = createJsonDefaultNode(node)

    }

    if(node.editBody != null){
        val editBody = createPhysicsJson(node.editBody);
        json.put(PHYSICS,editBody)
    }


    val children = JSONArray()
    for (child in node.children) {
        val jsonChild = createJsonNode(child)
        children.put(jsonChild)
    }
    json.put(NODES, children)
    return json


}

fun createPhysicsJson(editBody: CcEditBodyNode): JSONObject {
    val jsonObject = JSONObject()
    val b2EditBody = editBody.b2EditBody
    jsonObject.put(TYPE, enumToBodyType(b2EditBody.type))

    jsonObject.put(ID,b2EditBody.id)
    val jsonFixtures = JSONArray()
    jsonObject.put(FIXTURES,jsonFixtures)
    for (fixtureNode in editBody.fixtureNodes) {
        val jsonFixture = JSONObject()
        val fixture = fixtureNode.fixture
        jsonFixture.put(RESTITUTION,fixture.restitution)
        jsonFixture.put(DENSITY,fixture.density)
        jsonFixture.put(FRICTION,fixture.friction)
        val fixtureType = enumToFixtureType(fixture.type)
        jsonFixture.put(TYPE,fixtureType)

        if(fixtureNode is CcPolygon){
            jsonFixture.put(TYPE, POLYGON)
        }else if(fixtureNode is CcCircle){
            jsonFixture.put(TYPE, CIRCLE)
        }else if(fixture is CcChain){
            jsonFixture.put(TYPE, CHAIN)
        }

    }



    return jsonObject
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

