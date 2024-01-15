package pepse.world;

import danogl.GameObject;
import danogl.components.GameObjectPhysics;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;

public class Block extends GameObject {

    public static final int SIZE = 30;

    /**
     * Constructor
     */
    public Block(Vector2 topLeftCorner, Renderable renderable) {
        super(topLeftCorner, Vector2.ONES.mult(SIZE), renderable);
        physics().preventIntersectionsFromDirection(Vector2.ZERO);
        physics().setMass(GameObjectPhysics.IMMOVABLE_MASS);
    }

    /**
     *
     * @param x a number
     * @return the function returns x after the x is divisible by block's size
     */
    public static int DivisibleBySize(int x){
        while(x % Block.SIZE!=0){
            x--;
        }
        return x;
    }
}