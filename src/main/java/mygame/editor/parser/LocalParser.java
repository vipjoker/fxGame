package mygame.editor.parser;

import com.badlogic.gdx.math.Vector2;
import mygame.editor.model.Node;
import mygame.editor.model.Physics;
import mygame.editor.model.Point;
import mygame.editor.model.Sprite;
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
    private static final String SHAPE = "shape";
    private static final String NODE = "node";
    private static final String SPRITE = "sprite";
    private static final String PHYSICS = "physics";
    private static final String FIXTURES = "fixtures";
    private static final String RESTITUTION = "restitution";
    private static final String DENSITY = "density";
    private static final String FRICTION = "friction";
    private static final String POINTS = "points";

    private static final String CENTER = "center";
    private static final String RADIUS = "radius";

    private static final String RECT = "rect";
    private static final String CIRCLE = "circle";
    private static final String CHAIN = "chain";
    private static final String POLYGON = "polygon";
    private static final String DYNAMIC = "dynamic";
    private static final String KINEMATIC = "kinematic";
    private static final String STATIC = "static";


    public static List<Node> createNodesFromString(String json) {

        List<Node> layers = new ArrayList<>();


        JSONObject jsonObject = new JSONObject(json);
        JSONArray nodes = jsonObject.getJSONArray(NODES);

        for (int i = 0; i < nodes.length(); i++) {
            JSONObject jsonNode = nodes.getJSONObject(i);
            Node node = parseNode(jsonNode);
            layers.add(node);
        }

        return layers;
    }

    public static String createJsonFromNodes(List<Node> nodes) {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        jsonObject.put(NODES, jsonArray);
        for (Node ccNode : nodes) {
            JSONObject jsonNode = createJsonNode(ccNode);
            jsonArray.put(jsonNode);
        }
        return jsonObject.toString();
    }

    private static Node parseNode(JSONObject jsonObject) {
        String type = jsonObject.getString(TYPE);
        if (SPRITE.equals(type)) {
            return createSprite(jsonObject);
        } else {
            return createNode(jsonObject);
        }

    }

    private static Node createNode(JSONObject jsonObject) {
        Node node = new Node();
        fillNode(node, jsonObject);
        return node;
    }

    private static Sprite createSprite(JSONObject jsonObject) {
        String image = jsonObject.getString(IMAGE);
        Sprite sprite = new Sprite(image);
        fillNode(sprite, jsonObject);
        return sprite;
    }

