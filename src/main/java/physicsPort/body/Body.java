package physicsPort.body;

import javafx.scene.image.Image;
import mygame.editor.util.Resources;

import java.util.ArrayList;
import java.util.List;

public class Body {

    public static int counter;


    public static final int BODY_TYPE_STATIC = 0;
    public static final int BODY_TYPE_KINEMATIC = 1;
    public static final int BODY_TYPE_DYNAMIC = 2;


    public String name = "body" + Body.counter++;    // for editor
    public String userData = "";                        // for physics body
    public String texture = "";
    public Sprite sprite;
    public float[] spriteData;                    // [source-x, source-y, width, height, image-w, image-h]
    public float[] initialSpriteData;
    public List<Shape> shapes;
    public float[] position;
    public float[] scaleXY;
    public float rotation;
    public float[] bounds;
    public boolean isSelected;
    public int bodyType;    // default to dynmic body
    public boolean isBullet;
    public boolean isFixedRotation;
    public float linearDamping;
    public float angularDamping;


    public Body() {
        this.name = "body" + Body.counter++;    // for editor
        this.userData = "";                        // for physics body
        this.texture = "";

        this.shapes = new ArrayList<>();
        this.position = new float[]{0, 0};
        this.scaleXY = new float[]{1, 1};
        this.rotation = 0;
        this.bounds = new float[]{0, 0, 0, 0};
        this.isSelected = false;
        this.bodyType = Body.BODY_TYPE_DYNAMIC;    // default to dynmic body
        this.isBullet = false;
        this.isFixedRotation = false;
        this.linearDamping = 0;
        this.angularDamping = 0;
    }


    public void setSprite(String file, float x, float y, float w, float h) {

        Image image = Resources.initImage(file);
        this.texture = file;

        this.sprite =new Sprite(image,w,h);
        this.spriteData = new float[] {x, y, w, h, w, h} ;
        this.initialSpriteData = new float[]{w, h};


    }

    public void setSprite(String file) {
        Image image = Resources.initImage(file);
        this.sprite = new Sprite(image);

        this.initialSpriteData = new float[]{(float) image.getWidth(),(float) image.getHeight()};
        this.spriteData = new float[]{(float)image.getWidth(), (float)image.getHeight()};
    }


    public void setSpriteWidth(float width) {
        if (this.sprite == null) {
            return;
        }

        if (this.spriteData.length > 2) {
            this.spriteData[4] = width;
            this.spriteData[2] = width;
            return;
        }
        this.sprite.width = width;
        this.spriteData[0] = width;
    }

    ;

    public void setSpriteHeight(float height) {
        if (this.sprite == null) {
            return;
        }

        if (this.spriteData.length > 2) {
            this.spriteData[5] = height;
            this.spriteData[3] = height;
            return;
        }
        this.sprite.height = height;
        this.spriteData[1] = height;
    }

    ;

    public void setSpriteSourceWidth (float width) {
        if (this.sprite == null) {
            return;
        }

        if (this.spriteData.length == 2) {
            this.spriteData = new float[]{0, 0, width, this.initialSpriteData[1], this.initialSpriteData[0], this.initialSpriteData[1]};
            return;
        }

        this.spriteData[2] = width;
    }

    ;

    public void setSpriteSourceHeight(float height) {
        if (this.sprite == null) {
            return;
        }

        if (this.spriteData.length == 2) {
            this.spriteData = new float[]{0, 0, this.initialSpriteData[0], height, this.initialSpriteData[0], this.initialSpriteData[1]};
            return;
        }

        this.spriteData[3] = height;
    }

    ;

    public void setOffsetX (float x) {
        if (this.sprite == null) {
            return;
        }

        if (this.spriteData.length == 2) {
            this.spriteData = new float []{
            x, 0, this.initialSpriteData[0], this.initialSpriteData[1], this.initialSpriteData[0], this.initialSpriteData[1]};
        }

        if (this.spriteData.length > 2) {
            this.spriteData[0] = x;
            return;
        }
    }

    ;

    public void setOffsetY (float y) {
        if (this.sprite == null) {
            return;
        }

        if (this.spriteData.length == 2) {
            this.spriteData = new float[]{0, y, this.initialSpriteData[0], this.initialSpriteData[1], this.initialSpriteData[0], this.initialSpriteData[1]};

        }

        if (this.spriteData.length > 2) {
            this.spriteData[1] = y;
            return;
        }
    }



