package nl.kevinvanas.tally_ho.game.turns;

import nl.kevinvanas.tally_ho.game.game_objects.Tile;

/**
 * Created by Kevin on 09/01/2016.
 */
public abstract class Turn {

    protected Tile source;

    public Tile getSourceTile(){
        return source;
    }

}
