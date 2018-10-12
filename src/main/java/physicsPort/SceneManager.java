package physicsPort;

import com.badlogic.gdx.Input;
import javafx.scene.input.MouseEvent;
import physicsPort.body.*;
import physicsPort.viewport.InputHandler;
import physicsPort.viewport.Navigator;
import sun.security.provider.SHA;

import java.util.ArrayList;
import java.util.List;

public class SceneManager {

           public static final int STATE_DEFAULT_MODE 	= 0;
           public static final int STATE_SHAPE_EDIT_MODE 	= 1;
           public static final int STATE_BODY_EDIT_MODE 	= 2;

            public List<Body> bodies;
            public List<Joint> joints;
            public List <Body>selectedBodies;
            public List<Shape> selectedShapes;
            public List <Vertex>selectedVertices;
            public List<Joint> selectedJoints;
            public int selectedAnchor;
    public int state;
    public  SceneManager (){


            this.state 				= STATE_DEFAULT_MODE;
            this.bodies 			= new ArrayList<>();
            this.joints 			= new ArrayList<>();
            this.selectedBodies 	= new ArrayList<>();
            this.selectedShapes 	= new ArrayList<>();
            this.selectedVertices 	= new ArrayList<>();
            this.selectedJoints 	= new ArrayList<>();
        }

        public void enterDefaultMode (){
            this.state = STATE_DEFAULT_MODE;

            if (!this.selectedShapes.isEmpty()){
                this.selectedShapes.get(0).inEditMode = false;
                this.selectedShapes.get(0).isSelected = false;
            }
        };

        public void enterBodyEditMode (){
            if (this.selectedBodies.size()> 1 || this.selectedBodies.size()< 1)
                return;

            if (this.selectedShapes.size() > 0)
                this.selectedShapes.get(0).inEditMode = false;

            this.state = STATE_BODY_EDIT_MODE;
        };

        public void enterShapeEditMode (){
            if (this.state != STATE_BODY_EDIT_MODE)
                return;

            if (this.selectedShapes.size() != 1)
                return;

            this.state = STATE_SHAPE_EDIT_MODE;
            this.selectedShapes.get(0).inEditMode = true;
        };

        public void deleteSelectedObjects (){
            if (this.state == STATE_DEFAULT_MODE){
                for (int i = 0; i < this.selectedBodies.size(); i++){
                    this.removeBody(this.selectedBodies.get(i));
                }
                for (int i = 0; i < this.selectedJoints.size(); i++){
                    this.removeJoint(this.selectedJoints.get(i));
                }
            }
            else if (this.state == STATE_BODY_EDIT_MODE){
                for (int i = 0; i < this.selectedShapes.size(); i++){
                    this.selectedBodies.get(0).removeShapeGivenShape(this.selectedShapes.get(i));
                }
            }
            else if (this.state == STATE_SHAPE_EDIT_MODE){
                for (int i = 0; i < this.selectedVertices.size(); i++){
                    this.selectedShapes.get(0).removeVertexGivenVertex(this.selectedVertices.get(i));
                }
            }
        };

        public void duplicateSelection (){
            if (this.state == STATE_DEFAULT_MODE){
                for (int  i = 0; i < this.selectedBodies.size(); i++){
                    this.addBody(this.selectedBodies.get(i).clone());
                }
                for (int i = 0; i < this.selectedJoints.size(); i++){
                    this.addJoint(this.selectedJoints.get(i).clone());
                    // this.addBody(this.joints[this.joints.length - 1].bodyB);
                }
            }
            else if (this.state == STATE_BODY_EDIT_MODE){
                for (int i = 0; i < this.selectedShapes.size(); i++){
                    this.selectedBodies.get(0).addShape(this.selectedShapes.get(i).clone(),false);
                }
            }
            else if (this.state == STATE_SHAPE_EDIT_MODE){
                for (int i = 0; i < this.selectedVertices.size(); i++){
                    this.selectedShapes.get(0).vertices.remove(this.selectedShapes.get(0).indexOfVertex(this.selectedVertices.get(i)) + 1);
                }
            }
        };

        // don't use only aabb collision detection for chain shapes, instead use its edges
        public boolean checkCollisionWithChainShape (float pointx,float  pointy,Shape shape){
            LineSegment lineSegment;
            int index = 0;
            for (int i = 0; i < shape.vertices.size(); i++){
                if (i == shape.vertices.size() - 1){
                    lineSegment = new LineSegment(shape.vertices.get(i).x, shape.vertices.get(i).y, shape.vertices.get(0).x, shape.vertices.get(0).y);
                }
                else{
                    lineSegment = new LineSegment(shape.vertices.get(i).x, shape.vertices.get(i).y, shape.vertices.get(i + 1).x, shape.vertices.get(i + 1).y);
                }

                // use some threshold to determine collision
                if (lineSegment.distanceFromPoint(pointx, pointy) < 10){
                    if (lineSegment.checkInBoundsX(pointx) || lineSegment.checkInBoundsY(pointy)){
                        return true;
                    }
                }
            }
            return false;
        }

