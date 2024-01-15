package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.components.Transition;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import danogl.gui.ImageReader;
import java.util.function.Function;

public class Monster extends GameObject {
    private static final int NUM_PNG = 13;
    private static Vector2 topLeftCorner;
    private final Function<Float, Float> func;
    private static final String PATH_ENNEMIES="assets/enemies/enemie";
    private static final Vector2 DIM_ENEMIES = new Vector2(40,80);

    private static final float INIT_VAL=-100;
    private static final float FINAL_VAL=100;

    private static final int ACCELERATION_Y=500;
    private static final int VEL_X=-100;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Monster(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable, Function<Float,Float> func) {
        super(topLeftCorner, dimensions, renderable);
        this.func = func;

        transform().setAccelerationY(ACCELERATION_Y);
        transform().setVelocityX(VEL_X);
        setTag(PepseGameManager.MONSTER);
        new Transition<Float>(
                this,
                this::enemiesMove,
                INIT_VAL,
                FINAL_VAL,
                Transition.CUBIC_INTERPOLATOR_FLOAT,
                4,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH,
                null);


    }

    /**
     * This function creates the monsters of the game
     */
    public static void create(GameObjectCollection gameObjects,
                              int layer, ImageReader imageReader,
                              float positionX, Function<Float,Float> func) {
        float positionY= func.apply(positionX)-200;
        Monster.topLeftCorner= new Vector2(positionX, positionY);
        Renderable[] monsterImage = new Renderable[NUM_PNG];

        for (int i = 0; i < NUM_PNG; i++) {

            monsterImage[i] = imageReader.readImage(PATH_ENNEMIES+Integer.toString(i + 3)+PepseGameManager.PNG, true);
        }
        AnimationRenderable monster = new AnimationRenderable(monsterImage, 0.1);
        Monster enemie = new Monster(topLeftCorner, DIM_ENEMIES, monster,func);
        gameObjects.addGameObject(enemie,layer);
        enemie.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        enemie.physics().setMass(0);
        gameObjects.layers().shouldLayersCollide(PepseGameManager.LAYER_AVATAR,PepseGameManager.LAYER_MONSTER,true);
        gameObjects.layers().shouldLayersCollide(PepseGameManager.LAYER_MONSTER,PepseGameManager.LAYER_TERRAIN,true);
    }
    /**
     * This function moves the monsters
     */
    public void enemiesMove(float x){
        this.transform().setVelocity(x, ACCELERATION_Y);
        renderer().setIsFlippedHorizontally(!(x < 0));
    }

    /**
     * Collision with another object of the game
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if(!other.getTag().equals(PepseGameManager.FLAG)) {
            super.onCollisionEnter(other, collision);
            if (other.getTag().equals(PepseGameManager.TERRAIN)) {
                transform().setVelocityY(0);
            }
        }
    }
}