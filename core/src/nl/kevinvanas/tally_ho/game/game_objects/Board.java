package nl.kevinvanas.tally_ho.game.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;

import java.util.List;

import nl.kevinvanas.tally_ho.game.managers.BoardGridManager;
import nl.kevinvanas.tally_ho.game.managers.BoardInputManager;
import nl.kevinvanas.tally_ho.game.managers.TileSelectionManager;
import nl.kevinvanas.tally_ho.utils.Constants;

/**
 * Created by Kevin on 01/01/2016.
 */
public class Board extends Group {

    private Sprite sprite = new Sprite(new Texture(Gdx.files.internal("board.png")));

    public Board(){
        sprite.setBounds(0, 0, Constants.BOARD_SIZE, Constants.BOARD_SIZE);
        setBounds(sprite.getX(), sprite.getY(), sprite.getWidth(), sprite.getHeight());
        setTouchable(Touchable.enabled);
    }


    @Override
    protected void positionChanged() {
        sprite.setPosition(getX(), getY());
        super.positionChanged();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.disableBlending();
        sprite.draw(batch);
        batch.enableBlending();
        super.draw(batch, parentAlpha); // will draw tiles (child actors)
    }

//    @Override
//    public void act(float delta) {
//        super.act(delta);
//    }


}