        // for selecting objects
        public boolean onMouseDown (MouseEvent e, InputHandler inputHandler, Navigator navigator){
            float eoffsetX = (float) e.getX();
            float eoffsetY = (float) e.getY();

            if (this.state == STATE_SHAPE_EDIT_MODE){
                // for rendering vertices
                this.selectedShapes.get(0).inEditMode = true;

                // for adding vertex to the selected shape
                if (InputHandler.CTRL_PRESSED != 0){
                    float[] point = navigator.screenPointToWorld(eoffsetX, eoffsetY);
                    this.selectedShapes.get(0).addVertex(point[0], point[1]);
                    return true;
                }

                // for handling multiple vertices
                if (this.selectedVertices.size()> 1) {
                    for (int i = 0; i < this.selectedVertices.size(); i++){
                        Vertex vertex = this.selectedVertices.get(i);
                        if (navigator.checkPointInAABB(eoffsetX, eoffsetY,new float[] {vertex.x, vertex.y, vertex.width, vertex.height})){
                            if (InputHandler.SHIFT_PRESSED != 0){
                                break;
                            }
                            return true;
                        }
                    }
                }

                if (InputHandler.SHIFT_PRESSED == 0){
                    this.selectedVertices.clear();
                }
                boolean vertexInBounds = false;
                for (int i = 0; i < this.selectedShapes.get(0).vertices.size(); i++){
                    Vertex vertex = this.selectedShapes.get(0).vertices.get(i);

                    if (InputHandler.SHIFT_PRESSED == 0){
                        vertex.isSelected = false;
                    }
                    if (navigator.checkPointInAABB(eoffsetX, eoffsetY, new float[]{vertex.x, vertex.y, vertex.width, vertex.height})){
                        if (InputHandler.SHIFT_PRESSED == 0){
                            this.selectedVertices.set(0,vertex);
                            vertex.isSelected = true;
                        }
                        else {
                            if (this.selectedVertices.indexOf(vertex) < 0){
                                this.selectedVertices.add(vertex);
                                vertex.isSelected = true;
                            }
                        }
                        vertexInBounds = true;
                    }
                }
                return vertexInBounds;
            }

            if (this.state == STATE_BODY_EDIT_MODE){
                // for handling multiple shapes
                if (this.selectedShapes.size() > 1) {
                    for (int i = 0; i < this.selectedShapes.size(); i++){
                        Shape shape = this.selectedShapes.get(i);
                        if (navigator.checkPointInAABB(eoffsetX, eoffsetY, shape.bounds)){
                            // check for chain shapes
                            if (shape.shapeType == Shape.SHAPE_CHAIN){
                                float[] screenPointToWorld = navigator.screenPointToWorld(eoffsetX, eoffsetY);
                                if (!this.checkCollisionWithChainShape(screenPointToWorld[0], screenPointToWorld[1], shape)){
                                    continue;
                                }
                            }

                            if (InputHandler.SHIFT_PRESSED == 1){
                                break;
                            }
                            return true;
                        }
                    }
                }

                // don't reset selectedShapes array if shift is pressed (multiple selection)
                if (InputHandler.SHIFT_PRESSED== 0){
                    this.selectedShapes.clear();
                }
                float minDistance = 1000000000;
                float distance= 0;
                boolean shapeInBounds = false;
                for (int i = 0; i < this.selectedBodies.get(0).shapes.size(); i++){
                    Shape shape = this.selectedBodies.get(0).shapes.get(i);

                    if (InputHandler.SHIFT_PRESSED == 0){
                        shape.isSelected = false;
                    }

                    // check if test point is in the shape
                    if (navigator.checkPointInAABB(eoffsetX, eoffsetY, shape.bounds)){

                        float[] point = navigator.worldPointToScreen(shape.position[0], shape.position[1]);
                        distance = (point[0] - eoffsetX) * (point[0] - eoffsetX) + (point[1] - eoffsetY) * (point[1] - eoffsetY);
                        // check for minimum distance in case the test point is in multiple shapes
                        if (minDistance >= distance){

                            // if shape is chain_shape the check for intersection between test point and its edges with some threshold
                            if (shape.shapeType == Shape.SHAPE_CHAIN){
                                float[] screenPointToWorld = navigator.screenPointToWorld(eoffsetX, eoffsetY);
                                if (!this.checkCollisionWithChainShape(screenPointToWorld[0], screenPointToWorld[1], shape)){
                                    continue;
                                }
                            }

                            // multiple selection is disabled
                            if (InputHandler.SHIFT_PRESSED ==0){
                                this.selectedShapes.set(0,shape);
                                shape.isSelected = true;
                            }
                            // user is holding shift, so multiple selection is active
                            else {
                                // check if the shape is already selected
                                if (this.selectedShapes.indexOf(shape) < 0){
                                    this.selectedShapes.add(shape);
                                    shape.isSelected = true;
                                }
                            }

                            // shape selected so return true
                            shapeInBounds = true;
                            minDistance = distance;
                        }
                    }
                }
                return shapeInBounds;
            }

            if (this.state == STATE_DEFAULT_MODE){
                // editing joints
                if (this.selectedJoints.size()== 1 && this.selectedJoints.get(0).inEditMode){
                    Joint joint = this.selectedJoints.get(0);
                    joint.isSelected = true;
                    if (navigator.checkPointInAABB(eoffsetX, eoffsetY, joint.getAnchorABounds())){
                        this.selectedAnchor = 0;
                        return true;
                    }
                    else if (navigator.checkPointInAABB(eoffsetX, eoffsetY, joint.getAnchorBBounds())){
                        this.selectedAnchor = 1;
                        return true;
                    }

                    if (joint.jointType == Joint.JOINT_PULLEY){
                        if (navigator.checkPointInAABB(eoffsetX, eoffsetY, joint.getGroundAnchorABounds())){
                            this.selectedAnchor = 2;
                            return true;
                        }
                        else if (navigator.checkPointInAABB(eoffsetX, eoffsetY, joint.getGroundAnchorBBounds())){
                            this.selectedAnchor = 3;
                            return true;
                        }
                    }

                    this.selectedAnchor = -1;

                    if (navigator.checkPointInAABB(eoffsetX, eoffsetY, joint.bodyA.bounds) && joint.jointType != Joint.JOINT_PULLEY){
                        this.selectedAnchor = 2;
                        return true;
                    }
                    else if (navigator.checkPointInAABB(eoffsetX, eoffsetY, joint.bodyB.bounds)  && joint.jointType != Joint.JOINT_PULLEY){
                        this.selectedAnchor = 3;
                        return true;
                    }

                    return false;
                }

                // for handling multiple bodies
                if (this.selectedBodies.size() + this.selectedJoints.size() > 1){
                    for (int i = 0; i < this.selectedBodies.size(); i++){
                        Body body = this.selectedBodies.get(i);
                        if (navigator.checkPointInAABB(eoffsetX, eoffsetY, body.bounds)){
                            if (InputHandler.SHIFT_PRESSED == 1){
                                break;
                            }
                            return true;
                        }
                    }
                }

                // for handling multiple joints
                if (this.selectedJoints.size() + this.selectedBodies.size() > 1){
                    for (int i = 0; i < this.selectedJoints.size(); i++){
                        Joint joint = this.selectedJoints.get(i);
                        if (navigator.checkPointInAABB(eoffsetX, eoffsetY, joint.getBounds())){
                            if (InputHandler.SHIFT_PRESSED == 1){
                                break;
                            }
                            return true;
                        }
                    }
                }

                if (InputHandler.SHIFT_PRESSED == 0){
                    this.selectedBodies.clear();
                    this.selectedJoints.clear();
                }
                float minDistance = 1000000000;
                float distance = 0;
                boolean bodyInBounds = false;
                boolean jointInBounds = false;
                if (!InputHandler.J_KEY_PRESSED){
                    for (int i = 0; i < this.bodies.size(); i++){
                        Body body = this.bodies.get(i);

                        if(InputHandler.SHIFT_PRESSED==0){
                            body.isSelected = false;
                        }

                        if (navigator.checkPointInAABB(eoffsetX, eoffsetY, body.bounds)){
                            float[] point = navigator.worldPointToScreen(body.position[0], body.position[1]);
                            distance = (point[0] - eoffsetX) * (point[0] - eoffsetX) + (point[1] - eoffsetY) * (point[1] - eoffsetY);
                            if (minDistance >= distance){
                                if (InputHandler.SHIFT_PRESSED == 0){
                                    this.selectedBodies.set(0,body);
                                    body.isSelected = true;
                                }
                                else {
                                    if (this.selectedBodies.indexOf(body) < 0){
                                        this.selectedBodies.add(body);
                                        body.isSelected = true;
                                    }
                                }
                                minDistance = distance;
                            }
                            bodyInBounds = true;
                        }
                    }
                }
                if (!InputHandler.B_KEY_PRESSED){
                    minDistance = 1000000000;
                    for (int i = 0; i < this.joints.size(); i++){
                        Joint joint = this.joints.get(i);

                        if(InputHandler.SHIFT_PRESSED==0){
                            joint.isSelected = false;
                        }

                        if (navigator.checkPointInAABB(eoffsetX, eoffsetY, joint.getBounds())){
                            float[] point = navigator.worldPointToScreen(joint.position[0], joint.position[1]);
                            distance = (point[0] - eoffsetX) * (point[0] - eoffsetX) + (point[1] - eoffsetY) * (point[1] - eoffsetY);
                            if (minDistance >= distance){
                                if (InputHandler.SHIFT_PRESSED==0){
                                    this.selectedJoints.set(0,joint);
                                    joint.isSelected = true;
                                }
                                else {
                                    if (this.selectedJoints.indexOf(joint) < 0){
                                        this.selectedJoints.add(joint);
                                        joint.isSelected = true;
                                    }
                                }
                                minDistance = distance;
                            }
                            jointInBounds = true;
                        }
                    }
                }
                return bodyInBounds || jointInBounds;
            }
            return false;
        };

