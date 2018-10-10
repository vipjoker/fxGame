package physicsPort;

import com.badlogic.gdx.utils.viewport.Viewport;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
            var eoffsetX = e.offsetX == undefined ? e.layerX : e.offsetX;
            var eoffsetY = e.offsetY == undefined ? e.layerY : e.offsetY;

            var inputHandler = this.inputHandler;
            inputHandler.mouseStatus[0] = 1;

            inputHandler.pointerWorldPos[0] = this.navigator.screenPointToWorld(eoffsetX, eoffsetY)[0];
            inputHandler.pointerWorldPos[1] = this.navigator.screenPointToWorld(eoffsetX, eoffsetY)[1];

            // check whether right button is pressd or not
            if (e.which)
                inputHandler.mouseStatus[1] = (e.which == 3) + 1;
            else if (e.button)
                inputHandler.mouseStatus[1] = (e.button == 2) + 1;

            inputHandler.start = [eoffsetX, eoffsetY];

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
                if (this.sceneManager.state == this.sceneManager.STATE_DEFAULT_MODE){
                    inputHandler.selection = [];
                    for (var i = 0; i < this.sceneManager.selectedBodies.length; i++){
                        inputHandler.selection.push(this.sceneManager.selectedBodies[i]);
                    }
                    for (var i = 0; i < this.sceneManager.selectedJoints.length; i++){
                        inputHandler.selection.push(this.sceneManager.selectedJoints[i]);
                    }

                }
                else if (this.sceneManager.state == this.sceneManager.STATE_BODY_EDIT_MODE){
                    inputHandler.selection = this.sceneManager.selectedShapes;
                }
                else if (this.sceneManager.state == this.sceneManager.STATE_SHAPE_EDIT_MODE){
                    inputHandler.selection = this.sceneManager.selectedVertices;
                }
            }
        };

        Viewport.prototype.onMouseMove = function(e){
            var eoffsetX = e.offsetX == undefined ? e.layerX : e.offsetX;
            var eoffsetY = e.offsetY == undefined ? e.layerY : e.offsetY;
            var inputHandler = this.inputHandler, navigator = this.navigator, sceneManager = this.sceneManager;

            inputHandler.pointerWorldPos[2] = navigator.screenPointToWorld(eoffsetX, eoffsetY)[0];
            inputHandler.pointerWorldPos[3] = navigator.screenPointToWorld(eoffsetX, eoffsetY)[1];

            if (inputHandler.mouseStatus[0]){
                inputHandler.selectionArea[2] = (eoffsetX - inputHandler.selectionArea[0]);
                inputHandler.selectionArea[3] = (eoffsetY - inputHandler.selectionArea[1]);

                inputHandler.current = [eoffsetX, eoffsetY];

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

                if (inputHandler.selectionArea[4]){
                    return;
                }

                // edit bodies and shapes
                inputHandler.snappingData[0] = navigator.cell_size * 5;
                sceneManager.transformSelection(inputHandler.delta, inputHandler);
            }
        };

        Viewport.prototype.onMouseUp = function(e){
            var eoffsetX = e.offsetX == undefined ? e.layerX : e.offsetX;
            var eoffsetY = e.offsetY == undefined ? e.layerY : e.offsetY;
            var inputHandler = this.inputHandler, sceneManager = this.sceneManager;
            inputHandler.mouseStatus[0] = 0;

            // return if in game mode
            if (this.inputHandler.inGameMode)
                return;

            if (inputHandler.selectionArea[4]){
                var startPoint = this.screenPointToWorld(inputHandler.selectionArea[0], inputHandler.selectionArea[1]),
                        endPoint = this.screenPointToWorld(eoffsetX, eoffsetY);
                LineSegment lineSegment  = new LineSegment(startPoint[0], startPoint[1], endPoint[0], endPoint[1]);

                // edit bodies and shapes
                if (sceneManager.state == sceneManager.STATE_DEFAULT_MODE){
                    if (sceneManager.selectedJoints.length == 1){
                        if (sceneManager.selectedJoints[0].inEditMode){
                            inputHandler.selectionArea = [0, 0, 0, 0, 0];
                            return;
                        }
                    }
                    if (!inputHandler.SHIFT_PRESSED){
                        sceneManager.selectedBodies = [];
                        sceneManager.selectedJoints = [];
                    }
                    // bodies
                    for (var i = 0; i < sceneManager.bodies.length; i++){
                        if (lineSegment.checkInBoundsAABB(sceneManager.bodies[i].bounds)){
                            if (sceneManager.selectedBodies.indexOf(sceneManager.bodies[i]) < 0){
                                sceneManager.selectedBodies.push(sceneManager.bodies[i]);
                            }
                            sceneManager.bodies[i].isSelected = true;
                        }
                        else {
                            if (!inputHandler.SHIFT_PRESSED){
                                sceneManager.bodies[i].isSelected = false;
                            }
                        }
                    }
                    // joints
                    for (var i = 0; i < sceneManager.joints.length; i++){
                        if (lineSegment.checkInBoundsAABB(sceneManager.joints[i].getBounds())){
                            if (sceneManager.selectedJoints.indexOf(sceneManager.joints[i]) < 0){
                                sceneManager.selectedJoints.push(sceneManager.joints[i]);
                            }
                            sceneManager.joints[i].isSelected = true;
                        }
                        else {
                            if (!inputHandler.SHIFT_PRESSED){
                                sceneManager.joints[i].isSelected = false;
                            }
                        }
                    }
                }
                else if (sceneManager.state == sceneManager.STATE_BODY_EDIT_MODE){
                    if (!inputHandler.SHIFT_PRESSED){
                        sceneManager.selectedShapes = [];
                    }
                    for (var i = 0; i < sceneManager.selectedBodies[0].shapes.length; i++){

                        if (lineSegment.checkInBoundsAABB(sceneManager.selectedBodies[0].shapes[i].bounds)){
                            if (sceneManager.selectedShapes.indexOf(sceneManager.selectedBodies[0].shapes[i]) < 0){
                                sceneManager.selectedShapes.push(sceneManager.selectedBodies[0].shapes[i]);
                            }
                            sceneManager.selectedBodies[0].shapes[i].isSelected = true;
                        }
                        else {
                            if (!inputHandler.SHIFT_PRESSED){
                                sceneManager.selectedBodies[0].shapes[i].isSelected = false;
                            }
                        }
                    }
                }
                else if (sceneManager.state == sceneManager.STATE_SHAPE_EDIT_MODE){
                    if (!inputHandler.SHIFT_PRESSED){
                        sceneManager.selectedVertices = [];
                    }
                    for (var i = 0; i < sceneManager.selectedShapes[0].vertices.length; i++){
                        var vertex = sceneManager.selectedShapes[0].vertices[i];
                        if (lineSegment.checkInBoundsAABB([vertex.x, vertex.y, vertex.width, vertex.height])){
                            if (sceneManager.selectedVertices.indexOf(sceneManager.selectedShapes[0].vertices[i]) < 0){
                                sceneManager.selectedVertices.push(sceneManager.selectedShapes[0].vertices[i]);
                            }
                            sceneManager.selectedShapes[0].vertices[i].isSelected = true;
                        }
					else{
                            if (!inputHandler.SHIFT_PRESSED){
                                sceneManager.selectedShapes[0].vertices[i].isSelected = false;
                            }
                        }
                    }
                }

                // selected object goes to inputHandler.selection[]
                if (sceneManager.state == sceneManager.STATE_DEFAULT_MODE){
                    inputHandler.selection = [];
                    for (var i = 0; i < this.sceneManager.selectedBodies.length; i++){
                        inputHandler.selection.push(this.sceneManager.selectedBodies[i]);
                    }
                    for (var i = 0; i < this.sceneManager.selectedJoints.length; i++){
                        inputHandler.selection.push(this.sceneManager.selectedJoints[i]);
                    }
                }
                else if (sceneManager.state == sceneManager.STATE_BODY_EDIT_MODE){
                    inputHandler.selection = sceneManager.selectedShapes;
                }
                else if (this.sceneManager.state == sceneManager.STATE_SHAPE_EDIT_MODE){
                    inputHandler.selection = sceneManager.selectedVertices;
                }
            }

            inputHandler.selectionArea = [0, 0, 0, 0, 0];
        };

        // viewport scaling
        Viewport.prototype.onMouseWheel = function(e){
            var eoffsetX = e.offsetX == undefined ? e.layerX : e.offsetX;
            var eoffsetY = e.offsetY == undefined ? e.layerY : e.offsetY;

            var mouseX = eoffsetX;
            var mouseY = eoffsetY;
            var wheel = e.wheelDelta / 120;
            var zoom = 1 + (wheel > 0 ? 1 : -1) * Math.min(Math.abs(wheel / 20), 0.1);

            this.zoom(mouseX, mouseY, zoom);
        };

        Viewport.prototype.zoom = function(mouseX, mouseY, zoom){
            var navigator = this.navigator;

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

        Viewport.prototype.zoomIn = function(){
            this.zoom(this.canvas.width / 2, this.canvas.height / 2, 1.2);
        };

        Viewport.prototype.zoomOut = function(){
            this.zoom(this.canvas.width / 2, this.canvas.height / 2, 0.8);
        };

        Viewport.prototype.resetView = function(){
            this.zoom(this.canvas.width / 2, this.canvas.height / 2, 1 / this.navigator.scale);
            this.navigator.panning =
					[
            this.canvas.width / (this.navigator.scale * 2) + this.navigator.origin[0],
                    this.canvas.height / (this.navigator.scale * 2) + this.navigator.origin[1],
					];
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
