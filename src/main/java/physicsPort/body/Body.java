package physicsPort.body;

import java.util.ArrayList;
import java.util.List;

public class Body {

    public static int counter;


    public static final int BODY_TYPE_STATIC = 0;
    public static final int BODY_TYPE_KINEMATIC = 1;
    public static final int BODY_TYPE_DYNAMIC = 2;




        private String name = "body" + Body.counter++;	// for editor
        private String userData = "";						// for physics body
        private String texture = "";
        private String sprite;
        private List<Object> spriteData;					// [source-x, source-y, width, height, image-w, image-h]
        private List<Object> initialSpriteData ;
        private List<Shape> shapes ;
        private float[] position ;
        private float[] scaleXY ;
        private float rotation ;
        private float[] bounds;
        private boolean isSelected ;
        private int bodyType ;	// default to dynmic body
        private boolean isBullet ;
        private boolean isFixedRotation ;
        private float linearDamping ;
        private float angularDamping;


    public Body(){
        this.name = "body" + Body.counter++;	// for editor
        this.userData = "";						// for physics body
        this.texture = "";

        this.spriteData = new ArrayList<>();					// [source-x, source-y, width, height, image-w, image-h]
        this.initialSpriteData = new ArrayList<>();
        this.shapes = new ArrayList<>();
        this.position = new float[] {0, 0};
        this.scaleXY = new float[]{1, 1};
        this.rotation = 0;
        this.bounds = new float[]{0, 0, 0, 0};
        this.isSelected = false;
        this.bodyType = Body.BODY_TYPE_DYNAMIC;	// default to dynmic body
        this.isBullet = false;
        this.isFixedRotation = false;
        this.linearDamping = 0;
        this.angularDamping = 0;
    }


    Body.prototype.setSprite = function(file, x, y, w, h){
        if (x != null && y != null && w != null && h != null){	// image is sprite sheet
            this.sprite = new Image();
            this.sprite.src = file;
            this.spriteData = [x, y, w, h, w, h];
            this.initialSpriteData = [w, h];
        }

        else {
            this.sprite = new Image();
            this.sprite.src = file;
            var ref = this;
            this.sprite.onload = function(){
                console.log(this.width);
                ref.initialSpriteData = [this.width, this.height];
                ref.spriteData = [this.width, this.height];
            }
        }
        this.texture = file;
    };





    Body.prototype.setSpriteWidth = function(width){
        if (this.sprite == null){
            return;
        }

        if (this.spriteData.length > 2){
            this.spriteData[4] = width;
            this.spriteData[2] = width;
            return;
        }
        this.sprite.width = width;
        this.spriteData[0] = width;
    };

    Body.prototype.setSpriteHeight = function(height){
        if (this.sprite == null){
            return;
        }

        if (this.spriteData.length > 2){
            this.spriteData[5] = height;
            this.spriteData[3] = height;
            return;
        }
        this.sprite.height = height;
        this.spriteData[1] = height;
    };

    Body.prototype.setSpriteSourceWidth = function(width){
        if (this.sprite == null){
            return;
        }

        if (this.spriteData.length == 2){
            this.spriteData = [0, 0, width, this.initialSpriteData[1], this.initialSpriteData[0], this.initialSpriteData[1]];
            return;
        }

        this.spriteData[2] = width;
    };

    Body.prototype.setSpriteSourceHeight = function(height){
        if (this.sprite == null){
            return;
        }

        if (this.spriteData.length == 2){
            this.spriteData = [0, 0, this.initialSpriteData[0], height, this.initialSpriteData[0], this.initialSpriteData[1]];
            return;
        }

        this.spriteData[3] = height;
    };

    Body.prototype.setOffsetX = function(x){
        if (this.sprite == null){
            return;
        }

        if (this.spriteData.length == 2){
            this.spriteData = [x, 0, this.initialSpriteData[0], this.initialSpriteData[1], this.initialSpriteData[0], this.initialSpriteData[1]];
        }

        if (this.spriteData.length > 2){
            this.spriteData[0] = x;
            return;
        }
    };

    Body.prototype.setOffsetY = function(y){
        if (this.sprite == null){
            return;
        }

        if (this.spriteData.length == 2){
            this.spriteData = [0, y, this.initialSpriteData[0], this.initialSpriteData[1], this.initialSpriteData[0], this.initialSpriteData[1]];
        }

        if (this.spriteData.length > 2){
            this.spriteData[1] = y;
            return;
        }
    };

