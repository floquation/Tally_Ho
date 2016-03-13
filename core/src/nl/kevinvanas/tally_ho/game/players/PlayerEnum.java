package nl.kevinvanas.tally_ho.game.players;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import nl.kevinvanas.tally_ho.game.game_objects.TileEnum;

/**
 * Created by Kevin on 02/01/2016.
 */
public enum PlayerEnum {
    hunters(new Texture(Gdx.files.internal("HunterTile.jpg"))),
    animals(new Texture(Gdx.files.internal("BearTile.jpg")));

    private Texture icon;

    private PlayerEnum(Texture icon){
        this.icon=icon;
    }

    public Texture getIcon(){
        return icon;
    }
}
