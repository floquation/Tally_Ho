package nl.kevinvanas.tally_ho.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;

import nl.kevinvanas.tally_ho.utils.Constants;

/**
 * Created by Kevin on 01/01/2016.
 */
public class GameStage extends Stage {



    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    public GameStage() {
        super();
    }

    public GameStage(Viewport viewport) {
        super(viewport);
    }

//    /**
//     * Override the default `instant' behaviour for constant time-stepping.
//     * @param delta elapsed time since last frame
//     */
//    @Override
//    public void act(float delta) {
//        // Fixed timestep
//        accumulator += delta;
//        while (accumulator >= TIME_STEP) {
//            super.act(TIME_STEP);
//            accumulator -= TIME_STEP;
//        }
//    }

}
