package pepse.util;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.collisions.Layer;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.TextRenderable;
import danogl.util.Counter;
import danogl.util.Vector2;

public class NumericLifeCounter extends GameObject {
    private final int numOfLives;
    private GameObject textObject;
    private final Counter livesCounter;
    private final Vector2 topLeftCorner;
    private final Vector2 dimensions;
    private final GameObjectCollection gameObjectCollection;
    private final int LAYER_LIFE = Layer.FOREGROUND+2;

    /**
     * Construct a new src.GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     */
    public NumericLifeCounter(Counter livesCounter, Vector2 topLeftCorner, Vector2 dimensions,
                              GameObjectCollection gameObjectCollection){
        super(topLeftCorner, dimensions, null);
        this.livesCounter = livesCounter;
        numOfLives =livesCounter.value();
        this.topLeftCorner = topLeftCorner;
        this.dimensions = dimensions;
        this.gameObjectCollection = gameObjectCollection;
        TextRenderable numericLife = new TextRenderable(String.format("Lives: %d",livesCounter.value()));
        textObject = new GameObject(topLeftCorner, dimensions, numericLife);
        gameObjectCollection.addGameObject(textObject, LAYER_LIFE);
        textObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
    }


    /**
     * this function updates the numericLives in the game depending on if the user lost life,
     * in order to indicates to the user how many lives he has
     * @param deltaTime time
     */
    public void update(float deltaTime) {
        super.update(deltaTime);
        if(livesCounter.value()<numOfLives) {
            gameObjectCollection.removeGameObject(textObject, LAYER_LIFE);
            TextRenderable numericLife = new TextRenderable(String.format("Lives: %d", livesCounter.value()));
            textObject = new GameObject(topLeftCorner, dimensions, numericLife);
            gameObjectCollection.addGameObject(textObject, LAYER_LIFE);
            textObject.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        }
    }
}