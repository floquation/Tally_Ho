package nl.kevinvanas.tally_ho.game.game_objects;

/**
 * Created by Kevin on 02/01/2016.
 */
public class Tile_Tree extends Tile {

    public Tile_Tree(float x, float y, float width, float height){
        super(x, y, width, height, TileEnum.tree);
    }

    public Tile_Tree(){
        super(TileEnum.tree);
    }

    @Override
    protected int[][] whereCanIGo() {
        //TODO
        int numTargets = 4;
        return new int[numTargets][2];
    }


}
