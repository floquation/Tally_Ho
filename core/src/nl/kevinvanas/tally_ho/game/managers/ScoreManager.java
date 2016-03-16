package nl.kevinvanas.tally_ho.game.managers;

import java.util.ArrayList;

import nl.kevinvanas.tally_ho.game.game_objects.Tile;
import nl.kevinvanas.tally_ho.game.game_objects.TileEnum;
import nl.kevinvanas.tally_ho.game.players.PlayerEnum;
import nl.kevinvanas.tally_ho.stages.GameUIStage;

/**
 * Created by Kevin on 01/01/2016.
 */
public class ScoreManager{

    private GameManager mgr;
    private GameUIStage ui;

    private ArrayList<Tile> player1Tiles = new ArrayList<Tile>();
    private ArrayList<Tile> player2Tiles = new ArrayList<Tile>();

    // These are running scores. Use "countPoints()" to recompute the values using the tileLists.
    private int score1 = 0;
    private int score2 = 0;

    public ScoreManager(GameManager mgr, GameUIStage ui){
        this.mgr=mgr;
        this.ui=ui;
    }

    public void addTile(Tile tile){
        if(tile.getType()== TileEnum.notile) return;

        System.out.println("(ScoreManager) The active player scored a tile '" + tile.getType() + "' for " + tile.getType().getNumPoints() + " points!");
        int player = mgr.turnmgr.getActivePlayerNumber();
        if(player == 0){
            player1Tiles.add(tile);
            score1 += tile.getType().getNumPoints();
        }else if(player == 1){
            player2Tiles.add(tile);
            score2 += tile.getType().getNumPoints();
        }else{
            throw new RuntimeException("(ScoreManager) There was a programming error.\nTried to add points to player number " + player + ", but only the values 0 and 1 are possible!");
        }

        updateUI(tile);

        countPoints(); //TODO: temporarily line
    }

    private void updateUI(Tile tile){
        ui.addScoreTile(tile);
        ui.setScores(score1,score2);
    }

    public void countPoints(){
        int points1 = 0;
        int points2 = 0;
        for (Tile tile : player1Tiles){
            points1 += tile.getType().getNumPoints();
        }
        for (Tile tile : player2Tiles){
            points2 += tile.getType().getNumPoints();
        }

        score1=points1;
        score2=points2;

        System.out.println("Player1 ("+mgr.turnmgr.getPlayerByNumber(0)+") has " + points1 + " points. Player2 ("+mgr.turnmgr.getPlayerByNumber(1)+") has " + points2 + " points.");
        ui.setScores(points1,points2);
    }

}
