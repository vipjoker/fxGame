package mygame.editor.parser;

import com.badlogic.gdx.math.Vector2;
import mygame.editor.model.box2d.B2Body;
import mygame.editor.model.box2d.B2Fixture;
import mygame.editor.model.box2d.B2FixtureType;
import mygame.editor.model.box2d.B2Type;
import mygame.editor.views.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LocalParser {

    private static final String NODES = "nodes";
    private static final String ID = "id";
    private static final String X = "x";
    private static final String Y = "y";
    private static final String NAME = "name";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String ANGLE = "angle";
    private static final String IMAGE = "image";
    private static final String TYPE = "type";
    private static final String NODE = "node";
    private static final String SPRITE = "sprite";
    private static final String PHYSICS = "physics";
    private static final String FIXTURES = "fixtures";
    private static final String RESTITUTION = "restitution";
    private static final String DENSITY = "density";
    private static final String FRICTION = "friction";
    private static final String POINTS = "points";
    private static final String RECT = "rect";
    private static final String CIRCLE = "circle";
    private static final String CHAIN = "chain";
    private static final String EDGE = "edge";
    private static final String POLYGON = "polygon";
    private static final String DYNAMIC = "dynamic";
    private static final String KINEMATIC = "kinematic";
    private static final String STATIC = "static";


    public static List<CcNode> createNodesFromString(String json) {

        List<CcNode> layers = new ArrayList<>();


        JSONObject jsonObject = new JSONObject(json);
        JSONArray nodes = jsonObject.getJSONArray(NODES);

        for (int i = 0; i < nodes.length(); i++) {
            JSONObject jsonNode = nodes.getJSONObject(i);
            CcNode node = parseNode(jsonNode);
            layers.add(node);
        }

        return layers;
    }

    public static String createJsonFromNodes(List<CcNode> nodes) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put(NODES, jsonArray);
        for (CcNode ccNode : nodes) {
            JSONObject jsonNode = createJsonNode(ccNode);
            jsonArray.put(jsonNode);
        }
        return jsonObject.toString();
    }

    private static CcNode parseNode(JSONObject jsonObject) {
        String type = jsonObject.getString(TYPE);
        if (SPRITE.equals(type)) {
            return createSprite(jsonObject);
        } else {
            return createNode(jsonObject);
        }

    }

    private static CcNode createNode(JSONObject jsonObject) {
        CcNode node = new CcNode();
        fillNode(node, jsonObject);
        return node;
    }

    private static CcSprite createSprite(JSONObject jsonObject) {
        String image = jsonObject.getString(IMAGE);
        CcSprite sprite = new CcSprite(image);
        fillNode(sprite, jsonObject);
        return sprite;
    }

    private static void fillNode(CcNode node, JSONObject jsonObject) {
        double x = jsonObject.getDouble(X);
        double y = jsonObject.getDouble(Y);
        double width = jsonObject.getDouble(WIDTH);
        double height = jsonObject.getDouble(HEIGHT);
        String name = jsonObject.getString(NAME);
        double angle = jsonObject.getDouble(ANGLE);

        node.getX().set(x);
        node.getY().set(y);
        node.getWidth().set(width);
        node.getHeight().set(height);
        node.getName().set(name);
        node.getAngle().set(angle);
        if (jsonObject.has(PHYSICS)) {

            JSONObject physicsObject = jsonObject.getJSONObject(PHYSICS);
            String physicsType = physicsObject.getString(TYPE);
            B2Type bodyType = parseBodyType(physicsType);
            B2Body body = new B2Body(bodyType);

            JSONArray fixturesArray = physicsObject.getJSONArray(FIXTURES);
            for (int index = 0; index < fixturesArray.length(); index++) {
                JSONObject fixtureJson = fixturesArray.getJSONObject(index);
                String fixtureType = fixtureJson.getString(TYPE);


                B2FixtureType fixtureTypeEnum = parseFixtureType(fixtureType);


                float friction = fixtureJson.getFloat(FRICTION);
                float restitution = fixtureJson.getFloat(RESTITUTION);
                float density = fixtureJson.getFloat(DENSITY);

                JSONArray pointsJsonArray = fixtureJson.getJSONArray(POINTS);

                List<Vector2> list = new ArrayList<>();
                for (int pointIndex = 0; pointIndex < pointsJsonArray.length(); pointIndex++){
                    JSONObject jsonPoint = pointsJsonArray.getJSONObject(pointIndex);
                    float pointX = jsonPoint.getFloat(X);
                    float pointY = jsonPoint.getFloat(Y);
                    list.add(new Vector2(pointX, pointY));

                }
                B2Fixture fixture = new B2Fixture(fixtureTypeEnum, list);
                fixture.setDensity(density);
                fixture.setRestitution(restitution);
                fixture.setFriction(friction);

                body.addFixture(fixture);
            }
            node.setEditBody(new CcEditBodyNode(body));

        }

        if (jsonObject.has(NODES)) {
            JSONArray nodes = jsonObject.getJSONArray(NODES);
            for (int index = 0;index < nodes.length(); index++){
                JSONObject jsonNode = nodes.getJSONObject(index);
                CcNode parsedNode = parseNode(jsonNode);
                node.addChild(parsedNode);
            }
        }
    }

    private static B2Type parseBodyType(String type) {
        switch (type) {
            case STATIC:
                return B2Type.STATIC;
            case DYNAMIC:
                return B2Type.DYNAMIC;
            default:
                return B2Type.KINEMATIC;
        }
    }

    private static B2FixtureType parseFixtureType(String type) {
        switch (type) {
            case RECT:
                return B2FixtureType.RECT;
            case CIRCLE:
                return B2FixtureType.CIRCLE;
            case CHAIN:
                return B2FixtureType.CHAIN;
            case EDGE:
                return B2FixtureType.CHAIN;
            default:
                return B2FixtureType.POLYGON;
        }
    }

    private static String enumToBodyType(B2Type type) {
        switch (type) {
            case STATIC:
                return STATIC;
            case DYNAMIC:
                return DYNAMIC;
            default:
                return KINEMATIC;
        }
    }

    private static String enumToFixtureType(B2FixtureType type) {
        switch (type) {
            case CHAIN:
                return CHAIN;
            case CIRCLE:
                return CIRCLE;
            case RECT:
                return RECT;
            case EDGE:
                return EDGE;
            default:
                return POLYGON;
        }
    }

    private static JSONObject createJsonNode(CcNode node) {

        JSONObject json;
        if (node instanceof CcSprite){
            json = createJsonSprite((CcSprite) node);
        } else{
            json = createJsonDefaultNode(node);

        }

        if (node.getEditBody() != null) {
            JSONObject editBody = createPhysicsJson(node.getEditBody());
            json.put(PHYSICS, editBody);
        }


        JSONArray children = new JSONArray();
        for (CcNode child :node.getChildren()) {
            JSONObject jsonChild = createJsonNode(child);
            children.put(jsonChild);
        }
        json.put(NODES, children);
        return json;


    }

    private static JSONObject createPhysicsJson(CcEditBodyNode editBody) {
        JSONObject jsonObject = new JSONObject();
        B2Body b2EditBody = editBody.getB2EditBody();
        jsonObject.put(TYPE, enumToBodyType(b2EditBody.getType()));

        jsonObject.put(ID, b2EditBody.getId());
        JSONArray jsonFixtures = new JSONArray();
        jsonObject.put(FIXTURES, jsonFixtures);
        for (CcFixtureNode fixtureNode : editBody.getFixtureNodes()) {
            JSONObject jsonFixture = new JSONObject();
            B2Fixture fixture = fixtureNode.getFixture();
            jsonFixture.put(RESTITUTION, fixture.getRestitution());
            jsonFixture.put(DENSITY, fixture.getDensity());
            jsonFixture.put(FRICTION, fixture.getFriction());
            String fixtureType = enumToFixtureType(fixture.getType());
            jsonFixture.put(TYPE, fixtureType);

            if (fixtureNode instanceof CcPolygon){
                jsonFixture.put(TYPE, POLYGON);
            }else if (fixtureNode instanceof CcCircle){
                jsonFixture.put(TYPE, CIRCLE);
            }else if (fixtureNode instanceof CcChain){
                jsonFixture.put(TYPE, CHAIN);
            }

            JSONArray jsonPoints = new JSONArray();

            for (Vector2 point : fixtureNode.getPoints()) {

                JSONObject p =new  JSONObject();
                p.put(X, point.x);
                p.put(Y, point.y);
                jsonPoints.put(p);
            }
            jsonFixture.put(POINTS, jsonPoints);
            jsonFixtures.put(jsonFixture);
        }

        return jsonObject;
    }

    private static JSONObject createJsonSprite(CcSprite node) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TYPE, SPRITE);
        jsonObject.put(X, node.getX().doubleValue());
        jsonObject.put(Y, node.getY().doubleValue());
        jsonObject.put(NAME, node.getName().getValue());
        jsonObject.put(WIDTH, node.getWidth().doubleValue());
        jsonObject.put(HEIGHT, node.getHeight().doubleValue());
        jsonObject.put(ANGLE, node.getAngle().doubleValue());
        jsonObject.put(IMAGE, node.getImagePath());
        return jsonObject;
    }

    private static JSONObject createJsonDefaultNode(CcNode node) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TYPE, NODE);
        jsonObject.put(X, node.getX().doubleValue());
        jsonObject.put(Y, node.getY().doubleValue());
        jsonObject.put(NAME, node.getName().getValue());
        jsonObject.put(WIDTH, node.getWidth().doubleValue());
        jsonObject.put(HEIGHT, node.getHeight().doubleValue());
        jsonObject.put(ANGLE, node.getAngle().doubleValue());
        return jsonObject;
    }
}