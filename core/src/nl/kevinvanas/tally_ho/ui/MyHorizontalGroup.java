package nl.kevinvanas.tally_ho.ui;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;

/**
 * Created by Kevin on 13/03/2016.
 */
public class MyHorizontalGroup extends HorizontalGroup {

    @Override
    public float getPrefWidth () {
        return getWidth();
    }

    @Override
    public float getPrefHeight () {
        return getHeight();
    }
}