        /**
         *
         * params x,	 			position on x - axis, null if only y pos is to be set (use null only when move = 0)
         * params y,    			position on y - axis, null if only x pos is to be set (use null only when move = 0)
         * params move, 			1 for moving, 0 for setting position
         * params inputHandler, 	information about pivot mode and snapping data
         */
        public void setPositionOfSelectedObjects (float x,float  y,float move, InputHandler inputHandler){
            if (this.state == STATE_DEFAULT_MODE){
                // move anchor
                if (this.selectedJoints.size() == 1 && this.selectedJoints.get(0).inEditMode){
                    Joint joint = this.selectedJoints.get(0);
                    if (this.selectedAnchor == 0){
                        if (InputHandler.SNAPPING_ENABLED){
                            joint.setLocalAnchorA(inputHandler.pointerWorldPos[2] / inputHandler.snappingData[0] * inputHandler.snappingData[0],
                                    inputHandler.pointerWorldPos[3] / inputHandler.snappingData[0] * inputHandler.snappingData[0]);
                        }
                        else {
                            joint.moveAnchorA(x, y);
                        }
                    }
                    else if (this.selectedAnchor == 1){
                        if (InputHandler.SNAPPING_ENABLED){
                            joint.setLocalAnchorB(inputHandler.pointerWorldPos[2] / inputHandler.snappingData[0] * inputHandler.snappingData[0],
                                    inputHandler.pointerWorldPos[3] / inputHandler.snappingData[0] * inputHandler.snappingData[0]);
                        }
                        else {
                            joint.moveAnchorB(x, y);
                        }
                    }
                    else if (this.selectedAnchor == 2){
                        if (joint.jointType == Joint.JOINT_WELD || joint.jointType == Joint.JOINT_REVOLUTE){
                            if (InputHandler.SHIFT_PRESSED != 0){
                                joint.changeReferenceAngle(x);
                            }
                        }
                        else if (joint.jointType == Joint.JOINT_PULLEY){
                            if (InputHandler.SNAPPING_ENABLED){
                                joint.setGroundAnchorA(inputHandler.pointerWorldPos[2] / inputHandler.snappingData[0] * inputHandler.snappingData[0],
                                                inputHandler.pointerWorldPos[3] / inputHandler.snappingData[0] * inputHandler.snappingData[0]);
                            }
                            else {
                                joint.moveGroundAnchorA(x, y);
                            }
                        }
                        else if (joint.jointType == Joint.JOINT_PRISMATIC){
                            if (InputHandler.SHIFT_PRESSED != 0){
                                joint.changeReferenceAngle(x);
                            }
                            else {
                                joint.changeLowerAngle(x);
                            }
                        }
                        else if (joint.jointType == Joint.JOINT_WHEEL){
                            if (InputHandler.SHIFT_PRESSED !=0){
                                joint.changeLowerAngle(x);
                            }
                        }
                    }
                    else if (this.selectedAnchor == 3){
                        if (joint.jointType == Joint.JOINT_REVOLUTE){
                            if (joint.enableLimit){
                                if (InputHandler.SHIFT_PRESSED != 0){
                                    joint.changeUpperAngle(x);
                                }
                                else {
                                    joint.changeLowerAngle(x);
                                }
                            }
                        }
                        else if (joint.jointType == Joint.JOINT_PRISMATIC){
                            if (joint.enableLimit){
                                if (InputHandler.SHIFT_PRESSED!=0){
                                    joint.upperTranslation += x;
                                }
                                else {
                                    joint.lowerTranslation += x;
                                }
                            }
                        }
                        else if (joint.jointType == Joint.JOINT_PULLEY){
                            if (InputHandler.SNAPPING_ENABLED){
                                joint.setGroundAnchorB(inputHandler.pointerWorldPos[2] / inputHandler.snappingData[0] * inputHandler.snappingData[0],
                                        inputHandler.pointerWorldPos[3] / inputHandler.snappingData[0] * inputHandler.snappingData[0]);
                            }
                            else {
                                joint.moveGroundAnchorB(x, y);
                            }
                        }
                    }
                    return;
                }

                for (int i = 0; i < this.selectedBodies.size(); i++){
                    if (move>0){
                        this.selectedBodies.get(i).move(x, y);
                        if (InputHandler.SNAPPING_ENABLED){
                            this.selectedBodies.get(i).setPosition(inputHandler.pointerWorldPos[2] / inputHandler.snappingData[0] * inputHandler.snappingData[0],
                                    inputHandler.pointerWorldPos[3] / inputHandler.snappingData[0] * inputHandler.snappingData[0]);
                        }
                    }
                    else{
                        float px = x == 0? this.selectedBodies.get(i).position[0] : x;
                        float py = y == 0? this.selectedBodies.get(i).position[1] : y;
                        this.selectedBodies.get(i).setPosition(px, py);
                    }
                }
                // joints
                for (int i = 0; i < this.selectedJoints.size(); i++){
                    if (move>0){
                        this.selectedJoints.get(i).move(x, y);
                        if (InputHandler.SNAPPING_ENABLED){
                            this.selectedJoints.get(i).setPosition(inputHandler.pointerWorldPos[2] / inputHandler.snappingData[0] * inputHandler.snappingData[0],
                                    inputHandler.pointerWorldPos[3] / inputHandler.snappingData[0] * inputHandler.snappingData[0]);
                        }
                    }
                    else{
                        float px = x == 0? this.selectedJoints.get(i).position[0] : x;
                        float py = y == 0? this.selectedJoints.get(i).position[1] : y;
                        this.selectedJoints.get(i).setPosition(px, py);
                    }
                }
            }
            else if (this.state == STATE_BODY_EDIT_MODE){
                for (int i = 0; i < this.selectedShapes.size(); i++){
                    if (move>0){
                        this.selectedShapes.get(i).move(x, y);
                        if (InputHandler.SNAPPING_ENABLED){
                            this.selectedShapes.get(i).setPosition(inputHandler.pointerWorldPos[2] / inputHandler.snappingData[0] * inputHandler.snappingData[0],
                                    inputHandler.pointerWorldPos[3] / inputHandler.snappingData[0] * inputHandler.snappingData[0]);
                        }
                    }
                    else {
                        float px = x == 0? this.selectedShapes.get(i).position[0] : x;
                        float py = y == 0? this.selectedShapes.get(i).position[1] : y;
                        this.selectedShapes.get(i).setPosition(px, py);
                    }
                }
            }
            else if (	this.state == STATE_SHAPE_EDIT_MODE &&
                    this.selectedShapes.get(0).shapeType != Shape.SHAPE_BOX &&
                    this.selectedShapes.get(0).shapeType != Shape.SHAPE_CIRCLE){
                for (int i = 0; i < this.selectedVertices.size(); i++){
                    if (move>0){
                        this.selectedVertices.get(i).x = x + this.selectedVertices.get(i).x * move;
                        this.selectedVertices.get(i).y = y + this.selectedVertices.get(i).y * move;

                        if (InputHandler.SNAPPING_ENABLED){
                            this.selectedVertices.get(i).x = inputHandler.pointerWorldPos[2] / inputHandler.snappingData[0] * inputHandler.snappingData[0];
                            this.selectedVertices.get(i).y = inputHandler.pointerWorldPos[3] / inputHandler.snappingData[0] * inputHandler.snappingData[0];
                        }
                    }
                    else {
                        float px = x == 0? this.selectedVertices.get(i).x : x;
                        float py = y == 0? this.selectedVertices.get(i).y : y;
                        this.selectedVertices.get(i).x = px;
                        this.selectedVertices.get(i).y = py;
                    }
                }
            }
        };

