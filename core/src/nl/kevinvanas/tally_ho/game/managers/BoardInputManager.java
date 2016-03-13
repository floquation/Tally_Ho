package nl.kevinvanas.tally_ho.game.managers;


import java.util.ArrayList;
import java.util.List;

import nl.kevinvanas.tally_ho.game.InputBlocker;
import nl.kevinvanas.tally_ho.game.game_objects.Board;
import nl.kevinvanas.tally_ho.game.game_objects.Tile;
import nl.kevinvanas.tally_ho.game.game_objects.TileEnum;

/**
 * Created by Kevin on 02/01/2016.
 */
public class BoardInputManager{

    private GameManager mgr;

    private List<InputBlocker> inputBlockerObjects = new ArrayList<InputBlocker>();
    private boolean nextTurnOnUnblock = false;
    private Board board;

    public BoardInputManager(GameManager mgr, Board board){
        this.mgr=mgr;
        this.board=board;
    }

    public void TileTapped(Tile tile){
        if(inputBlockerObjects.size() == 0 && tile != null) { // If can process input and the tile exists

            // Check if tile is a target:
            if(mgr.selectionmgr.isSelected() && !mgr.selectionmgr.isSelected(tile)){ // If a tile is selected, but not the present tile
                if(mgr.selectionmgr.isTileTarget(tile)){ // If the present tile is a target of the selected tile
                    // Move/Attack target, next turn
                    Tile selectedTile = mgr.selectionmgr.getSelectedTile();
                    nextTurnOnUnblock=true;
                    mgr.gridmgr.moveTileTo(selectedTile, tile);
                    mgr.selectionmgr.deselectTile();
                    return;
                }
                System.out.println("Must deselect the selected tile first.");
                return;
            }

            // The current tile is not a target. So the current tile is the 'subject'.

            if (tile.getType() == TileEnum.notile) return; // Further processing cannot occur for a null-type tile: it can only be a target.

            // No tile is currently selected: flip/select/deselect the current tile.
            if (tile.isFlipped_up()) { // A flipped-up tile is selected, which is either the selectedTile, or no tile is selected at all.
                // Check ownership of current tile: you can only manipulate a tile as a subject if you own it (or it is neutral).
                if(!mgr.turnmgr.tileUsableByActivePlayer(tile)){
                    System.out.println("(BoardInputManager) The selected tile is NOT owned by the active player. Cannot manipulate it.");
                    return;
                }

                if (mgr.selectionmgr.isSelected(tile)) { // The selectedTile is tapped
                    mgr.selectionmgr.deselectTile();
                    return;
                } else { // No tile is selected, and the current tile is tapped.
                    mgr.selectionmgr.selectTile(tile);
                    return;
                }
            } else { // A flipped-down tile is selected
                // Flip it up, next turn.
                nextTurnOnUnblock=true;
                mgr.historymgr.newFlipTurn(tile);
                tile.setFlipped_up();
                mgr.animmgr.flipUpTile(tile,true, null);
                return;
            }
        }else{
            System.out.println("(BoardInputManager) Input was received, but it is currently NOT ALLOWED!");
            return;
        }
    }

    public void TileLongPress(Tile tile){

    }

    public void blockInput(InputBlocker blocker){
        inputBlockerObjects.add(blocker);
    }

    private List<InputBlocker> deletionList = new ArrayList<InputBlocker>(); // Used by "tryUnblockInput". Class-variable to prevent having to re-instantiate an ArrayList every timestep.
    public void tryUnblockInput(){
        if(inputBlockerObjects.size() != 0){
//            System.out.println("(BoardInputManager) There are " + inputBlockerObjects.size() + " inputblockers active.");
            // Scan all InputBlockers to see if they are still blocking
            for(InputBlocker blocker : inputBlockerObjects){
                if(!blocker.isBlocked())deletionList.add(blocker);
            }
            // Delete all unblocked InputBlockers from our list
            inputBlockerObjects.removeAll(deletionList);
            deletionList.clear();
            // Next turn?
            if(inputBlockerObjects.size() == 0 && nextTurnOnUnblock){
                nextTurnOnUnblock=false;
                mgr.turnmgr.nextTurn();
            }
        }
    }

}
