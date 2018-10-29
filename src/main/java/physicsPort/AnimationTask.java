package physicsPort;

public class AnimationTask {
    private final Interpolator interpolator;
    private final float duration;
    private long targetTime;


    public AnimationTask(Interpolator interpolator, float duration) {
        this.interpolator = interpolator;
        this.duration = duration;
        targetTime = System.currentTimeMillis() + (long)duration;
    }

    public float animate(){
        final long time = System.currentTimeMillis();
        if(targetTime > time){

            return interpolator.update(0,1, 1 - (targetTime-time)/duration);
        }else{
            return 1;
        }

    }
    public boolean isFinished(){
        return targetTime < System.currentTimeMillis();
    }

}