        /**
         *
         * params sx,	 			x scale, null if only y scale is to be set (use null only when scale = 0)
         * params sy,    			y scale, null if only x scale is to be set (use null only when scale = 0)
         * params scale, 			1 for scaling, 0 for setting scale
         * params inputHandler, 	information about pivot mode and snapping data
         */
        public void setScaleOfSelectedObjects (float sx, float sy,float scale,InputHandler inputHandler){
            if (this.state == STATE_DEFAULT_MODE){
                if (inputHandler.pivotMode == 3){								// InputHandler.PIVOT_LOCAL_MODE
                    for (int i = 0; i < this.selectedBodies.size(); i++){
                        if (scale > 0){
                            this.selectedBodies.get(i).scale(sx, sy);
                        }
                        else {
                            float sclx = sx == 0? this.selectedBodies.get(i).scaleXY[0] : sx;
                            float scly = sy == 0 ? this.selectedBodies.get(i).scaleXY[1] : sy;
                            this.selectedBodies.get(i).setScale(sclx, scly,0,0);
                        }
                    }
                    // joints
                    for (int  i = 0; i < this.selectedJoints.size(); i++){
                        if (scale> 0){
                            this.selectedJoints.get(i).scale(sx, sy);
                        }
                        else{
                            float sclx = sx == 0? this.selectedJoints.get(i).scaleXY[0] : sx;
                            float scly = sy == 0? this.selectedJoints.get(i).scaleXY[1] : sy;
                            this.selectedJoints.get(i).setScale(sclx, scly,0,0);
                        }
                    }
                    return;
                }

                // if selection center is used as pivot (selection center)
                float[] pivot =new float [2];
                for (int i = 0; i < this.selectedBodies.size(); i++){
                    pivot[0] += this.selectedBodies.get(i).position[0];
                    pivot[1] += this.selectedBodies.get(i).position[1];
                }
                for (int i = 0; i < this.selectedJoints.size(); i++){
                    pivot[0] += this.selectedJoints.get(i).position[0];
                    pivot[1] += this.selectedJoints.get(i).position[1];
                }
                pivot[0] /= (this.selectedBodies.size() + this.selectedJoints.size());
                pivot[1] /= (this.selectedBodies.size() + this.selectedJoints.size());

                for (int i = 0; i < this.selectedBodies.size(); i++){
                    if (scale > 0){
                        this.selectedBodies.get(i).scale(sx, sy, pivot[0], pivot[1]);
                    }
                    else {
                        float sclx = sx == 0 ? this.selectedBodies.get(i).scaleXY[0] : sx;
                        float scly = sy == 0 ? this.selectedBodies.get(i).scaleXY[1] : sy;
                        this.selectedBodies.get(i).setScale(sclx, scly, pivot[0], pivot[1]);
                    }
                }
                // joints
                for (int i = 0; i < this.selectedJoints.size(); i++){
                    if (scale> 0){
                        this.selectedJoints.get(i).scale(sx, sy, pivot[0], pivot[1]);
                    }
                    else{
                        float sclx = sx ==0 ? this.selectedJoints.get(i).scaleXY[0] : sx;
                        float scly = sy ==0 ? this.selectedJoints.get(i).scaleXY[1] : sy;
                        this.selectedJoints.get(i).setScale(sclx, scly, pivot[0], pivot[1]);
                    }
                }
            }
            else if (this.state == STATE_BODY_EDIT_MODE){
                if (inputHandler.pivotMode == 3){
                    for (int i = 0; i < this.selectedShapes.size(); i++){
                        if (scale>0){
                            this.selectedShapes.get(i).scale(sx, sy);
                        }
                        else {
                            float sclx = sx == 0? this.selectedShapes.get(i).scaleXY[0] : sx;
                            float scly = sy == 0? this.selectedShapes.get(i).scaleXY[1] : sy;
                            this.selectedShapes.get(i).setScale(sclx, scly,0,0);
                        }
                    }
                    return;
                }

                float[] pivot = new float[2];
                for (int i = 0; i < this.selectedShapes.size(); i++){
                    pivot[0] += this.selectedShapes.get(i).position[0];
                    pivot[1] += this.selectedShapes.get(i).position[1];
                }
                pivot[0] /= this.selectedShapes.size();
                pivot[1] /= this.selectedShapes.size();

                for (int i = 0; i < this.selectedShapes.size(); i++){
                    if (scale > 0){
                        this.selectedShapes.get(i).scale(sx, sy, pivot[0], pivot[1]);
                    }
                    else {
                        float sclx = sx == 0 ? this.selectedShapes.get(i).scaleXY[0] : sx;
                        float scly = sy == 0 ? this.selectedShapes.get(i).scaleXY[1] : sy;
                        this.selectedShapes.get(i).setScale(sclx, scly, pivot[0], pivot[1]);
                    }
                }
            }
            else if (	this.state == STATE_SHAPE_EDIT_MODE &&
                    this.selectedShapes.get(0).shapeType != Shape.SHAPE_BOX &&
                    this.selectedShapes.get(0).shapeType != Shape.SHAPE_CIRCLE){
                if (this.selectedVertices.size() < 1)
                    return;

                // here we always use selection center pivot mode
                float []pivot = new float[2];
                for (int i = 0; i < this.selectedVertices.size(); i++){
                    pivot[0] += this.selectedVertices.get(i).x;
                    pivot[1] += this.selectedVertices.get(i).y;
                }
                pivot[0] /= this.selectedVertices.size();
                pivot[1] /= this.selectedVertices.size();

                for (int i = 0; i < this.selectedVertices.size(); i++){
                    Vertex vertex = this.selectedVertices.get(i);
                    vertex.move(-pivot[0], -pivot[1]);
                    float sclx = sx == 0? 1 : sx;
                    float scly = sy == 0? 1 : sy;
                    vertex.x *= sclx;
                    vertex.y *= scly;
                    vertex.move(pivot[0], pivot[1]);
                }
            }
        };

