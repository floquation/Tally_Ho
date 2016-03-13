package nl.kevinvanas.tally_ho;

import com.badlogic.gdx.Game;

import nl.kevinvanas.tally_ho.screens.GameScreen;

public class MyGdxGame extends Game {

	@Override
	public void create () {
		setScreen(new GameScreen());
	}

}
