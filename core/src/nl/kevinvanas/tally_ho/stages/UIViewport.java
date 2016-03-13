package nl.kevinvanas.tally_ho.stages;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

/** A viewport where the world size is based on the size of the screen. By default 1 world unit == 1 screen pixel, but this ratio
 * can be {@link #setUnitsPerPixel(float) changed}.
 * @author Daniel Holderbaum
 * @author Kevin van As */


/**
 * Created by Kevin on 16/01/2016.
 */
public class UIViewport extends Viewport {

    /** Creates a new viewport using a new {@link OrthographicCamera}. */
    public UIViewport () {
        this(new OrthographicCamera());
    }

    public UIViewport (Camera camera) {
        setCamera(camera);
    }

    @Override
    public void update (int screenWidth, int screenHeight, boolean centerCamera) {
        update(this.getScreenX(),this.getScreenY(),screenWidth,screenHeight,centerCamera);
    }

    public void update(int screenX, int screenY, int screenWidth, int screenHeight, boolean centerCamera){
        setScreenBounds(screenX, screenY, screenWidth, screenHeight);
        apply(centerCamera);
    }


}
