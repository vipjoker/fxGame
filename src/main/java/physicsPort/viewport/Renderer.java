package physicsPort.viewport;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import mygame.editor.util.Resources;
import physicsPort.body.Body;
import physicsPort.body.Joint;
import physicsPort.body.Shape;
import physicsPort.body.Vertex;


public class Renderer {
    public GraphicsContext context;
    public float width;
    public float height;
    public Color clearColor;
    public Color shapeColor;
    public Color shapeSelectedColor;
    public Color bodySelectedColor;
    public Color staticBodyColor;
    public Color kinematicBodyColor;
    public Color vertexColor;
    public Color boundsColor;
    public Image pivotImage;

    public Image[] jointAnchors;
    private double fontSize = 10 * 1.06;
    ;

    // to render viewport
    public Renderer(GraphicsContext context) {
        this.context = context;
        this.width = 0;
        this.height = 0;
        this.clearColor = Color.BLACK;
        this.shapeColor = Color.rgb(228, 177, 177, 0.6);
        this.shapeSelectedColor = Color.rgb(228, 228, 177, 0.6);
        this.bodySelectedColor = Color.rgb(0, 177, 177, 0.6);
        this.staticBodyColor = Color.rgb(177, 228, 177, 0.6);
        this.kinematicBodyColor = Color.rgb(177, 177, 228, 0.6);
        this.vertexColor = Color.rgb(255, 0, 0, 1);
        this.boundsColor = Color.rgb(228, 177, 177, 1);

        Image jointAnchor = Resources.initImage("resources/ui/crossair_white.png");

        Image jointAnchorA = Resources.initImage("resources/ui/crossair_blue.png");
        Image jointAnchorB = Resources.initImage("resources/ui/crossair_red.png");


        this.jointAnchors = new Image[]{jointAnchor, jointAnchorA, jointAnchorB};
        this.pivotImage = Resources.initImage("resources/ui/pivot.png");
    }

    public void renderVertex(Vertex v) {
        this.context.setFill(this.vertexColor);

        if (v.isSelected) {
            this.context.setStroke(Color.web("#0f0"));

            this.context.setLineWidth(2);
            this.renderBox(v.x, v.y, v.width, v.height, false);
            this.context.setLineWidth(1);

            // reset
            this.context.setStroke(Color.web("#000"));
        }

        this.renderBox(v.x, v.y, v.width, v.height, true);
    }

    ;

    public void setStageWidthHeight(float w, float h) {
        this.width = w;
        this.height = h;
    }

    ;

