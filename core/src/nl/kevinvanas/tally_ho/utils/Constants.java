package nl.kevinvanas.tally_ho.utils;

/**
 * Created by Kevin on 01/01/2016.
 */
public class Constants {

    public static final int APP_WIDTH = 800;
    public static final int APP_HEIGHT = 480;

    public static final int BOARD_GRID_WIDTH = 7;
    public static final int BOARD_GRID_HEIGHT = 7;
    public static final float BOARD_SIZE = Math.min(APP_WIDTH,APP_HEIGHT);
    public static final float BOARD_XOFFSET_FACTOR = 0.04f;
    public static final float BOARD_XOFFSET_ABSOLUTE = BOARD_XOFFSET_FACTOR*BOARD_SIZE;
    public static final float BOARD_YOFFSET_FACTOR = 0.04f;
    public static final float BOARD_YOFFSET_ABSOLUTE = BOARD_YOFFSET_FACTOR*BOARD_SIZE;
    public static final float BOARD_TILE_SPRITE_OFFSET_FACTOR = 0.06f;
    public static final float BOARD_TILE_WIDTH = (float)(BOARD_SIZE*(1-2*BOARD_XOFFSET_FACTOR)/7);
    public static final float BOARD_TILE_HEIGHT = BOARD_TILE_WIDTH;

}
