package mygame.editor.component;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import mygame.editor.util.Resources;
import mygame.editor.views.NodeView;

import java.io.File;

/**
 * Created by oleh on 17.05.18.
 */
public class SpriteComponent extends Component {


    private int id;
    private String name;
    private String url;
    private Image mImage;

    public SpriteComponent(String path) {
        this.url = path;
        this.mImage = Resources.initImage(path);
    }
    public SpriteComponent(File path) {
        this.url = path.getPath();
        this.mImage = new Image(path.toURI().toString());

    }



    @Override
    public void update() {

    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(mImage, 0, -owner.getHeight().doubleValue(), owner.getWidth().doubleValue(), owner.getHeight().doubleValue());
    }

    @Override
    public void setNode(NodeView node) {
        this.owner = node;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    @Override
    public int getZorder() {
        return 0;
    }

    @Override
    public Type getType() {
        return Type.SPRITE;
    }
}