    public void renderJoint(Joint joint) {
        this.context.save();
        float width, height;

        width = (float) this.jointAnchors[0].getWidth();
        height = (float) this.jointAnchors[0].getHeight();

        if (joint.jointType != Joint.JOINT_REVOLUTE) {
            joint.position = new float[]{(joint.localAnchorA[0] + joint.localAnchorB[0]) / 2, (joint.localAnchorA[1] + joint.localAnchorB[1]) / 2};
        } else {
            joint.position = new float[]{(joint.bodyA.position[0] + joint.bodyB.position[0]) / 2, (joint.bodyA.position[1] + joint.bodyB.position[1]) / 2};
        }

        this.context.translate(joint.position[0], joint.position[1]);
        this.context.drawImage(this.jointAnchors[0], -width / 2, -height / 2, width, height);

        if (joint.isSelected) {
            this.context.translate(joint.localAnchorA[0] - joint.position[0], joint.localAnchorA[1] - joint.position[1]);
            this.context.drawImage(this.jointAnchors[1], -width / 2, -height / 2, width, height);
            if (joint.inEditMode) {
                this.context.setFill(Color.WHITE);
                this.context.setFont(new Font(10 * 1.06));
                this.context.fillText("localAnchorA", 10, 15);
            }
            this.context.translate(joint.localAnchorB[0] - joint.localAnchorA[0], joint.localAnchorB[1] - joint.localAnchorA[1]);
            this.context.drawImage(this.jointAnchors[2], -width / 2, -height / 2, width, height);
            if (joint.inEditMode) {
                this.context.setFill(Color.web("#f00"));
                this.context.setFont(new Font(10 * 1.06));
                this.context.fillText("localAnchorB", 10, -10);
            }
            if (joint.jointType == Joint.JOINT_PULLEY) {
                this.context.translate(joint.groundAnchorA[0] - joint.localAnchorB[0], joint.groundAnchorA[1] - joint.localAnchorB[1]);
                this.context.drawImage(this.jointAnchors[1], -width / 2, -height / 2, width, height);
                if (joint.inEditMode) {
                    this.context.setFill(Color.web("#0f0"));
                    this.context.setFont(new Font(10 * 1.06));
                    this.context.fillText("groundAnchorA", 10, -10);
                }
                this.context.translate(joint.groundAnchorB[0] - joint.groundAnchorA[0], joint.groundAnchorB[1] - joint.groundAnchorA[1]);
                this.context.drawImage(this.jointAnchors[2], -width / 2, -height / 2, width, height);
                if (joint.inEditMode) {
                    this.context.setFill(Color.web("#f00"));
                    this.context.setFont(new Font(10 * 1.06));
                    ;
                    this.context.fillText("groundAnchorB", 10, -10);
                }
            }
        }

        this.context.restore();

        this.context.beginPath();
        this.context.setLineDashes(5, 5);
        this.context.setLineWidth(2);
        this.context.setStroke(Color.web("#aaa"));
        if (joint.jointType == Joint.JOINT_DISTANCE) {
            this.context.moveTo(joint.localAnchorA[0], joint.localAnchorA[1]);
            this.context.lineTo(joint.localAnchorB[0], joint.localAnchorB[1]);
            this.context.stroke();
        }
        if (joint.jointType == Joint.JOINT_WELD) {
            this.context.setStroke(Color.web("#0f0"));
            this.context.beginPath();
            this.context.moveTo(joint.localAnchorB[0], joint.localAnchorB[1]);
            double x = joint.localAnchorB[0] + 100 * Math.cos(joint.referenceAngle * Math.PI / 180);
            double y = joint.localAnchorB[1] + 100 * Math.sin(joint.referenceAngle * Math.PI / 180);
            this.context.lineTo(x, y);
            this.context.stroke();
            if (joint.inEditMode) {
                this.context.setFill(Color.web("#0f0"));
                this.context.setFont(new Font(fontSize));
                ;
                this.context.fillText("referenceAngle", x + 10, y);
            }
            this.context.closePath();
        } else if (joint.jointType == Joint.JOINT_REVOLUTE) {
            // draw reference angle vector line
            this.context.setStroke(Color.web("#0f0"));
            this.context.beginPath();
            this.context.moveTo(joint.localAnchorB[0], joint.localAnchorB[1]);
            double x = joint.localAnchorB[0] + 100 * Math.cos(joint.referenceAngle * Math.PI / 180);
            double y = joint.localAnchorB[1] + 100 * Math.sin(joint.referenceAngle * Math.PI / 180);
            this.context.lineTo(x, y);
            this.context.stroke();
            if (joint.inEditMode) {
                this.context.setFill(Color.web("#0f0"));
                this.context.setFont(new Font(fontSize));
                ;

                this.context.fillText("referenceAngle", x + 10, y);
            }
            this.context.closePath();

            if (joint.enableLimit) {
                // draw lower angle vector line
                this.context.setStroke(Color.web("#f00"));
                this.context.beginPath();
                this.context.moveTo(joint.localAnchorB[0], joint.localAnchorB[1]);
                x = joint.localAnchorB[0] + 100 * Math.cos(joint.lowerAngle * Math.PI / 180);
                y = joint.localAnchorB[1] + 100 * Math.sin(joint.lowerAngle * Math.PI / 180);
                this.context.lineTo(x, y);
                this.context.stroke();
                if (joint.inEditMode) {
                    this.context.setFill(Color.web("#f00"));
                    this.context.setFont(new Font(fontSize));
                    ;

                    this.context.fillText("lowerAngle", x + 10, y - 10);
                }
                this.context.closePath();

                // draw lower angle arc
                this.context.arc(joint.localAnchorB[0], joint.localAnchorB[1], 30, joint.lowerAngle * Math.PI / 180, 0, 10);
                this.context.stroke();

                // draw upper angle vector line
                this.context.setStroke(Color.web("#00f"));
                this.context.beginPath();
                this.context.moveTo(joint.localAnchorB[0], joint.localAnchorB[1]);
                x = joint.localAnchorB[0] + 100 * Math.cos(joint.upperAngle * Math.PI / 180);
                y = joint.localAnchorB[1] + 100 * Math.sin(joint.upperAngle * Math.PI / 180);
                this.context.lineTo(x, y);
                this.context.stroke();
                if (joint.inEditMode) {
                    this.context.setFill(Color.web("#00f"));
                    this.context.setFont(new Font(fontSize));
                    ;

                    this.context.fillText("upperAngle", x + 10, y + 10);
                }
                this.context.closePath();

                // draw upper angle arc
                this.context.arc(joint.localAnchorB[0], joint.localAnchorB[1], 30, 0, joint.upperAngle * Math.PI / 180, 10);
                this.context.stroke();
            }
        } else if (joint.jointType == Joint.JOINT_WHEEL || joint.jointType == Joint.JOINT_PRISMATIC) {
            // draw local axis
            this.context.setStroke(Color.web("0f0"));
            this.context.beginPath();
            this.context.moveTo(joint.localAnchorB[0], joint.localAnchorB[1]);
            double angle = Math.atan2(joint.localAxisA[1], joint.localAxisA[0]);
            double x = joint.localAnchorB[0] + 100 * Math.cos(angle);
            double y = joint.localAnchorB[1] + 100 * Math.sin(angle);
            this.context.lineTo(x, y);
            this.context.stroke();
            if (joint.inEditMode) {
                this.context.setFill(Color.web("#0f0"));
                this.context.setFont(new Font(fontSize));
                ;

                this.context.fillText("localAxisA", x + 10, y);
            }
            this.context.closePath();

            if (joint.jointType == Joint.JOINT_PRISMATIC) {
                // draw reference angle
                this.context.setStroke(Color.web("#0f0"));
                this.context.beginPath();
                this.context.moveTo(joint.localAnchorB[0], joint.localAnchorB[1]);
                x = joint.localAnchorB[0] + 100 * Math.cos(joint.referenceAngle * Math.PI / 180);
                y = joint.localAnchorB[1] + 100 * Math.sin(joint.referenceAngle * Math.PI / 180);
                this.context.lineTo(x, y);
                this.context.stroke();
                if (joint.inEditMode) {
                    this.context.setFill(Color.web("#0f0"));
                    this.context.setFont(new Font(fontSize));

                    this.context.fillText("referenceAngle", x + 10, y);
                }
                this.context.closePath();

                if (joint.enableLimit) {
                    // this.context.beginPath();
                    this.context.setFill(Color.web("#f00"));
                    x = joint.localAnchorA[0] + joint.localAxisA[0] * joint.lowerTranslation;
                    y = joint.localAnchorA[1] + joint.localAxisA[1] * joint.lowerTranslation;
                    this.renderCircle((float) x, (float) y, 10, true);
                    this.context.fillText("lowerTranslation", x + 10, y);
                    this.context.setFill(Color.web("#00f"));
                    x = joint.localAnchorA[0] + joint.localAxisA[0] * joint.upperTranslation;
                    y = joint.localAnchorA[1] + joint.localAxisA[1] * joint.upperTranslation;
                    this.renderCircle((float) x, (float) y, 10, true);
                    if (joint.inEditMode) {
                        this.context.fillText("upperTranslation", x + 10, y);
                    }
                    // this.context.closePath();
                }
            }
        }

        this.context.setLineWidth(1);
        this.context.setLineDashes(0, 0);

        this.context.closePath();
    }