    public float getSpriteWidth () {
        if (this.spriteData.length > 2) {
            return this.spriteData[4];
        }
        return this.sprite.width;
    }



    public float getSpriteHeight () {
        if (this.spriteData.length > 2) {
            return this.spriteData[5];
        }
        return this.sprite.height;
    }



    public float getSpriteSourceHeight () {
        if (this.spriteData.length > 2) {
            return this.spriteData[3];
        }
        return 0;
    }

    ;

    public float getSpriteSourceWidth () {
        if (this.spriteData.length > 2) {
            return this.spriteData[2];
        }
        return 0;
    }

    ;

    public float getSpriteOffsetX () {
        if (this.spriteData.length > 2) {
            return this.spriteData[0];
        }
        return 0;
    }


    public float getSpriteOffsetY () {
        if (this.spriteData.length > 2) {
            return this.spriteData[1];
        }
        return 0;
    }

    ;

    // if (setPos == true) => shape would be moved to bodies center
    public void addShape(Shape shape,boolean setPos) {
        if (setPos) {
            shape.setPosition(this.position[0], this.position[1]);
        }
        this.shapes.add(shape);
    }

    ;

    public void removeShapeGivenIndex(int index) {
        shapes.remove(index);

    }

    public void removeShapeGivenShape(Shape shape) {
        for (int i = 0; i < this.shapes.size(); i++) {
            if (this.shapes.get(i) == shape) {
                this.removeShapeGivenIndex(i);
                break;
            }
        }
    }

    ;

    public void calculateBounds() {
        float minX = 100000, maxX = -100000, minY = 100000, maxY = -100000;

        for (int i = 0; i < this.shapes.size(); i++) {
            for (int j = 0; j < this.shapes.get(i).vertices.size(); j++) {
                Vertex v = this.shapes.get(i).vertices.get(j);
                minX = Math.min(minX, v.x);
                maxX = Math.max(maxX, v.x);
                minY = Math.min(minY, v.y);
                maxY = Math.max(maxY, v.y);
            }
        }
        this.bounds[0] = (maxX + minX) / 2;
        this.bounds[1] = (maxY + minY) / 2;
        this.bounds[2] = maxX - minX;
        this.bounds[3] = maxY - minY;
    }

    public void move(float dx, float dy) {
        this.position[0] += dx;
        this.position[1] += dy;

        for (int i = 0; i < this.shapes.size(); i++) {
            this.shapes.get(i).move(dx, dy);
        }
    }

    ;

    public void setPosition(float x, float y) {
        this.move(x - this.position[0], y - this.position[1]);
    }

    ;


    public void scale(float sx, float sy) {

        float pivotX = this.position[0];
        float pivotY = this.position[1];
        scale(sx, sy, pivotX, pivotY);


    }

    public void scale(float sx, float sy, float pivotX, float pivotY) {


        this.scaleXY[0] *= sx;
        this.scaleXY[1] *= sy;

        this.move(-pivotX, -pivotY);

        this.position[0] *= sx;
        this.position[1] *= sy;

        this.move(pivotX, pivotY);

        for (int i = 0; i < this.shapes.size(); i++) {
            this.shapes.get(i).scale(sx, sy, pivotX, pivotY);
        }

    }

    public void setScale(float sx, float sy, float pivotX, float pivotY) {
        this.scale(sx / this.scaleXY[0], sy / this.scaleXY[1], pivotX, pivotY);
    }

    ;

    public void rotate(float angle) {
        float pivotX = this.position[0];
        float pivotY = this.position[1];
        rotate(angle, pivotX, pivotY);
    }

    public void rotate(float angle, float pivotX, float pivotY) {


        this.rotation += angle;
        for (int i = 0; i < this.shapes.size(); i++) {
            this.shapes.get(i).rotate(angle, pivotX, pivotY);
        }

        // update position
        float x = this.position[0] - pivotX;
        float y = this.position[1] - pivotY;
        float newAngle = angle + (float) Math.atan2(y, x) * 180 / (float) Math.PI;
        float length = (float) Math.pow(x * x + y * y, 0.5);
        this.position[0] = pivotX + length * (float) Math.cos(newAngle * Math.PI / 180);
        this.position[1] = pivotY + length * (float) Math.sin(newAngle * Math.PI / 180);
    }

    public void setRotation(float angle, float pivotX, float pivotY) {
        this.rotate(angle - this.rotation, pivotX, pivotY);
    }

    public Body clone() {
        return null;
    }

}
