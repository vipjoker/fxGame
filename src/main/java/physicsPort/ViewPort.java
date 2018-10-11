package physicsPort;

import com.badlogic.gdx.utils.viewport.Viewport;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.*;
import javafx.scene.paint.Color;
import physicsPort.body.Body;
import physicsPort.body.LineSegment;
import physicsPort.body.Vertex;
import physicsPort.viewport.InputHandler;
import physicsPort.viewport.Navigator;
import physicsPort.viewport.Renderer;

public class ViewPort {




        private Canvas canvas ;
        public GraphicsContext context ;
        public Navigator navigator ;
        public InputHandler inputHandler ;
        public Renderer renderer ;
        public SceneManager sceneManager ;

        public GameView gameView;

        // prevent default right click behaviour

        public  ViewPort(Canvas canvas, SceneManager sceneManager){
            this.canvas = canvas;
            this.context = canvas.getGraphicsContext2D();
            this.navigator = new Navigator();
            this.navigator.panning[0] += canvas.getWidth() / 2;			// move origin-x to center of viewport (canvas)
            this.navigator.panning[1] += canvas.getHeight() / 2;			// move origin-y to center of viewport (canvas)
            this.inputHandler = new InputHandler();
            this.renderer = new Renderer(this.context);
            this.renderer.setStageWidthHeight((float) canvas.getWidth(),(float) canvas.getHeight());

            this.sceneManager = sceneManager;


            // prevent default right click behaviour
//            this.canvas.addEventListener("contextmenu", function(e){
//                e.preventDefault();
//            });
        }

            public void onKeyDown (KeyEvent e){
            // return if in game mode
            if (inputHandler.inGameMode)
                return;

            if (e.getCode() == KeyCode.CONTROL){
                InputHandler.CTRL_PRESSED = 1;
            }
            else if (e.getCode() == KeyCode.SHIFT){
                InputHandler.SHIFT_PRESSED = 1;
            }
            else if (e.getCode() == KeyCode.ALT){
                InputHandler.ALT_PRESSED = 1;
            }
            else if (e.getCode() == KeyCode.S){
                InputHandler.SNAPPING_ENABLED = !InputHandler.SNAPPING_ENABLED;
            }
            else if (e.getCode() == KeyCode.B){		// mask joints
                InputHandler.B_KEY_PRESSED = true;
            }
            else if (e.getCode() == KeyCode.J){		// mask body
                InputHandler.J_KEY_PRESSED = true;
            }
        };

