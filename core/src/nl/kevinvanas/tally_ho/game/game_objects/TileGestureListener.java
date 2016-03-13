package nl.kevinvanas.tally_ho.game.game_objects;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;

import nl.kevinvanas.tally_ho.game.managers.BoardInputManager;

/**
 * Created by Kevin on 02/01/2016.
 */
public class TileGestureListener extends ActorGestureListener {

    private BoardInputManager inputmgr;

    public TileGestureListener(BoardInputManager inputmgr){
        this.inputmgr = inputmgr;
    }

    public void tap(InputEvent event, float x, float y, int count, int button) {
        System.out.println("tap " + x + ", " + y + ", count=" + count + ", button=" + button);
        if(count==1){ //TODO: Flip on single tap?
            if(event.getListenerActor() instanceof Tile){
                Tile tile = (Tile)(event.getListenerActor());
                inputmgr.TileTapped(tile);
            }else{
                throw new IllegalArgumentException("Cannot use 'TileGestureListener' for any other class than 'Tile'! Programming error.");
            }
        }
    }

    public boolean longPress(Actor actor, float x, float y) {
        System.out.println("long press " + x + ", " + y);
        if(actor instanceof Tile){
            Tile tile = (Tile)(actor);
            inputmgr.TileLongPress(tile);
        }else{
            throw new IllegalArgumentException("Cannot use 'TileGestureListener' for any other class than 'Tile'! Programming error.");
        }
        return true;
    }

    public void fling(InputEvent event, float velocityX, float velocityY, int button) {
        System.out.println("fling " + velocityX + ", " + velocityY);
    }

    public void zoom(InputEvent event, float initialDistance, float distance) {
        System.out.println("zoom " + initialDistance + ", " + distance);
    }
}

