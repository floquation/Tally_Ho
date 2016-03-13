package nl.kevinvanas.tally_ho.game.game_objects;

/**
 * Created by Kevin on 02/01/2016.
 */
public class Tile_Pheasant extends Tile {

    public Tile_Pheasant(float x, float y, float width, float height){
        super(x, y, width, height, TileEnum.pheasant);
    }

    public Tile_Pheasant(){
        super(TileEnum.pheasant);
    }

    @Override
    protected int[][] whereCanIGo() {
        //TODO
        int numTargets = 4;
        return new int[numTargets][2];
    }


}