    public void renderBody(Body body) {
        // update aabb
        body.calculateBounds();

        // render sprite
        if (body.sprite != null) {
            this.context.save();
            double alpha = this.context.getGlobalAlpha();

            this.context.setGlobalAlpha(0.5);

            // if sprite is contained in a spritesheet
            if (body.spriteData.length > 2) {
                float sourceX = body.spriteData[0],
                        sourceY = body.spriteData[1],
                        sourceW = body.spriteData[2],
                        sourceH = body.spriteData[3],
                        imageW = body.spriteData[4];
                float imageH = body.spriteData[5];

                // handle sprite rotation and translation
                this.context.translate(body.position[0], body.position[1]);
                this.context.rotate(body.rotation * Math.PI / 180);

                // draw sprite
                this.context.drawImage(body.sprite.image, sourceX, sourceY, sourceW, sourceH, -imageW / 2, -imageH / 2, imageW, imageH);
            }
            // sprite is a separate image
            else {
                float imageW = body.sprite.width;
                float imageH = body.sprite.height;

                // handle sprite rotation and translation
                this.context.translate(body.position[0], body.position[1]);
                this.context.rotate(body.rotation * Math.PI / 180);

                // draw sprite
                this.context.drawImage(body.sprite.image, -imageW / 2, -imageH / 2, imageW, imageH);
            }

            this.context.setGlobalAlpha(alpha);
            this.context.restore();
        }

        // render shapes
        for (int i = 0; i < body.shapes.size(); i++) {
            if (body.bodyType == 0) {
                this.renderShape(body.shapes.get(i), this.staticBodyColor);
            } else if (body.bodyType == 1) {
                this.renderShape(body.shapes.get(i), this.kinematicBodyColor);
            } else {
                this.renderShape(body.shapes.get(i), null);
            }
        }

        // render aabb
        if (body.isSelected) {
            this.context.setStroke(this.bodySelectedColor);
            this.context.setLineWidth(2);
            this.renderBox(body.bounds[0], body.bounds[1], body.bounds[2], body.bounds[3], false);
            this.context.setLineWidth(1);
        }

        // draw position of body
        // this.context.fillStyle = "#000";
        // this.context.fillRect(body.position[0] - 5, body.position[1] - 5, 10, 10);
        this.context.drawImage(this.pivotImage, body.position[0] - 24, body.position[1] - 24, 48, 48);
    }

