package nl.kevinvanas.tally_ho.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import nl.kevinvanas.tally_ho.game.managers.GameManager;
import nl.kevinvanas.tally_ho.stages.GameStage;
import nl.kevinvanas.tally_ho.stages.GameUIStage;
import nl.kevinvanas.tally_ho.stages.UIViewport;
import nl.kevinvanas.tally_ho.utils.Constants;

/**
 * Created by Kevin on 01/01/2016.
 */
public class GameScreen implements Screen {

    private GameStage stage;
    private GameUIStage uistage;
    private GameManager gamemgr;

    @Override
    public void show() {
        //Stages
        stage = new GameStage();
        stage.getViewport().setWorldSize(Constants.BOARD_SIZE, Constants.BOARD_SIZE); // The world is per definition the board.
        uistage = new GameUIStage(new UIViewport());
        uistage.getViewport().setWorldSize(Constants.BOARD_SIZE,Constants.BOARD_SIZE);

        //Input processing
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(uistage); // Add UI first, as it is on top of the game
        multiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(multiplexer);

        // Start the game
        gamemgr = new GameManager(stage,uistage);
        gamemgr.beforeFirstTurn();
    }

    @Override
    public void render(float delta) {
        //Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //Update the stage
        stage.getViewport().apply();
        stage.draw();
        uistage.getViewport().apply();
        uistage.draw();

        if(!gamemgr.isGameOver()){
            gamemgr.act(delta);
            uistage.act(delta);
            stage.act(delta);
        }else{
            // TODO: End game somehow
        }
    }

    @Override
    public void resize(int width, int height) {
        // Tell the viewport to update its width and height.
        // Stretching is automatic, but the values must be updated, otherwise hit detection will be wrong.
        int boardsize = Math.min(width, height); // Maintain a square board
        stage.getViewport().update(boardsize, boardsize, true);
        ((UIViewport)uistage.getViewport()).setWorldSize(width - boardsize, height);
        ((UIViewport)uistage.getViewport()).update(boardsize, 0, width - boardsize, height, true);
        uistage.onResize(width - boardsize, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
        uistage.dispose();
    }
}