        public void onKeyUp (KeyEvent e){
            // return if in game mode
            if (this.inputHandler.inGameMode)
                return;

            if (e.getCode() == KeyCode.CONTROL){
                InputHandler.CTRL_PRESSED = 0;
            }
            else if (e.getCode() == KeyCode.SHIFT){
                InputHandler.SHIFT_PRESSED = 0;
            }
            else if (e.getCode()== KeyCode.ALT){
                InputHandler.ALT_PRESSED = 0;
            }

            else if (e.getCode()== KeyCode.DELETE){		// delete selected object
                this.sceneManager.deleteSelectedObjects();
            }

            else if (e.getCode()== KeyCode.B){		// unmask joints
                InputHandler.B_KEY_PRESSED = false;
            }
            else if (e.getCode()== KeyCode.J){		// unmask body
                InputHandler.J_KEY_PRESSED = false;
            }

            else if (e.getCode() == KeyCode.F){		// f - key pressed => align view to selection
                if (this.inputHandler.selection.get(0) instanceof Vertex){
                    float[] pivot = new float[2];
                    for (int i = 0; i < this.inputHandler.selection.size(); i++){
                        pivot[0] += ((Vertex)this.inputHandler.selection.get(i)).x;
                        pivot[1] += ((Vertex)this.inputHandler.selection.get(i)).y;
                    }
                    pivot[0] /= Math.max(this.inputHandler.selection.size(), 1);
                    pivot[1] /= Math.max(this.inputHandler.selection.size(), 1);
                    this.navigator.panning = new float[]
                            {
                                    (float) this.canvas.getWidth() / (this.navigator.scale * 2) - pivot[0] + this.navigator.origin[0],
                                    (float) this.canvas.getHeight() / (this.navigator.scale * 2) - pivot[1] + this.navigator.origin[1],
                            };
                }
                else {
                    float[] pivot = new float[]{0, 0};
                    for (int i = 0; i < this.inputHandler.selection.size(); i++){
                        pivot[0] += ((Body)this.inputHandler.selection.get(i)).position[0];//TODO add interface Positionable
                        pivot[1] += ((Body)this.inputHandler.selection.get(i)).position[1];//TODO add interface Positionable
                    }
                    pivot[0] /= Math.max(this.inputHandler.selection.size(), 1);
                    pivot[1] /= Math.max(this.inputHandler.selection.size(), 1);
                    this.navigator.panning = new float[]
                            {
                                    (float) this.canvas.getWidth() / (this.navigator.scale * 2) - pivot[0] + this.navigator.origin[0],
                                    (float) this.canvas.getHeight()/ (this.navigator.scale * 2) - pivot[1] + this.navigator.origin[1],
                            };
                }
            }

            else if (e.getCode() == KeyCode.W){		// w - key pressed => enable translation tool
                this.inputHandler.activateTranslationTool();
            }
            else if (e.getCode() == KeyCode.E){		// e - key pressed => enable rotationtool
                this.inputHandler.activateRotationTool();
            }
            else if (e.getCode() == KeyCode.R){		// r - key pressed => enable scale tool
                this.inputHandler.activateScaleTool();
            }

            else if (e.getCode() == KeyCode.D && InputHandler.SHIFT_PRESSED == 1){
                this.sceneManager.duplicateSelection();
            }
        };

        public void onMouseDown (MouseEvent e){
            float eoffsetX = (float) e.getX();
            float eoffsetY = (float) e.getY();

            inputHandler.mouseStatus[0] = 1;

            inputHandler.pointerWorldPos[0] = this.navigator.screenPointToWorld(eoffsetX, eoffsetY)[0];
            inputHandler.pointerWorldPos[1] = this.navigator.screenPointToWorld(eoffsetX, eoffsetY)[1];

            // check whether right button is pressd or not
            if (e.getButton() == MouseButton.SECONDARY)
                inputHandler.mouseStatus[1] = 2;
            else if (e.getButton() == MouseButton.PRIMARY)
                inputHandler.mouseStatus[1] =1;

            inputHandler.start = new float[]{eoffsetX, eoffsetY};

            if (inputHandler.mouseStatus[1] == InputHandler.IS_RIGHT_MOUSE_BUTTON)
                return;

            // return if in game mode
            if (this.inputHandler.inGameMode)
                return;

            // select bodies
            if (!this.sceneManager.onMouseDown(e, this.inputHandler, this.navigator)){
                inputHandler.selectionArea[0] = eoffsetX;
                inputHandler.selectionArea[1] = eoffsetY;
                inputHandler.selectionArea[4] = 1;
            }

            // selected object goes to inputHandler.selection[]
            else {
                if (this.sceneManager.state == SceneManager.STATE_DEFAULT_MODE){
                    inputHandler.selection.clear();
                    for (int i = 0; i < this.sceneManager.selectedBodies.size(); i++){
                        inputHandler.selection.add(this.sceneManager.selectedBodies.get(i));
                    }
                    for (int i = 0; i < this.sceneManager.selectedJoints.size(); i++){
                        inputHandler.selection.add(this.sceneManager.selectedJoints.get(i));
                    }

                }
                else if (this.sceneManager.state == SceneManager.STATE_BODY_EDIT_MODE){
                    inputHandler.selection.addAll(this.sceneManager.selectedShapes);
                }
                else if (this.sceneManager.state == SceneManager.STATE_SHAPE_EDIT_MODE){
                    inputHandler.selection.addAll(this.sceneManager.selectedVertices);
                }
            }
        };

