package mygame.editor.parser;

import com.badlogic.gdx.math.Quaternion;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import mygame.editor.parser.json.*;
import mygame.editor.parser.model.*;

import java.io.File;
import java.io.FilenameFilter;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParserMain {
    static final String NODE = "cc.Node";
    static final String SCENE = "cc.Scene";
    static final String SPRITE = "cc.Sprite";
    static final String CANVAS = "cc.Canvas";
    static final String RIGID = "cc.RigidBody";
    static final String CIRCLE_COLLIDER = "cc.PhysicsCircleCollider";
    static final String BOX_COLLIDER = "cc.PhysicsBoxCollider";
    static final String CHAIN_COLLIDER = "cc.PhysicsChainCollider";
    static final String POLYGON_COLLIDER = "cc.PhysicsPolygonCollider";

    static final String LABEL = "cc.Label";

    static Gson gson = new Gson();
    public static Map<Integer, Typeable> nodes = new HashMap<>();
    public static Map<String, Texture> textures = new HashMap<>();
    public static List<Font> fonts = new ArrayList<>();

    public static void main(String[] args) throws Exception {


        parseTextures("/assets/Texture/", textures);
        parseFonts("/assets/Fonts/", fonts);
        parseScene("/assets/Scene/");


        for (String key : textures.keySet()) {
            Texture texture = textures.get(key);
            System.out.println(texture.getUuID() + " -> " + texture.getPath());

        }
        System.out.println("*******************FONT********************");
        for (Font font : fonts) {
            System.out.println(font.getPath() + " -> " + font.getUuid());

        }
        Scene scene = new Scene();
        List<Node> children = new ArrayList<>();
        scene.setChildren(children);
        nodes.forEach((k, v) -> {
            if (SCENE.equals(v.getType())) {
                JsonScene jsonScene = (JsonScene) v;
                System.out.println("scene found");

                for (JsonId child : jsonScene.getChildren()) {
                    JsonNode jsonNode = (JsonNode) nodes.get(child.getId());
                    Node node = createNode(jsonNode);
                    children.add(node);
                }
            }

        });


        saveNodeToFile(scene);
    }


    private static Node createNode(JsonNode node) {
        Node n = null;

        JsonSprite sprite = findSprite(node);
        if (sprite != null) {
            System.out.println("Sprite found");
            n = new Sprite();
            String uuid = sprite.get_spriteFrame().getUuid();
            Texture texture = textures.get(uuid);

            if (texture != null) {
                ((Sprite) n).setSpriteName(texture.getPath());
                n.setType("sprite");
                System.out.println("frame found " + texture.getPath());
            } else {
                System.out.println(uuid + " not found");
            }

        }

        if (n == null) {
            n = new Node();
            n.setType("node");

        }
        JsonRigidBody rigidBody = findRigidBody(node);
        if (rigidBody != null) {
            List<JsonColider> coliders = findColiders(node);

            Physics physics = new Physics();
            physics.setAngularDamping(rigidBody.get_angularDamping().floatValue());
            physics.setLinearDamping(rigidBody.get_linearDamping().floatValue());
            physics.setBullet(rigidBody.getBullet());
            physics.setFixedRotation(rigidBody.get_fixedRotation());
            physics.setAngularVelocity(rigidBody.get_angularVelocity().floatValue());
            physics.setLinearVelocityX(rigidBody.get_linearVelocity().getX().floatValue());
            physics.setLinearVelocityY(rigidBody.get_linearVelocity().getY().floatValue());
            switch (rigidBody.get_type()) {
                case 0:
                    physics.setType("static");
                    break;
                case 1:
                    physics.setType("kinematic");
                    break;
                case 2:
                    physics.setType("dynamic");
                    break;
            }

            List<Shape> shapes = new ArrayList<>();
            for (JsonColider colider : coliders) {
                Shape shape = new Shape();
                shape.setDensity(colider.getDensity());
                shape.setFriction(colider.getFriction());
                shape.setRestitution(colider.getRestitution());
                shape.setSensor(colider.getSensor());
                if (colider instanceof JsonBoxCollider) {

                    JsonBoxCollider boxCollider = (JsonBoxCollider) colider;
                    shape.setType("box");
                    shape.setWidth(boxCollider.getSize().getWidth());
                    shape.setHeight(boxCollider.getSize().getHeight());

                } else if (colider instanceof JsonCircleColider) {
                    shape.setType("circle");
                    JsonCircleColider circleColider = (JsonCircleColider) colider;
                    shape.setRadius(circleColider.getRadius().floatValue());

                } else if (colider instanceof JsonChainCollider) {
                    shape.setType("chain");
                    JsonChainCollider chainCollider = (JsonChainCollider) colider;
                    List<Point> points = new ArrayList<>();
                    for (JsonVec2 point : chainCollider.getPoints()) {
                        Point p = new Point(point.getX().floatValue(), point.getY().floatValue());
                        points.add(p);
                    }
                    shape.setPoints(points);
                    shape.setLoop(chainCollider.getLoop());


                } else if (colider instanceof JsonPolygonCollider) {
                    shape.setType("polygon");
                    JsonPolygonCollider polygonCollider = (JsonPolygonCollider) colider;
                    List<Point> points = new ArrayList<>();
                    for (JsonVec2 point : polygonCollider.getPoints()) {
                        Point p = new Point(point.getX().floatValue(), point.getY().floatValue());
                        points.add(p);
                    }
                    shape.setPoints(points);

                }

                shapes.add(shape);
            }
            physics.setShapes(shapes);
            n.setPhysics(physics);


        }


        n.setName(node.get_name());
        n.setPositionX(node.get_position().getX().floatValue());
        n.setPositionY(node.get_position().getY().floatValue());
        Double rotationz = node.get_quat().getZ();
        Double rotationw = node.get_quat().getW();


        Quaternion quaternion = new Quaternion();
        quaternion.w = rotationw.floatValue();
        quaternion.z = rotationz.floatValue();
        float angle = Math.round(quaternion.getAngle());


//        float  deg = (float) (rad * (180 / Math.PI));
        n.setAngle(angle);
        n.setScaleX(node.get_scale().getX().floatValue());
        n.setScaleY(node.get_scale().getY().floatValue());

        List<Node> children = new ArrayList<>();
        for (JsonId child : node.get_children()) {
            JsonNode jsonNode = (JsonNode)nodes.get(child.getId());
            Node ch = createNode(jsonNode);
            children.add(ch);
        }
        n.setChildren(children);
        return n;
    }

    private static JsonSprite findSprite(JsonNode node) {
        for (JsonId component : node.getComponents()) {
            Typeable typeable = nodes.get(component.getId());
            if (typeable instanceof JsonSprite) {
                return (JsonSprite) typeable;
            }
        }

        return null;
    }


    private static JsonRigidBody findRigidBody(JsonNode node) {
        for (JsonId component : node.getComponents()) {
            Typeable typeable = nodes.get(component.getId());
            if (typeable instanceof JsonRigidBody) {
                return (JsonRigidBody) typeable;
            }
        }
        return null;
    }

    private static List<JsonColider> findColiders(JsonNode node) {
        List<JsonColider> coliders = new ArrayList<>();
        for (JsonId component : node.getComponents()) {
            Typeable typeable = nodes.get(component.getId());
            if (typeable instanceof JsonChainCollider) {
                coliders.add((JsonChainCollider) typeable);
            } else if (typeable instanceof JsonCircleColider) {
                coliders.add((JsonCircleColider) typeable);
            } else if (typeable instanceof JsonBoxCollider) {
                coliders.add((JsonBoxCollider) typeable);
            }
        }
        return coliders;

    }

    private static void parseTextures(String path, Map<String, Texture> list) {
        try {
            URL assets = ParserMain.class.getResource(path);
            File dir = new File(assets.toURI());

            FilenameFilter filter = (dir1, name) -> !name.endsWith(".meta");

            for (File file : dir.listFiles(filter)) {
                System.out.println(file.getName());
                File meta = new File(file.getPath() + ".meta");
                Type type = new TypeToken<BasicMeta<TextureMeta>>() {
                }.getType();

                BasicMeta<TextureMeta> basicMeta = fileToJson(meta, type);
                Map<String, TextureMeta> subMetas = basicMeta.getSubMetas();
                String first = (String) subMetas.keySet().toArray()[0];
                TextureMeta textureMeta = subMetas.get(first);

                Texture text = new Texture();
                text.setUuID(basicMeta.getUuid());
                text.setHeight(textureMeta.getHeight());
                text.setWidth(textureMeta.getWidth());
                text.setPath(path + file.getName());
                list.put(textureMeta.getUuid(), text);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseFonts(String path, List<Font> fonts) {
        try {
            URL assets = ParserMain.class.getResource(path);
            File dir = new File(assets.toURI());

            FilenameFilter filter = (dir1, name) -> !name.endsWith(".meta");

            for (File file : dir.listFiles(filter)) {
                File meta = new File(file.getPath() + ".meta");
                Type type = new TypeToken<BasicMeta<TextureMeta>>() {
                }.getType();

                BasicMeta basicMeta = fileToJson(meta, type);
                Font text = new Font();
                text.setUuid(basicMeta.getUuid());
                text.setPath(path + file.getName());
                fonts.add(text);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void parseScene(String path) {
        try {
            URL assets = ParserMain.class.getResource(path);
            File dir = new File(assets.toURI());

            FilenameFilter filter = (dir1, name) -> !name.endsWith(".meta");

            for (File file : dir.listFiles(filter)) {


                JsonArray array = fileToJson(file, JsonArray.class);

                int id = 0;
                for (JsonElement jsonElement : array) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String type = jsonObject.get("__type__").getAsString();


                    System.out.println(type);
                    switch (type) {
                        case NODE:
                            JsonNode jsonNode = gson.fromJson(jsonObject, JsonNode.class);
                            nodes.put(id, jsonNode);
                            break;
                        case SPRITE:
                            JsonSprite jsonSprite = gson.fromJson(jsonObject, JsonSprite.class);
                            nodes.put(id, jsonSprite);
                            break;
                        case SCENE:
                            JsonScene jsonScene = gson.fromJson(jsonObject, JsonScene.class);
                            nodes.put(id, jsonScene);
                            break;
                        case RIGID:
                            JsonRigidBody rigidBody = gson.fromJson(jsonObject, JsonRigidBody.class);
                            nodes.put(id, rigidBody);
                            break;
                        case CIRCLE_COLLIDER:
                            JsonCircleColider circleColider = gson.fromJson(jsonObject, JsonCircleColider.class);
                            nodes.put(id, circleColider);
                            break;
                        case BOX_COLLIDER:
                            JsonBoxCollider jsonBoxCollider = gson.fromJson(jsonObject, JsonBoxCollider.class);
                            nodes.put(id, jsonBoxCollider);
                            break;
                        case CHAIN_COLLIDER:
                            JsonChainCollider jsonChainCollider = gson.fromJson(jsonObject, JsonChainCollider.class);
                            nodes.put(id, jsonChainCollider);
                            break;

                        case POLYGON_COLLIDER:
                            JsonPolygonCollider polygonCollider = gson.fromJson(jsonObject, JsonPolygonCollider.class);
                            nodes.put(id, polygonCollider);
                        case CANVAS:
                            JsonCanvas jsonCanvas = gson.fromJson(jsonObject, JsonCanvas.class);
                            nodes.put(id, jsonCanvas);
                            break;
                        case LABEL:
                            JsonLabel jsonLabel = gson.fromJson(jsonObject, JsonLabel.class);
                            nodes.put(id, jsonLabel);
                            break;
                    }

                    id++;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static <T> T fileToJson(File file, Type type) {
        try {
            String json = Files.lines(file.toPath()).collect(Collectors.joining("\n"));
            T basicMeta = gson.fromJson(json, type);
            return basicMeta;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    private static void saveNodeToFile(Scene scene) {
        String json = gson.toJson(scene);
        System.out.println(json);
    }
}
