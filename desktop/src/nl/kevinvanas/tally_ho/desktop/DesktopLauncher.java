package nl.kevinvanas.tally_ho.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import nl.kevinvanas.tally_ho.MyGdxGame;
import nl.kevinvanas.tally_ho.utils.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Constants.APP_WIDTH;
		config.height = Constants.APP_HEIGHT;
		new LwjglApplication(new MyGdxGame(), config);
	}
}
