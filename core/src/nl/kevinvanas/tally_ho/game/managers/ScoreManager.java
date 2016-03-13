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
        }else if(player == 1){
            player2Tiles.add(tile);
        }else{
            throw new RuntimeException("(ScoreManager) There was a programming error.\nTried to add points to player number " + player + ", but only the values 0 and 1 are possible!");
        }

        countPoints(); //TODO: temporarily line
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

        System.out.println("Player1 ("+mgr.turnmgr.getPlayerByNumber(0)+") has " + points1 + " points. Player2 ("+mgr.turnmgr.getPlayerByNumber(1)+") has " + points2 + " points.");
        ui.setScores(points1,points2);
    }

}