        public void onMouseMove (MouseEvent e){
            float eoffsetX = (float) e.getX();
            float  eoffsetY = (float)e.getY();

            inputHandler.pointerWorldPos[2] = navigator.screenPointToWorld(eoffsetX, eoffsetY)[0];
            inputHandler.pointerWorldPos[3] = navigator.screenPointToWorld(eoffsetX, eoffsetY)[1];

            if (inputHandler.mouseStatus[0] != 0){
                inputHandler.selectionArea[2] = (eoffsetX - inputHandler.selectionArea[0]);
                inputHandler.selectionArea[3] = (eoffsetY - inputHandler.selectionArea[1]);

                inputHandler.current = new float[]{eoffsetX, eoffsetY};

                inputHandler.delta[0] = inputHandler.current[0] - inputHandler.start[0];
                inputHandler.delta[0] *= inputHandler.mouseSensitivity / navigator.scale;
                inputHandler.delta[1] = inputHandler.current[1] - inputHandler.start[1];
                inputHandler.delta[1] *= inputHandler.mouseSensitivity / navigator.scale;

                inputHandler.start[0] = inputHandler.current[0];
                inputHandler.start[1] = inputHandler.current[1];

                // panning
                if (inputHandler.mouseStatus[1] == InputHandler.IS_RIGHT_MOUSE_BUTTON){
                    navigator.panning[0] += inputHandler.delta[0];
                    navigator.panning[1] += inputHandler.delta[1];

                    inputHandler.selectionArea[2] = 0;
                    inputHandler.selectionArea[3] = 0;

                    return;
                }

                // return if in game mode
                if (this.inputHandler.inGameMode){
                    inputHandler.selectionArea[2] = 0;
                    inputHandler.selectionArea[3] = 0;
                    return;
                }

                if (inputHandler.selectionArea[4]!= 0){
                    return;
                }

                // edit bodies and shapes
                inputHandler.snappingData[0] = navigator.cell_size * 5;
                sceneManager.transformSelection(inputHandler.delta, inputHandler);
            }
        };

