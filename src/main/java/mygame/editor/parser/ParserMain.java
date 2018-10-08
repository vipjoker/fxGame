package mygame.editor.parser;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import mygame.editor.parser.json.*;

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
    static final String SCENE_ASSET = "cc.SceneAsset";
    static final String NODE = "cc.Node";
    static final String SCENE = "cc.Scene";
    static final String SPRITE = "cc.Sprite";
    static final String CANVAS = "cc.Canvas";
    static final String RIGID = "cc.RigidBody";
    static final String CIRCLE_COLLIDER = "cc.PhysicsCircleCollider";
    static final String BOX_COLLIDER = "cc.PhysicsBoxCollider";
    static final String CHAIN_COLLIDER = "cc.PhysicsChainCollider";
    static final String LABEL = "cc.Label";

    static Gson gson = new Gson();
    public static Map<Integer,Typeable> nodes = new HashMap<>();




    public static void main(String[] args) throws Exception {

        List<Texture> textures = new ArrayList<>();
        List<Font> fonts = new ArrayList<>();

        parseTextures("/assets/Texture/", textures);
        parseFonts("/assets/Fonts/", fonts);
        parseScene("/assets/Scene/");



        for (Texture texture : textures) {
            System.out.println(texture.getUuID());
            System.out.println(texture.getPath());
        }
        System.out.println("*******************FONT********************");
        for (Font font : fonts) {
            System.out.println(font.getPath());
            System.out.println(font.getUuid());
        }




    }


    private static void parseTextures(String path, List<Texture> list) {
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
                list.add(text);

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

                int id  = 1;
                for (JsonElement jsonElement : array) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();
                    String type = jsonObject.get("__type__").getAsString();


                    System.out.println(type);
                    switch (type) {
                        case SCENE_ASSET:
                            JsonSceneAsset jsonSceneAsset = gson.fromJson(jsonObject, JsonSceneAsset.class);
                            nodes.put(id, jsonSceneAsset);
                            break;
                        case NODE:
                            JsonNode jsonNode = gson.fromJson(jsonObject, JsonNode.class);
                            nodes.put(id,jsonNode);
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
                            JsonCircleColider circleColider = gson.fromJson(jsonObject,JsonCircleColider.class);
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
}
