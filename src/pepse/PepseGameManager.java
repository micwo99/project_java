package pepse;

import danogl.GameManager;
import danogl.GameObject;
import danogl.collisions.Layer;
import danogl.gui.ImageReader;
import danogl.gui.SoundReader;
import danogl.gui.UserInputListener;
import danogl.gui.WindowController;
import danogl.gui.rendering.Camera;
import danogl.util.Vector2;
import pepse.world.Avatar;
import pepse.world.*;
import pepse.world.daynight.Night;
import pepse.world.daynight.Sun;
import pepse.world.daynight.SunHalo;
import pepse.world.trees.Tree;
import java.awt.*;
import java.util.Random;

public class PepseGameManager extends GameManager {

    private int nextLine;
    private int prevLine;
    private int cycleLength;
    private float avatarPosition;
    private Vector2 windowsDimension;
    private Terrain terrain;
    private Avatar avatar;
    private Tree tree;
    private WindowController windowController;
    private static final Color SUN_HALO_COLOR = new Color(255, 255, 0, 20);
    public static final  String TRUNK="Trunk";
    public static final  String LEAF= "Leaf";
    public static final String MONSTER="Monster";
    public static final String AVATAR="avatar";
    public static final String BLOCK="block";
    public static final String TERRAIN="Terrain";
    public static final String SEC_TERRAIN="secTerrain";
    public static final String FLAG="Flag";
    public static final String SKY="sky";
    public static final String NIGHT="night";
    public static final String LOOSE_INFORM_USER = "You Loose... Play Again?";
    public static final String WIN_INFORM_USER = "You Win... Play Again?";
    public static final String PNG =".png";
    public static final int MOVE_LINE = Block.SIZE*10;
    public static final int CYCLE_LENGTH = 30;
    public static final int LAYER_FLAG =Layer.STATIC_OBJECTS+20;
    public static final int LAYER_LEAF =Layer.DEFAULT+10;
    public static final int LAYER_TRUNK =Layer.DEFAULT;
    public static final int LAYER_MONSTER =Layer.DEFAULT+20;
    public static final int LAYER_TERRAIN =Layer.STATIC_OBJECTS;
    public static final int LAYER_SEC_TERRAIN =Layer.STATIC_OBJECTS+10;
    public static final int LAYER_AVATAR=Layer.DEFAULT;
    public static final int LAYER_SKY=Layer.BACKGROUND;
    public static final int LAYER_NIGHT=Layer.FOREGROUND;
    public static final int LAYER_SUN=Layer.BACKGROUND + 1;
    public static final int LAYER_SUN_HALO=Layer.BACKGROUND + 10;

    private static float MIDDLE_FACTOR=0.5f;

    /**
     * This function initializes the sky, the night and the sun of the game
     * @param windowController window controller
     */
    private void initializeSkyNightSun(WindowController windowController){
        Sky.create(gameObjects(), windowController.getWindowDimensions(), LAYER_SKY);
        Night.create(gameObjects(), LAYER_NIGHT, windowController.getWindowDimensions(), cycleLength);
        GameObject sun = Sun.create(gameObjects(), LAYER_SUN, windowsDimension, cycleLength);
        SunHalo.create(gameObjects(), LAYER_SUN_HALO, sun, SUN_HALO_COLOR);
    }

    /**
     * This function initializes the terrain of the game
     * @param windowController windoxw controller
     * @param seed
     */
    private void initializeTerrain(WindowController windowController,int seed){
        terrain= new Terrain(gameObjects(),LAYER_TERRAIN,
                windowController.getWindowDimensions(),seed);
        terrain.createInRange((int)-(windowsDimension.x()/2), (int) (1.5* windowsDimension.x()));
    }

    /**
     * This function initializes the trees of the game
     * @param imageReader image reader
     * @param random random
     * @param avatarPosition the avatar position
     * @param windowController window controller
     */
    private void initializeTree(ImageReader imageReader, Random random, int avatarPosition,
                                WindowController windowController,int seed){
        int finishFlagRight= 6000 + random.nextInt(8000);
        finishFlagRight =Block.DivisibleBySize(finishFlagRight);
        int finishFlagLeft=-finishFlagRight;
        tree =new Tree(gameObjects(), terrain::groundHeightAt,
                imageReader,finishFlagLeft,finishFlagRight,avatarPosition,windowController,seed);
        int minX =Block.DivisibleBySize((int)-(windowsDimension.x()/2));
        int maxX =Block.DivisibleBySize( (int) (1.5* windowsDimension.x()));
        tree.createInRange(minX,maxX);
    }

