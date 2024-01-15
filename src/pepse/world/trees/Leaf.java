package pepse.world.trees;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.components.Component;
import danogl.components.ScheduledTask;
import danogl.components.Transition;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Block;
import pepse.world.Sky;

import java.util.Random;
import java.util.function.Consumer;

public class Leaf extends GameObject {
    private static final Random random = new Random();
    private float timeToReset;
    private static final float FADEOUT_TIME = 5f;
    private int startFadeOut;
    private Transition<Float> horizontalTransition;
    private Component movementTransition;


    /**
     * Random
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Leaf(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner,Vector2.ONES.mult(Block.SIZE), renderable);
        setTag(PepseGameManager.LEAF);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        startFadeOut = 10+random.nextInt(50);
        timeToReset=  10 + random.nextInt(10);
        live.accept(topLeftCorner);
    }


    private final Consumer<Vector2> live = (position) -> {
        new ScheduledTask(this, random.nextInt(3), false, this::moveLeave);
        float lifeTime = startFadeOut;
        new ScheduledTask(this, lifeTime, false,()->fadeLeaf(position));};


    private final Consumer<Vector2>  dead = (position) -> {
        new ScheduledTask(this, timeToReset, false, () ->reset(position));};

    /**
     * Set the angle of the leaf and also its dimension
     */
    private void moveLeave() {

        Transition<Float> setAngleLeaf = new Transition<Float>(
                this, this.renderer()::setRenderableAngle, -5f, 5f,
                Transition.LINEAR_INTERPOLATOR_FLOAT, 1f,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);

        Transition<Float> setDimensionsLeaf = new Transition<Float>(this,
                (x) -> this.setDimensions(new Vector2((1 + x / 100) * Block.SIZE, Block.SIZE)),
                -5f, 5f, Transition.LINEAR_INTERPOLATOR_FLOAT,
                1f, Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
    }

    /**
     * Set horizontal transition
     */
    private void setHorizontalTransition(){
        horizontalTransition = new Transition<Float>(
                this, x -> this.transform().setVelocity(x, 100f),
                -20f, 20f, Transition.CUBIC_INTERPOLATOR_FLOAT, 2,
                Transition.TransitionType.TRANSITION_LOOP, null);
    }

    /**
     * Fade the leaf
     */
    private void fadeLeaf(Vector2 position){
        this.renderer().fadeOut(FADEOUT_TIME, ()-> this.dead.accept(position));
        this.setHorizontalTransition();
    }

    /**
     * Reset the opaqueness and the position of the leaf at the beginning
     */
    private void reset(Vector2 position){
        this.setCenter(position.add(new Vector2(Block.SIZE/2f, Block.SIZE/2f)));
        this.renderer().fadeIn(0);
        this.live.accept(position);
    }

    /**
     * Called on the first frame of a collision.
     *
     * @param other     The GameObject with which a collision occurred.
     * @param collision Information regarding this collision.
     *                  A reasonable elastic behavior can be achieved with:
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        removeComponent(horizontalTransition);
        removeComponent(movementTransition);
        this.transform().setVelocity(Vector2.ZERO);
    }
}