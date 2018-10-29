package physicsPort;

/**
 * Pass this to the "ease" parameter in the optional hashtable, to get a different easing behavior<br><br>
 * <strong>Example: </strong><br>physicsPort.LeanTween.rotateX(gameObject, 270.0f, 1.5f).setEase(LeanTweenType.easeInBack);
 */

import com.badlogic.gdx.math.Vector2;

import java.util.Map;

public class LeanTween {


//    public static class AnimationTask {
//        public boolean toggle;
//        public TransformF trans;
//        public Vector2 from;
//        public Vector2 to;
//        public Vector2 diff;
//        public Vector2 point;
//        public Vector2 axis;
//        public Vector2 origRotation;
//        public float time;
//        public float lastVal;
//        public float passed;
//
//
//        public long delay;
//        private String id;
//        public Interpolator loopType;
//        public int loopCount;
//        public float direction;
//        public Action<Float> onUpdateFloat;
//        public Action<Vector2> onUpdateVector2;
//        public Action onComplete;
//
//    }
}

//
//        /**
//         * Cancel a tween
//         *
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method cancel
//         */
//        public LTDescr cancel() {
//            LeanTween.removeTween(this._id);
//            return this;
//        }
//
//        public int uniqueId()
//
//        {
//
//            int toId = _id | ((int) type.ordinal() << 24);
//
//			/*Debug.Log("toId:"+_id+" toType:"+(TweenAction)type);
//
//			int backId = toId & 0xFFFFFF;
//			int backType = toId >> 24;
//
//			Debug.Log("backId:"+backId+" backType:"+(TweenAction)backType);*/
//
//            return toId;
//
//        }
//
//        public int id() {
//            return uniqueId();
//        }
//
//        public void reset() {
//            this.toggle = true;
//            this.optional = null;
//            this.passed = this.delay = 0.0f;
//            this.useEstimatedTime = this.useFrames = this.hasInitiliazed = false;
//            this.animationCurve = null;
//            this.tweenType = LeanTweenType.linear;
//            this.loopType = LeanTweenType.once;
//            this.direction = 1.0f;
//            this.onUpdateFloat = null;
//            this.onUpdateVector2 = null;
//            this.onComplete = null;
//            this.onCompleteParam = null;
//            this.point = Vector2.Zero;
//        }
//
//        /**
//         * Pause a tween
//         *
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method pause
//         */
//        public LTDescr pause() {
//            this.lastVal = this.direction;
//            this.direction = 0.0f;
//            return this;
//        }
//
//        /**
//         * Resume a paused tween
//         *
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method resume
//         */
//        public LTDescr resume() {
//            this.direction = this.lastVal;
//            return this;
//        }
//
//        public LTDescr setAxis(Vector2 axis) {
//            this.axis = axis;
//            return this;
//        }
//
//        /**
//         * Delay the start of a tween
//         *
//         * @param {float} float time The time to complete the tween in
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setDelay
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setDelay(1.5f);
//         */
//        public LTDescr setDelay(float delay) {
//            this.delay = delay;
//            return this;
//        }
//
//        /**
//         * Set the type of easing used for the tween. <br>
//         * <ul><li><a href="LeanTweenType.html">List of all the ease types</a>.</li>
//         * <li><a href="http://www.robertpenner.com/easing/easing_demo.html">This page helps visualize the different easing equations</a></li>
//         * </ul>
//         *
//         * @param {LeanTweenType} easeType:LeanTweenType the easing type to use
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setEase
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setEase(LeanTweenType.easeInBounce);
//         */
//        public LTDescr setEase(LeanTweenType easeType) {
//            this.tweenType = easeType;
//            return this;
//        }
//
//        /**
//         * Set the type of easing used for the tween with a custom curve. <br>
//         *
//         * @param {AnimationCurve} easeDefinition:AnimationCurve an <a href="http://docs.unity3d.com/Documentation/ScriptReference/AnimationCurve.html" target="_blank">AnimationCure</a> that describes the type of easing you want, this is great for when you want a unique type of movement
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setEase (AnimationCurve)
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setEase(LeanTweenType.easeInBounce);
//         */
//        public LTDescr setEase(Vector2[] easeCurve) {
//            this.animationCurve = easeCurve;
//            return this;
//        }
//
//        public LTDescr setFrom(Vector2 from) {
//            this.from = from;
//            return this;
//        }
//
//        public LTDescr setId(int id) {
//            this._id = id;
//            return this;
//        }
//
//        /**
//         * Set the tween to repeat a number of times.
//         *
//         * @param {int} repeatNum:int the number of times to repeat the tween. -1 to repeat infinite times
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setRepeat
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setRepeat(10).setLoopPingPong();
//         */
//        public LTDescr setRepeat(int repeat) {
//            this.loopCount = repeat;
//            if (repeat > 1 && this.loopType == LeanTweenType.once) {
//                this.loopType = LeanTweenType.clamp;
//            }
//            return this;
//        }
//
//        public LTDescr setLoopType(LeanTweenType loopType) {
//            this.loopType = loopType;
//            return this;
//        }
//
//        /**
//         * Use estimated time when tweening an object. Great for pause screens, when you want all other action to be stopped (or slowed down)
//         *
//         * @param {boolean} useEstimatedTime:boolean whether to use estimated time or not
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setUseEstimatedTime
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setRepeat(2).setUseEstimatedTime(true);
//         */
//        public LTDescr setUseEstimatedTime(boolean useEstimatedTime) {
//            this.useEstimatedTime = useEstimatedTime;
//            return this;
//        }
//
//        /**
//         * Use frames when tweening an object, when you don't want the animation to be time-frame independent...
//         *
//         * @param {boolean} useFrames:boolean whether to use estimated time or not
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setUseFrames
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setRepeat(2).setUseFrames(true);
//         */
//        public LTDescr setUseFrames(boolean useFrames) {
//            this.useFrames = useFrames;
//            return this;
//        }
//
//        public LTDescr setLoopCount(int loopCount) {
//            this.loopCount = loopCount;
//            return this;
//        }
//
//        /**
//         * No looping involved, just run once (the default)
//         *
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setLoopOnce
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setLoopOnce();
//         */
//        public LTDescr setLoopOnce() {
//            this.loopType = LeanTweenType.once;
//            return this;
//        }
//
//        /**
//         * When the animation gets to the end it starts back at where it began
//         *
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setLoopClamp
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setRepeat(2).setLoopClamp();
//         */
//        public LTDescr setLoopClamp() {
//            this.loopType = LeanTweenType.clamp;
//            return this;
//        }
//
//        /**
//         * When the animation gets to the end it then tweens back to where it started (and on, and on)
//         *
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setLoopPingPong
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setRepeat(2).setLoopClamp();
//         */
//        public LTDescr setLoopPingPong() {
//            this.loopType = LeanTweenType.pingPong;
//            return this;
//        }
//
//        /**
//         * Have a method called when the tween finishes
//         *
//         * @param {Action} onComplete:Action the method that should be called when the tween is finished ex: tweenFinished(){ }
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setOnComplete
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setOnComplete(tweenFinished);
//         */
//        public LTDescr setOnComplete(Action onComplete) {
//            this.onComplete = onComplete;
//            return this;
//        }
//
//
//        /**
//         * Have a method called on each frame that the tween is being animated (passes a float value)
//         *
//         * @param {Action<float>} onUpdate:Action<float> a method that will be called on every frame with the float value of the tweened object
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setOnUpdate
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setOnUpdate(tweenMoved);<br>
//         * <br>
//         * void tweenMoved( float val ){ }<br>
//         */
//        public LTDescr setOnUpdate(Action<Float> onUpdate) {
//            this.onUpdateFloat = onUpdate;
//            return this;
//        }
//
//
//        public LTDescr setOnUpdateVector2(Action<Vector2> onUpdate) {
//            this.onUpdateVector2 = onUpdate;
//            return this;
//        }
//
//
//        /**
//         * Have an object passed along with the onUpdate method
//         *
//         * @param {object} onUpdateParam:object an object that will be passed along with the onUpdate method
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setOnUpdateParam
//         * @example physicsPort.LeanTween.moveX(gameObject, 5f, 2.0f).setOnUpdate(tweenMoved).setOnUpdateParam(myObject);<br>
//         * <br>
//         * void tweenMoved( float val, object obj ){ }<br>
//         */
//        public LTDescr setOnUpdateParam(Object onUpdateParam) {
//            this.onUpdateParam = onUpdateParam;
//            return this;
//        }
//
//        /**
//         * While tweening along a curve, set this property to true, to be perpendicalur to the path it is moving upon
//         *
//         * @param {boolean} doesOrient:boolean whether the gameobject will orient to the path it is animating along
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setOrientToPath
//         * @example physicsPort.LeanTween.move(ltLogo, path, 1.0f).setEase(LeanTweenType.easeOutQuad).setOrientToPath(true);<br>
//         */
//        public LTDescr setOrientToPath(boolean doesOrient) {
//            if (this.path == null)
//                this.path = new LTBezierPath();
//            this.path.orientToPath = doesOrient;
//            return this;
//        }
//
//        public LTDescr setRect(LTRect rect) {
//            this.ltRect = rect;
//            return this;
//        }
//
//        public LTDescr setRect(Rect rect) {
//            this.ltRect = new LTRect(rect);
//            return this;
//        }
//
//        public LTDescr setPath(LTBezierPath path) {
//            this.path = path;
//            return this;
//        }
//
//        /**
//         * Set the point at which the GameObject will be rotated around
//         *
//         * @param {Vector2} point:Vector2 point at which you want the object to rotate around (local space)
//         * @return {LTDescr} LTDescr an object that distinguishes the tween
//         * @method setPoint
//         * @example physicsPort.LeanTween.rotateAround(cube, Vector2.up, 360.0f, 1.0f) .setPoint( new Vector2(1f,0f,0f) ) .setEase( LeanTweenType.easeInOutBounce );<br>
//         */
//        public LTDescr setPoint(Vector2 point) {
//            this.point = point;
//            return this;
//        }
//
//    }
//
//    /**
//     * Animate GUI Elements by creating this object and passing the *.rect variable to the GUI method<br><br>
//     * <strong>Example Javascript: </strong><br>var bRect:LTRect = new LTRect( 0, 0, 100, 50 );<br>
//     * physicsPort.LeanTween.scale( bRect, Vector2(bRect.rect.width, bRect.rect.height) * 1.3, 0.25 );<br>
//     * function OnGUI(){<br>
//     * &nbsp; if(GUI.Button(bRect.rect, "Scale")){ }<br>
//     * }<br>
//     * <br>
//     * <strong>Example C#: </strong> <br>
//     * LTRect bRect = new LTRect( 0f, 0f, 100f, 50f );<br>
//     * physicsPort.LeanTween.scale( bRect, new Vector2(150f,75f), 0.25f );<br>
//     * void OnGUI(){<br>
//     * &nbsp; if(GUI.Button(bRect.rect, "Scale")){ }<br>
//     * }<br>
//     *
//     * @param {float} x:float X location
//     * @param {float} y:float Y location
//     * @param {float} width:float Width
//     * @param {float} height:float Height
//     * @class LTRect
//     * @constructor
//     */
//
//    static class LTRect {
//        /**
//         * Pass this value to the GUI Methods
//         *
//         * @property rect
//         * @type {Rect} rect:Rect Rect object that controls the positioning and size
//         */
//        public Rect _rect;
//        public float alpha;
//        public float rotation;
//        public Vector2 pivot;
//
//        public boolean rotateEnabled;
//        public boolean rotateFinished;
//        public boolean alphaEnabled;
//
//        public static boolean colorTouched;
//
//        public LTRect() {
//            reset();
//            this.rotateEnabled = this.alphaEnabled = true;
//            _rect = new Rect(0f, 0f, 1f, 1f);
//        }
//
//        public LTRect(Rect rect) {
//            _rect = rect;
//            reset();
//        }
//
//        public LTRect(float x, float y, float width, float height) {
//            _rect = new Rect(x, y, width, height);
//            this.alpha = 1.0f;
//            this.rotation = 0.0f;
//            this.rotateEnabled = this.alphaEnabled = false;
//        }
//
//        public LTRect(float x, float y, float width, float height, float alpha) {
//            _rect = new Rect(x, y, width, height);
//            this.alpha = alpha;
//            this.rotation = 0.0f;
//            this.rotateEnabled = this.alphaEnabled = false;
//        }
//
//        public LTRect(float x, float y, float width, float height, float alpha, float rotation) {
//            _rect = new Rect(x, y, width, height);
//            this.alpha = alpha;
//            this.rotation = rotation;
//            this.rotateEnabled = this.alphaEnabled = false;
//            if (rotation != 0.0f) {
//                this.rotateEnabled = true;
//                resetForRotation();
//            }
//        }
//
//        public void reset() {
//            this.alpha = 1.0f;
//            this.rotation = 0.0f;
//            this.rotateEnabled = this.alphaEnabled = false;
//        }
//
//        public void resetForRotation() {
//            if (pivot == Vector2.Zero) {
//                pivot = new Vector2(_rect.x + _rect.width * 0.5f, _rect.y + _rect.height * 0.5f);
//                _rect.x += -pivot.x;
//                _rect.y += -pivot.y;
//            }
//        }
//
//        public float x() {
//
//            return _rect.x;
//        }
//
//        public void x(float value) {
//            _rect.x = value;
//
//        }
//
//        public float y()
//
//        {
//
//            return _rect.y;
//        }
//
//        void y(float value) {
//            _rect.y = value;
//
//        }
//
//        public Rect rect()
//
//        {
//
////            get {
////            if (colorTouched) {
////                colorTouched = false;
////                GUI.color = new Color(GUI.color.r, GUI.color.g, GUI.color.b, 1.0f);
////            }
////            if (rotateEnabled) {
////                if (rotateFinished) {
////                    rotateFinished = false;
////                    rotateEnabled = false;
////                    _rect.x += pivot.x;
////                    _rect.y += pivot.y;
////                    pivot = Vector2.zero;
////                    GUI.matrix = Matrix4x4.identity;
////                } else {
////                    Matrix4x4 trsMatrix = Matrix4x4.identity;
////                    trsMatrix.SetTRS(pivot, Quaternion.Euler(0f, 0f, rotation), Vector2.one);
////                    GUI.matrix = trsMatrix;
////                }
////            }
////            if (alphaEnabled) {
////                GUI.color = new Color(GUI.color.r, GUI.color.g, GUI.color.b, alpha);
////                colorTouched = true;
////            }
////            return _rect;
////        }
////
////            set {
////            _rect = value;
////        }
//            return null;
//
//        }
//    }
//
//    static class LTBezier {
//        public float length;
//
//        private Vector2 a;
//        private Vector2 aa;
//        private Vector2 bb;
//        private Vector2 cc;
//        private float len;
//        private float[] arclengths;
//
//        public LTBezier(Vector2 a, Vector2 b, Vector2 c, Vector2 d, float precision) {
////            this.a = a;
////            aa = (-a + 3 * (b - c) + d);
////            bb = 3 * (a + c) - 6 * b;
////            cc = 3 * (b - a);
////
////            this.len = 1.0f / precision;
////            arclengths = new float[(int) this.len + (int) 1];
////            arclengths[0] = 0;
////
////            Vector2 ov = a;
////            Vector2 v;
////            float clen = 0.0f;
////            for (int i = 1; i <= this.len; i++) {
////                v = bezierPoint(i * precision);
////                clen += (ov - v).magnitude;
////                this.arclengths[i] = clen;
////                ov = v;
////            }
////            this.length = clen;
//        }
//
//        private float map(float u) {
//            float targetlength = u * this.arclengths[(int) this.len];
//            int low = 0;
//            int high = (int) this.len;
//            int index = 0;
//            while (low < high) {
//                index = low + ((int) ((high - low) / 2.0f) | 0);
//                if (this.arclengths[index] < targetlength) {
//                    low = index + 1;
//                } else {
//                    high = index;
//                }
//            }
//            if (this.arclengths[index] > targetlength)
//                index--;
//            if (index < 0)
//                index = 0;
//
//            return (index + (targetlength - arclengths[index]) / (arclengths[index + 1] - arclengths[index])) / this.len;
//        }
//
//        //private Vector2 bezierPoint(float t) {
////            return ((aa * t + (bb)) * t + cc) * t + a;
////        }
//
////        public Vector2 point(float t) {
////            return bezierPoint(map(t));
////        }
//    }
//
//    /**
//     * Manually animate along a bezier path with this class
//     *
//     * @param {float} pts:Vector2[] A set of points that define one or many bezier paths (the paths should be passed in multiples of 4, which correspond to each individual bezier curve)
//     * @class LTBezierPath
//     * @constructor
//     */
//    static class LTBezierPath {
//        public Vector2[] pts;
//        public float length;
//        public boolean orientToPath;
//
//        private LTBezier[] beziers;
//        private float[] lengthRatio;
//
//        public LTBezierPath() {
//        }
//
//        public LTBezierPath(Vector2[] pts_) {
//            setPoints(pts_);
//        }
//
//        public void setPoints(Vector2[] pts_) {
//            if (pts_.length < 4)
//                LeanTween.logError("physicsPort.LeanTween - When passing values for a vector path, you must pass four or more values!");
//            if (pts_.length % 4 != 0)
//                LeanTween.logError("physicsPort.LeanTween - When passing values for a vector path, they must be in sets of four: controlPoint1, controlPoint2, endPoint2, controlPoint2, controlPoint2...");
//
//            pts = pts_;
//
//            int k = 0;
//            beziers = new LTBezier[pts.length / 4];
//            lengthRatio = new float[beziers.length];
//            int i;
//            for (i = 0; i < pts.length; i += 4) {
//                beziers[k] = new LTBezier(pts[i + 0], pts[i + 2], pts[i + 1], pts[i + 3], 0.05f);
//                length += beziers[k].length;
//                k++;
//            }
//            // Debug.Log("beziers.length:"+beziers.length + " beziers:"+beziers);
//            for (i = 0; i < beziers.length; i++) {
//                lengthRatio[i] = beziers[i].length / length;
//            }
//        }
//
//        public Vector2 point(float ratio) {
////            float added = 0.0f;
////            for (int i = 0; i < lengthRatio.length; i++) {
////                added += lengthRatio[i];
////                if (added >= ratio)
////                    return beziers[i].point((ratio - (added - lengthRatio[i])) / lengthRatio[i]);
////            }
////            return beziers[lengthRatio.length - 1].point(1.0f);
//            return null;
//        }
//
//        public void place(TransformF transform, float ratio) {
//            place(transform, ratio, Vector2.Y);
//        }
//
//        public void place(TransformF transform, float ratio, Vector2 worldUp) {
////            transform.position = point(ratio);
////            ratio += 0.001f;
////            if (ratio <= 1.0f)
////                transform.LookAt(point(ratio), worldUp);
//        }
//
//    }
//
//    enum TweenAction {
//        MOVE_X,
//        MOVE_Y,
//        MOVE_Z,
//        MOVE_LOCAL_X,
//        MOVE_LOCAL_Y,
//        MOVE_LOCAL_Z,
//        MOVE_CURVED,
//        MOVE_CURVED_LOCAL,
//        SCALE_X,
//        SCALE_Y,
//        SCALE_Z,
//        ROTATE_X,
//        ROTATE_Y,
//        ROTATE_Z,
//        ROTATE_AROUND,
//        ALPHA,
//        ALPHA_VERTEX,
//        CALLBACK,
//        MOVE,
//        MOVE_LOCAL,
//        ROTATE,
//        ROTATE_LOCAL,
//        SCALE,
//        VALUE3,
//        GUI_MOVE,
//        GUI_SCALE,
//        GUI_ALPHA,
//        GUI_ROTATE
//    }
//
//    /**
//     * physicsPort.LeanTween is an efficient tweening engine for Unity3d<br><br>
//     * <a href="#index">Index of All Methods</a> | <a href="LTDescr.html">Optional Paramaters that can be passed</a><br><br>
//     * <strong id='optional'>Optional Parameters</strong> are passed at the end of every method<br>
//     * <br>
//     * <i>Example:</i><br>
//     * physicsPort.LeanTween.moveX( gameObject, 1f, 1f).setEase( <a href="LeanTweenType.html">LeanTweenType</a>.easeInQuad ).setDelay(1f);<br>
//     * <br>
//     * You can pass the optional parameters in any order, and chain on as many as you wish.<br>
//     * You can also pass parameters at a later time by saving a reference to what is returned.<br>
//     * <br>
//     * <i>Example:</i><br>
//     * <a href="LTDescr.html">LTDescr</a> d = physicsPort.LeanTween.moveX(gameObject, 1f, 1f);<br>
//     * &nbsp; ...later set some parameters<br>
//     * d.setOnComplete( onCompleteFunc ).setEase( <a href="LeanTweenType.html">LeanTweenType</a>.easeInOutBack );<br>
//     *
//     * @class physicsPort.LeanTween
//     */
//
//    static class LTEvent {
//        public int id;
//        public Object data;
//
//        public LTEvent(int id, Object data) {
//            this.id = id;
//            this.data = data;
//        }
//    }
//
//    public static boolean throwErrors = true;
//
//    private static final List<LTDescr> tweens = new ArrayList<>();
//    private static int frameRendered = -1;
//    private static GameObject _tweenEmpty;
//    private static float dtEstimated;
//    private static float previousRealTime;
//    private static float dt;
//    private static float dtActual;
//    private static LTDescr tween;
//    private static int i;
//    private static int j;
//    private static Vector2[] punch = {new Vector2(0.0f, 0.0f),
//            new Vector2(0.112586f, 0.9976035f),
//            new Vector2(0.3120486f, -0.1720615f),
//            new Vector2(0.4316337f, 0.07030682f),
//            new Vector2(0.5524869f, -0.03141804f),
//            new Vector2(0.6549395f, 0.003909959f),
//            new Vector2(0.770987f, -0.009817753f),
//            new Vector2(0.8838775f, 0.001939224f),
//            new Vector2(1.0f, 0.0f)};
//
//
//
//
//    public static void reset() {
//        tweens.clear();
//    }
//
//    public void Update() {
//        LeanTween.update();
//    }
//
//    private static TransformF trans;
//    private static long timeTotal;
//    private static TweenAction tweenAction;
//    private static float ratioPassed;
//    private static float from;
//    private static float to;
//    private static float val;
//    private static Vector2 newVect;
//    private static boolean isTweenFinished;
//    private static GameObject target;
//    private static GameObject customTarget;
//
//    public static void update() {
//        if (frameRendered != Time.frameCount) { // make sure update is only called once per frame
//
//
//            dtEstimated = Time.realtimeSinceStartup - previousRealTime;
//            if (dtEstimated > 0.2f) // a catch put in, when at the start sometimes this number can grow unrealistically large
//                dtEstimated = 0.2f;
//            previousRealTime = Time.realtimeSinceStartup;
//            dtActual = Time.deltaTime * Time.timeScale;
//            // if(tweenMaxSearch>1500)
//            // 	Debug.Log("tweenMaxSearch:"+tweenMaxSearch +" maxTweens:"+maxTweens);
//            for (int i = 0; i < tweenMaxSearch && i < maxTweens; i++) {
//
//                //Debug.Log("tweens["+i+"].toggle:"+tweens[i].toggle);
//                if (tweens[i].toggle) {
//                    tween = tweens[i];
//                    trans = tween.trans;
//                    timeTotal = tween.time ;
//                    tweenAction = tween.type;
//
//                    dt = dtActual;
//                    if (tween.useEstimatedTime) {
//                        dt = dtEstimated;
//                        timeTotal = tween.time;
//                    } else if (tween.useFrames) {
//                        dt = 1;
//                    }
//
//                    if (trans == null) {
//                        removeTween(i);
//                        continue;
//                    }
//
//                    // Check for tween finished
//                    isTweenFinished = false;
//                    if ((tween.passed + dt > timeTotal && tween.direction > 0.0f)) {
//                        isTweenFinished = true;
//                        tween.passed = timeTotal; // Set to the exact end time so that it can finish tween exactly on the end value
//                    } else if (tween.direction < 0.0f && tween.passed - dt < 0.0f) {
//                        isTweenFinished = true;
//                        tween.passed = Mathf.Epsilon;
//                    }
//
//                    if ((tween.passed == 0.0 && tweens[i].delay == 0.0) || (!tween.hasInitiliazed && tween.passed > 0.0)) {
//                        tween.hasInitiliazed = true;
//                        // Initialize From Values
//
//
//                        switch (tweenAction) {
//                            case MOVE:
//                                tween.from = trans.position;
//                                break;
//                            case MOVE_X:
//                                tween.from.x = trans.position.x;
//                                break;
//                            case MOVE_Y:
//                                tween.from.x = trans.position.y;
//                                break;
//
//                            case MOVE_LOCAL_X:
//                                tweens.get(i).from.x = trans.localPosition.x;
//                                break;
//                            case MOVE_LOCAL_Y:
//                                tweens.get(i).from.x = trans.localPosition.y;
//                                break;
//
//                            break;
//                            case SCALE_X:
//                                tween.from.x = trans.localScale.x;
//                                break;
//                            case SCALE_Y:
//                                tween.from.x = trans.localScale.y;
//                                break;
//
//                            break;
//                            case ALPHA:
////                                tween.from.x = trans.alpha;
//                                break;
//                            case MOVE_LOCAL:
//                                tween.from = trans.localPosition;
//                                break;
//                            case MOVE_CURVED:
//                                // tween.path.pts[0] = trans.position;
//                                tween.from.x = 0;
//                                break;
//                            case MOVE_CURVED_LOCAL:
//                                // tween.path.pts[0] = trans.localPosition;
//                                tween.from.x = 0;
//                                break;
//
//                            case SCALE:
//                                tween.from = trans.localScale;
//                                break;
//
//
//                        }
//                        tween.diff.x = tween.to.x - tween.from.x;
//                        tween.diff.y = tween.to.y - tween.from.y;
//                    }
//                    if (tween.delay <= 0) {
//                        // Move Values
//                        if (timeTotal <= 0f) {
//                            //Debug.LogError("time total is zero Time.timeScale:"+Time.timeScale+" useEstimatedTime:"+tween.useEstimatedTime);
//                            ratioPassed = 0f;
//                        } else {
//                            ratioPassed = tween.passed / timeTotal;
//                        }
//
//                        if (ratioPassed > 1.0)
//                            ratioPassed = 1.0f;
//
//                        if (tweenAction.ordinal() >= TweenAction.MOVE_X.ordinal() && tweenAction.ordinal() <= TweenAction.CALLBACK.ordinal()) {
//
//                            switch (tween.tweenType) {
//                                case linear:
//                                    val = tween.from.x + tween.diff.x * ratioPassed;
//                                    break;
//                                case easeOutQuad:
//                                    val = easeOutQuadOpt(tween.from.x, tween.diff.x, ratioPassed);
//                                    break;
//                                case easeInQuad:
//                                    val = easeInQuadOpt(tween.from.x, tween.diff.x, ratioPassed);
//                                    break;
//                                case easeInOutQuad:
//                                    val = easeInOutQuadOpt(tween.from.x, tween.diff.x, ratioPassed);
//                                    break;
//                                case easeInCubic:
//                                    val = easeInCubic(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutCubic:
//                                    val = easeOutCubic(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutCubic:
//                                    val = easeInOutCubic(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInQuart:
//                                    val = easeInQuart(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutQuart:
//                                    val = easeOutQuart(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutQuart:
//                                    val = easeInOutQuart(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInQuint:
//                                    val = easeInQuint(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutQuint:
//                                    val = easeOutQuint(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutQuint:
//                                    val = easeInOutQuint(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInSine:
//                                    val = easeInSine(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutSine:
//                                    val = easeOutSine(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutSine:
//                                    val = easeInOutSine(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInExpo:
//                                    val = easeInExpo(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutExpo:
//                                    val = easeOutExpo(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutExpo:
//                                    val = easeInOutExpo(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInCirc:
//                                    val = easeInCirc(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutCirc:
//                                    val = easeOutCirc(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutCirc:
//                                    val = easeInOutCirc(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInBounce:
//                                    val = easeInBounce(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutBounce:
//                                    val = easeOutBounce(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutBounce:
//                                    val = easeInOutBounce(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInBack:
//                                    val = easeInBack(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutBack:
//                                    val = easeOutBack(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutBack:
//                                    val = easeInOutElastic(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInElastic:
//                                    val = easeInElastic(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeOutElastic:
//                                    val = easeOutElastic(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case easeInOutElastic:
//                                    val = easeInOutElastic(tween.from.x, tween.to.x, ratioPassed);
//                                    break;
//                                case punch:
//                                    tween.animationCurve = LeanTween.punch;
//                                    tween.to.x = tween.from.x + tween.to.x;
////                                    val = tweenOnCurve(tween, ratioPassed);
//                                    break;
//                                default: {
//                                    val = tween.from.x + tween.diff.x * ratioPassed;
//                                    break;
//                                }
//                            }
//
//
//                            //Debug.Log("from:"+from+" to:"+to+" val:"+val+" ratioPassed:"+ratioPassed);
//                            if (tweenAction == TweenAction.MOVE_X) {
//                                trans.position = new Vector2(val, trans.position.y);
//                            } else if (tweenAction == TweenAction.MOVE_Y) {
//                                trans.position = new Vector2(trans.position.x, val);
//                            } else if (tweenAction == TweenAction.MOVE_Z) {
//                                trans.position = new Vector2(trans.position.x, trans.position.y);
//                            }
//                            if (tweenAction == TweenAction.MOVE_LOCAL_X) {
//                                trans.localPosition = new Vector2(val, trans.localPosition.y);
//                            } else if (tweenAction == TweenAction.MOVE_LOCAL_Y) {
//                                trans.localPosition = new Vector2(trans.localPosition.x, val);
//                            } else if (tweenAction == TweenAction.MOVE_LOCAL_Z) {
//                                trans.localPosition = new Vector2(trans.localPosition.x, trans.localPosition.y);
////                            } else if (tweenAction == TweenAction.MOVE_CURVED) {
////                                if (tween.path.orientToPath) {
////                                    tween.path.place(trans, val);
////                                } else {
////                                    trans.position = tween.path.point(val);
////                                }
//                                // Debug.Log("val:"+val+" trans.position:"+trans.position + " 0:"+ tween.curves[0] +" 1:"+tween.curves[1] +" 2:"+tween.curves[2] +" 3:"+tween.curves[3]);
//                            } else if (tweenAction == TweenAction.SCALE_X) {
//                                trans.localScale = new Vector2(val, trans.localScale.y);
//                            } else if (tweenAction == TweenAction.SCALE_Y) {
//                                trans.localScale = new Vector2(trans.localScale.x, val);
//                            } else if (tweenAction == TweenAction.SCALE_Z) {
//                                trans.localScale = new Vector2(trans.localScale.x, trans.localScale.y);
//
//                            } else if (tweenAction == TweenAction.ALPHA) {
//
//                            }
//                        } else if (tweenAction.ordinal() >= TweenAction.MOVE.ordinal()) {
//                            //
//
//
//                            if (tween.tweenType == LeanTweenType.linear) {
//                                newVect.x = tween.from.x + tween.diff.x * ratioPassed;
//                                newVect.y = tween.from.y + tween.diff.y * ratioPassed;
//                            } else if (tween.tweenType.ordinal() >= LeanTweenType.linear.ordinal()) {
//                                switch (tween.tweenType) {
//                                    case easeOutQuad:
//                                        newVect = new Vector2(easeOutQuadOpt(tween.from.x, tween.diff.x, ratioPassed), easeOutQuadOpt(tween.from.y, tween.diff.y, ratioPassed));
//                                        break;
//                                    case easeInQuad:
//                                        newVect = new Vector2(easeInQuadOpt(tween.from.x, tween.diff.x, ratioPassed), easeInQuadOpt(tween.from.y, tween.diff.y, ratioPassed));
//                                        break;
//                                    case easeInOutQuad:
//                                        newVect = new Vector2(easeInOutQuadOpt(tween.from.x, tween.diff.x, ratioPassed), easeInOutQuadOpt(tween.from.y, tween.diff.y, ratioPassed));
//                                        break;
//                                    case easeInCubic:
//                                        newVect = new Vector2(easeInCubic(tween.from.x, tween.to.x, ratioPassed), easeInCubic(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutCubic:
//                                        newVect = new Vector2(easeOutCubic(tween.from.x, tween.to.x, ratioPassed), easeOutCubic(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutCubic:
//                                        newVect = new Vector2(easeInOutCubic(tween.from.x, tween.to.x, ratioPassed), easeInOutCubic(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInQuart:
//                                        newVect = new Vector2(easeInQuart(tween.from.x, tween.to.x, ratioPassed), easeInQuart(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutQuart:
//                                        newVect = new Vector2(easeOutQuart(tween.from.x, tween.to.x, ratioPassed), easeOutQuart(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutQuart:
//                                        newVect = new Vector2(easeInOutQuart(tween.from.x, tween.to.x, ratioPassed), easeInOutQuart(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInQuint:
//                                        newVect = new Vector2(easeInQuint(tween.from.x, tween.to.x, ratioPassed), easeInQuint(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutQuint:
//                                        newVect = new Vector2(easeOutQuint(tween.from.x, tween.to.x, ratioPassed), easeOutQuint(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutQuint:
//                                        newVect = new Vector2(easeInOutQuint(tween.from.x, tween.to.x, ratioPassed), easeInOutQuint(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInSine:
//                                        newVect = new Vector2(easeInSine(tween.from.x, tween.to.x, ratioPassed), easeInSine(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutSine:
//                                        newVect = new Vector2(easeOutSine(tween.from.x, tween.to.x, ratioPassed), easeOutSine(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutSine:
//                                        newVect = new Vector2(easeInOutSine(tween.from.x, tween.to.x, ratioPassed), easeInOutSine(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutExpo:
//                                        newVect = new Vector2(easeOutExpo(tween.from.x, tween.to.x, ratioPassed), easeOutExpo(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInExpo:
//                                        newVect = new Vector2(easeInExpo(tween.from.x, tween.to.x, ratioPassed), easeInExpo(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutExpo:
//                                        newVect = new Vector2(easeInOutExpo(tween.from.x, tween.to.x, ratioPassed), easeInOutExpo(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInCirc:
//                                        newVect = new Vector2(easeInCirc(tween.from.x, tween.to.x, ratioPassed), easeInCirc(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutCirc:
//                                        newVect = new Vector2(easeOutCirc(tween.from.x, tween.to.x, ratioPassed), easeOutCirc(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutCirc:
//                                        newVect = new Vector2(easeInOutCirc(tween.from.x, tween.to.x, ratioPassed), easeInOutCirc(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInBounce:
//                                        newVect = new Vector2(easeInBounce(tween.from.x, tween.to.x, ratioPassed), easeInBounce(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutBounce:
//                                        newVect = new Vector2(easeOutBounce(tween.from.x, tween.to.x, ratioPassed), easeOutBounce(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutBounce:
//                                        newVect = new Vector2(easeInOutBounce(tween.from.x, tween.to.x, ratioPassed), easeInOutBounce(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInBack:
//                                        newVect = new Vector2(easeInBack(tween.from.x, tween.to.x, ratioPassed), easeInBack(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutBack:
//                                        newVect = new Vector2(easeOutBack(tween.from.x, tween.to.x, ratioPassed), easeOutBack(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutBack:
//                                        newVect = new Vector2(easeInOutBack(tween.from.x, tween.to.x, ratioPassed), easeInOutBack(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInElastic:
//                                        newVect = new Vector2(easeInElastic(tween.from.x, tween.to.x, ratioPassed), easeInElastic(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeOutElastic:
//                                        newVect = new Vector2(easeOutElastic(tween.from.x, tween.to.x, ratioPassed), easeOutElastic(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case easeInOutElastic:
//                                        newVect = new Vector2(easeInOutElastic(tween.from.x, tween.to.x, ratioPassed), easeInOutElastic(tween.from.y, tween.to.y, ratioPassed));
//                                        break;
//                                    case punch:
//                                        tween.animationCurve = LeanTween.punch;
//                                        tween.to.x = tween.from.x + tween.to.x;
//                                        tween.to.y = tween.from.y + tween.to.y;
//                                        if (tweenAction == TweenAction.ROTATE || tweenAction == TweenAction.ROTATE_LOCAL) {
//                                            tween.to.x = closestRot(tween.from.x, tween.to.x);
//                                            tween.to.y = closestRot(tween.from.y, tween.to.y);
//                                        }
////                                            newVect = tweenOnCurveVector(tween, ratioPassed);
//                                        break;
//                                }
//                            } else {
//                                newVect.x = tween.from.x + tween.diff.x * ratioPassed;
//                                newVect.y = tween.from.y + tween.diff.y * ratioPassed;
//                            }
//                        }
//
//                        if (tweenAction == TweenAction.MOVE) {
//                            trans.position = newVect;
//                        }
//
//                        //Debug.Log("tween.delay:"+tween.delay + " tween.passed:"+tween.passed + " tweenAction:"+tweenAction + " from:"+newVect);
//
//                        if (tween.onUpdateFloat != null) {
//                            tween.onUpdateFloat.call(val);
//
//                        } else if (tween.onUpdateVector2 != null) {
//                            tween.onUpdateVector2.call(newVect);
//                        }
//                    }
//
//                    if (isTweenFinished) {
//                        // Debug.Log("fininished tween:"+tween.id);
//                        if (tweenAction == TweenAction.GUI_ROTATE)
//                            tween.ltRect.rotateFinished = true;
//
//                        if (tween.loopType == LeanTweenType.once || tween.loopCount == 1) {
//                            if (tween.onComplete != null) {
//                                removeTween(i);
//                                tween.onComplete.call(null);
//
//                            } else {
//                                if (tween.loopCount >= 1) {
//                                    tween.loopCount--;
//                                }
//                                if (tween.loopType == LeanTweenType.clamp) {
//                                    tween.passed = Mathf.Epsilon;
//                                    // tween.delay = 0.0;
//                                } else if (tween.loopType == LeanTweenType.pingPong) {
//                                    tween.direction = 0.0f - (tween.direction);
//                                }
//                            }
//                        } else if (tween.delay <= 0) {
//                            tween.passed += dt * tween.direction;
//                        } else {
//                            tween.delay -= dt;
//                            if (tween.delay < 0) {
//                                tween.passed = 0.0f;//-tween.delay
//                                tween.delay = 0.0f;
//                            }
//                        }
//                    }
//                }
//
//                frameRendered = Time.frameCount;
//            }
//        }
//    }
//
//    public static void removeTween(int i) {
//
//    }
//
//    public static Vector2[] add(Vector2[] a, Vector2 b) {
//        Vector2[] c = new Vector2[a.length];
//        for (i = 0; i < a.length; i++) {
//            c[i] = a[i].add(b);
//        }
//
//        return c;
//    }
//
//    public static float closestRot(float from, float to) {
//        float minusWhole = 0 - (360 - to);
//        float plusWhole = 360 + to;
//        float toDiffAbs = Mathf.abs(to - from);
//        float minusDiff = Mathf.abs(minusWhole - from);
//        float plusDiff = Mathf.abs(plusWhole - from);
//        if (toDiffAbs < minusDiff && toDiffAbs < plusDiff) {
//            return to;
//        } else {
//            if (minusDiff < plusDiff) {
//                return minusWhole;
//            } else {
//                return plusWhole;
//            }
//        }
//    }
//
//    /**
//     * Cancel all tweens that are currently targeting the gameObject
//     *
//     * @param {GameObject} gameObject:GameObject gameObject whose tweens you want to cancel
//     * @method physicsPort.LeanTween.cancel
//     */
//    public static void cancel(GameObject gameObject) {
//        TransformF trans = gameObject.transform;
//        for (int i = 0; i < tweenMaxSearch; i++) {
//            if (tweens[i].trans == trans)
//                removeTween(i);
//        }
//    }
//
//    // Deprecated use cancel( id )
//    public static void cancel(GameObject gameObject, int id) {
//        cancel(id);
//    }
//
//    // Deprecated use cancel( id )
//    public static void cancel(LTRect ltRect, int id) {
//        cancel(id);
//    }
//
//    /**
//     * Cancel all tweens that are currently targeting the gameObject
//     *
//     * @param {int} id:int id of the tween you want to cancel
//     * @method physicsPort.LeanTween.cancel
//     * @example int tweenIDMove = physicsPort.LeanTween.move( gameObject, new Vector2(0f,1f,2f), 1f).id; <br>
//     * physicsPort.LeanTween.cancel( tweenIDMove );
//     */
//    public static void cancel(int uniqueId) {
//        int backId = uniqueId & 0xFFFFFF;
//        int backType = uniqueId >> 24;
//        // Debug.Log("id:"+backId+" action:"+backType + " tweens[id].type:"+tweens[backId].type +" action:"+(TweenAction)backType);
////        if (tweens[backId].type == (TweenAction) backType)
//            removeTween(backId);
//    }
//
//
//
//
//
//    /**
//     * Pause all tweens for a GameObject
//     *
//     * @param {GameObject} gameObject:GameObject GameObject whose tweens you want to pause
//     * @method physicsPort.LeanTween.pause
//     */
//    public static void pause(GameObject gameObject) {
//        TransformF trans = gameObject.transform;
//        for (int i = 0; i < tweenMaxSearch; i++) {
//            if (tweens[i].trans == trans) {
//                tweens[i].lastVal = tweens[i].direction;
//                tweens[i].direction = 0.0f;
//            }
//        }
//    }
//
//
//
//
//    /**
//     * Resume all the tweens on a GameObject
//     *
//     * @param {GameObject} gameObject:GameObject GameObject whose tweens you want to resume
//     * @method physicsPort.LeanTween.resume
//     */
//    public static void resume(GameObject gameObject) {
//        TransformF trans = gameObject.transform;
//        for (int i = 0; i < tweenMaxSearch; i++) {
//            if (tweens[i].trans == trans)
//                tweens[i].direction = tweens[i].lastVal;
//        }
//    }
//
//    /**
//     * Test whether or not a tween is active on a GameObject
//     *
//     * @param {GameObject} gameObject:GameObject GameObject that you want to test if it is tweening
//     * @method physicsPort.LeanTween.isTweening
//     */
//    public static boolean isTweening(GameObject gameObject) {
//        TransformF trans = gameObject.transform;
//        for (int i = 0; i < tweenMaxSearch; i++) {
//            if (tweens[i].toggle && tweens[i].trans == trans)
//                return true;
//        }
//        return false;
//    }
//
//    /**
//     * Test whether or not a tween is active on a LTRect
//     *
//     * @param {LTRect} ltRect:LTRect LTRect that you want to test if it is tweening
//     * @method physicsPort.LeanTween.isTweening
//     */
//    public static boolean isTweening(LTRect ltRect) {
//        for (int i = 0; i < tweenMaxSearch; i++) {
//            if (tweens[i].toggle && tweens[i].ltRect == ltRect)
//                return true;
//        }
//        return false;
//    }
//
////    public static void drawBezierPath(Vector2 a, Vector2 b, Vector2 c, Vector2 d) {
////        Vector2 last = a;
////        Vector2 p;
////        Vector2 aa = (-a + 3 * (b - c) + d);
////        Vector2 bb = 3 * (a + c) - 6 * b;
////        Vector2 cc = 3 * (b - a);
////        float t;
////        for (float k = 1.0f; k <= 30.0f; k++) {
////            t = k / 30.0f;
////            p = ((aa * t + (bb)) * t + cc) * t + a;
////            // Gizmos.DrawLine(last, p);
////            last = p;
////        }
////    }
//
//    public static void logError(String error) {
//        if (throwErrors) {
//            System.out.println(error);
//        } else {
//            System.err.println(error);
//        }
//
//    }
//
//
//
//
//    public static LTDescr descr;
//
//    private static LTDescr pushNewTween(GameObject gameObject, Vector2 to, float time, TweenAction tweenAction, LTDescr tween) {
//
//        if (gameObject == null)
//            return null;
//        tween.trans = gameObject.transform;
//        tween.to = to;
//        tween.time = time;
//        tween.type = tweenAction;
//
//        return tween;
//    }
//
//    /**
//     * Fade a gameobject's material to a certain alpha value. The material's shader needs to support alpha. <a href="http://owlchemylabs.com/content/">Owl labs has some excellent efficient shaders</a>.
//     *
//     * @param {GameObject} gameObject:GameObject Gameobject that you wish to fade
//     * @param {float}      to:float the final alpha value (0-1)
//     * @param {float}      time:float The time with which to fade the object
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.alpha
//     * @example physicsPort.LeanTween.alpha(gameObject, 1f, 1f) .setDelay(1f);
//     */
//    public static LTDescr alpha(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.ALPHA, options());
//    }
//
//
//
//
//
//
//    public static LTDescr delayedCall(GameObject gameObject, float delayTime, Action callback) {
//        return pushNewTween(gameObject, Vector2.Zero, delayTime, TweenAction.CALLBACK, options().setOnComplete(callback));
//    }
//
///*public static LTDescr delayedCall(GameObject gameObject, float delayTime, string callback){
//    return pushNewTween( gameObject, Vector2.zero, delayTime, TweenAction.CALLBACK, options().setOnComplete( callback ) );
//}*/
//
//    /**
//     * Move a GameObject to a certain location
//     *
//     * @param {GameObject} GameObject gameObject Gameobject that you wish to move
//     * @param {Vector2}    vec:Vector2 to The final positin with which to move to
//     * @param {float}      time:float time The time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.move
//     * @example physicsPort.LeanTween.move(gameObject, new Vector2(0f,-3f, 5f), 2.0f) .setEase( physicsPort.LeanTween.easeOutQuad );
//     */
//    public static LTDescr move(GameObject gameObject, Vector2 to, float time) {
//        return pushNewTween(gameObject, to, time, TweenAction.MOVE, options());
//    }
//
//    /**
//     * Move a GameObject along a set of bezier curves
//     *
//     * @param {GameObject} gameObject:GameObject Gameobject that you wish to move
//     * @param {Vector2[]}  path:Vector2[] A set of points that define the curve(s) ex: Point1,Handle1,Handle2,Point2,...
//     * @param {float}      time:float The time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.move
//     * @example <i>Javascript:</i><br>
//     * physicsPort.LeanTween.move(gameObject, [Vector2(0,0,0),Vector2(1,0,0),Vector2(1,0,0),Vector2(1,0,1)], 2.0) .setEase(physicsPort.LeanTween.easeOutQuad).setOrientToPath(true);<br><br>
//     * <i>C#:</i><br>
//     * physicsPort.LeanTween.move(gameObject, new Vector2{Vector2(0f,0f,0f),Vector2(1f,0f,0f),Vector2(1f,0f,0f),Vector2(1f,0f,1f)}, 1.5f) .setEase(physicsPort.LeanTween.easeOutQuad).setOrientToPath(true);;<br>
//     */
//    public static LTDescr move(GameObject gameObject, Vector2[] to, float time) {
//        descr = options();
//        if (descr.path == null)
//            descr.path = new LTBezierPath(to);
//        else
//            descr.path.setPoints(to);
//
//        return pushNewTween(gameObject, new Vector2(1.0f, 0.0f), time, TweenAction.MOVE_CURVED, descr);
//    }
//
//
//
//    /**
//     * Move a GameObject along the x-axis
//     *
//     * @param {GameObject} gameObject:GameObject gameObject Gameobject that you wish to move
//     * @param {float}      to:float to The final position with which to move to
//     * @param {float}      time:float time The time to complete the move in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.moveX
//     */
//    public static LTDescr moveX(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.MOVE_X, options());
//    }
//
//    /**
//     * Move a GameObject along the y-axis
//     *
//     * @param {GameObject} GameObject gameObject Gameobject that you wish to move
//     * @param {float}      float to The final position with which to move to
//     * @param {float}      float time The time to complete the move in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.moveY
//     */
//    public static LTDescr moveY(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.MOVE_Y, options());
//    }
//
//    /**
//     * Move a GameObject along the z-axis
//     *
//     * @param {GameObject} GameObject gameObject Gameobject that you wish to move
//     * @param {float}      float to The final position with which to move to
//     * @param {float}      float time The time to complete the move in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.moveZ
//     */
//    public static LTDescr moveZ(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.MOVE_Z, options());
//    }
//
//    /**
//     * Move a GameObject to a certain location relative to the parent transform.
//     *
//     * @param {GameObject} GameObject gameObject Gameobject that you wish to rotate
//     * @param {Vector2}    Vector2 to The final positin with which to move to
//     * @param {float}      float time The time to complete the tween in
//     * @param {Hashtable}  Hashtable optional Hashtable where you can pass <a href="#optional">optional items</a>.
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.moveLocal
//     */
//    public static LTDescr moveLocal(GameObject gameObject, Vector2 to, float time) {
//        return pushNewTween(gameObject, to, time, TweenAction.MOVE_LOCAL, options());
//    }
//
//    /**
//     * Move a GameObject along a set of bezier curves
//     *
//     * @param {GameObject} gameObject:GameObject Gameobject that you wish to move
//     * @param {Vector2[]}  path:Vector2[] A set of points that define the curve(s) ex: Point1,Handle1,Handle2,Point2,...
//     * @param {float}      time:float The time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.move
//     * @example <i>Javascript:</i><br>
//     * physicsPort.LeanTween.move(gameObject, [Vector2(0,0,0),Vector2(1,0,0),Vector2(1,0,0),Vector2(1,0,1)], 2.0).setEase(physicsPort.LeanTween.easeOutQuad).setOrientToPath(true);<br><br>
//     * <i>C#:</i><br>
//     * physicsPort.LeanTween.move(gameObject, new Vector2{Vector2(0f,0f,0f),Vector2(1f,0f,0f),Vector2(1f,0f,0f),Vector2(1f,0f,1f)}).setEase(physicsPort.LeanTween.easeOutQuad).setOrientToPath(true);<br>
//     */
//    public static LTDescr moveLocal(GameObject gameObject, Vector2[] to, float time) {
//        descr = options();
//        if (descr.path == null)
//            descr.path = new LTBezierPath(to);
//        else
//            descr.path.setPoints(to);
//
//        return pushNewTween(gameObject, new Vector2(1.0f, 0.0f), time, TweenAction.MOVE_CURVED_LOCAL, descr);
//    }
//
//    public static LTDescr moveLocalX(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.MOVE_LOCAL_X, options());
//    }
//
//    public static LTDescr moveLocalY(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.MOVE_LOCAL_Y, options());
//    }
//
//    public static LTDescr moveLocalZ(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.MOVE_LOCAL_Z, options());
//    }
//
//    /**
//     * Rotate a GameObject, to values are in passed in degrees
//     *
//     * @param {GameObject} GameObject gameObject Gameobject that you wish to rotate
//     * @param {Vector2}    Vector2 to The final rotation with which to rotate to
//     * @param {float}      float time The time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.rotate
//     * @example physicsPort.LeanTween.rotate(cube, new Vector2(180f, 30f, 0f), 1.5f);
//     */
//
//    public static LTDescr rotate(GameObject gameObject, Vector2 to, float time) {
//        return pushNewTween(gameObject, to, time, TweenAction.ROTATE, options());
//    }
//
//
//    /**
//     * Rotate a GameObject in the objects local space (on the transforms localEulerAngles object)
//     *
//     * @param {GameObject} gameObject:GameObject Gameobject that you wish to rotate
//     * @param {Vector2}    to:Vector2 The final rotation with which to rotate to
//     * @param {float}      time:float The time to complete the rotation in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.rotateLocal
//     */
//    public static LTDescr rotateLocal(GameObject gameObject, Vector2 to, float time) {
//        return pushNewTween(gameObject, to, time, TweenAction.ROTATE_LOCAL, options());
//    }
//
//    /**
//     * Rotate a GameObject only on the X axis
//     *
//     * @param {GameObject} GameObject Gameobject that you wish to rotate
//     * @param {float}      to:float The final x-axis rotation with which to rotate
//     * @param {float}      time:float The time to complete the rotation in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.rotateX
//     */
//    public static LTDescr rotateX(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.ROTATE_X, options());
//    }
//
//    /**
//     * Rotate a GameObject only on the Y axis
//     *
//     * @param {GameObject} GameObject Gameobject that you wish to rotate
//     * @param {float}      to:float The final y-axis rotation with which to rotate
//     * @param {float}      time:float The time to complete the rotation in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.rotateY
//     */
//    public static LTDescr rotateY(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.ROTATE_Y, options());
//    }
//
//    /**
//     * Rotate a GameObject only on the Z axis
//     *
//     * @param {GameObject} GameObject Gameobject that you wish to rotate
//     * @param {float}      to:float The final z-axis rotation with which to rotate
//     * @param {float}      time:float The time to complete the rotation in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.rotateZ
//     */
//    public static LTDescr rotateZ(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.ROTATE_Z, options());
//    }
//
//    /**
//     * Rotate a GameObject around a certain Axis (the best method to use when you want to rotate beyond 180 degrees)
//     *
//     * @param {GameObject} gameObject:GameObject Gameobject that you wish to rotate
//     * @param {Vector2}    vec:Vector2 axis in which to rotate around ex: Vector2.up
//     * @param {float}      degrees:float the degrees in which to rotate
//     * @param {float}      time:float time The time to complete the rotation in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.rotateAround
//     */
//    public static LTDescr rotateAround(GameObject gameObject, Vector2 axis, float add, float time) {
//        return pushNewTween(gameObject, new Vector2(add, 0f), time, TweenAction.ROTATE_AROUND, options().setAxis(axis));
//    }
//
//    /**
//     * Scale a GameObject to a certain size
//     *
//     * @param {GameObject} gameObject:GameObject gameObject Gameobject that you wish to scale
//     * @param {Vector2}    vec:Vector2 to The size with which to tween to
//     * @param {float}      time:float time The time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.scale
//     */
//    public static LTDescr scale(GameObject gameObject, Vector2 to, float time) {
//        return pushNewTween(gameObject, to, time, TweenAction.SCALE, options());
//    }
//
//
//    /**
//     * Scale a GameObject to a certain size along the x-axis only
//     *
//     * @param {GameObject} gameObject:GameObject Gameobject that you wish to scale
//     * @param {float}      scaleTo:float the size with which to scale to
//     * @param {float}      time:float the time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.scaleX
//     */
//    public static LTDescr scaleX(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.SCALE_X, options());
//    }
//
//    /**
//     * Scale a GameObject to a certain size along the y-axis only
//     *
//     * @param {GameObject} gameObject:GameObject Gameobject that you wish to scale
//     * @param {float}      scaleTo:float the size with which to scale to
//     * @param {float}      time:float the time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.scaleY
//     */
//    public static LTDescr scaleY(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.SCALE_Y, options());
//    }
//
//    /**
//     * Scale a GameObject to a certain size along the z-axis only
//     *
//     * @param {GameObject} gameObject:GameObject Gameobject that you wish to scale
//     * @param {float}      scaleTo:float the size with which to scale to
//     * @param {float}      time:float the time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.scaleZ
//     */
//    public static LTDescr scaleZ(GameObject gameObject, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.SCALE_Z, options());
//    }
//
//    /**
//     * Tween any particular value, it does not need to be tied to any particular type or GameObject
//     *
//     * @param {GameObject}    GameObject gameObject GameObject with which to tie the tweening with. This is only used when you need to cancel this tween, it does not actually perform any operations on this gameObject
//     * @param {Action<float>} callOnUpdate:Action<float> The function that is called on every Update frame, this function needs to accept a float value ex: function updateValue( float val ){ }
//     * @param {float}         float from The original value to start the tween from
//     * @param {float}         float to The value to end the tween on
//     * @param {float}         float time The time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.value (float)
//     */
//    public static LTDescr value(GameObject gameObject, Action<Float> callOnUpdate, float from, float to, float time) {
//        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.CALLBACK, options().setFrom(new Vector2(from, 0)).setOnUpdate(callOnUpdate));
//    }
//
//    /**
//     * Tween any particular value (Vector2), this could be used to tween an arbitrary value like a material color
//     *
//     * @param {GameObject}      gameObject:GameObject Gameobject that you wish to attach the tween to
//     * @param {Action<Vector2>} callOnUpdate:Action<Vector2> The function that is called on every Update frame, this function needs to accept a float value ex: function updateValue( Vector2 val ){ }
//     * @param {float}           from:Vector2 The original value to start the tween from
//     * @param {Vector2}         to:Vector2 The final Vector2 with which to tween to
//     * @param {float}           time:float The time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.value (Vector2)
//     */
//    public static LTDescr value(GameObject gameObject, Action<Vector2> callOnUpdate, Vector2 from, Vector2 to, float time) {
//        return pushNewTween(gameObject, to, time, TweenAction.VALUE3, options().setFrom(from).setOnUpdateVector2(callOnUpdate));
//    }
//
//    /**
//     * Tween any particular value (float)
//     *
//     * @param {GameObject}           gameObject:GameObject Gameobject that you wish to attach the tween to
//     * @param {Action<float,object>} callOnUpdate:Action<float,object> The function that is called on every Update frame, this function needs to accept a float value ex: function updateValue( Vector2 val, object obj ){ }
//     * @param {float}                from:Vector2 The original value to start the tween from
//     * @param {Vector2}              to:Vector2 The final Vector2 with which to tween to
//     * @param {float}                time:float The time to complete the tween in
//     * @return {LTDescr} LTDescr an object that distinguishes the tween
//     * @method physicsPort.LeanTween.value (float,object)
//     */
//
//
//    public static Map<String, Object> h(Object[] arr) {
//        if (arr.length % 2 == 1) {
//            logError("physicsPort.LeanTween - You have attempted to create a Hashtable with an odd number of values.");
//            return null;
//        }
//        Map hash = new HashMap();
//        for (i = 0; i < arr.length; i += 2) {
//            hash.put(arr[i], arr[i + 1]);
//        }
//
//        return hash;
//    }
//
//    private static int idFromUnique(int uniqueId) {
//        return uniqueId & 0xFFFFFF;
//    }
//
//
////    public static int value(String callOnUpdate, float from, float to, float time, Map<String,Object> optional) {
////        return value(tweenEmpty, callOnUpdate, from, to, time, optional);
////    }
////
////    public static int value(GameObject gameObject, String callOnUpdate, float from, float to, float time) {
////        return value(gameObject, callOnUpdate, from, to, time, new HashMap<>());
////    }
////
////    public static int value(GameObject gameObject, String callOnUpdate, float from, float to, float time, object[] optional) {
////        return value(gameObject, callOnUpdate, from, to, time, h(optional));
////    }
////
////    public static int value(GameObject gameObject, Action<float> callOnUpdate, float from, float to, float time, object[] optional) {
////        return value(gameObject, callOnUpdate, from, to, time, h(optional));
////    }
////
////    public static int value(GameObject gameObject, Action<float, Hashtable> callOnUpdate, float from, float to, float time, object[] optional) {
////        return value(gameObject, callOnUpdate, from, to, time, h(optional));
////    }
////
////    public static int value(GameObject gameObject, String callOnUpdate, float from, float to, float time, Map<String,Object> optional) {
////        if (optional == null || optional.size() == 0)
////            optional = new HashMap<>();
////
////        optional.put("onUpdate",callOnUpdate);
////        int id = idFromUnique(pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.CALLBACK, optional));
////        tweens[id].from = new Vector2(from, 0);
////        return id;
////    }
//
////    public static int value(GameObject gameObject, Action<Float> callOnUpdate, float from, float to, float time, Hashtable optional) {
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["onUpdate"] = callOnUpdate;
////        int id = idFromUnique(pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.CALLBACK, optional));
////        tweens[id].from = new Vector2(from, 0, 0);
////        return id;
////    }
////
////    public static int value(GameObject gameObject, Action<Float, Hashtable> callOnUpdate, float from, float to, float time, Hashtable optional) {
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["onUpdate"] = callOnUpdate;
////        int id = idFromUnique(pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.CALLBACK, optional));
////        tweens[id].from = new Vector2(from, 0, 0);
////        return id;
////    }
////
////    public static int value(GameObject gameObject, String callOnUpdate, Vector2 from, Vector2 to, float time, Hashtable optional) {
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["onUpdate"] = callOnUpdate;
////        int id = idFromUnique(pushNewTween(gameObject, to, time, TweenAction.VALUE3, optional));
////        tweens[id].from = from;
////        return id;
////    }
////
////    public static int value(GameObject gameObject, String callOnUpdate, Vector2 from, Vector2 to, float time, object[] optional) {
////        return value(gameObject, callOnUpdate, from, to, time, h(optional));
////    }
////
////    public static int value(GameObject gameObject, Action<Vector2> callOnUpdate, Vector2 from, Vector2 to, float time, Hashtable optional) {
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["onUpdate"] = callOnUpdate;
////        int id = idFromUnique(pushNewTween(gameObject, to, time, TweenAction.VALUE3, optional));
////        tweens[id].from = from;
////        return id;
////    }
////
////    public static int value(GameObject gameObject, Action<Vector2, Hashtable> callOnUpdate, Vector2 from, Vector2 to, float time, Hashtable optional) {
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["onUpdate"] = callOnUpdate;
////        int id = idFromUnique(pushNewTween(gameObject, to, time, TweenAction.VALUE3, optional));
////        tweens[id].from = from;
////        return id;
////    }
////
////    public static int value(GameObject gameObject, Action<Vector2> callOnUpdate, Vector2 from, Vector2 to, float time, object[] optional) {
////        return value(gameObject, callOnUpdate, from, to, time, h(optional));
////    }
////
////    public static int value(GameObject gameObject, System.Action<Vector2, Hashtable> callOnUpdate, Vector2 from, Vector2 to, float time, object[] optional) {
////        return value(gameObject, callOnUpdate, from, to, time, h(optional));
////    }
////
////    public static int rotate(GameObject gameObject, Vector2 to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, to, time, TweenAction.ROTATE, optional);
////    }
////
////    public static int rotate(GameObject gameObject, Vector2 to, float time, object[] optional) {
////        return rotate(gameObject, to, time, h(optional));
////    }
////
////
////    public static int rotate(LTRect ltRect, float to, float time, Hashtable optional) {
////        init();
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["rect"] = ltRect;
////        return pushNewTween(tweenEmpty, new Vector2(to, 0f), time, TweenAction.GUI_ROTATE, optional);
////    }
////
////    public static int rotate(LTRect ltRect, float to, float time, object[] optional) {
////        return rotate(ltRect, to, time, h(optional));
////    }
////
////    public static int rotateX(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.ROTATE_X, optional);
////    }
////
////    public static int rotateX(GameObject gameObject, float to, float time, object[] optional) {
////        return rotateX(gameObject, to, time, h(optional));
////    }
////
////    public static int rotateY(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.ROTATE_Y, optional);
////    }
////
////    public static int rotateY(GameObject gameObject, float to, float time, object[] optional) {
////        return rotateY(gameObject, to, time, h(optional));
////    }
////
////    public static int rotateZ(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.ROTATE_Z, optional);
////    }
////
////    public static int rotateZ(GameObject gameObject, float to, float time, object[] optional) {
////        return rotateZ(gameObject, to, time, h(optional));
////    }
////
////    public static int rotateLocal(GameObject gameObject, Vector2 to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, to, time, TweenAction.ROTATE_LOCAL, optional);
////    }
////
////    public static int rotateLocal(GameObject gameObject, Vector2 to, float time, object[] optional) {
////        return rotateLocal(gameObject, to, time, h(optional));
////    }
////
////
////    public static int rotateAround(GameObject gameObject, Vector2 axis, float add, float time, Hashtable optional) {
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["axis"] = axis;
////        if (optional["point"] == null)
////            optional["point"] = Vector2.zero;
////
////        return pushNewTween(gameObject, new Vector2(add, 0f, 0f), time, TweenAction.ROTATE_AROUND, optional);
////    }
////
////    public static int rotateAround(GameObject gameObject, Vector2 axis, float add, float time, object[] optional) {
////        return rotateAround(gameObject, axis, add, time, h(optional));
////    }
////
////    public static int moveX(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.MOVE_X, optional);
////    }
////
////    public static int moveX(GameObject gameObject, float to, float time, object[] optional) {
////        return moveX(gameObject, to, time, h(optional));
////    }
////
////    public static int moveY(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.MOVE_Y, optional);
////    }
////
////    public static int moveY(GameObject gameObject, float to, float time, object[] optional) {
////        return moveY(gameObject, to, time, h(optional));
////    }
////
////    public static int moveZ(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.MOVE_Z, optional);
////    }
////
////    public static int moveZ(GameObject gameObject, float to, float time, object[] optional) {
////        return moveZ(gameObject, to, time, h(optional));
////    }
////
////    public static int move(GameObject gameObject, Vector2 to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, to, time, TweenAction.MOVE, optional);
////    }
////
////    public static int move(GameObject gameObject, Vector2 to, float time, object[] optional) {
////        return move(gameObject, to, time, LeanTween.h(optional));
////    }
////
////    public static int move(GameObject gameObject, Vector2[] to, float time, Hashtable optional) {
////        if (to.length < 4) {
////            string errorMsg = "physicsPort.LeanTween - When passing values for a vector path, you must pass four or more values!";
////            if (throwErrors) Debug.LogError(errorMsg);
////            else Debug.Log(errorMsg);
////            return -1;
////        }
////        if (to.length % 4 != 0) {
////            string errorMsg2 = "physicsPort.LeanTween - When passing values for a vector path, they must be in sets of four: controlPoint1, controlPoint2, endPoint2, controlPoint2, controlPoint2...";
////            if (throwErrors) Debug.LogError(errorMsg2);
////            else Debug.Log(errorMsg2);
////            return -1;
////        }
////
////        init();
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        LTBezierPath ltPath = new LTBezierPath(to);
////        if (optional["orientToPath"] != null)
////            ltPath.orientToPath = true;
////        optional["path"] = ltPath;
////
////        return pushNewTween(gameObject, new Vector2(1.0f, 0.0f, 0.0f), time, TweenAction.MOVE_CURVED, optional);
////    }
////
////    public static int move(GameObject gameObject, Vector2[] to, float time, object[] optional) {
////        return move(gameObject, to, time, LeanTween.h(optional));
////    }
////
////    public static int move(LTRect ltRect, Vector2 to, float time, Hashtable optional) {
////        init();
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["rect"] = ltRect;
////        return pushNewTween(tweenEmpty, to, time, TweenAction.GUI_MOVE, optional);
////    }
////
////    public static int move(LTRect ltRect, Vector2 to, float time, object[] optional) {
////        return move(ltRect, to, time, LeanTween.h(optional));
////    }
////
////    public static int moveLocal(GameObject gameObject, Vector2 to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, to, time, TweenAction.MOVE_LOCAL, optional);
////    }
////
////    public static int moveLocal(GameObject gameObject, Vector2 to, float time, object[] optional) {
////        return moveLocal(gameObject, to, time, LeanTween.h(optional));
////    }
////
////    public static int moveLocal(GameObject gameObject, Vector2[] to, float time, Hashtable optional) {
////        if (to.length < 4) {
////            string errorMsg = "physicsPort.LeanTween - When passing values for a vector path, you must pass four or more values!";
////            if (throwErrors) Debug.LogError(errorMsg);
////            else Debug.Log(errorMsg);
////            return -1;
////        }
////        if (to.length % 4 != 0) {
////            string errorMsg2 = "physicsPort.LeanTween - When passing values for a vector path, they must be in sets of four: controlPoint1, controlPoint2, endPoint2, controlPoint2, controlPoint2...";
////            if (throwErrors) Debug.LogError(errorMsg2);
////            else Debug.Log(errorMsg2);
////            return -1;
////        }
////
////        init();
////        if (optional == null)
////            optional = new Hashtable();
////
////        LTBezierPath ltPath = new LTBezierPath(to);
////        if (optional["orientToPath"] != null)
////            ltPath.orientToPath = true;
////        optional["path"] = ltPath;
////
////        return pushNewTween(gameObject, new Vector2(1.0f, 0.0f, 0.0f), time, TweenAction.MOVE_CURVED_LOCAL, optional);
////    }
////
////    public static int moveLocal(GameObject gameObject, Vector2[] to, float time, object[] optional) {
////        return moveLocal(gameObject, to, time, LeanTween.h(optional));
////    }
////
////    public static int moveLocalX(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0), time, TweenAction.MOVE_LOCAL_X, optional);
////    }
////
////    public static int moveLocalX(GameObject gameObject, float to, float time, object[] optional) {
////        return moveLocalX(gameObject, to, time, h(optional));
////    }
////
////    public static int moveLocalY(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.MOVE_LOCAL_Y, optional);
////    }
////
////    public static int moveLocalY(GameObject gameObject, float to, float time, object[] optional) {
////        return moveLocalY(gameObject, to, time, h(optional));
////    }
////
////    public static int moveLocalZ(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.MOVE_LOCAL_Z, optional);
////    }
////
////    public static int moveLocalZ(GameObject gameObject, float to, float time, object[] optional) {
////        return moveLocalZ(gameObject, to, time, h(optional));
////    }
////
////    public static int scale(GameObject gameObject, Vector2 to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, to, time, TweenAction.SCALE, optional);
////    }
////
////    public static int scale(GameObject gameObject, Vector2 to, float time, object[] optional) {
////        return scale(gameObject, to, time, h(optional));
////    }
////
////    public static int scale(LTRect ltRect, Vector2 to, float time, Hashtable optional) {
////        init();
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        optional["rect"] = ltRect;
////        return pushNewTween(tweenEmpty, to, time, TweenAction.GUI_SCALE, optional);
////    }
////
////    public static int scale(LTRect ltRect, Vector2 to, float time, object[] optional) {
////        return scale(ltRect, to, time, h(optional));
////    }
////
////    public static int alpha(LTRect ltRect, float to, float time, Hashtable optional) {
////        init();
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////
////        ltRect.alphaEnabled = true;
////        optional["rect"] = ltRect;
////        return pushNewTween(tweenEmpty, new Vector2(to, 0f, 0f), time, TweenAction.GUI_ALPHA, optional);
////    }
////
////    public static int alpha(LTRect ltRect, float to, float time, object[] optional) {
////        return alpha(ltRect, to, time, h(optional));
////    }
////
////    public static int scaleX(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.SCALE_X, optional);
////    }
////
////    public static int scaleX(GameObject gameObject, float to, float time, object[] optional) {
////        return scaleX(gameObject, to, time, h(optional));
////    }
////
////    public static int scaleY(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.SCALE_Y, optional);
////    }
////
////    public static int scaleY(GameObject gameObject, float to, float time, object[] optional) {
////        return scaleY(gameObject, to, time, h(optional));
////    }
////
////    public static int scaleZ(GameObject gameObject, float to, float time, Hashtable optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.SCALE_Z, optional);
////    }
////
////    public static int scaleZ(GameObject gameObject, float to, float time, object[] optional) {
////        return scaleZ(gameObject, to, time, h(optional));
////    }
////
////    public static int delayedCall(float delayTime, string callback, Hashtable optional) {
////        init();
////        return delayedCall(tweenEmpty, delayTime, callback, optional);
////    }
////
////    public static int delayedCall(float delayTime, Action callback, object[] optional) {
////        init();
////        return delayedCall(tweenEmpty, delayTime, callback, h(optional));
////    }
////
////    public static int delayedCall(GameObject gameObject, float delayTime, String callback, object[] optional) {
////        return delayedCall(gameObject, delayTime, callback, h(optional));
////    }
////
////    public static int delayedCall(GameObject gameObject, float delayTime, Action callback, object[] optional) {
////        return delayedCall(gameObject, delayTime, callback, h(optional));
////    }
////
////    public static int delayedCall(GameObject gameObject, float delayTime, String callback, Hashtable optional) {
////        if (optional == null || optional.Count == 0)
////            optional = new Hashtable();
////        optional["onComplete"] = callback;
////
////        return pushNewTween(gameObject, Vector2.zero, delayTime, TweenAction.CALLBACK, optional);
////    }
////
////    public static int delayedCall(GameObject gameObject, float delayTime, Action callback, Map optional) {
////        if (optional == null)
////            optional = new Hashtable();
////        optional["onComplete"] = callback;
////
////        return pushNewTween(gameObject, Vector2.zero, delayTime, TweenAction.CALLBACK, optional);
////    }
////
////    public static int delayedCall(GameObject gameObject, float delayTime, Action<object> callback, Map optional) {
////        if (optional == null)
////            optional = new Hashtable();
////        optional["onComplete"] = callback;
////
////        return pushNewTween(gameObject, Vector2.zero, delayTime, TweenAction.CALLBACK, optional);
////    }
////
////    public static int alpha(GameObject gameObject, float to, float time, Map optional) {
////        return pushNewTween(gameObject, new Vector2(to, 0, 0), time, TweenAction.ALPHA, optional);
////    }
////
////    public static int alpha(GameObject gameObject, float to, float time, object[] optional) {
////        return alpha(gameObject, to, time, h(optional));
////    }
//
//
//
//
//
//
//    public static void addListener(GameObject caller, int eventId, Action<LTEvent> callback) {
////        if (eventListeners == null) {
////            eventListeners = new Action<LTEvent>[EVENTS_MAX * LISTENERS_MAX];
////            goListeners = new GameObject[EVENTS_MAX * LISTENERS_MAX];
////        }
////        // Debug.Log("searching for an empty space for:"+caller + " eventid:"+event);
////        for (i = 0; i < LISTENERS_MAX; i++) {
////            int point = eventId * LISTENERS_MAX + i;
////            if (goListeners[point] == null || eventListeners[point] == null) {
////                eventListeners[point] = callback;
////                goListeners[point] = caller;
////                if (i >= eventsMaxSearch)
////                    eventsMaxSearch = i + 1;
////                // Debug.Log("adding event for:"+caller.name);
////
////                return;
////            }
////
////        }
//    }
//
//
//    public static boolean removeListener(GameObject caller, int eventId, Action<LTEvent> callback) {
//        for (i = 0; i < eventsMaxSearch; i++) {
//            int point = eventId * LISTENERS_MAX + i;
//
//        }
//        return false;
//    }
//
//    public static void dispatchEvent(int eventId) {
//        dispatchEvent(eventId, null);
//    }
//
//    public static void dispatchEvent(int eventId, Object data) {
////        for (int k = 0; k < eventsMaxSearch; k++) {
////            int point = eventId * LISTENERS_MAX + k;
////            if (eventListeners[point] != null) {
////                if (goListeners[point]) {
////                    eventListeners[point] (new LTEvent(eventId, data));
////                } else {
////                    eventListeners[point] = null;
////                }
////            }
////        }
//    }