    Body.prototype.getSpriteWidth = function(){
        if (this.spriteData.length > 2){
            return this.spriteData[4];
        }
        return this.sprite.width;
    };

    Body.prototype.getSpriteHeight = function(){
        if (this.spriteData.length > 2){
            return this.spriteData[5];
        }
        return this.sprite.height;
    };

    Body.prototype.getSpriteSourceHeight = function(){
        if (this.spriteData.length > 2){
            return this.spriteData[3];
        }
        return null;
    };

    Body.prototype.getSpriteSourceWidth = function(){
        if (this.spriteData.length > 2){
            return this.spriteData[2];
        }
        return null;
    };

    Body.prototype.getSpriteOffsetX = function(){
        if (this.spriteData.length > 2){
            return this.spriteData[0];
        }
        return null;
    };

    Body.prototype.getSpriteOffsetY = function(){
        if (this.spriteData.length > 2){
            return this.spriteData[1];
        }
        return null;
    };

// if (setPos == true) => shape would be moved to bodies center
    Body.prototype.addShape = function(shape, setPos){
        if (setPos){
            shape.setPosition(this.position[0], this.position[1]);
        }
        this.shapes.push(shape);
    };

    Body.prototype.removeShapeGivenIndex = function(index){
        if (index == 0){
            this.shapes.shift();
        }
        else if (index == this.shapes.length - 1){
            this.shapes.pop();
        }
        else {
            this.shapes.splice(index, 1);
        }
    };

    Body.prototype.removeShapeGivenShape = function(shape){
        for (var i = 0; i < this.shapes.length; i++){
            if (this.shapes[i] == shape){
                this.removeShapeGivenIndex(i);
                break;
            }
        }
    };

    Body.prototype.calculateBounds = function(){
        var minX = 100000, maxX = -100000, minY = 100000, maxY = -100000;
        var v;

        for (var i = 0; i < this.shapes.length; i++){
            for (var j = 0; j < this.shapes[i].vertices.length; j++){
                v = this.shapes[i].vertices[j];
                minX = Math.min(minX, v.x)
                maxX = Math.max(maxX, v.x);
                minY = Math.min(minY, v.y);
                maxY = Math.max(maxY, v.y);
            }
        }
        this.bounds[0] = (maxX + minX) / 2;
        this.bounds[1] = (maxY + minY) / 2;
        this.bounds[2] = maxX - minX;
        this.bounds[3] = maxY - minY;
    };

    Body.prototype.move = function(dx, dy){
        this.position[0] += dx;
        this.position[1] += dy;

        for (var i = 0; i < this.shapes.length; i++){
            this.shapes[i].move(dx, dy);
        }
    };

    Body.prototype.setPosition = function(x, y){
        this.move(x - this.position[0], y - this.position[1]);
    };

    Body.prototype.scale = function(sx, sy, pivotX, pivotY){
        if (pivotX == null || pivotY == null){
            pivotX = this.position[0];
            pivotY = this.position[1];
        }

        this.scaleXY[0] *= sx;
        this.scaleXY[1] *= sy;

        this.move(-pivotX, -pivotY);

        this.position[0] *= sx;
        this.position[1] *= sy;

        this.move(pivotX, pivotY);

        for (var i = 0; i < this.shapes.length; i++){
            this.shapes[i].scale(sx, sy, pivotX, pivotY);
        }

    };

    Body.prototype.setScale = function(sx, sy, pivotX, pivotY){
        this.scale(sx / this.scaleXY[0], sy / this.scaleXY[1], pivotX, pivotY);
    };

    Body.prototype.rotate = function(angle, pivotX, pivotY){
        if (pivotX == null || pivotY == null){
            pivotX = this.position[0];
            pivotY = this.position[1];
        }

        this.rotation += angle;
        for (var i = 0; i < this.shapes.length; i++){
            this.shapes[i].rotate(angle, pivotX, pivotY);
        }

        // update position
        var x = this.position[0] - pivotX;
        var y = this.position[1] - pivotY;
        var newAngle = angle + Math.atan2(y, x) * 180 / Math.PI;
        var length = Math.pow(x * x + y * y, 0.5);
        this.position[0] = pivotX + length * Math.cos(newAngle * Math.PI / 180);
        this.position[1] = pivotY + length * Math.sin(newAngle * Math.PI / 180);
    };

    Body.prototype.setRotation = function(angle, pivotX, pivotY){
        this.rotate(angle - this.rotation, pivotX, pivotY);
    };

    Body.prototype.clone = function(){
        var b = clone(this);
        return b;
    };

}
