package mygame.editor.util;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Created by oleh on 17.04.18.
 */
public class ResourceUtil {
    public static String loadString(String path) {

        StringBuilder builder = new StringBuilder();
        try {
            URL resource = ResourceUtil.class.getResource(path);
            Files.readAllLines(Paths.get(resource.toURI())).forEach(builder::append);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