    ;

    public void renderShape(Shape shape, Color bodyColor) {
        shape.calculateBounds();

        if (shape.vertices.size() > 1) {
            this.context.beginPath();
            this.context.moveTo(shape.vertices.get(0).x, shape.vertices.get(0).y);

            if (shape.inEditMode)
                this.renderVertex(shape.vertices.get(0));

            for (int i = 1; i < shape.vertices.size(); i++) {
                this.context.lineTo(shape.vertices.get(i).x, shape.vertices.get(i).y);
                if (shape.inEditMode) {
                    this.renderVertex(shape.vertices.get(i));
                }
            }

            if (shape.shapeType == Shape.SHAPE_CHAIN) {
                this.context.setStroke(Color.web("#f00"));
                this.context.stroke();
            } else {
                this.context.closePath();
                this.context.setFill(bodyColor == null ? this.shapeColor : bodyColor);
                if (shape.isSelected) {
                    this.context.setFill(this.shapeSelectedColor);
                }
                this.context.fill();
            }

            // render aabb for polygon
            if (shape.isSelected) {
                this.context.setStroke(Color.web("#ff0"));
                this.context.setLineWidth(2);
                this.renderBox(shape.bounds[0], shape.bounds[1], shape.bounds[2], shape.bounds[3], false);
                this.context.setLineWidth(1);
            }

            this.context.setStroke(Color.BLACK);
        }

        // draw position of shape
        // this.context.fillStyle = "#000";
        // this.context.fillRect(shape.position[0] - 5, shape.position[1] - 5, 10, 10);
        this.context.drawImage(this.pivotImage, shape.position[0] - 16, shape.position[1] - 16, 32, 32);

        // draw centroid of shape
        // this.context.fillStyle = "#ff0";
        // this.context.fillRect(shape.centroid[0] - 5, shape.centroid[1] - 5, 10, 10);
    }

    ;

