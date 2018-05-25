package mygame.editor.component;

import javafx.scene.canvas.GraphicsContext;
import mygame.editor.views.CcNode;

/**
 * Created by oleh on 17.05.18.
 */
public class SpriteComponent extends Component {


    private int id;
    private String name;
    private String url;
    public SpriteComponent(CcNode owner) {
        super(owner);
    }

    @Override
    public void update() {

    }

    @Override
    public void draw(GraphicsContext g) {

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
}