        /**
         *
         * params angle,	 		rotation
         * params rotate,    	1 for rotating, 0 for setting rotation (do not use rotate = 0 when editing vertices)
         * params inputHandler, 	information about pivot mode and snapping data
         */
        public void setRotationOfSelectedObjects (float angle, boolean rotate, InputHandler inputHandler){
            if (this.state == STATE_DEFAULT_MODE){
                if (inputHandler.pivotMode == 3){								// InputHandler.PIVOT_LOCAL_MODE
                    for (int i = 0; i < this.selectedBodies.size(); i++){
                        if (rotate){
                            this.selectedBodies.get(i).rotate(angle);
                        }
                        else {
                            this.selectedBodies.get(i).setRotation(angle,0,0);
                        }
                    }
                    return;
                }

                // if selection center is used as pivot (selection center)
                float[] pivot = new float[2];
                for (int i = 0; i < this.selectedBodies.size(); i++){
                    pivot[0] += this.selectedBodies.get(i).position[0];
                    pivot[1] += this.selectedBodies.get(i).position[1];
                }
                pivot[0] /= this.selectedBodies.size();
                pivot[1] /= this.selectedBodies.size();

                for (int i = 0; i < this.selectedBodies.size(); i++){
                    if (rotate){
                        this.selectedBodies.get(i).rotate(angle, pivot[0], pivot[1]);
                    }
                    else {
                        this.selectedBodies.get(i).setRotation(angle, pivot[0], pivot[1]);
                    }
                }
            }
            else if (this.state == STATE_BODY_EDIT_MODE){
                if (inputHandler.pivotMode == 3){
                    for (int i = 0; i < this.selectedShapes.size(); i++){
                        if (rotate){
                            this.selectedShapes.get(i).rotate(angle);
                        }
                        else {
                            this.selectedShapes.get(i).setRotation(angle,0,0);
                        }

                    }
                    return;
                }

                float[] pivot = new float[2];
                for (int i = 0; i < this.selectedShapes.size(); i++){
                    pivot[0] += this.selectedShapes.get(i).position[0];
                    pivot[1] += this.selectedShapes.get(i).position[1];
                }
                pivot[0] /= this.selectedShapes.size();
                pivot[1] /= this.selectedShapes.size();

                for (int i = 0; i < this.selectedShapes.size(); i++){
                    if (rotate){
                        this.selectedShapes.get(i).rotate(angle, pivot[0], pivot[1]);
                    }
                    else {
                        this.selectedShapes.get(i).setRotation(angle, pivot[0], pivot[1]);
                    }
                }
            }
            else if (	this.state == STATE_SHAPE_EDIT_MODE &&
                    this.selectedShapes.get(0).shapeType != Shape.SHAPE_BOX &&
                    this.selectedShapes.get(0).shapeType != Shape.SHAPE_CIRCLE){
                if (this.selectedVertices.size()< 1)
                    return;

                // here we always use selection center pivot mode
                float [] pivot = new float[2];
                for (int i = 0; i < this.selectedVertices.size(); i++){
                    pivot[0] += this.selectedVertices.get(i).x;
                    pivot[1] += this.selectedVertices.get(i).y;
                }
                pivot[0] /= this.selectedVertices.size();
                pivot[1] /= this.selectedVertices.size();

                for (int i = 0; i < this.selectedVertices.size(); i++){
                    Vertex vertex = this.selectedVertices.get(i);
                    float x = vertex.x - pivot[0];
                    float y = vertex.y - pivot[1];
                    float newAngle = angle + (rotate ?1:0)* (float)Math.atan2(y, x) * 180 / (float) Math.PI;
                    float length = (float) Math.pow(x * x + y * y, 0.5);
                    vertex.x = pivot[0] + length * (float) Math.cos(newAngle * Math.PI / 180);
                    vertex.y = pivot[1] + length * (float) Math.sin(newAngle * Math.PI / 180);
                }
            }
        };

