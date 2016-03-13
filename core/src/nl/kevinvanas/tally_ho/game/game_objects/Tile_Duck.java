package nl.kevinvanas.tally_ho.game.game_objects;

/**
 * Created by Kevin on 02/01/2016.
 */
public class Tile_Duck extends Tile {

    public Tile_Duck(float x, float y, float width, float height){
        super(x, y, width, height, TileEnum.duck);
    }

    public Tile_Duck(){
        super(TileEnum.duck);
    }

    @Override
    protected int[][] whereCanIGo() {
        //TODO
        int numTargets = 4;
        return new int[numTargets][2];
    }


}
