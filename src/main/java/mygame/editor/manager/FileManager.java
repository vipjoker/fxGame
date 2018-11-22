package mygame.editor.manager;

import javafx.scene.Scene;
import javafx.stage.FileChooser;
import mygame.editor.App;
import mygame.editor.controlers.MainController;
import mygame.editor.util.ResourceUtil;
import org.python.google.common.io.Files;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

public class FileManager {
    private File currentSceneFile;
    private File workingFolder;

    private static FileManager  INSTANCE = new FileManager();
    private FileManager(){ }

    public static FileManager getInstance() {
        return INSTANCE;
    }

    public void saveScene(String json)  {
        if(currentSceneFile != null){
            try {
                Files.write(json,currentSceneFile, Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String loadScene(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Json Files", "*.json"));



        currentSceneFile = fileChooser.showOpenDialog(App.instance.scene.getWindow());
        if(currentSceneFile != null) {
            final File parentFile = currentSceneFile.getParentFile();
            MainController controller = App.instance.getController(MainController.class);
            setWorkingFolder(parentFile);

            controller.setLeftPane(parentFile);
            return ResourceUtil.loadString(currentSceneFile);
        }else{
            return null;
        }

    }


    public void setWorkingFolder(File workingFolder) {
        this.workingFolder = workingFolder;
    }

    public File getWorkingFolder() {
        return workingFolder;
    }

}
