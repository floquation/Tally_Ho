package nl.kevinvanas.tally_ho.game.managers;

import nl.kevinvanas.tally_ho.game.InputBlocker;
import nl.kevinvanas.tally_ho.game.game_objects.Board;
import nl.kevinvanas.tally_ho.game.players.PlayerEnum;
import nl.kevinvanas.tally_ho.stages.GameStage;
import nl.kevinvanas.tally_ho.stages.GameUIStage;

/**
 * Created by Kevin on 02/01/2016.
 */
public class GameManager {

    protected BoardInputManager boardInputmgr;
    protected BoardGridManager gridmgr;
    protected TurnManager turnmgr;
    protected ScoreManager scoremgr;
    protected UIInputManager uiInputmgr;
    protected TileSelectionManager selectionmgr;
    protected TurnHistoryManager historymgr;
    protected AnimationManager animmgr;

    private GameStage stage;
    private GameUIStage uistage;

    public GameManager(GameStage stage, GameUIStage uistage){
        this.stage=stage;
        this.uistage=uistage;

        //TODO: Generate board inside the GameStage? I kind of want the GameManager to only link the other managers... Not do anything else.
        Board board = new Board();
        stage.addActor(board);
        stage.setKeyboardFocus(board);

        animmgr = new AnimationManager(this);

        boardInputmgr = new BoardInputManager(this,board);
        gridmgr = new BoardGridManager(this,board);
        turnmgr = new TurnManager(this,PlayerEnum.hunters,PlayerEnum.animals); //This should be changed later to random / dialog.
        scoremgr = new ScoreManager(this,uistage);

        uiInputmgr = new UIInputManager(this);
        selectionmgr = new TileSelectionManager(this);

        historymgr = new TurnHistoryManager(this);

        // After all managers are created: update some default values
        if(turnmgr.getActivePlayerNumber()==0){ // active player = player1
            uistage.setPlayer1Icon(turnmgr.getActivePlayer().getIcon());
            uistage.setPlayer2Icon(turnmgr.getOtherPlayer().getIcon());
        }else{ // active player = player2
            uistage.setPlayer2Icon(turnmgr.getActivePlayer().getIcon());
            uistage.setPlayer1Icon(turnmgr.getOtherPlayer().getIcon());
        }
    }


    /**
     * This is called before the first turn begins.
     * This is called by "GameScreen", right after the "GameManager" is created.
     *
     */
    public void beforeFirstTurn(){
        turnmgr.beforeFirstTurn();
    }

    /**
     * This is called as soon as a new turn begins.
     * This is called by "TurnManager", right after it finishes its "nextTurn" method.
     *
     */
    public void onNextTurn(){
        turnmgr.checkFinalPhase();
    }

    /**
     * This is done every timestep.
     *
     * @param delta
     */
    public void act(float delta){
        boardInputmgr.tryUnblockInput();
    }

    public void prepareForEndGame(){
        System.out.println("(GameManager) prepareForEndGame() was called.");
        boardInputmgr.blockInput(new InputBlocker()); // Disable any further input
        scoremgr.countPoints(); // Compute the score
        // TODO: Go to score screen or whatever
    }

    public boolean isGameOver(){
        return turnmgr.isGameOver();
    }

}
