package mygame.editor.util;

import com.google.gson.Gson;

import java.io.*;
import java.util.List;

/**
 * Created by Makhobey Oleh on 6/28/16.
 * email: tajcig@ya.ru
 */
public class FileUtil {

    private static Gson gson = new Gson();


    public static String loadString(File file) {
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

        return builder.toString();
    }

    public static  void saveStringToFile (String value,File file){
        try(FileWriter writer = new FileWriter(file)){
            writer.write(value);
            writer.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
