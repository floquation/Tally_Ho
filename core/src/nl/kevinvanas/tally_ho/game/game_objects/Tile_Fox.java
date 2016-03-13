package nl.kevinvanas.tally_ho.game.game_objects;

/**
 * Created by Kevin on 02/01/2016.
 */
public class Tile_Fox extends Tile {

    public Tile_Fox(float x, float y, float width, float height){
        super(x, y, width, height, TileEnum.fox);
    }

    public Tile_Fox(){
        super(TileEnum.fox);
    }

    @Override
    protected int[][] whereCanIGo() {
        //TODO
        int numTargets = 4;
        return new int[numTargets][2];
    }


}
