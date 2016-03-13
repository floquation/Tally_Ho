package nl.kevinvanas.tally_ho.game.game_objects;

/**
 * Created by Kevin on 02/01/2016.
 */
public class Tile_Bear extends Tile {

//    List<String> places = Arrays.asList("Buenos Aires", "Córdoba", "La Plata");

    public Tile_Bear(float x, float y, float width, float height){
        super(x, y, width, height, TileEnum.bear);
    }

    public Tile_Bear(){
        super(TileEnum.bear);
    }

    @Override
    protected int[][] whereCanIGo() {
        //TODO
        int numTargets = 4;
        return new int[numTargets][2];
    }


}
