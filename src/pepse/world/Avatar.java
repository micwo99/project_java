package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.gui.UserInputListener;
import danogl.gui.rendering.AnimationRenderable;
import danogl.gui.rendering.Renderable;
import danogl.util.Counter;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.NumericLifeCounter;

import danogl.gui.ImageReader;
import java.awt.event.KeyEvent;

import static pepse.world.Block.*;

public class Avatar extends GameObject {

    public final Counter life;
    private static final float VELOCITY_X = 250;
    private static final float VELOCITY_Y = -400;
    private static final String PATH_AVATAR ="assets/hero/";

    private static final int LIVES = 3;
    private static int layer;
    private float energy;
    private boolean flag = false;
    private static GameObjectCollection gameObjects;
    private static AnimationRenderable moveAvatar;
    private static AnimationRenderable immobileAvatar;
    private static AnimationRenderable flyAvatar;
    private static AnimationRenderable jumpAvatar;
    private static Vector2 topLeftCorner;
    private static UserInputListener inputListener;
    private static final Vector2 DIM_AVATAR = new Vector2(40, 80);
    private static final Vector2 DIM_LIVES = new Vector2(30, 30);
    private static final Vector2 POSITION_LIVES = new Vector2(30, 30);
    private static final String RIGHT ="walk";
    private static final String FLY ="ballon";
    private static final String AVATAR ="pik";
    private static final String JUMP ="jump";
    private static final float TIME_BETWEEN_CLIPS = (float) 0.1;
    private static final float TIME_JUMP =  0.08f;
    private static final float TIME_BETWEEN_CLIPS_IMMOBILE = (float) 0.2;
    private static final float GAIN_OR_LOSE_ENERGY = (float) 0.5;
    private static final int LAYER_LIVES = Layer.BACKGROUND + 2;
    private static final int ENERGY = 100;


    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public Avatar(Vector2 topLeftCorner, Vector2 dimensions, Renderable renderable) {
        super(topLeftCorner, dimensions, renderable);
        this.energy = 100;
        this.life = new Counter(LIVES);
        setTag(PepseGameManager.AVATAR);
        transform().setAccelerationY(500);
        GameObject numeric = new NumericLifeCounter(life, POSITION_LIVES, DIM_LIVES, gameObjects);
        gameObjects.addGameObject(numeric, LAYER_LIVES);
    }


    /**
     * @return This funtion creates the avatar of the game and his lives
     */
    public static Avatar create(GameObjectCollection gameObjects, int layer, Vector2 topLeftCorner,
                                UserInputListener inputListener, ImageReader imageReader) {
        Avatar.gameObjects = gameObjects;
        Avatar.layer = layer;
        Avatar.topLeftCorner = topLeftCorner;
        Avatar.inputListener = inputListener;
        Renderable[] immobile = new Renderable[7];


        for (int i = 0; i < 7; i++) {
            String avatar = PATH_AVATAR + AVATAR+Integer.toString(i + 1)+PepseGameManager.PNG;
            immobile[i] = imageReader.readImage(avatar, true);
        }

        Renderable[] move = new Renderable[5];
        for (int i = 0; i < 5; i++) {
            String avatar = PATH_AVATAR + RIGHT+Integer.toString(i + 1)+PepseGameManager.PNG;
            move[i] = imageReader.readImage(avatar, true);

        }

        Renderable[] fly = new Renderable[6];
        for (int i = 0; i < 6; i++) {
            String avatar = PATH_AVATAR + FLY +Integer.toString(i + 1)+PepseGameManager.PNG;
            fly[i] = imageReader.readImage(avatar, true);

        }

        Renderable[] jump = new Renderable[21];
        for (int i = 0; i < 21; i++) {
            String avatar = PATH_AVATAR + JUMP +Integer.toString(i + 1)+PepseGameManager.PNG;
            jump[i] = imageReader.readImage(avatar, true);
        }

        boolean isTree = false;

        for (GameObject trunk : gameObjects.objectsInLayer(Layer.DEFAULT)) {
            if (trunk.getTag().equals(PepseGameManager.TRUNK) && trunk.getTopLeftCorner() == Avatar.topLeftCorner) {
                isTree = true;
                break;
            }
        }
        if (isTree) {
            Avatar.topLeftCorner = new Vector2(topLeftCorner.x() + SIZE * 2, topLeftCorner.y());
        }

        immobileAvatar = new AnimationRenderable(immobile, TIME_BETWEEN_CLIPS_IMMOBILE);
        moveAvatar = new AnimationRenderable(move, TIME_BETWEEN_CLIPS);
        flyAvatar = new AnimationRenderable(fly, TIME_BETWEEN_CLIPS);
        jumpAvatar = new AnimationRenderable(jump, TIME_JUMP);
        Avatar avatar = new Avatar(topLeftCorner, DIM_AVATAR, immobileAvatar);
        avatar.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        gameObjects.addGameObject(avatar, layer);
        return avatar;
    }

