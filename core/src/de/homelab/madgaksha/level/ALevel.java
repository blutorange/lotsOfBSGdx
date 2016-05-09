package de.homelab.madgaksha.level;

import com.badlogic.gdx.utils.viewport;

/**
 * Base class for all the different game levels.
 * 
 * Method and variable names ending with "W" refer
 * to world space coordinates, those ending with "S" to screen space
 * coordinates.
 *
 * @author madgaksha
 */
public abstract class ALevel {

    private static int VIEWPORT_GAME_AR_NUM = 8;
    private static int VIEWPORT_GAME_AR_DEN = 9;
    private static float VIEWPORT_INFO_WIDTH_MIN_S = 0.3f;
    private static float VIEWPORT_INFO_HEIGHT_MIN_S = 0.3f;

    // Origin always at (0,0)
    /**
     * @return Width of the map in world coordinates.
     */
    public abstract int getMapWidthW();
    /**
     * @return Height of the map in world coordinates.
     */
    public abstract int getMapHeightW();

    /**
     * The viewport for rendering the world, separate from
     * the {@link #getInfoViewport()}. Rendering takes place
     * as follows:
     *  1) Render background.
     *  2) Render in-game.
     *  3) Render overly info. (high score, weapons, shields, etc.).
     *
     * For this game, the aspect ratio of the viewport will always be 8:9. Its
     * size will be adjusted so that it fits the screen. The info viewport
     * exists in two versions, one for portrait-like screens, and one for
     * landscape-like screens. However, the info viewport must have a certain
     * minimum width (landscape) or height (portrait). The game viewport must
     * be scaled down proportionately.
     *
     * Minimum width/height of the info screen is given by the constants
     * {@link ALevel.VIEWPORT_INFO_WIDTH_MIN} and {@link ALevel.VIEWPORT_INFO_HEIGHT_MIN}.
     *
     * Depending on the screen aspect ratio, the world and info viewport
     * will be arranged differently on the screen.
     *
     * Portrait:
     * /---------\
     * |  INFO   |
     * | SCREEN  |
     * |_________|
     * |         |
     * |  GAME   |
     * | SCREEN  |
     * |    x    |
     * |         |
     * \---------/
     *    
     * Landscape:
     * /---------+------\
     * |         |      |
     * |  GAME   | INFO |
     * | SCREEN  |SCREEN|
     * |         |      |
     * \---------+------/
     *
     *
     * Square:
     * /----------------\
     * |           |    |
     * |           |INFO|
     * |   GAME    |    |
     * |           | SCR|
     * |  SCREEN   | EEN|
     * |           |    |
     * |           |    |
     * |           |    |
     * \----------------/
     * @see #getInfoViewport()}
     * @return The viewport for the world.
     */
    public Viewport getWorldViewport() {
      //TODO
    }
 
    /**
     * The viewport for the info screen.
     * @see #getWorldViewport()
     */  
    public Viewport getInfoViewport() {
        //TODO
    }

    /**
     * The viewport for drawing general elements directly
     * in screen coordinates.
     */
    public Viewport getScreenViewport() {
    }
}
