package nl.kevinvanas.tally_ho.game.managers;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.SizeToAction;
import com.badlogic.gdx.utils.Align;

import nl.kevinvanas.tally_ho.game.InputBlocker;
import nl.kevinvanas.tally_ho.game.game_objects.Tile;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.parallel;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

/**
 * Created by Kevin on 13/01/2016.
 */
public class AnimationManager {

    private GameManager mgr;

    public AnimationManager(GameManager mgr){
        this.mgr=mgr;
    }

    public void flipUpTile(final Tile tile, final boolean blockInput, Runnable runLast){
        final InputBlocker blocker;
        if(blockInput){
            mgr.boardInputmgr.blockInput(blocker = new InputBlocker());
        }else{
            blocker=null;
        }

        final float width = tile.getWidth();
        final float height = tile.getHeight();
        float duration = 1f;
        final boolean isHorizontal = (tile.getFaceUpRotation()/90)%2 == 0;

        Interpolation interpFlip1 = new Interpolation() {
            public float apply (float a) {
                return 1- MathUtils.cos(a * MathUtils.PI / 2);
            }
        };
        Interpolation interpFlip2 = new Interpolation() {
            public float apply (float a) {
                return MathUtils.sin(a * MathUtils.PI / 2);
            }
        };

        SizeToAction resize1 = new SizeToAction();
        resize1.setDuration(duration);
        resize1.setSize(0f, height);
        resize1.setInterpolation(interpFlip1);
        SizeToAction resize2 = new SizeToAction();
        resize2.setDuration(duration);
        resize2.setSize(width, height);
        resize2.setInterpolation(interpFlip2);

        MoveByAction move1 = new MoveByAction();
        move1.setAmount(width / 2, 0f);
        move1.setDuration(duration);
        move1.setInterpolation(interpFlip1);
        MoveByAction move2 = new MoveByAction();
        if(isHorizontal){
            move2.setAmount(-width / 2, 0f);
        }else{
            move2.setAmount(0, -height / 2);
        }
        move2.setDuration(duration);
        move2.setInterpolation(interpFlip2);

        ParallelAction flip1 = new ParallelAction();
        flip1.addAction(resize1);
        flip1.addAction(move1);
        ParallelAction flip2 = new ParallelAction();
        flip2.addAction(resize2);
        flip2.addAction(move2);

        RunnableAction changetexture = new RunnableAction();
        changetexture.setRunnable(new Runnable() {
            @Override
            public void run() {
                tile.setFaceUpTexture();
                tile.setRotation(tile.getFaceUpRotation());
                if(!isHorizontal){
                    tile.setSize(width, 0f);
                    tile.moveBy(-width/2,height/2);
                }
            }
        });

        RunnableAction animFinish = new RunnableAction();
        animFinish.setRunnable(new Runnable() {
            @Override
            public void run() {
                if(blockInput) blocker.UnblockInput();
            }
        });

        tile.addAction(sequence(flip1, changetexture, flip2, animFinish));


        if(runLast==null){
            tile.addAction(sequence(flip1, changetexture, flip2, animFinish));
        }else{
            tile.addAction(sequence(flip1, changetexture, flip2, animFinish, run(runLast)));
        }

    }

    public void moveTileTo(final Tile tile, float x, float y, final boolean blockInput, Runnable runLast){

        final InputBlocker blocker;
        if(blockInput){
            mgr.boardInputmgr.blockInput(blocker = new InputBlocker());
        }else{
            blocker=null;
        }

        float duration = 1f;

        MoveToAction move = new MoveToAction();
        move.setPosition(x,y, Align.center);
        move.setDuration(duration);

        RunnableAction animFinish = new RunnableAction();
        animFinish.setRunnable(new Runnable() {
            @Override
            public void run() {
                if (blockInput) blocker.UnblockInput();
            }
        });

        if(runLast==null){
            tile.addAction(sequence(move, animFinish));
        }else{
            tile.addAction(sequence(move, animFinish, run(runLast)));
        }
    }

    public void fadeOutTile(final Tile tile, final boolean blockInput, Runnable runLast){
        float duration = 0.5f;

        final InputBlocker blocker;
        if(blockInput){
            mgr.boardInputmgr.blockInput(blocker = new InputBlocker());
        }else{
            blocker=null;
        }

        AlphaAction fade = new AlphaAction();
        fade.setDuration(duration);
        fade.setAlpha(0f);

        RunnableAction animFinish = new RunnableAction();
        animFinish.setRunnable(new Runnable() {
            @Override
            public void run() {
                if (blockInput) blocker.UnblockInput();
            }
        });

        if(runLast==null){
            tile.addAction(sequence(fade, animFinish));
        }else{
            tile.addAction(sequence(fade, animFinish, run(runLast)));
        }
    }

    public void moveTo_FadeTile(final Tile tile, float x, float y, final boolean blockInput, Runnable runLast){

        final InputBlocker blocker;
        if(blockInput){
            mgr.boardInputmgr.blockInput(blocker = new InputBlocker());
        }else{
            blocker=null;
        }

        float duration = 1f;

        MoveToAction move = new MoveToAction();
        move.setPosition(x,y, Align.center);
        move.setDuration(duration);

        AlphaAction fade = new AlphaAction();
        fade.setDuration(duration);
        fade.setAlpha(0f);

        RunnableAction animFinish = new RunnableAction();
        animFinish.setRunnable(new Runnable() {
            @Override
            public void run() {
                if (blockInput) blocker.UnblockInput();
            }
        });

        if(runLast==null){
            tile.addAction(sequence(parallel(move,fade), animFinish));
        }else{
            tile.addAction(sequence(parallel(move,fade), animFinish, run(runLast)));
        }
    }

}
