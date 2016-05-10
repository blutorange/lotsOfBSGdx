package de.homelab.madgaksha.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcecache.Resources.ETexture;

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
    
    private final static float CAMERA_GAME_FIELD_OF_VIEW_Y = 30.0f;
    
    /**
     * The info window can work with virtual coordinates [0.0-1.0].
     */
    private final static float VIEWPORT_INFO_VIRTUAL_WIDTH = 1.0f;
    /**
     * The info window can work with virtual coordinates [0.0-1.0].
     */
    private final static float VIEWPORT_INFO_VIRTUAL_HEIGHT = 1.0f;

    /**
     * The screen window can work with virtual coordinates [0.0..1280.0].
     */
    public final static float VIEWPORT_SCREEN_VIRTUAL_WIDTH = 1280.0f;
    /**
     * The screen window can work with virtual coordinates [0.0..720.0].
     */
    public final static float VIEWPORT_SCREEN_VIRTUAL_HEIGHT = 720.0f;
    
    protected float mapWidthW = 10.0f;
    protected float mapHeightW = 10.0f;
    protected ETexture backgroundImage = null;

    
    // =============================
    //          Constructor
    // =============================
    public ALevel() {
    	mapWidthW = requestedMapWidthW();
    	mapHeightW = requestedMapHeightW();
    	backgroundImage = requestedBackgroundImage();
    }
    
    // =============================
    //       Abstract methods
    // =============================

    /**
     * @return The width of the map in world coordinates.
     */
    protected abstract float requestedMapWidthW();
    /**
     * @return The height of the map in world coordinates. 
     */
    protected abstract float requestedMapHeightW();

    /** The image to use as a background for those portions
     * of the screen that would be blank otherwise. May not
     * be seen at all with with common aspect ratios.
     * @return Path to the background image relative to the assets folder.
     */
    protected abstract ETexture requestedBackgroundImage();

    /**
     * This can be overridden to setup the initial viewport and camera for the game.
     * In particular, this could set:
     *   - the initial camera position and orientation (get the camera with {@link Viewport#getCamera()})
     *   - the initial world width / height of the viewport with {@link Viewport#setWorldHeight(float)} and {@link Viewport#setWorldWidth(float)}
     * @param viewport The viewport to setup.
     */
    protected void setupInitialGameViewport(Viewport viewport){};
    
    // =============================
    //       Implementations
    // =============================
    
    /**
     * @return The width of the map in world coordinates.
     */
    public float getMapWidthW() {
    	return mapWidthW;
    }
    /**
     * @return The height of the map in world coordinates.
     */
    public float getMapHeightW() {
    	return mapHeightW;
    }

    public Texture getBackgroundImage() {
    	return ResourceCache.getTexture(backgroundImage);
    }
    
    /**
     * The viewport for the info screen.
     * @see #getWorldViewport()
     */  
    public Viewport getInfoViewport(int screenWidth, int screenHeight) {
        return new InfoViewport(screenWidth, screenHeight);
    }
    
    /**
     * The viewport for drawing general elements directly
     * in screen coordinates.
     */
    public Viewport getScreenViewport(int screenWidth, int screenHeight) {
    	// OrthographicCamera is created automatically by the FillViewport.
    	final Viewport viewport = new FillViewport(VIEWPORT_SCREEN_VIRTUAL_WIDTH, VIEWPORT_SCREEN_VIRTUAL_HEIGHT);
		final Camera camera = viewport.getCamera();
		camera.position.set(VIEWPORT_SCREEN_VIRTUAL_WIDTH*0.5f,VIEWPORT_SCREEN_VIRTUAL_HEIGHT*0.5f,1.0f);
		return viewport;
    }
    
    /**
     * The viewport for rendering the world, separate from
     * the {@link #getInfoViewport()}.
     *  
     * @see #getInfoViewport()}
     * @return The viewport for the world.
     */
    public Viewport getGameViewport(int screenWidth, int screenHeight) {
    	Viewport vw = new GameViewport(screenWidth, screenHeight, false);
    	setupInitialGameViewport(vw);
    	return vw;
    }
    
    /**
     * A viewport for the game world. The game world is always located at
     * the x-y-plane. The camera will be position above at the positive
     * z-axis and look down on the x-y plane.
     * <br>
     * The game window itself will not fill the entire screen, see
     * {@link ALevel#computeGameViewportDimensions(int, int)} for further
     * details.
     * 
     * @author madgaksha
     *
     */
    private class GameViewport extends Viewport {
		public GameViewport(int screenWidth, int screenHeight, boolean centerCamera) {		
			// Compute dimensions of the game window in pixels.
			Vector2 screenDimensions = computeGameViewportDimensions(screenWidth, screenHeight);
			int gameWidth = (int)screenDimensions.x;
			int gameHeight = (int)screenDimensions.y;
		
			// Create a new camera.
			Camera camera = new PerspectiveCamera(CAMERA_GAME_FIELD_OF_VIEW_Y, gameWidth, gameHeight);
			
			// Position camera to show entire world height initially.
			float elevation = getMapHeightW()*0.5f/(float)Math.tan(CAMERA_GAME_FIELD_OF_VIEW_Y*Math.PI/180.0f);
			camera.position.set(getMapWidthW()*0.5f, getMapHeightW()*0.5f, elevation);

			setWorldHeight(getMapHeightW());
			setWorldWidth(getMapHeightW()*Game.VIEWPORT_GAME_AR);
			
			// Setup clipping planes appropriately.
			camera.near = elevation * 0.05f;
			camera.far = elevation * 2.0f;
			
			// Now look down on the world initially.
			camera.direction.set(0.0f,0.0f,-1.0f);			
			
			// Connect camera.
			setCamera(camera);
			
			// Game window goes to the bottom left.
			setScreenBounds(0, 0, gameWidth, gameHeight);
			
			// Apply needs to be called by the update loop anyway.
			// apply(centerCamera);
		}
		
		@Override
		public void update(int screenWidth, int screenHeight, boolean centerCamera) {
			// Compute dimensions of the game window in pixels.
			Vector2 screenDimensions = computeGameViewportDimensions(screenWidth, screenHeight);
			int gameWidth = (int)screenDimensions.x;
			int gameHeight = (int)screenDimensions.y;			
			
			// Game window goes to the bottom left.
			setScreenBounds(0, 0, gameWidth, gameHeight);
			
			// Apply needs to be called by the update loop anyway.
			// apply(false);
		}
    }

    /**
     * The info viewport for highscores etc.
     * @author madgaksha
     *
     */
    private class InfoViewport extends Viewport {
		public InfoViewport(int screenWidth, int screenHeight) {
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
					
			// Create a new camera with the virtual screen resolution.
			Camera camera = new OrthographicCamera(VIEWPORT_INFO_VIRTUAL_WIDTH, VIEWPORT_INFO_VIRTUAL_HEIGHT);
			
			// Connect camera.
			setCamera(camera);
			
			// Game window goes to the top left.
			setScreenBounds(infoX, infoY, infoWidth, infoHeight);
			
			// Set camera position so that it looks at the area [0.0,1.0]x[0.0,1.0].
			camera.position.set(VIEWPORT_INFO_VIRTUAL_WIDTH*0.5f, VIEWPORT_INFO_VIRTUAL_HEIGHT*0.5f, 1.0f);
			
			setWorldHeight(VIEWPORT_INFO_VIRTUAL_HEIGHT);
			setWorldWidth(VIEWPORT_INFO_VIRTUAL_WIDTH);
			
			// Set camera to look down at the info window.
			camera.direction.set(0.0f, 0.0f, -1.0f);
			
			// Setup near and far clipping plane to make our screen visible...
			camera.near = 0.5f;
			camera.far = 1.5f;
			
			// Apply needs to be called by the update loop anyway.
			// apply(false);
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
				gameWidth = (int)((1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * (float)screenWidth);
				gameHeight = gameWidth * Game.VIEWPORT_GAME_AR_DEN / Game.VIEWPORT_GAME_AR_NUM;
			}
		}
		else {
			// Portrait.
			gameWidth = screenWidth;
			gameHeight = gameWidth * Game.VIEWPORT_GAME_AR_DEN / Game.VIEWPORT_GAME_AR_NUM;
			// Allocate enough space to the top of the game window.
			if (gameHeight > (1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * screenHeight) {
				gameHeight = (int)((1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * (float)screenHeight);
				gameWidth = gameHeight*Game.VIEWPORT_GAME_AR_NUM/Game.VIEWPORT_GAME_AR_DEN;
			}
		}
		// Floats can store ints exactly, unless too large
		// which screen dimensions are not. Mantissa 23 bit.
		return new Vector2((float)gameWidth,(float)gameHeight);
    }
    
    //TODO
    // change this when making updates and adding more fields
	@Override
	public void write(Json json) {
		// TODO Auto-generated method stub
		json.writeValue("mapWidthW",mapHeightW);
		json.writeValue("mapHeightW",mapHeightW);
		json.writeValue("backgroundImage", backgroundImage.toString());
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		mapWidthW = jsonData.get("mapWidthW").asFloat();
		mapHeightW = jsonData.get("mapHeightW").asFloat();
		backgroundImage = ETexture.valueOf(jsonData.get("backgroundImage").asString());
	}
}