    public void renderGrid(int range) {
        int cell_size = 10;
        for (int x = -range; x <= range; x += cell_size) {
            this.context.moveTo(x * cell_size, -range * cell_size);
            this.context.lineTo(x * cell_size, range * cell_size);
        }
        for (int y = -range; y <= range; y += cell_size) {
            this.context.moveTo(-range * cell_size, y * cell_size);
            this.context.lineTo(range * cell_size, y * cell_size);
        }
        this.context.setStroke(Color.web("#f00"));
        this.context.setLineWidth(0.15 + 0.05 / cell_size);
        this.context.stroke();

        cell_size = 5;
        for (int x = -range; x <= range; x += cell_size) {
            if (x == 0) {                                    // to darken y - axis
                this.context.moveTo(-0.05 * cell_size, -range * cell_size);
                this.context.lineTo(-0.05 * cell_size, range * cell_size);

                this.context.moveTo(-0.025 * cell_size, -range * cell_size);
                this.context.lineTo(-0.025 * cell_size, range * cell_size);

                this.context.moveTo(x * cell_size, -range * cell_size);
                this.context.lineTo(x * cell_size, range * cell_size);

                this.context.moveTo(0.025 * cell_size, -range * cell_size);
                this.context.lineTo(0.025 * cell_size, range * cell_size);

                this.context.moveTo(0.05 * cell_size, -range * cell_size);
                this.context.lineTo(0.05 * cell_size, range * cell_size);
            }
            this.context.moveTo(x * cell_size, -range * cell_size);
            this.context.lineTo(x * cell_size, range * cell_size);
        }
        for (int y = -range; y <= range; y += cell_size) {
            if (y == 0) {                                    // to darken x - axis
                this.context.moveTo(-range * cell_size, -0.05 * cell_size);
                this.context.lineTo(range * cell_size, -0.05 * cell_size);

                this.context.moveTo(-range * cell_size, -0.025 * cell_size);
                this.context.lineTo(range * cell_size, -0.025 * cell_size);

                this.context.moveTo(-range * cell_size, y * cell_size);
                this.context.lineTo(range * cell_size, y * cell_size);

                this.context.moveTo(-range * cell_size, 0.025 * cell_size);
                this.context.lineTo(range * cell_size, 0.025 * cell_size);

                this.context.moveTo(-range * cell_size, 0.05 * cell_size);
                this.context.lineTo(range * cell_size, 0.05 * cell_size);
            }
            this.context.moveTo(-range * cell_size, y * cell_size);
            this.context.lineTo(range * cell_size, y * cell_size);
        }
        this.context.setStroke(Color.web("#0f0"));
        this.context.setLineWidth(0.1 + 0.05 / cell_size);
        this.context.stroke();
        this.context.setLineWidth(1);
        this.context.setStroke(Color.BLACK);

        // draw scale
        this.context.setFill(Color.web("#0f0"));
        this.context.setFont(new Font(10 * (1 + 0.6 / cell_size)));
        for (int x = -range; x <= range; x += cell_size) {
            if (x != 0 && (x * cell_size) % 100 == 0) {
                this.context.fillText(x * cell_size + "", x * cell_size - 10, (-20 / cell_size));
                this.renderCircle(x * cell_size, 0, 3 * (1 + 0.5f / cell_size), true);
            }
        }
        for (int y = -range; y <= range; y += cell_size) {
            if (y != 0 && (y * cell_size) % 100 == 0) {
                this.context.fillText(y * cell_size + "", (20 / cell_size), y * cell_size + 5);
                this.renderCircle(0, y * cell_size, 3 * (1 + 0.5f / cell_size), true);
            }
        }
    }

    ;

    public void renderBox(float x, float y, float w, float h, boolean fill) {
        if (fill) this.context.fillRect(x - w / 2, y - h / 2, w, h);
        this.context.strokeRect(x - w / 2, y - h / 2, w, h);
    }

    public void renderCircle(float x, float y, float r, boolean fill) {
        this.context.beginPath();

        this.context.strokeOval(x, y, r * 2, r * 2);
        if (fill) this.context.fill();
        this.context.closePath();
        this.context.stroke();
    }




    public void clear(float x, float y, float w, float h) {
        this.context.setFill(this.clearColor);
        this.context.clearRect(x, y, w, h);
        this.context.fillRect(x, y, w, h);
    }

    ;

    public GraphicsContext getContext() {
        return context;
    }


    public void setLineDash(int i, int i1) {
        context.setLineDashes(i,i1);
    }
}
