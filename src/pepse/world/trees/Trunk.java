package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.util.ColorSupplier;
import pepse.world.Block;
import java.awt.*;

class Trunk {

    private static final Color TRUNK_COLOR = new Color(100, 50, 20);
    private final float coorX;
    private final float height;
    private final float heightTrunk;
    private final GameObjectCollection gameObjects;

    /**
     * Constructor
     */
    public Trunk(float coorX,float height,int heightTrunk, GameObjectCollection gameObjects){
        this.coorX=coorX;
        this.height = height;
        this.gameObjects = gameObjects;
        this.heightTrunk=heightTrunk;
    }

    /**
     *  This function creates trunk
     */
    public void  createTrunk(){
        float begin =height-Block.SIZE;
        while(begin>heightTrunk){
            Block block =new Block(new Vector2(coorX,begin),
                    new RectangleRenderable(ColorSupplier.approximateColor(TRUNK_COLOR)));
            block.setTag(PepseGameManager.TRUNK);
            gameObjects.addGameObject(block,PepseGameManager.LAYER_TRUNK);
            begin-=Block.SIZE;
        }
    }
}