package nl.kevinvanas.tally_ho.game.turns;

import nl.kevinvanas.tally_ho.game.game_objects.Tile;

/**
 * Created by Kevin on 09/01/2016.
 */
public class MoveTurn extends Turn {

    // Source position
    private int ix0;
    private int iy0;

    // Target position
    private Tile target;
    private int ix1;
    private int iy1;

    public MoveTurn(Tile source, int ix0, int iy0, Tile target, int ix1, int iy1){
        super.source = source;
        this.ix0 = ix0;
        this.iy0 = iy0;
        this.target = target;
        this.ix1 = ix1;
        this.iy1 = iy1;
    }

    public Tile getTargetTile(){
        return target;
    }
    public int getIStart(){
        return ix0;
    }
    public int getIEnd(){
        return ix1;
    }
    public int getJStart(){
        return iy0;
    }
    public int getJEnd(){
        return iy1;
    }

    public String toString(){
        return "MoveTurn[" + source.getType() + "," + ix0 + "," + iy0 + ";" + target.getType() + "," + ix1 + "," + iy1 + "]";
    }

}
