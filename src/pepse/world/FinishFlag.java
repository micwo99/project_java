package pepse.world;

import danogl.GameObject;
import danogl.collisions.Collision;
import danogl.gui.WindowController;
import danogl.gui.rendering.Renderable;
import danogl.util.Vector2;
import pepse.PepseGameManager;

public class FinishFlag extends GameObject {
    private final WindowController windowController;

    /**
     * Construct a new GameObject instance.
     *
     * @param topLeftCorner Position of the object, in window coordinates (pixels).
     *                      Note that (0,0) is the top-left corner of the window.
     * @param dimensions    Width and height in window coordinates.
     * @param renderable    The renderable representing the object. Can be null, in which case
     */
    public FinishFlag(Vector2 topLeftCorner, Vector2 dimensions,
                      Renderable renderable, WindowController windowController) {
        super(topLeftCorner, dimensions, renderable);
        this.windowController = windowController;
        this.physics().preventIntersectionsFromDirection(Vector2.ZERO);
        setTag(PepseGameManager.FLAG);
    }

    /**
     * Collision with another object of the game
     */
    @Override
    public void onCollisionEnter(GameObject other, Collision collision) {
        super.onCollisionEnter(other, collision);
        if (other.getTag().equals(PepseGameManager.AVATAR)) {
            if (windowController.openYesNoDialog(PepseGameManager.WIN_INFORM_USER)) {
                windowController.resetGame();
            } else windowController.closeWindow();
        }
    }
}