	/*
	*
	* params delta, 		array for x and y axis manipulation
	* params inputHandler, 	info about pivot mode and snapping data
	*/
        public void transformSelection (float []delta, InputHandler inputHandler){
            if (inputHandler.transformTool == 5){					// scale
                if (Math.abs(delta[0]) >= 3 * Math.abs(delta[1])){
                    delta[1] = 0;
                }
                else if (Math.abs(delta[1]) >= 3 * Math.abs(delta[0])){
                    delta[0] = 0;
                }
                this.setScaleOfSelectedObjects(1 + delta[0] / 80, 1 - delta[1] / 80, 1, inputHandler);
            }
            else if (inputHandler.transformTool == 6){				// rotate
                this.setRotationOfSelectedObjects(delta[0], true, inputHandler);

            }
            else if (inputHandler.transformTool == 7){				// translate
                this.setPositionOfSelectedObjects(delta[0], delta[1], 1, inputHandler);
            }
        };

        public void addBody (Body body){
            this.bodies.add(body);
        };

        /**
         *
         * params shapeType, shape to start with (use polygon or chain for editing it)
         * params asCircle,  1 if circle shape is to be generated, otherwise defaults to box (use only when polygon or chain shape is created)
         * creates new body and adds it to the scene
         */
        public void createBody (int shapeType, boolean asCircle){
            Body body = new Body();
            Shape shape =null;
            if (shapeType == Shape.SHAPE_POLYGON || shapeType == Shape.SHAPE_CHAIN){

                if (asCircle){
                    shape = new Shape(shapeType, 0,0);
                }
                else {
                     shape = new Shape(shapeType,0,0);
                }
            }
            else {
                shape = new Shape(shapeType,0,0);
            }

            body.addShape(shape,false);
            this.addBody(body);
        };

        /**
         *
         * params shapeType, shape to be created (use polygon or chain for editing it)
         * params asCircle,  1 if circle shape is to be generated, otherwise defaults to box (use only when polygon or chain shape is created)
         * creates new shape and adds it to the selected body
         */
        public void createShape (int shapeType, boolean asCircle){
            if (this.state != STATE_BODY_EDIT_MODE){
                System.out.println("shapes can be created only when editing body");
                return;
            }
            Shape shape = null;
            if (shapeType == Shape.SHAPE_POLYGON || shapeType == Shape.SHAPE_CHAIN){

                if (asCircle){
                     shape = new Shape(shapeType,0,0);
                }
                else {
                     shape = new Shape(shapeType,0,0);
                }
            }
            else {
                 shape = new Shape(shapeType,0,0);
            }

            this.selectedBodies.get(0).addShape(shape, true);
        };

        /**
         * params points, array of points ([pos_x, pox_y])
         * create a new polygon shape with given vertices
         */
        public void createShapeFromPoints (List<Vertex> points){
            if (this.state != STATE_BODY_EDIT_MODE){
                return ;
            }

            Shape shape = new Shape(Shape.SHAPE_NONE,0,0);
            for (Vertex v:points){
                Vertex vertex = new Vertex(v.x, v.y, 10, 10);
                shape.vertices.add(vertex);
            }

            // remove overlapping vertices
            for (Vertex vertex :shape.vertices){
                for (Vertex another :shape.vertices){
                    if (another!= vertex){
                        float dx = vertex.x - another.x;
                        float dy = vertex.y - another.y;
                        if (dx * dx + dy * dy < 120){
                            shape.removeVertexGivenIndex(shape.vertices.indexOf(another));
                        }
                    }
                }
            }

            this.selectedBodies.get(0).addShape(shape, true);
        };

        // removes body from the scene
        public void removeBody (Body body){
            bodies.remove(body);
        };

        public void addJoint (Joint joint){
            this.joints.add(joint);
        };