        public void onMouseUp (MouseEvent e){
            float eoffsetX = (float) e.getX();
            float eoffsetY = (float) e.getY();

            inputHandler.mouseStatus[0] = 0;

            // return if in game mode
            if (this.inputHandler.inGameMode)
                return;

            if (inputHandler.selectionArea[4] != 0){
                float[] startPoint = this.screenPointToWorld(inputHandler.selectionArea[0], inputHandler.selectionArea[1]);
                      float[]  endPoint = this.screenPointToWorld(eoffsetX, eoffsetY);
                LineSegment lineSegment  = new LineSegment(startPoint[0], startPoint[1], endPoint[0], endPoint[1]);

                // edit bodies and shapes
                if (sceneManager.state == SceneManager.STATE_DEFAULT_MODE){
                    if (sceneManager.selectedJoints.size() == 1){
                        if (sceneManager.selectedJoints.get(0).inEditMode){
                            inputHandler.selectionArea = new float[]{0, 0, 0, 0, 0};
                            return;
                        }
                    }
                    if (InputHandler.SHIFT_PRESSED ==0){
                        sceneManager.selectedBodies.clear();
                        sceneManager.selectedJoints.clear();
                    }
                    // bodies
                    for (int i = 0; i < sceneManager.bodies.size(); i++){
                        if (lineSegment.checkInBoundsAABB(sceneManager.bodies.get(i).bounds)){
                            if (sceneManager.selectedBodies.indexOf(sceneManager.bodies.get(i)) < 0){
                                sceneManager.selectedBodies.add(sceneManager.bodies.get(i));
                            }
                            sceneManager.bodies.get(i).isSelected = true;
                        }
                        else {
                            if (InputHandler.SHIFT_PRESSED== 0){
                                sceneManager.bodies.get(i).isSelected = false;
                            }
                        }
                    }
                    // joints
                    for (int i = 0; i < sceneManager.joints.size(); i++){
                        if (lineSegment.checkInBoundsAABB(sceneManager.joints.get(i).getBounds())){
                            if (sceneManager.selectedJoints.indexOf(sceneManager.joints.get(i)) < 0){
                                sceneManager.selectedJoints.add(sceneManager.joints.get(i));
                            }
                            sceneManager.joints.get(i).isSelected = true;
                        }
                        else {
                            if (InputHandler.SHIFT_PRESSED == 0){
                                sceneManager.joints.get(i).isSelected = false;
                            }
                        }
                    }
                }
                else if (sceneManager.state == SceneManager.STATE_BODY_EDIT_MODE){
                    if (InputHandler.SHIFT_PRESSED ==0){
                        sceneManager.selectedShapes.clear();
                    }
                    for (int i = 0; i < sceneManager.selectedBodies.get(0).shapes.size(); i++){

                        if (lineSegment.checkInBoundsAABB(sceneManager.selectedBodies.get(0).shapes.get(i).bounds)){
                            if (sceneManager.selectedShapes.indexOf(sceneManager.selectedBodies.get(0).shapes.get(i)) < 0){
                                sceneManager.selectedShapes.add(sceneManager.selectedBodies.get(0).shapes.get(i));
                            }
                            sceneManager.selectedBodies.get(0).shapes.get(i).isSelected = true;
                        }
                        else {
                            if (InputHandler.SHIFT_PRESSED == 0){
                                sceneManager.selectedBodies.get(0).shapes.get(i).isSelected = false;
                            }
                        }
                    }
                }
                else if (sceneManager.state == SceneManager.STATE_SHAPE_EDIT_MODE){
                    if (InputHandler.SHIFT_PRESSED == 0){
                        sceneManager.selectedVertices.clear();
                    }
                    for (int  i = 0; i < sceneManager.selectedShapes.get(0).vertices.size(); i++){
                        Vertex vertex = sceneManager.selectedShapes.get(0).vertices.get(i);
                        if (lineSegment.checkInBoundsAABB(new float []{vertex.x, vertex.y, vertex.width, vertex.height})){
                            if (sceneManager.selectedVertices.indexOf(sceneManager.selectedShapes.get(0).vertices.get(i)) < 0){
                                sceneManager.selectedVertices.add(sceneManager.selectedShapes.get(0).vertices.get(i));
                            }
                            sceneManager.selectedShapes.get(0).vertices.get(i).isSelected = true;
                        }
					else{
                            if (InputHandler.SHIFT_PRESSED == 0){
                                sceneManager.selectedShapes.get(0).vertices.get(i).isSelected = false;
                            }
                        }
                    }
                }

                // selected object goes to inputHandler.selection[]
                if (sceneManager.state == SceneManager.STATE_DEFAULT_MODE){
                    inputHandler.selection.clear();
                    for (int i = 0; i < this.sceneManager.selectedBodies.size(); i++){
                        inputHandler.selection.add(this.sceneManager.selectedBodies.get(i));
                    }
                    for (int i = 0; i < this.sceneManager.selectedJoints.size(); i++){
                        inputHandler.selection.add(this.sceneManager.selectedJoints.get(i));
                    }
                }
                else if (sceneManager.state == SceneManager.STATE_BODY_EDIT_MODE){
                    inputHandler.selection .addAll(sceneManager.selectedShapes);
                }
                else if (this.sceneManager.state == SceneManager.STATE_SHAPE_EDIT_MODE){
                    inputHandler.selection.addAll(sceneManager.selectedVertices);
                }
            }

            inputHandler.selectionArea = new float[]{0, 0, 0, 0, 0};
        };

        // viewport scaling
        public void onMouseWheel (ScrollEvent e){
            float eoffsetX = (float) e.getX();
            float eoffsetY = (float) e.getY();


            float mouseX = eoffsetX;
            float  mouseY = eoffsetY;
            float  wheel = (float) e.getDeltaX() / 120;
            float zoom = 1 + (wheel > 0 ? 1 : -1) * (float)Math.min(Math.abs(wheel / 20), 0.1);

            this.zoom(mouseX, mouseY, zoom);
        };

