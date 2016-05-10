package de.homelab.madgaksha.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.Game;

/**
 * Base class for all the different game levels.
 * 
 * Method and variable names ending with "W" refer
 * to world space coordinates, those ending with "S" to screen space
 * coordinates.
 *
 * @author madgaksha
 */
public abstract class ALevel implements Serializable {

    // No argument constructor for serialization.
    public ALevel() {}
    
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
     * the {@link #getInfoViewport()}.
     *  
     * @see #getInfoViewport()}
     * @return The viewport for the world.
     */
    public Viewport getGameViewport(int screenWidth, int screenHeight) {
    	return new GameViewport(screenWidth, screenHeight, false);
    }
 
    /**
     * The viewport for the info screen.
     * @see #getWorldViewport()
     */  
    public Viewport getInfoViewport(int screenWidth, int screenHeight) {
        return new InfoViewport(screenWidth, screenHeight, false);
    }

    /**
     * The viewport for drawing general elements directly
     * in screen coordinates.
     */
    public Viewport getScreenViewport(int screenWidth, int screenHeight) {
    	return new ExtendViewport(screenWidth, screenHeight, screenWidth, screenHeight);
    }
    
    public class GameViewport extends Viewport {
		public GameViewport(int screenWidth, int screenHeight, boolean centerCamera) {		
			// Compute dimensions of the game window in pixels.
			Vector2 screenDimensions = computeGameViewportDimensions(screenWidth, screenHeight);
			int gameWidth = (int)screenDimensions.x;
			int gameHeight = (int)screenDimensions.y;

			// Create a new camera.
			Camera camera = new PerspectiveCamera(30, gameWidth, gameHeight);
			
			// Connect camera.
			setCamera(camera);
			
			// Game window goes to the top left.
			setScreenBounds(0, 0, gameWidth, gameHeight);
			
			// Apply game dimensions.
			apply(centerCamera);
		}
		
		@Override
		public void update(int screenWidth, int screenHeight, boolean centerCamera) {
			// Compute dimensions of the game window in pixels.
			Vector2 screenDimensions = computeGameViewportDimensions(screenWidth, screenHeight);
			int gameWidth = (int)screenDimensions.x;
			int gameHeight = (int)screenDimensions.y;

			// Game window goes to the top left.
			setScreenBounds(0, 0, gameWidth, gameHeight);
			
			// Apply game dimensions.
			apply(centerCamera);			
		}
    }

    public class InfoViewport extends Viewport {
		public InfoViewport(int screenWidth, int screenHeight, boolean centerCamera) {
			// Compute dimensions of the game window in pixels.
			Vector2 screenDimensions = computeGameViewportDimensions(screenWidth, screenHeight);
			
			// Set info window to the right / top.
			int gameWidth = (int)screenDimensions.x;
			int gameHeight = (int)screenDimensions.y;
			int infoWidth, infoHeight;
			int infoX, infoY;
			if (screenWidth > screenHeight) {
				// Landscape
				infoWidth = screenWidth-gameWidth;
				infoHeight = screenHeight;
				infoX = gameWidth;
				infoY = 0;
			}
			else {
				infoHeight = screenHeight-gameWidth;
				infoWidth = screenWidth;				
				infoX = 0;
				infoY = gameHeight;
			}
			
			// Create a new camera.
			Camera camera = new OrthographicCamera(infoWidth, infoHeight);
			
			// Connect camera.
			setCamera(camera);
			
			// Game window goes to the top left.
			setScreenBounds(infoX, infoY, infoWidth, infoHeight);
			
			// Apply game dimensions.
			apply(centerCamera);
		}
		
		@Override
		public void update(int screenWidth, int screenHeight, boolean centerCamera) {
			// Compute dimensions of the game window in pixels.
			Vector2 screenDimensions = computeGameViewportDimensions(screenWidth, screenHeight);
			
			// Set info window to the right / top.
			int gameWidth = (int)screenDimensions.x;
			int gameHeight = (int)screenDimensions.y;
			int infoWidth, infoHeight;
			int infoX, infoY;
			if (screenWidth > screenHeight) {
				// Landscape
				infoWidth = screenWidth-gameWidth;
				infoHeight = screenHeight;
				infoX = gameWidth;
				infoY = 0;
			}
			else {
				infoHeight = screenHeight-gameWidth;
				infoWidth = screenWidth;				
				infoX = 0;
				infoY = gameHeight;
			}
			
			// Game window goes to the top left.
			setScreenBounds(infoX, infoY, infoWidth, infoHeight);
			
			// Apply game dimensions.
			apply(centerCamera);
		}
    }

    
    /**
     * Computes the viewport for rendering the world. Rendering
     * proceeds as follows:
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
     * 
     * @param screenWidth Current width of the screen in pixels.
     * @param screenHeight Current height of the screen in pixels.
     * @return Width and height of the game window in pixels.
     */
    private static Vector2 computeGameViewportDimensions(int screenWidth, int screenHeight) {
    	int gameWidth, gameHeight;
		if (screenWidth > screenHeight) {
			// Landscape
			gameHeight = screenHeight;
			gameWidth = screenHeight*Game.VIEWPORT_GAME_AR_NUM/Game.VIEWPORT_GAME_AR_DEN;
			// Allocate enough space to the right of the game window.
			if (gameWidth > (1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * screenWidth) {
				gameWidth = (int)((1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * screenWidth);
				gameHeight = gameWidth * Game.VIEWPORT_GAME_AR_DEN / Game.VIEWPORT_GAME_AR_NUM;
			}
		}
		else {
			// Portrait.
			gameWidth = screenWidth;
			gameHeight = gameWidth * Game.VIEWPORT_GAME_AR_DEN / Game.VIEWPORT_GAME_AR_NUM;
			// Allocate enough space to the top of the game window.
			if (gameHeight > (1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * screenHeight) {
				gameHeight = (int)((1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * screenHeight);
				gameWidth = screenHeight*Game.VIEWPORT_GAME_AR_NUM/Game.VIEWPORT_GAME_AR_DEN;	
			}
		}
		// Floats can store ints exactly, unless too large
		// which screen dimensions are not. Mantissa 23 bit.
		return new Vector2((float)gameWidth,(float)gameHeight);
    }
}