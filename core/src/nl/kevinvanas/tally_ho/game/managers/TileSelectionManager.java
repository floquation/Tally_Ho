package nl.kevinvanas.tally_ho.game.managers;

import java.util.ArrayList;
import java.util.List;

import nl.kevinvanas.tally_ho.game.game_objects.Tile;

/**
 * Created by Kevin on 07/01/2016.
 */
public class TileSelectionManager{

    private GameManager mgr;

    private Tile selectedTile = null;
    private List<Tile> targetTiles = new ArrayList<Tile>();

    public TileSelectionManager(GameManager mgr){
        this.mgr=mgr;
    }

    public boolean isSelected(){
        return selectedTile!=null;
    }

    public boolean isSelected(Tile tile){
        return tile==selectedTile;
    }

    public boolean isTileTarget(Tile tile){
        return this.targetTiles.contains(tile);
    }

    public void selectTile(Tile tile){
        if (this.isSelected()) {
            this.deselectTile();
        }
        selectedTile = tile;
        tile.select();
        mgr.gridmgr.findMarkTargetsForTile(tile, targetTiles);
        this.notifyAllTilesOfTarget();
    }

    public void deselectTile(){
        selectedTile.deselect();
        selectedTile = null;
        this.clearAllTargets();
    }

    public Tile getSelectedTile(){
        return selectedTile;
    }

//    private void makeTileTarget(Tile tile){
//        tile.setIs_possible_target(true);
//        targetTiles.add(tile);
//    }

    private void notifyAllTilesOfTarget(){
        for(Tile tile : targetTiles){
            tile.setIs_possible_target(true);
        }
    }

//    private void unmakeTileTarget(Tile tile){
//        tile.setIs_possible_target(false);
//        targetTiles.remove(tile);
//    }

    private void clearAllTargets(){
        for(Tile tile : targetTiles){
            tile.setIs_possible_target(false);
        }
        targetTiles.clear();
    }

}
