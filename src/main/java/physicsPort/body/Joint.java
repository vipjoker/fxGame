package physicsPort.body;

public class Joint {


    public static int counter = 0;
    public static final int JOINT_DISTANCE	= 0;
    public static final int JOINT_WELD 		= 1;
    public static final int JOINT_REVOLUTE	= 2;
    public static final int JOINT_WHEEL 		= 3;
    public static final int JOINT_PULLEY		= 4;
    public static final int JOINT_GEAR		= 5;
    public static final int JOINT_PRISMATIC	= 6;
    public static final int JOINT_ROPE		= 7;
    public int upperTranslation;
    public  int lowerTranslation;

    public int jointType;
    public boolean collideConnected;
    public boolean enableLimit;
    public boolean enableMotor;
    public float lowerAngle;
    public float upperAngle;
    public float maxMotorTorque;
    public float motorSpeed;
    public String userData;
    public float [] localAnchorA;
    public float [] localAnchorB;
    public float [] localAxisA;
    public int bodyIndexA;
    public int bodyIndexB;
    public float length;
    public float frequencyHZ;
    public float dampingRatio;
    public float referenceAngle;
    public Body bodyA;
    public Body bodyB;
    public float [] groundAnchorA;
    public float [] groundAnchorB;
    public float lengthA;
    public float lengthB;
    public float maxLengthA;
    public float maxLengthB;
    public Joint joint1;
    public Joint joint2;
    public int jointIndex1;
    public int jointIndex2;


    // editor parameters
        public float[]  position;
        public float [] scaleXY ;
        public boolean isSelected = false;
        public boolean inEditMode = false;
        public String  name;

    public Joint(int type){



        this.position = new float[2];
        this.scaleXY = new float[]{1,1};

        this.isSelected =false;
        this.inEditMode = false;
        this.name = "joint" + Joint.counter++;
        this.bodyIndexA = -1;
        this.bodyIndexB = -1;
        this.localAnchorA = new float[2];
        this.localAnchorB = new float[2];
        this.userData = "";
        this.collideConnected = false;

        this.jointType = type;
        if (type == Joint.JOINT_DISTANCE){
            this.length 		= 100;
            this.frequencyHZ 	= 60;
            this.dampingRatio 	= 1;
        }
        else if (type == Joint.JOINT_WELD){
            this.referenceAngle = 0;
        }
        else if (type == Joint.JOINT_REVOLUTE){
            this.enableLimit 	= false;
            this.enableMotor 	= false;
            this.lowerAngle 	= 0;
            this.maxMotorTorque = 100;
            this.motorSpeed 	= 100;
            this.referenceAngle = 0;
            this.upperAngle		= 0;
        }
        else if (type == Joint.JOINT_WHEEL){
            this.localAxisA 	= new float[] {0, 1};
            this.enableMotor 	= false;
            this.maxMotorTorque = 100;
            this.motorSpeed 	= 100;
            this.frequencyHZ 	= 60;
            this.dampingRatio 	= 1;
        }
        else if (type == Joint.JOINT_PULLEY){
            this.groundAnchorA 	= new float[2];
            this.groundAnchorB 	= new float[2];
            this.lengthA	   	= 100;
            this.lengthB		= 100;
            this.maxLengthA     = 100;
            this.maxLengthB     = 100;
            this.frequencyHZ    = 1;			// frequecyHZ is equivalent to ratio in this case (makes it easy to use the current ui layout)
        }
        else if (type == Joint.JOINT_GEAR){
            // localAnchors not needed for this joint
            this.frequencyHZ    = 1;			// frequecyHZ is equivalent to ratio in this case (makes it easy to use the current ui layout)
            this.jointIndex1    = -1;
            this.jointIndex2	= -1;
        }
        else if (type == Joint.JOINT_PRISMATIC){
            this.enableLimit 	= false;
            this.enableMotor 	= false;
            this.lowerTranslation 	= 0;
            this.upperTranslation   = 100;
            this.localAxisA 	= new float[]{1, 0};
            this.maxMotorTorque = 100;
            this.motorSpeed 	= 100;
            this.referenceAngle = 0;
        }
        else if (type == Joint.JOINT_ROPE){
            this.frequencyHZ    = 100;			// frequecyHZ is equivalent to maxLength in this case (makes it easy to use the current ui layout)
        }

        // editor parameters
        this.position = [0, 0];
        this.scaleXY = [1, 1];
        this.isSelected = false;
        this.inEditMode = false;
        this.name = "joint" + Joint.counter++;
    }


//
//    public Joint clone (cloneBodyA, cloneBodyB){
//        var copy = new Joint(this.jointType);
//        for (var attr in this) {
//            if (attr == 'bodyA' && !cloneBodyA){		// donot clone body
//                copy[attr] = this[attr];
//                continue;
//            }
//            if (attr == 'bodyB' && !cloneBodyB){		// donot clone body
//                copy[attr] = this[attr];
//                continue;
//            }
//            if (this.hasOwnProperty(attr) && attr != "name") copy[attr] = clone(this[attr]);
//        }
//        return copy;
//    }

