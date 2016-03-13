package nl.kevinvanas.tally_ho.game.game_objects;

/**
 * Created by Kevin on 02/01/2016.
 */
public class Tile_Hunter extends Tile {

    public Tile_Hunter(float x, float y, float width, float height){
        super(x, y, width, height, TileEnum.hunter);
    }

    public Tile_Hunter(){
        super(TileEnum.hunter);
    }

    @Override
    protected int[][] whereCanIGo() {
        //TODO
        int numTargets = 4;
        return new int[numTargets][2];
    }


}
