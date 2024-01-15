package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.awt.*;
import java.util.*;

public class Leaves{

    private final float coorX;
    private final float heightTrunk;
    private final int terrainHeight;
    private final GameObjectCollection gameObjects;
    private final Random random;
    private static final Color LEAVES_COLOR = new Color(65, 168, 51);

    /**
     * Constructor
     */
    public Leaves(float coorX, float heightTrunk, GameObjectCollection gameObjects, int terrainHeight,int seed){
        this.coorX=coorX;
        this.heightTrunk = heightTrunk;
        this.gameObjects = gameObjects;
        this.terrainHeight = terrainHeight;
        random= new Random(Objects.hash(coorX, seed));
    }

    /**
     * This function creates leaves on the tree
     */
    public void createLeaves() {
        Vector2 start = new Vector2(coorX, heightTrunk);
        int sizeLeaves = 4+random.nextInt(5);
        while(sizeLeaves%2==0){
            sizeLeaves =4 +random.nextInt(5);
        }
        int x = (sizeLeaves-1)/2;
        start = start.add(new Vector2(-x* Block.SIZE, +  x*Block.SIZE));
        for (int k = 0; k < sizeLeaves; k++) {
            for (int i = 0; i < sizeLeaves; i++) {
                if(random.nextInt(4)!=1) {
                    Leaf leaf= new Leaf(start, new RectangleRenderable(ColorSupplier.approximateColor(LEAVES_COLOR)));
                    gameObjects.addGameObject(leaf, PepseGameManager.LAYER_LEAF);
                }
                start = start.add(new Vector2(0, -Block.SIZE));
            }
            start = start.add(new Vector2(Block.SIZE, sizeLeaves * Block.SIZE));
        }
        gameObjects.layers().shouldLayersCollide(PepseGameManager.LAYER_LEAF,
                PepseGameManager.LAYER_TERRAIN, true);
    }
}