    public void setUserData (String data){
        this.userData = data;
    }

    public void setLocalAnchorA (float x, float y){
        this.localAnchorA = new float[]{x, y};
    }

    public void setLocalAnchorB (float x, float y){
        this.localAnchorB = new float[]{x, y};
    }

    public void moveAnchorA (float x, float y){
        this.localAnchorA[0] += x;
        this.localAnchorA[1] += y;
    }

    public void moveAnchorB (float x,float y){
        this.localAnchorB[0] += x;
        this.localAnchorB[1] += y;
    };

    public void move (float x,float y){
        this.position[0] += x;
        this.position[1] += y;

        this.moveAnchorA(x, y);
        this.moveAnchorB(x, y);

        if (this.jointType == Joint.JOINT_PULLEY){
            this.moveGroundAnchorA(x, y);
            this.moveGroundAnchorB(x, y);
        }
    }

    public void setPosition (float x, float y){
        this.move(x - this.position[0], y - this.position[1]);
    };

    public void scale (float sx, float sy, float pivotX, float pivotY){


        this.scaleXY[0] *= sx;
        this.scaleXY[1] *= sy;

        this.move(-pivotX, -pivotY);

        this.position[0] *= sx;
        this.position[1] *= sy;

        this.localAnchorA[0] *= sx;
        this.localAnchorA[1] *= sy;
        this.localAnchorB[0] *= sx;
        this.localAnchorB[1] *= sy;

        this.move(pivotX, pivotY);
    };



    public void scale(float sx,float sy){

            float pivotX = this.position[0];
            float pivotY = this.position[1];
            scale(sx,sy,pivotX,pivotY);

    }


    public void setScale (float sx,float  sy, float pivotX, float pivotY){
        this.scale(sx / this.scaleXY[0], sy / this.scaleXY[1], pivotX, pivotY);
    }

    public float []getBounds() {
        return new float[]{this.position[0], this.position[1], 32, 32};
    }

    public float[] getAnchorABounds (){
        return new float[]{this.localAnchorA[0], this.localAnchorA[1], 32, 32};
    }

    public float []getAnchorBBounds (){
        return new float[]{this.localAnchorB[0], this.localAnchorB[1], 32, 32};
    }

// distance joint
    public void setLength (float length){
        this.length = length;
    }

    public void setDampingRatio (float dampingRatio){
        this.dampingRatio = dampingRatio;
    }
    public void setFrequency (float frequency){
        this.frequencyHZ = frequency;
    }

// weld joint
    public void setReferenceAngle (float angle){
        this.referenceAngle = angle * (float) Math.PI / 180;
    };

    public void changeReferenceAngle (float delta){
        this.referenceAngle += delta;
    };

// revolute joint
    public void changeLowerAngle (float delta){
        this.lowerAngle += delta;
        // if joint is wheel or prismatic, then edit localAxis
        if (this.jointType == Joint.JOINT_PRISMATIC || this.jointType == Joint.JOINT_WHEEL){
            this.rotateLocalAxis(delta);
        }
    }

    public void changeUpperAngle (float delta){
        this.upperAngle += delta;
    }

    public void setLowerAngle (float angle){
        this.lowerAngle = angle;
    }
    public void setUpperAngle (float angle){
        this.upperAngle = angle;
    }