        public void zoom (float mouseX, float mouseY,float zoom){

            if (zoom > 1){
                if (navigator.scale > navigator.scaleLimits[1])
                    return;
            }
            else{
                if (navigator.scale < navigator.scaleLimits[0])
                    return;
            }

            this.context.translate(
                    navigator.origin[0],
                    navigator.origin[1]
            );
            this.context.scale(zoom,zoom);
            this.context.translate(
                    -( mouseX / navigator.scale + navigator.origin[0] - mouseX / ( navigator.scale * zoom ) ),
                    -( mouseY / navigator.scale + navigator.origin[1] - mouseY / ( navigator.scale * zoom ) )
            );

            navigator.origin[0] = ( mouseX / navigator.scale + navigator.origin[0] - mouseX / ( navigator.scale * zoom ) );
            navigator.origin[1] = ( mouseY / navigator.scale + navigator.origin[1] - mouseY / ( navigator.scale * zoom ) );
            navigator.scale *= zoom;
        };

        public void zoomIn (){
            this.zoom((float) this.canvas.getWidth()/ 2, (float) this.canvas.getHeight()/ 2, 1.2f);
        };

        public void zoomOut (){
            this.zoom((float) this.canvas.getWidth()/ 2, (float) this.canvas.getHeight()/ 2, 0.8f);
        };

        public void resetView (){
            this.zoom((float) this.canvas.getWidth()/ 2, (float) this.canvas.getHeight()/ 2, 1 / this.navigator.scale);
            this.navigator.panning =new float[]
                    {
                            (float) this.canvas.getWidth() / (this.navigator.scale * 2) + this.navigator.origin[0],
                            (float) this.canvas.getHeight() / (this.navigator.scale * 2) + this.navigator.origin[1],
                    };
        };

        public void draw (GameView gameView){


            // clear screen
            renderer.clear(navigator.origin[0], navigator.origin[1], renderer.width / navigator.scale, renderer.height / navigator.scale);

            // saving the current state of the canvas
            renderer.getContext().save();

            // applying panning to canvas
            renderer.getContext().translate(navigator.panning[0], navigator.panning[1]);

            // rendering the grid
            navigator.range = 1000;
//            navigator.cell_size = 10 / Math.max(1, Math.min((navigator.scale), 2));
            renderer.renderGrid(navigator.range);

            if (!inputHandler.inGameMode){
                // rendering the bodies
                for (int i = 0; i < sceneManager.bodies.size(); i++){
                    renderer.renderBody(sceneManager.bodies.get(i));
                }

                // rendering the joints
                for (int i = 0; i < sceneManager.joints.size(); i++){
                    renderer.renderJoint(sceneManager.joints.get(i));
                }

                // draw selection area if active
                if (inputHandler.selectionArea[4] != 0){
                    float[] position = this.screenPointToWorld(inputHandler.selectionArea[0], inputHandler.selectionArea[1]);
                            float width = inputHandler.selectionArea[2] / navigator.scale;
                            float height = inputHandler.selectionArea[3] / navigator.scale;

                    this.renderer.setLineDash(5, 5);
                    renderer.getContext().setStroke(Color.web("#fff"));
                    this.renderer.renderBox(position[0] + width / 2, position[1] + height / 2, width, height, false);
                    renderer.getContext().setStroke(Color.web("#000"));
                    this.renderer.setLineDash(0, 0);
                }
            }
            else {
                if (gameView != null && gameView.hasLoaded){
                    gameView.updateView();
                }
            }

            // restoring the saved canvas state
            renderer.getContext().restore();
        };

        public float[] screenPointToWorld (float x,float y){
            return 	this.navigator.screenPointToWorld(x, y);
        };

        float[] worldPointToScreen (float x,float y){
            return 	this.navigator.worldPointToScreen(x, y);
        };

        Navigator getNavigator (){
            return this.navigator;
        };

        InputHandler getInputHandler (){
            return this.inputHandler;
        };

        Renderer getRenderer (){
            return this.renderer;
        };

        SceneManager getSceneManager (){
            return this.sceneManager;
        };


}
