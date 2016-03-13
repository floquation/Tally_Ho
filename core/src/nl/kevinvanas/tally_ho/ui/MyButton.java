package nl.kevinvanas.tally_ho.ui;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;

/**
 * Created by Kevin on 22/01/2016.
 */
public class MyButton extends Image{

    private Drawable drawUp;
    private Drawable drawDown;

    private Runnable runOnPress = null;

    public MyButton(Texture textureUp, Texture textureDown){
        super();
        drawUp = new TextureRegionDrawable(new TextureRegion(textureUp));
        drawDown = new TextureRegionDrawable(new TextureRegion(textureDown));
        this.setDrawable(drawUp);

        this.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(isTouchable()){
                    pushDown();
                    return true;
                }
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                pullUp();
                if(runOnPress != null && hit(x, y, isTouchable())!=null){
                    runOnPress.run();
                }
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                pushDown();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                pullUp();
            }
        });
    }


    private void pushDown() {
        this.setDrawable(drawDown);
    }
    private void pullUp(){
        this.setDrawable(drawUp);
    }

    @Override
    protected void sizeChanged(){
        drawDown.setMinWidth(this.getWidth());
        drawDown.setMinHeight(this.getHeight());
        drawUp.setMinWidth(this.getWidth());
        drawUp.setMinHeight(this.getHeight());
        super.sizeChanged();
    }

    public void registerOnButtonPress(Runnable run){
        this.runOnPress = run;
    }


//    @Override
//    public float getPrefWidth () {
//        return getWidth();
//    }
//
//    @Override
//    public float getPrefHeight () {
//        return getHeight();
//    }


//    @Override
//    public void draw (Batch batch, float parentAlpha) {
//
//        float x = getX();
//        float y = getY();
//        float scaleX = getScaleX();
//        float scaleY = getScaleY();
//
//        System.out.println("(x,y,scaleX,scaleY)=("+x+","+y+","+scaleX+","+scaleY+");");
//
//        super.draw(batch,parentAlpha);
//    }
}
