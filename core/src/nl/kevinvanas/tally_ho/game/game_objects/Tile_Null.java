package nl.kevinvanas.tally_ho.game.game_objects;

/**
 * Created by Kevin on 02/01/2016.
 */
public class Tile_Null extends Tile {

    public Tile_Null(float x, float y, float width, float height){
        super(x, y, width, height, null);
    }

    public Tile_Null(){
        super(TileEnum.notile);
    }

    @Override
    protected int[][] whereCanIGo() {
        return null;
    }


}
