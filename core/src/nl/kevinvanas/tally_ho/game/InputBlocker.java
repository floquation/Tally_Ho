package nl.kevinvanas.tally_ho.game;

/**
 * This class is used by "BoardInputManager" to block the input.
 * When this class is instantiated, it is blocking input.
 * Use the method "UnblockInput()" to unblock the input.
 * It is not possible to reuse an instance. Once you unblock the input, this instance may be disposed of: it became useless.
 *
 * Created by Kevin on 13/01/2016.
 */
public class InputBlocker {

    private boolean blocked = true;

    public InputBlocker(){}

//    public void BlockInput(){blocked=true;}
    public void UnblockInput(){blocked=false;}
    public boolean isBlocked(){return blocked;}

}