        /**
         *
         * params jointType
         * creates a new joint
         */
        public String createJoint (int jointType){
            if (this.selectedBodies.size() == 2 && this.state == STATE_DEFAULT_MODE){
                Joint joint = new Joint(jointType);
                joint.bodyA = this.selectedBodies.get(0);
                joint.bodyB = this.selectedBodies.get(1);
                joint.setLocalAnchorA(joint.bodyA.position[0], joint.bodyA.position[1]);
                joint.setLocalAnchorB(joint.bodyB.position[0], joint.bodyB.position[1]);
                if (jointType == Joint.JOINT_REVOLUTE) {
                    joint.setLocalAnchorA(joint.bodyB.position[0], joint.bodyB.position[1]);
                    //joint.position = [(joint.bodyA.position[0] + joint.bodyB.position[0]) / 2, (joint.bodyA.position[1] + joint.bodyB.position[1]) / 2];
                }
                else if (jointType == Joint.JOINT_PULLEY){
                    joint.setGroundAnchorA(joint.bodyA.position[0], joint.bodyA.position[1] - 100);
                    joint.setGroundAnchorB(joint.bodyB.position[0], joint.bodyB.position[1] - 100);
                }
                else if (jointType == Joint.JOINT_GEAR){
                    if (this.selectedJoints.size() == 2 && ((this.selectedJoints.get(0).jointType == Joint.JOINT_REVOLUTE &&
                            this.selectedJoints.get(1).jointType == Joint.JOINT_REVOLUTE) || (this.selectedJoints.get(0).jointType == Joint.JOINT_PRISMATIC &&
                            this.selectedJoints.get(1).jointType == Joint.JOINT_PRISMATIC) || (this.selectedJoints.get(0).jointType == Joint.JOINT_PRISMATIC &&
                            this.selectedJoints.get(1).jointType == Joint.JOINT_REVOLUTE) || (this.selectedJoints.get(0).jointType == Joint.JOINT_REVOLUTE &&
                            this.selectedJoints.get(1).jointType == Joint.JOINT_PRISMATIC))){
                        joint.joint1 = this.selectedJoints.get(0);
                        joint.joint2 = this.selectedJoints.get(1);
                    }
                    else {
                        return "select 2 revolute/prismatic joints to create gear joint";
                    }
                }
                else if (jointType == Joint.JOINT_ROPE){
                    float [] lengthVec = new float[]{joint.localAnchorA[0] - joint.localAnchorB[0], joint.localAnchorA[1] - joint.localAnchorB[1]};
                    joint.frequencyHZ = ((float)Math.pow(lengthVec[0] * lengthVec[0] + lengthVec[1] * lengthVec[1], 0.5));
                }
                if (jointType != Joint.JOINT_REVOLUTE){
                    joint.position = new float []{(joint.localAnchorA[0] + joint.localAnchorB[0]) / 2, (joint.localAnchorA[1] + joint.localAnchorB[1]) / 2};
                }
                else {
                    joint.position = new float []{(joint.bodyA.position[0] + joint.bodyB.position[0]) / 2, (joint.bodyA.position[1] + joint.bodyB.position[1]) / 2};
                }
                this.addJoint(joint);
            }
            else {
                return "select 2 bodies to create a joint";
            }
            return null;
        };

