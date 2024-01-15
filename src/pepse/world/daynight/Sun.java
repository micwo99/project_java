package pepse.world.daynight;

import danogl.GameObject;
import danogl.collisions.GameObjectCollection;
import danogl.components.CoordinateSpace;
import danogl.components.Transition;
import danogl.gui.rendering.OvalRenderable;
import danogl.util.Vector2;

import java.awt.*;

public class Sun {

    private static final String SUN = "sun";
    private static final Vector2 DIM_SUN = new Vector2(150,150);

    /**
     *
     * @param gameObjects object of the game
     * @param layer layer of the object
     * @param windowDimensions the dimensions of the window
     * @param cycleLength cycle length
     * @return thus function returns an object sun
     */
    public static GameObject create(GameObjectCollection gameObjects, int layer,
                                    Vector2 windowDimensions, float cycleLength){
        GameObject sun =new GameObject(Vector2.ZERO,DIM_SUN,new OvalRenderable(Color.YELLOW));
        sun.setCoordinateSpace(CoordinateSpace.CAMERA_COORDINATES);
        gameObjects.addGameObject(sun,layer);
        sun.setTag(SUN);
        new Transition<Float>(sun,
                angle->sun.setCenter(new Vector2(
                        (float)(((windowDimensions.x()/2)+ 400 *Math.sin(Math.toRadians(angle)))),
                        (float)(((windowDimensions.y()/2)+400*(-1)*Math.cos(Math.toRadians(angle)))))),
                360f, 0f, Transition.LINEAR_INTERPOLATOR_FLOAT, cycleLength,
                Transition.TransitionType.TRANSITION_LOOP, null);
        return sun;
    }

}