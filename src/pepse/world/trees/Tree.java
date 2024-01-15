package pepse.world.trees;

import danogl.collisions.GameObjectCollection;
import danogl.gui.ImageReader;
import danogl.gui.WindowController;
import danogl.gui.rendering.ImageRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;
import pepse.world.Block;
import pepse.world.Monster;
import pepse.world.FinishFlag;
import java.util.Objects;
import java.util.Random;
import java.util.function.Function;

public class Tree {

    private static final String FLAG_PATH = "assets/flag.png";
    private final int seed;
    private final int flag1;
    private final int flag2;
    private final int avatarPosition;
    private final GameObjectCollection gameObjects;
    private final Function<Float, Float> func;
    private final ImageReader imageReader;
    private final WindowController windowController;
    private final Vector2 DIM_FLAG = new Vector2(50,500);

    /**
     * Constructor
     */
    public Tree(GameObjectCollection gameObjects, Function<Float,Float> func, ImageReader imageReader,
                int flag1, int flag2, int avatarPosition, WindowController windowController,int seed){
        this.gameObjects = gameObjects;
        this.func = func;
        this.imageReader = imageReader;
        this.flag1 = flag1;
        this.flag2 = flag2;
        this.avatarPosition = Block.DivisibleBySize(avatarPosition);
        this.windowController = windowController;
        this.seed = seed;
    }

    /**
     * This function creates trees from minX and maxX by create trunks and leaves
     * this function also creates the finish flag of the game, and the monsters of the game
     * @param minX minX
     * @param maxX maxX
     */
    public void createInRange(int minX, int maxX) {
        int  begin=minX;
        boolean flag=true;
        while(begin<maxX) {
            Random random= new Random(Objects.hash(begin, seed));
            int createTrunk = random.nextInt(10);
            if (createTrunk == 0 && begin!=avatarPosition && begin!=flag1 && begin!=flag2) {
                flag=true;
                float  heightTerrain =  func.apply((float)begin);
                int heightTrunk= Block.DivisibleBySize((int) (heightTerrain-random.nextInt(300)-150));
                Trunk trunk = new Trunk(begin, heightTerrain, heightTrunk, gameObjects);
                trunk.createTrunk();
                Leaves leaves = new Leaves(begin, heightTrunk, gameObjects, (int) heightTerrain, seed);
                leaves.createLeaves();
            }
            else{
                createFinishFlag(begin);
                int haveMonster=random.nextInt(15);
                if(haveMonster==0 && flag){
                    Monster.create(gameObjects, PepseGameManager.LAYER_MONSTER,imageReader,begin,func);
                    flag=false;
                }
            }
            begin += 30;
        }
    }

    /**
     *  This function, depending on the position of the avatar, creates the finish flag of the game
     */
    private void createFinishFlag(int position){
        ImageRenderable flagImage =imageReader.readImage(FLAG_PATH,true);
        float coorY=  func.apply((float)position)-500;
        if(position==flag1) {
            FinishFlag Flag1= new FinishFlag(new Vector2(position,coorY),DIM_FLAG,flagImage, windowController);
            gameObjects.addGameObject(Flag1,PepseGameManager.LAYER_FLAG);
            gameObjects.layers().shouldLayersCollide(PepseGameManager.LAYER_AVATAR,
                    PepseGameManager.LAYER_FLAG,true);
        }
        if(position==flag2) {
            FinishFlag Flag2 = new FinishFlag(new Vector2(position, coorY), DIM_FLAG, flagImage,windowController);
            gameObjects.addGameObject(Flag2, PepseGameManager.LAYER_FLAG);
            gameObjects.layers().shouldLayersCollide(PepseGameManager.LAYER_AVATAR,
                    PepseGameManager.LAYER_FLAG, true);
        }
    }
}