private static void fillNode(Node node, JSONObject jsonObject) {
        double x = jsonObject.getDouble(X);
        double y = jsonObject.getDouble(Y);
        double width = jsonObject.getDouble(WIDTH);
        double height = jsonObject.getDouble(HEIGHT);
        String name = jsonObject.getString(NAME);
        double angle = jsonObject.getDouble(ANGLE);

        node.getPosition().set(x,y);
        node.getWidth().set(width);
        node.getHeight().set(height);
        node.getName().set(name);
        node.getAngle().set(angle);
        if (jsonObject.has(PHYSICS)) {

            JSONObject physicsObject = jsonObject.getJSONObject(PHYSICS);

            String physicsType = physicsObject.getString(TYPE);
            double friction = physicsObject.getDouble(FRICTION);
            double restitution = physicsObject.getDouble(RESTITUTION);
            double density = physicsObject.getDouble(DENSITY);

            Physics physics = new Physics();
            physics.setType(physicsType);
            physics.setFriction(friction);
            physics.setRestitution(restitution);
            physics.setDensity(density);

            String shape = physicsObject.getString(SHAPE);

            switch (shape){
                case RECT:
                    double rectWidth = physicsObject.getDouble(WIDTH);
                    double rectHeight = physicsObject.getDouble(HEIGHT);
                    physics.setWidth(rectWidth);
                    physics.setHeight(rectHeight);
                    break;
                case CIRCLE :
                    JSONObject centerPointJson = physicsObject.getJSONObject(CENTER);
                    double centerPointX = centerPointJson.getDouble(X);
                    double centerPointY = centerPointJson.getDouble(Y);
                    double radius  = physicsObject.getDouble(RADIUS);
                    physics.setCenter(centerPointX,centerPointY);
                    physics.setRadius(radius);
                    break;
                case CHAIN :
                    JSONArray pointsJson = physicsObject.getJSONArray(POINTS);
                    for (int i = 0; i < pointsJson.length(); i++) {
                        JSONObject jsonPoint = pointsJson.getJSONObject(i);
                        double pointX = jsonPoint.getDouble(X);
                        double pointY = jsonPoint.getDouble(Y);
                        physics.getPoints().add(new Point(pointX, pointY));
                    }
                    break;

            }
            node.setPhysics(physics);

        }

        if (jsonObject.has(NODES)) {
            JSONArray nodes = jsonObject.getJSONArray(NODES);
            for (int index = 0;index < nodes.length(); index++){
                JSONObject jsonNode = nodes.getJSONObject(index);
                Node parsedNode = parseNode(jsonNode);
                node.addChild(parsedNode);
            }
        }
    }



    private static JSONObject createJsonNode(Node node) {

        JSONObject json;
        if (node instanceof Sprite){
            json = createJsonSprite((Sprite) node);
        } else{
            json = createJsonDefaultNode(node);

        }

        if (node.getPhysics() != null) {
            JSONObject editBody = createPhysicsJson(node.getPhysics());
            json.put(PHYSICS, editBody);
        }


        JSONArray children = new JSONArray();
        for (Node child :node.getChildren()) {
            JSONObject jsonChild = createJsonNode(child);
            children.put(jsonChild);
        }
        json.put(NODES, children);
        return json;


    }

    private static JSONObject createPhysicsJson(Physics physics) {

        JSONObject jsonObject = new JSONObject();

        jsonObject.put(RESTITUTION, physics.getRestitution().get());
        jsonObject.put(DENSITY, physics.getDensity().get());
        jsonObject.put(FRICTION, physics.getFriction().get());
        jsonObject.put(TYPE,physics.getType());
        switch (physics.getShape().getValue()){
            case RECT:
                jsonObject.put(SHAPE,RECT);
                jsonObject.put(WIDTH, physics.getWidth().doubleValue());
                jsonObject.put(HEIGHT,physics.getHeight().doubleValue());
                break;
            case CIRCLE:
                jsonObject.put(SHAPE,CIRCLE);
                jsonObject.put(RADIUS,physics.getRadius().doubleValue());
                JSONObject center = new JSONObject();
                center.put(X,physics.getCenter().getX().doubleValue());
                center.put(Y,physics.getCenter().getY().doubleValue());
                jsonObject.put(CENTER,center);

                break;
            case CHAIN:
                jsonObject.put(SHAPE,CHAIN);
                JSONArray points = new JSONArray();
                for (Point point : physics.getPoints()) {
                    JSONObject p = new JSONObject();
                    p.put(X, point.getX().getValue());
                    p.put(Y, point.getY().getValue());
                    points.put(p);
                }
                jsonObject.put(POINTS, points);
                break;
        }

        return jsonObject;
    }

    private static JSONObject createJsonSprite(Sprite node) {
        JSONObject jsonObject = createJsonDefaultNode(node);
        jsonObject.put(TYPE, SPRITE);
        jsonObject.put(IMAGE, node.getImage().get());
        return jsonObject;
    }

    private static JSONObject createJsonDefaultNode(Node node) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TYPE, NODE);
        jsonObject.put(X, node.getPosition().getX().doubleValue());
        jsonObject.put(Y, node.getPosition().getY().doubleValue());
        jsonObject.put(NAME, node.getName().getValue());
        jsonObject.put(WIDTH, node.getWidth().doubleValue());
        jsonObject.put(HEIGHT, node.getHeight().doubleValue());
        jsonObject.put(ANGLE, node.getAngle().doubleValue());
        return jsonObject;
    }
}