    /**
     * Collision with other object
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        if (!other.getTag().equals(PepseGameManager.MONSTER)) {
            super.onCollisionEnter(other, collision);
            if (other.getTag().equals(PepseGameManager.TRUNK)){
                transform().setVelocityY(0);
                if (energy < ENERGY) {
                    this.energy += GAIN_OR_LOSE_ENERGY;
                }
            }
            if (other.getTag().equals(PepseGameManager.TERRAIN)){
                transform().setVelocityY(0);
            }

        }
        else {
            this.life.decrement();
            gameObjects.removeGameObject(other, PepseGameManager.LAYER_MONSTER);
        }
    }

    /**
     * @return This function checks if the avatar jumps
     */
    private float checkJumpAvatar(boolean jump, float yVel) {
        if (jump) {
            flag = true;
            yVel = VELOCITY_Y;
            transform().setVelocityY(yVel);
            transform().setAccelerationY(500);
            renderer().setRenderable(jumpAvatar);
        }
        return yVel;
    }

    /**
     * @return This function checks if the avatar moves left
     */
    private float checkLeftAvatar(boolean left, float xVel) {
        if (left) {
            xVel = -VELOCITY_X;
            renderer().setRenderable(moveAvatar);
            renderer().setIsFlippedHorizontally(false);
        }
        return xVel;
    }

    /**
     * @return This function checks if the avatar moves right
     */
    private float checkRightAvatar(boolean right, float xVel) {
        if(right) {
            xVel = VELOCITY_X;
            renderer().setRenderable(moveAvatar);
            renderer().setIsFlippedHorizontally(true);
        }
        return xVel;
    }

    /**
     * @return This function checks if the avatar flies
     */
    private float checkFlyAvatar(boolean fly, float yVel){
        if(fly){
            energy -= GAIN_OR_LOSE_ENERGY;
            yVel = VELOCITY_Y;
            renderer().setRenderable(flyAvatar);
            transform().setVelocityY(yVel);
            transform().setAccelerationY(500);
        }
        return yVel;
    }

    /**
     * This function update the position of the avatar according to the key pressed :
     * fly, jump, left, right
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        float xVel = 0;
        float yVel =0;
        if(getVelocity().y()==0){
            flag=false;
            if (energy < ENERGY){
                energy += 0.5;
            }
        }

        boolean right=inputListener.isKeyPressed(KeyEvent.VK_RIGHT);
        boolean left=inputListener.isKeyPressed(KeyEvent.VK_LEFT);
        boolean jump = inputListener.isKeyPressed(KeyEvent.VK_SPACE) && getVelocity().y() == 0 && !flag;
        boolean fly =energy>0 && inputListener.isKeyPressed(KeyEvent.VK_SPACE)
                && inputListener.isKeyPressed(KeyEvent.VK_SHIFT);

        if(!fly && !jump && !right && !left && getVelocity().y()==0){
            renderer().setRenderable(immobileAvatar);
        }


        xVel = checkRightAvatar(right,xVel);
        xVel = checkLeftAvatar(left,xVel);
        yVel = checkJumpAvatar(jump,yVel);
        checkFlyAvatar(fly,yVel);
        transform().setVelocityX(xVel);
    }
}