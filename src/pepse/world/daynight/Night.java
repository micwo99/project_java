package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.RectangleRenderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

import java.awt.*;

public class Night {

    private static final Float MIDNIGHT_OPACITY = 0.5f;
    private static final Float MORNING_OPACITY = 0f;

    /**
     *
     * @param gameObjects object of the game
     * @param layer the layer of the object
     * @param windowDimensions dimensions of the window
     * @param cycleLength cycle length
     * @return this function returns the object night
     */
    public static GameObject create(GameObjectCollection gameObjects,
                                    int layer,
                                    Vector2 windowDimensions,
                                    float cycleLength){

        GameObject night =new GameObject(Vector2.ZERO,windowDimensions,new RectangleRenderable(Color.BLACK));
        night.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(night,layer);
        new Transition<Float>(night, night.renderer()::setOpaqueness,
                MORNING_OPACITY, MIDNIGHT_OPACITY, Transition.CUBIC_INTERPOLATOR_FLOAT, cycleLength/2,
                Transition.TransitionType.TRANSITION_BACK_AND_FORTH, null);
        night.setTag(PepseGameManager.NIGHT);
        return night;
    }
}