package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class SunHalo {

    private static final String SUN_HALO = "sunHalo";

    /**
     *
     * @param gameObjects object of the game
     * @param layer layer of the object
     * @param sun sun
     * @param color color
     * @return this function returns an object sun
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer, GameObject sun, Color color){

        GameObject sunHalo =new GameObject(Vector2.ZERO,
                sun.getDimensions().mult(2),new OvalRenderable(color));
        sunHalo.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sunHalo,layer);
        sunHalo.addComponent(time->sunHalo.setCenter(sun.getCenter()));
        sunHalo.setTag(SUN_HALO);
        return sun;
    }
}