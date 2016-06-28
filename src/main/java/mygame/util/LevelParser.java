package mygame.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mygame.editor.model.TileModel;

import java.io.*;
import java.util.List;

/**
 * Created by Makhobey Oleh on 6/28/16.
 * email: tajcig@ya.ru
 */
public class LevelParser {

    private static Gson gson = new Gson();


    public static List<TileModel> loadLevel(File file) {
        StringBuilder builder = new StringBuilder();
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {

            String line;
            while((line = bufferedReader.readLine()) != null){
                builder.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return gson.fromJson(builder.toString(), new TypeToken<List<TileModel>>(){}.getType());
    }

    public static  void saveLevel(List<TileModel> levels,File file){

        String strLevels = gson.toJson(levels);
        try(FileWriter writer = new FileWriter(file)){
            writer.write(strLevels);
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }



    }



}