    /**
     * This function initializes the avatar of the game
     * @param inputListener input listener
     * @param imageReader image reader
     * @param center center of the avatar
     */
    private void initializeAvatar(UserInputListener inputListener,ImageReader imageReader,Vector2 center){
        avatar=Avatar.create(gameObjects(),LAYER_AVATAR,center,inputListener,imageReader);
        avatarPosition = avatar.getCenter().x();
    }

    /**
     * This function initializes all the game
     * @param imageReader image reader
     * @param soundReader sound reader
     * @param inputListener input listener
     * @param windowController window controller
     */
    @Override
    public void initializeGame(ImageReader imageReader, SoundReader soundReader, UserInputListener inputListener,
                               WindowController windowController) {
        this.windowController = windowController;
        super.initializeGame(imageReader, soundReader, inputListener, windowController);
        windowsDimension =windowController.getWindowDimensions();
        cycleLength=CYCLE_LENGTH;
        int seed =new Random().nextInt();
        Random random=new Random(seed);
        Vector2 center= windowsDimension.mult(MIDDLE_FACTOR);
        initializeSkyNightSun(windowController);
        initializeTerrain(windowController,seed);
        initializeTree(imageReader,random,(int)center.x(),windowController,seed);
        initializeAvatar(inputListener,imageReader,new Vector2(center.x(),terrain.groundHeightAt(center.x())-60));
        center=center.mult(-1);
        Vector2 vec = windowController.getWindowDimensions().mult(MIDDLE_FACTOR).add(center);
        nextLine = Block.DivisibleBySize((int)(1.5* windowsDimension.x()));
        prevLine = Block.DivisibleBySize((int)(-windowsDimension.x()/2));
        setCamera(new Camera(avatar, vec, windowController.getWindowDimensions(),
                windowController.getWindowDimensions()));

    }

    /**
     * This function removes and creates depending on the avatar move
     */
    private void avatarMovedRight(){
        if(avatar.getCenter().x()>=avatarPosition+(MOVE_LINE)){
            avatarPosition = avatar.getCenter().x();
            terrain.createInRange((nextLine) ,nextLine+(MOVE_LINE));
            tree.createInRange((nextLine) ,nextLine+(MOVE_LINE));
            remove(prevLine ,prevLine+(MOVE_LINE));
            prevLine+=(MOVE_LINE);
            nextLine+=(MOVE_LINE);
        }
    }

    /**
     * This function removes and creates depending on the avatar move
     */
    private  void avatarMovedLeft(){
        if(avatar.getCenter().x()<=avatarPosition-(MOVE_LINE)){
            avatarPosition =avatar.getCenter().x();
            terrain.createInRange(prevLine-(MOVE_LINE),prevLine);
            tree.createInRange( prevLine-(MOVE_LINE),prevLine);
            remove(nextLine-(MOVE_LINE),nextLine);
            prevLine-=(MOVE_LINE);
            nextLine-=(MOVE_LINE);
        }
    }

    /**
     * the function checks if the user loose the game
     */
    private void looseGame(){
        if(avatar.life.value()==0){
            if(windowController.openYesNoDialog(LOOSE_INFORM_USER)){
                windowController.resetGame();
            }
            else windowController.closeWindow();
        }
    }

    /**
     * This function update the game according to if the avatar move left or right
     */
    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        avatarMovedRight();
        avatarMovedLeft();
        looseGame();

    }

    /**
     * This function removes all the objects of the given tag from min x to max x and at the given layer
     * @param minX min x
     * @param maxX max x
     * @param tag tag of an object
     * @param layer layer of the object
     */
    private  void removeOneLayer(float minX,float maxX,String tag,int layer) {
        for (GameObject block : gameObjects().objectsInLayer(layer)) {
            if (block.getTag().equals(tag) && block.getCenter().x() >= minX && block.getCenter().x() <= maxX) {
                gameObjects().removeGameObject(block, layer);
            }
        }
    }

    /**
     *  This function removes all the objects from min x to max x
     * @param minX minimum of the x
     * @param maxX maximum of the x
     */
    private void remove(float minX,float maxX) {
        removeOneLayer(minX,maxX,LEAF,LAYER_LEAF);
        removeOneLayer(minX,maxX,TRUNK,LAYER_TRUNK);
        removeOneLayer(minX,maxX,MONSTER,LAYER_MONSTER);
        removeOneLayer(minX,maxX,TERRAIN,LAYER_TERRAIN);
        removeOneLayer(minX,maxX,SEC_TERRAIN,LAYER_SEC_TERRAIN);
    }

    /**
     * This function runs the game
     */
    public static void main(String[] args) {
        new PepseGameManager().run();
    }
}