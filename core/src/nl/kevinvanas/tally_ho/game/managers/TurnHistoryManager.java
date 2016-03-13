package nl.kevinvanas.tally_ho.game.managers;

import java.util.ArrayList;
import java.util.List;

import nl.kevinvanas.tally_ho.game.game_objects.Tile;
import nl.kevinvanas.tally_ho.game.turns.*;

/**
 * Created by Kevin on 09/01/2016.
 */
public class TurnHistoryManager {

    private GameManager mgr;

    private List<Turn> history = new ArrayList<Turn>();

    public TurnHistoryManager(GameManager mgr){
        this.mgr=mgr;
    }

    public void newMoveTurn(Tile source, int ix0, int iy0, Tile target, int ix1, int iy1){
        history.add(new MoveTurn(source, ix0, iy0, target, ix1, iy1));
    }

    public void newFlipTurn(Tile source){
        history.add(new FlipTurn(source));
    }

    public boolean lastTurnWasMove(){
        return this.getLastTurn() instanceof MoveTurn;
    }
    public boolean lastTurnWasFlip(){
        return this.getLastTurn() instanceof FlipTurn;
    }

    private Turn getLastTurn(){
        return history.get(history.size()-1);
    }
    private Turn getLastTurnMinus(int i){
        System.out.println(history.size());
        if(i>=0 && history.size()-1-i >= 0)
            return history.get(history.size()-1-i);
        return null;
    }

    public Tile getLastTurnTile(){
        return this.getLastTurn().getSourceTile();
    }

    public boolean isReverseMoveOf(MoveTurn newMove, int lastTurnMinus){
        return isReverseMoveOf(newMove.getSourceTile(),newMove.getIStart(),newMove.getIEnd(),newMove.getJStart(),newMove.getJEnd(),lastTurnMinus);
    }

    public boolean isReverseMoveOf(Tile source, int ix0, int iy0, int ix1, int iy1, int lastTurnMinus){
        Turn lastTurn = getLastTurnMinus(lastTurnMinus);
        System.out.println("lastTurn := " + lastTurn);
        if(lastTurn instanceof MoveTurn && lastTurn.getSourceTile()==source){
            MoveTurn lastMoveTurn = (MoveTurn)lastTurn;
//            System.out.println("(TurnHistoryManager) Last turn was a move turn with the same source tile. :" + (lastMoveTurn.getIStart() == ix1 && lastMoveTurn.getIEnd() == ix0 &&
//                    lastMoveTurn.getJStart() == iy1 && lastMoveTurn.getJEnd() == iy0));
            return lastMoveTurn.getIStart() == ix1 && lastMoveTurn.getIEnd() == ix0 &&
                    lastMoveTurn.getJStart() == iy1 && lastMoveTurn.getJEnd() == iy0;
        }
        return false;
    }


}
