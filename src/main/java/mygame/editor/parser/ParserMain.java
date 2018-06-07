package mygame.editor.parser;

import com.google.gson.Gson;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.nio.file.Files;
import java.util.stream.Collectors;

public class ParserMain {
    static Gson gson = new Gson();
    public static void main(String[] args) throws Exception {


        parseTextures("/assets/Texture/");
    }



    private static void parseTextures(String path){
        try {
            URL assets = ParserMain.class.getResource(path);
            File dir = new File(assets.toURI());

            FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
                    return !name.endsWith(".meta");
                }
            };

            for (File  file : dir.listFiles(filter)) {
                System.out.println(file.getName());
                File meta = new File(file.getPath() + ".meta");
                BasicMeta basicMeta = fileToJson(meta, BasicMeta.class);

                Texture text = new Texture();
                text.setUuID(basicMeta.getUuid());


            }




        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void parseFonts(){

    }

    private static void parseScene(){

    }


    private static   <T> T fileToJson(File file,Class<T> clazz){
        try {
            String json = Files.lines(file.toPath()).collect(Collectors.joining("\n"));
            T basicMeta = gson.fromJson(json, clazz);
            return basicMeta;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }
}
