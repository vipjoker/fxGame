package physicsPort.viewport;

import java.util.ArrayList;
import java.util.List;

public class InputHandler {


    public static int IS_LEFT_MOUSE_BUTTON = 1;
    public static int IS_RIGHT_MOUSE_BUTTON = 2;
    public static int PIVOT_LOCAL_MODE = 3;
    public static int PIVOT_SELECTION_MIDDLE = 4;
    public static int TRANSFORM_TOOL_SCALE = 5;
    public static int TRANSFORM_TOOL_ROTATION = 6;
    public static int TRANSFORM_TOOL_TRANSLATION = 7;

    public boolean inGameMode;
    public int pivotMode;
    public int transformTool;
    public float[] pointerWorldPos;
    public float[] start;
    public float[] current;
    public float[] delta;
    public int mouseSensitivity;
    public float[] mouseStatus;
    public float[] selectionArea;
    public List<Object> selection = new ArrayList<>();
    public float[] snappingData;


    public static int CTRL_PRESSED = 0;
    public static int SHIFT_PRESSED = 0;
    public static int ALT_PRESSED = 0;
    public static boolean B_KEY_PRESSED = false;
    public static boolean J_KEY_PRESSED = false;
    public static boolean SNAPPING_ENABLED = false;


    public InputHandler() {
        // mouse tracking variables
        this.pointerWorldPos = new float[]{0, 0, 0, 0};        // mouse_down and current position of cursor in world coordinate
        this.start = new float[]{0, 0};                        // [mouse_on_down.x  , mouse_on_down.y  ]
        this.current = new float[]{0, 0};                        // [mouse_on_up.x    , mouse_on_up.y    ]
        this.delta = new float[]{0, 0};                        // [mouse_delta_pos.x, mouse_delta_pos.y]
        this.mouseSensitivity = 1;                    // navigation speed, depends on scale of the viewport
        this.mouseStatus = new float[]{0, 0};                    // [is_down, is_left = 1 or is_right = 2]
        this.selectionArea = new float[]{0, 0, 0, 0, 0};        // [x, y, width, height, is_active]
        // array of selected objects
        CTRL_PRESSED = 0;
        SHIFT_PRESSED = 0;
        ALT_PRESSED = 0;
        this.snappingData = new float[]{0, 0.1f, 10};            // [snap_pos, delta_scale, delta_angle]
        this.transformTool = InputHandler.TRANSFORM_TOOL_TRANSLATION;
        this.pivotMode = InputHandler.PIVOT_LOCAL_MODE;

        this.inGameMode = false;
    }


    public boolean isMouseDown() {
        return this.mouseStatus[0] == 1;
    }


    public boolean isRightClick() {
        return this.mouseStatus[1] == InputHandler.IS_RIGHT_MOUSE_BUTTON;
    }


    public boolean isLeftClick() {
        return this.mouseStatus[1] == InputHandler.IS_LEFT_MOUSE_BUTTON;
    }


    public void activateTranslationTool() {
        this.transformTool = InputHandler.TRANSFORM_TOOL_TRANSLATION;
    }

    public void activateScaleTool() {
        this.transformTool = InputHandler.TRANSFORM_TOOL_SCALE;
    }

    public void activateRotationTool() {
        this.transformTool = InputHandler.TRANSFORM_TOOL_ROTATION;
    }


    public void activateLocalPivotMode() {
        this.pivotMode = InputHandler.PIVOT_LOCAL_MODE;
    }


    public void activateSelectionPivotMode() {
        this.pivotMode = InputHandler.PIVOT_SELECTION_MIDDLE;
    }

}
