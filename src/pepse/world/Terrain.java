package pepse.world;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.util.Noise;

import java.awt.*;

public class Terrain {

    private static final Color BASE_GROUND_COLOR = new Color(212, 123, 74);
    private static final int TERRAIN_DEPTH = 20;
    private static final float HEIGTH_FACTOR=0.666f;
    private static final float NOISE_FACTOR=1.5f;
    private final GameObjectCollection gameObjects;
    private final int groundLayer;
    private final Vector2 windowDimensions;
    private final float groundHeightAtX0;
    private final Noise noiseFunc;

    /**
     * Constructor
     */
    public Terrain(GameObjectCollection gameObjects, int groundLayer, Vector2 windowDimensions,int seed) {
        this.gameObjects = gameObjects;
        this.groundLayer = groundLayer;
        this.windowDimensions = windowDimensions;
        noiseFunc =new Noise(seed);
        groundHeightAtX0 = groundHeightAt(0);

    }

    /**
     *
     * @return This function returns the height of the terrain at a given coordinate X
     */
    public float groundHeightAt(float x) {
        float factor=(float)(HEIGTH_FACTOR- noiseFunc.noise(x/(Block.SIZE))*NOISE_FACTOR);
        return Block.DivisibleBySize((int)(windowDimensions.y()*factor));
    }

    /**
     * this function creates the terrain according to the minX and the maxX
     * @param minX minX
     * @param maxX maxX
     */
    public void createInRange(int minX, int maxX) {
        minX= Block.DivisibleBySize(minX);
        maxX= Block.DivisibleBySize(maxX);
        while (minX <= maxX) {
            int locY =(int) groundHeightAt(minX);
            Block block;
            for (int i = 0; i < TERRAIN_DEPTH; i++) {
                if (i==0||i==1){
                    block = new Block(new Vector2(minX, locY + i * Block.SIZE),
                            new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                    gameObjects.addGameObject(block, groundLayer);
                    block.setTag(PepseGameManager.TERRAIN);
                }
                else{
                    block = new Block(new Vector2(minX, locY + i * Block.SIZE),
                            new RectangleRenderable(ColorSupplier.approximateColor(BASE_GROUND_COLOR)));
                    gameObjects.addGameObject(block, groundLayer+10);
                    block.setTag(PepseGameManager.SEC_TERRAIN);
                }
            }
            minX+=Block.SIZE;
        }
    }
}