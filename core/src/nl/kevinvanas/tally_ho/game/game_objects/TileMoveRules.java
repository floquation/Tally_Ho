package nl.kevinvanas.tally_ho.game.game_objects;

/**
 * Created by Kevin on 05/01/2016.
 */
public class TileMoveRules {

    private TileMoveEnum direction; // What movement directions are allowed
    private int maxSteps; // Maximum number of steps this tile may set. -1 == infinite.
    private boolean onlyInFacingAngle = false; // If true, only checks the direction of the facing angle
    private boolean canAttack = true; // If false, only checks for movement (=empty tiles)

    public TileMoveRules(TileMoveEnum direction, int maxSteps, boolean onlyInFacingAngle, boolean canAttack){
        this.direction=direction;
        this.maxSteps=maxSteps;
        this.onlyInFacingAngle=onlyInFacingAngle;
        this.canAttack=canAttack;
    }

    /**
     * Takes defaults: <br>
     * onlyInFacingAngle = false <br>
     * canAttackInThisDirection = true
     * @param direction
     * @param maxSteps
     */
    public TileMoveRules(TileMoveEnum direction, int maxSteps){
        this.direction=direction;
        this.maxSteps=maxSteps;
    }

    public TileMoveEnum getDirection(){
        return direction;
    }
    public int getMaxSteps() {
        return maxSteps;
    }
    public boolean canAttack(){return canAttack;}
    public boolean onlyInFacingAngle(){return onlyInFacingAngle;}

}