        // removes joint from the scene
        public void removeJoint (Joint joint){
            joints.remove(joint);
        };

//        // export the scene
//        public void exportWorld (){
//            var world = {
//                    bodies : [],
//            joints : []
//		};
//            for (var i = 0; i < this.bodies.length; i++){
//                world.bodies.push(this.bodies[i].toPhysics());
//            }
//            for (var i = 0; i < this.joints.length; i++){
//                if (this.joints[i].jointType == Joint.JOINT_DISTANCE){
//                    var lengthVec = [this.joints[i].localAnchorA[0] - this.joints[i].localAnchorB[0], this.joints[i].localAnchorA[1] - this.joints[i].localAnchorB[1]];
//                    this.joints[i].setLength(Math.pow(lengthVec[0] * lengthVec[0] + lengthVec[1] * lengthVec[1], 0.5));
//                }
//                world.joints.push(this.joints[i].toPhysics(this.bodies, this.joints));
//            }
//
//            return world;
//        };
//
//        // exports the seleted object(s)
//        public void exportSelection (){
//            if (this.state == STATE_DEFAULT_MODE){
//                var array = {
//                        bodies: []
//			};
//                if (this.selectedBodies.length == 1){
//                    var body = this.selectedBodies[0];
//                    var position = [body.position[0], body.position[1]];
//                    body.setPosition(0, 0);
//                    array.bodies.push(body.toPhysics());
//                    body.move(position[0], position[1]);
//                    return array;
//                }
//                for (var i = 0; i < this.selectedBodies.length; i++){
//                    array.bodies.push(this.selectedBodies[i].toPhysics());
//                }
//                return array;
//            }
//            else if (this.state == this.STATE_BODY_EDIT_MODE){
//                var array = {
//                        fixtures: []
//			};
//                if (this.selectedShapes.length == 1){
//                    var shape = this.selectedShapes[0];
//                    var position = [shape.position[0], shape.position[1]];
//                    shape.setPosition(0, 0);
//                    array.fixtures.push(shape.toPhysics(0, 0));
//                    shape.move(position[0], position[1]);
//                    return array;
//                }
//                for (var i = 0; i < this.selectedShapes.length; i++){
//                    array.fixtures.push(this.selectedShapes[i].toPhysics(0, 0));
//                    return array;
//                }
//            }
//            console.log("only body and shapes can be exported");
//        };
//
//        public void saveScene (){
//            for (var i = 0; i < this.joints.length; i++){
//                this.joints[i].bodyIndexA = this.bodies.indexOf(this.joints[i].bodyA);
//                this.joints[i].bodyIndexB = this.bodies.indexOf(this.joints[i].bodyB);
//
//                if (this.joints[i].jointType == Joint.JOINT_GEAR){
//                    this.joints[i].jointIndex1 = this.joints.indexOf(this.joints[i].joint1);
//                    this.joints[i].jointIndex2 = this.joints.indexOf(this.joints[i].joint2);
//                }
//            }
//            var scene = {
//                    bodies: [],
//            joints: []
//		};
//            scene.bodies = this.bodies;
//            scene.joints = this.joints;
//            // for (var i = 0; i < scene.joints.length; i++){
//            // 	scene.joints[i].bodyA = undefined;
//            // 	scene.joints[i].bodyB = undefined;
//            // }
//            return scene;
//        };
//
//        public void newScene (){
//            this.state = STATE_DEFAULT_MODE;
//            this.bodies = [];
//            this.joints = [];
//        };
//
//        public void loadScene(scene){
//            for (var i = 0; i < scene.bodies.length; i++){
//                this.addBody(loadBody(scene.bodies[i]));
//            }
//            for (var i = 0; i < scene.joints.length; i++){
//                this.addJoint(loadJoint(scene.joints[i], this.bodies, this.joints));
//            }
//        };
//
//        public static void cloneArray(obj){
//        if (obj instanceof Array) {
//            copy = [];
//            for (var i = 0, len = obj.length; i < len; i++) {
//                copy[i] = clone(obj[i]);
//            }
//            return copy;
//        }
//	}
//
//        public static void loadVertex(obj){
//                var vertices = [];
//        for (var i = 0; i < obj.length; i++){
//            var vertex = new Vertex();
//            vertex.x = obj[i].x;
//            vertex.y = obj[i].y;
//            vertex.width = obj[i].width;
//            vertex.height = obj[i].height;
//            vertices.push(vertex);
//        }
//        return vertices;
//	}
//
//        public static void loadShape(obj){
//                var shapes = [];
//        for (var i = 0; i < obj.length; i++){
//            var shape = new Shape(Shape.SHAPE_NONE);
//            shape.shapeType = obj[i].shapeType;
//            shape.position = cloneArray(obj[i].position);						// position
//            shape.scaleXY = cloneArray(obj[i].scaleXY);						// scale
//            shape.rotation = obj[i].rotation;							// only for editor purpose
//            shape.vertices = loadVertex(obj[i].vertices);
//            shape.bounds = cloneArray(obj[i].bounds);					// AABB for selecting
//            shape.centroid = cloneArray(obj[i].centroid);						// centroid for shape
//
//            // fixture properties
//            shape.friction = obj[i].friction;
//            shape.restitution = obj[i].restitution;
//            shape.density = obj[i].density;
//            shape.isSensor = obj[i].isSensor;
//            shape.maskBits = obj[i].maskBits;
//            shape.categoryBits = obj[i].categoryBits;
//            shape.groupIndex = obj[i].groupIndex;
//
//            if (shape.shapeType == Shape.SHAPE_BOX){
//                shape.width = obj[i].width;
//                shape.height = obj[i].height;
//            }
//            else if (shape.shapeType == Shape.SHAPE_CIRCLE){
//                shape.radius = obj[i].radius;
//            }
//
//            shapes.push(shape);
//        }
//        return shapes;
//	}
//
//        public static void loadBody(obj){
//                var body = new Body();
//        body.name = obj.name;
//        body.userData = obj.userData;
//        body.texture = obj.texture;
//        if (obj.texture != ""){
//            body.initialSpriteData = cloneArray(obj.initialSpriteData);
//            body.spriteData = cloneArray(obj.spriteData);
//            body.sprite = new Image();
//            body.sprite.src = obj.texture;
//            if (body.spriteData.length == 2){
//                body.sprite.width = body.spriteData[0];
//                body.sprite.height = body.spriteData[1];
//            }
//        }
//        body.shapes = loadShape(obj.shapes);
//        body.position = cloneArray(obj.position);
//        body.scaleXY = cloneArray(obj.scaleXY);
//        body.rotation = obj.rotation;
//        body.bounds = cloneArray(obj.bounds);
//
//        body.bodyType = obj.bodyType;	// default to dynmic body
//        body.isBulllet = obj.isBulllet;
//        body.isFixedRotation = obj.isFixedRotation;
//        body.linearDamping = obj.linearDamping;
//        body.angularDamping = obj.angularDamping;
//        return body;
//	}
//
//        public static void  loadJoint(obj, bodies, joints){
//            var joint = new Joint();
//            joint.name = obj.name;
//            joint.userData = obj.userData;
//            joint.position = cloneArray(obj.position);
//            joint.scaleXY = cloneArray(obj.scaleXY);
//
//            joint.jointType = obj.jointType;
//            joint.collideConnected = obj.collideConnected;
//            joint.localAnchorA = cloneArray(obj.localAnchorA);
//            joint.localAnchorB = cloneArray(obj.localAnchorB);
//            joint.bodyA = bodies[obj.bodyIndexA];
//            joint.bodyB = bodies[obj.bodyIndexB];
//
//            if (joint.jointType == Joint.JOINT_DISTANCE){
//                joint.length 		= obj.length;
//                joint.frequencyHZ 	= obj.frequencyHZ;
//                joint.dampingRatio 	= obj.dampingRatio;
//            }
//            else if (joint.jointType == Joint.JOINT_WELD){
//                joint.referenceAngle = obj.referenceAngle;
//            }
//            else if (joint.jointType == Joint.JOINT_REVOLUTE){
//                joint.enableLimit 	 = obj.enableLimit;
//                joint.enableMotor 	 = obj.enableMotor;
//                joint.lowerAngle 	 = obj.lowerAngle;
//                joint.upperAngle	 = obj.upperAngle;
//                joint.maxMotorTorque = obj.maxMotorTorque;
//                joint.motorSpeed 	 = obj.motorSpeed;
//                joint.referenceAngle = obj.referenceAngle;
//            }
//            else if (joint.jointType == Joint.JOINT_WHEEL){
//                joint.localAxisA 	= cloneArray(obj.localAxisA);
//                joint.enableMotor 	= obj.enableMotor;
//                joint.maxMotorTorque = obj.maxMotorTorque;
//                joint.motorSpeed 	= obj.motorSpeed;
//                joint.frequencyHZ 	= obj.frequencyHZ;
//                joint.dampingRatio 	= obj.dampingRatio;
//            }
//            else if (joint.jointType == Joint.JOINT_PULLEY){
//                joint.groundAnchorA  = cloneArray(obj.groundAnchorA);
//                joint.groundAnchorB = cloneArray(obj.groundAnchorB);
//                joint.lengthA	   	 = obj.lengthA;
//                joint.lengthB		 = obj.lengthB;
//                joint.maxLengthA     = obj.maxLengthA;
//                joint.maxLengthB     = obj.maxLengthB;
//                joint.frequencyHZ    = obj.frequencyHZ;
//            }
//            else if (joint.jointType == Joint.JOINT_GEAR){
//                joint.joint1		= joints[obj.jointIndex1];
//                joint.joint2		= joints[obj.jointIndex2];
//                joint.frequencyHZ = obj.frequencyHZ;
//            }
//            else if (joint.jointType == Joint.JOINT_PRISMATIC){
//                joint.enableLimit 	 = obj.enableLimit;
//                joint.enableMotor 	 = obj.enableMotor;
//                joint.localAxisA 	= cloneArray(obj.localAxisA);
//                joint.lowerTranslation 	 = obj.lowerTranslation;
//                joint.upperTranslation	 = obj.upperTranslation;
//                joint.maxMotorTorque = obj.maxMotorTorque;
//                joint.motorSpeed 	 = obj.motorSpeed;
//                joint.referenceAngle = obj.referenceAngle;
//            }
//            else if (joint.jointType == Joint.JOINT_ROPE){
//                joint.frequencyHZ = obj.frequencyHZ;
//            }
//            return joint;
//        }


}
