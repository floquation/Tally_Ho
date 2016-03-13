package nl.kevinvanas.tally_ho.game.turns;

import nl.kevinvanas.tally_ho.game.game_objects.Tile;

/**
 * Created by Kevin on 09/01/2016.
 */
public class FlipTurn extends Turn {

    public FlipTurn (Tile source){
        super.source=source;
    }


    public String toString(){
        return "FlipTurn[" + source.getType() + "]";
    }

}