    public void setMotorSpeed (float speed){
        this.motorSpeed = speed;
    };
    public void setMaxMotorTorque (float torque){
        this.maxMotorTorque = torque;
    };

// wheel and prismatic joint
    Joint.prototype.rotateLocalAxis = function(delta){
        var newAngle = -Math.atan2(this.localAxisA[1], this.localAxisA[0]) + delta * Math.PI / 180;
        this.localAxisA = [Math.cos(newAngle), -Math.sin(newAngle)];
    };

// pulley joint
    Joint.prototype.setGroundAnchorA = function(x, y){
        this.groundAnchorA = [x, y];
    };
    Joint.prototype.setGroundAnchorB = function(x, y){
        this.groundAnchorB = [x, y];
    };

    Joint.prototype.moveGroundAnchorA = function(x, y){
        this.groundAnchorA[0] += x;
        this.groundAnchorA[1] += y;
    };

    Joint.prototype.moveGroundAnchorB = function(x, y){
        this.groundAnchorB[0] += x;
        this.groundAnchorB[1] += y;
    };

    Joint.prototype.setLengthA = function(length){
        this.lengthA = length;
    };

    Joint.prototype.setLengthB = function(length){
        this.lengthB = length;
    };

    Joint.prototype.setMaxLengthA = function(length){
        this.maxLengthA = length;
    };

    Joint.prototype.setMaxLengthB = function(length){
        this.maxLengthB = length;
    };

    Joint.prototype.getGroundAnchorABounds = function(){
        return [this.groundAnchorA[0], this.groundAnchorA[1], 32, 32];
    };

    Joint.prototype.getGroundAnchorBBounds = function(){
        return [this.groundAnchorB[0], this.groundAnchorB[1], 32, 32];
    };

    Joint.prototype.toPhysics = function(bodies, joints){
        var joint = new PhysicsJoint(this);
        joint.bodyA = bodies.indexOf(this.bodyA);
        joint.bodyB = bodies.indexOf(this.bodyB);
        if (joint.jointType == Joint.JOINT_PULLEY){
            joint.maxLengthA = Math.pow((this.groundAnchorA[0] - this.localAnchorA[0]) * (this.groundAnchorA[0] - this.localAnchorA[0]) +
                    (this.groundAnchorA[1] - this.localAnchorA[1]) * (this.groundAnchorA[1] - this.localAnchorA[1]), 0.5);
            joint.maxLengthB = Math.pow((this.groundAnchorB[0] - this.localAnchorB[0]) * (this.groundAnchorB[0] - this.localAnchorB[0]) +
                    (this.groundAnchorB[1] - this.localAnchorB[1]) * (this.groundAnchorB[1] - this.localAnchorB[1]), 0.5);
            joint.lengthA = joint.maxLengthA;
            joint.lengthB = joint.maxLengthB;
        }
        if (joint.jointType != Joint.JOINT_GEAR){
            var rotation, dx, dy, length, angle;
            if (this.bodyA.rotation != 0){
                rotation = -this.bodyA.rotation;
                dx = this.localAnchorA[0] - this.bodyA.position[0];
                dy = this.localAnchorA[1] - this.bodyA.position[1];
                length = Math.pow(dx * dx + dy * dy, 0.5);
                angle = Math.atan2(dy, dx);

                joint.localAnchorA = [	length * Math.cos(angle + rotation * Math.PI / 180),
                        length * Math.sin(angle + rotation * Math.PI / 180)  ];
            }
            else {
                joint.localAnchorA = [this.localAnchorA[0] - this.bodyA.position[0], this.localAnchorA[1] - this.bodyA.position[1]];
            }

            if (this.bodyB.rotation != 0){
                rotation = -this.bodyB.rotation;
                dx = this.localAnchorB[0] - this.bodyB.position[0];
                dy = this.localAnchorB[1] - this.bodyB.position[1]
                length = Math.pow(dx * dx + dy * dy, 0.5);
                angle = Math.atan2(dy, dx);

                joint.localAnchorB = [	length * Math.cos(angle + rotation * Math.PI / 180),
                        length * Math.sin(angle + rotation * Math.PI / 180)  ];
            }
            else {
                joint.localAnchorB = [this.localAnchorB[0] - this.bodyB.position[0], this.localAnchorB[1] - this.bodyB.position[1]];
            }
        }
        if (joint.jointType == Joint.JOINT_GEAR) {
            joint.joint1 = joints.indexOf(this.joint1);
            joint.joint2 = joints.indexOf(this.joint2);
        }
        return joint;
    };
}
