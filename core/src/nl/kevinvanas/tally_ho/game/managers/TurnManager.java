package nl.kevinvanas.tally_ho.game.managers;

import nl.kevinvanas.tally_ho.game.game_objects.Tile;
import nl.kevinvanas.tally_ho.game.players.PlayerEnum;

/**
 * Created by Kevin on 02/01/2016.
 */
public class TurnManager {

    private GameManager mgr;

    private PlayerEnum[] player = new PlayerEnum[2];
    private int activePlayer = 0;
    private int turn = 0;
    private int finalTurn = Integer.MAX_VALUE;

    private boolean gameOver = false;

    private boolean finalPhase = false;

    public TurnManager(GameManager mgr, PlayerEnum player1, PlayerEnum player2){
        this.mgr=mgr;
        this.player[0]=player1;
        this.player[1]=player2;
        if(player1==PlayerEnum.hunters){ // First player is ALWAYS the animals.
            activePlayer=1;
        }
    }

    public void nextTurn(){
        activePlayer =(activePlayer +1)%2;
        turn++;
        if(finalPhase){ // Only 5 or less turns left. Print a message every turn.
            //TODO: Display in the game
            if(turn>finalTurn){
                gameOver = true;
                mgr.prepareForEndGame();
                return;
            }else{
                System.out.print("[[Only " + (finalTurn-turn+1) + " turns left.]] ");
            }
        }
        if(!gameOver){
            System.out.println("("+turn+") Next turn. It is player" + (activePlayer+1) + "'s (" + player[activePlayer].name() +") turn.");
            mgr.onNextTurn();
        }
    }

    public void beforeFirstTurn(){
        System.out.println("First turn. It is player" + (activePlayer+1) + "'s (" + player[activePlayer].name() +") turn.");
    }

    public PlayerEnum getActivePlayer(){ return player[activePlayer]; }

    /**
     * Returns the number of the active player.
     * @return 0 or 1
     */
    public int getActivePlayerNumber(){ return activePlayer; }
    public PlayerEnum getOtherPlayer(){ return player[(activePlayer +1)%2]; }
    public int getOtherPlayerNumber(){ return (activePlayer +1)%2; }
    public boolean isActivePlayer(PlayerEnum player){ return player==this.player[activePlayer]; }
    public int getTurnNumber(){ return turn; }
    public PlayerEnum getPlayerByNumber(int i){
        if(i>=0 && i<player.length){
            return player[i];
        }
        return null;
    }

    /**
     * Checks whether 'tile' is owned by the active player.
     *
     * @param tile
     * @return
     */
    public boolean tileOwnedByActivePlayer(Tile tile){ return tile.getType().getOwner() == this.getActivePlayer(); }

    /**
     * Checks whether 'tile' is controllable by the active player (= an owned tile, or a neutral tile)
     *
     * @param tile
     * @return
     */
    public boolean tileUsableByActivePlayer(Tile tile){
        PlayerEnum tileOwner = tile.getType().getOwner();
        return tileOwner == null || tileOwner == this.getActivePlayer();
    }

    public void checkFinalPhase(){
        if(!finalPhase && mgr.gridmgr.countFaceDownTiles() == 0) {
            finalPhase = true;
            finalTurn = turn+9; // Each player has only 5 turns left until the game ends.
            System.out.println("(TurnManager) endPhase is beginning! Each player has only 5 turns left, starting with player" + this.getActivePlayerNumber() + " (" + this.getActivePlayer() + ").");
        }
    }

    public boolean isGameOver(){return gameOver;}

    public boolean isFinalPhase(){return finalPhase;}

}
