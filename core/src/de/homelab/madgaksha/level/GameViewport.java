package de.homelab.madgaksha.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.Game;
import de.homelab.madgaksha.logging.Logger;

/**
 * A viewport for the game world. The game world is always located at the
 * x-y-plane. The camera will be position above at the positive z-axis and look
 * down on the x-y plane. <br>
 * The game window itself will not fill the entire screen, see
 * {@link ALevel#computeGameViewportDimensions(int, int)} for further details.
 * 
 * @author madgaksha
 *
 */
public class GameViewport extends Viewport {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(GameViewport.class);
	
	private PerspectiveCamera perspectiveCamera;
	private float tanFovyH;
	private float angleUpXY = 0.0f;
	private float angleDirXY = 0.0f;
	
	private Vector2 yPlus = new Vector2(0.0f,1.0f);
	private Vector2 u = new Vector2();
	private Vector3 v;
	
	@Override
	public void apply() {
		super.apply(false);
		angleDirXY = computeRotationDirXY();
		angleUpXY = computeRotationUpXY();
	}
	@Override
	public void apply(boolean x) {
		super.apply(x);
		angleDirXY = computeRotationDirXY();
		angleUpXY = computeRotationUpXY();
	}
	
	
	public GameViewport(int screenWidth, int screenHeight, float mapWidthW, float mapHeightW, boolean centerCamera) {
		// Compute dimensions of the game window in pixels.
		Vector2 screenDimensions = computeGameViewportDimensions(screenWidth, screenHeight);
		int gameWidth = (int) screenDimensions.x;
		int gameHeight = (int) screenDimensions.y;

		// Create a new camera.
		Camera camera = new PerspectiveCamera(ALevel.CAMERA_GAME_FIELD_OF_VIEW_Y, gameWidth, gameHeight);
		perspectiveCamera = (PerspectiveCamera)camera;
		
		// Position camera to show entire world height initially.
		float elevation = mapHeightW * 0.5f * ALevel.CAMERA_GAME_TAN_FIELD_OF_VIEW_Y_HALF_INV;
		camera.position.set(mapWidthW * 0.5f, mapHeightW * 0.5f, elevation);

		tanFovyH = (float)Math.tan(0.5*ALevel.CAMERA_GAME_FIELD_OF_VIEW_Y);
		
		setWorldHeight(mapHeightW);
		setWorldWidth(mapHeightW * Game.VIEWPORT_GAME_AR);

		// Setup clipping planes appropriately.
		camera.near = elevation * 0.05f;
		camera.far = elevation * 2.0f;

		// Now look down on the world initially.
		camera.direction.set(0.0f, 0.0f, -1.0f);
	
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
		int gameWidth = (int) screenDimensions.x;
		int gameHeight = (int) screenDimensions.y;

		// Game window goes to the bottom left.
		setScreenBounds(0, 0, gameWidth, gameHeight);

		apply(centerCamera);
	}

	/**
	 * Computes the viewport for rendering the world. Rendering proceeds as
	 * follows: 1) Render background. 2) Render in-game. 3) Render overly info.
	 * (high score, weapons, shields, etc.).
	 *
	 * For this game, the aspect ratio of the viewport will always be 8:9. Its
	 * size will be adjusted so that it fits the screen. The info viewport
	 * exists in two versions, one for portrait-like screens, and one for
	 * landscape-like screens. However, the info viewport must have a certain
	 * minimum width (landscape) or height (portrait). The game viewport must be
	 * scaled down proportionately.
	 *
	 * Minimum width/height of the info screen is given by the constants
	 * {@link ALevel.VIEWPORT_INFO_WIDTH_MIN} and
	 * {@link ALevel.VIEWPORT_INFO_HEIGHT_MIN}.
	 *
	 * Depending on the screen aspect ratio, the world and info viewport will be
	 * arranged differently on the screen.
	 *
	 * Portrait: /---------\ | INFO | | SCREEN | |_________| | | | GAME | |
	 * SCREEN | | x | | | \---------/
	 * 
	 * Landscape: /---------+------\ | | | | GAME | INFO | | SCREEN |SCREEN| | |
	 * | \---------+------/
	 *
	 *
	 * Square: /----------------\ | | | | |INFO| | GAME | | | | SCR| | SCREEN |
	 * EEN| | | | | | | | | | \----------------/
	 * 
	 * @param screenWidth
	 *            Current width of the screen in pixels.
	 * @param screenHeight
	 *            Current height of the screen in pixels.
	 * @return Width and height of the game window in pixels.
	 */
	public static Vector2 computeGameViewportDimensions(int screenWidth, int screenHeight) {
		int gameWidth, gameHeight;
		if (screenWidth > screenHeight) {
			// Landscape
			gameHeight = screenHeight;
			gameWidth = screenHeight * Game.VIEWPORT_GAME_AR_NUM / Game.VIEWPORT_GAME_AR_DEN;
			// Allocate enough space to the right of the game window.
			if (gameWidth > (1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * screenWidth) {
				gameWidth = (int) ((1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * (float) screenWidth);
				gameHeight = gameWidth * Game.VIEWPORT_GAME_AR_DEN / Game.VIEWPORT_GAME_AR_NUM;
			}
		} else {
			// Portrait.
			gameWidth = screenWidth;
			gameHeight = gameWidth * Game.VIEWPORT_GAME_AR_DEN / Game.VIEWPORT_GAME_AR_NUM;
			// Allocate enough space to the top of the game window.
			if (gameHeight > (1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * screenHeight) {
				gameHeight = (int) ((1.0f - Game.VIEWPORT_INFO_WIDTH_MIN_S) * (float) screenHeight);
				gameWidth = gameHeight * Game.VIEWPORT_GAME_AR_NUM / Game.VIEWPORT_GAME_AR_DEN;
			}
		}
		// Floats can store ints exactly, unless too large
		// which screen dimensions are not. Mantissa 23 bit.
		return new Vector2((float) gameWidth, (float) gameHeight);
	}
	
	public PerspectiveCamera getPerspectiveCamera() {
		return perspectiveCamera;
	}
	/**
	 * @return tan(fieldOfView/2)
	 */
	public float getTanFovyH() {
		return tanFovyH;
	}

	public void setRotation(float thetaZ) {
		perspectiveCamera.up.set(1.0f,0.0f,0.0f).rotate(perspectiveCamera.direction, thetaZ);
		perspectiveCamera.direction.set(0.0f,0.0f,-1.0f);
	}
	
	private float computeRotationUpXY() {
		v = perspectiveCamera.up;
		return u.set(v.x,v.y).angle(yPlus);
	}
	private float computeRotationDirXY() {
		v = perspectiveCamera.direction;
		return u.set(v.x,v.y).angle(yPlus);
	}
	
	public float getRotationUpXY() {
		return angleUpXY;
	}
	public float getRotationDirXY() {
		return angleDirXY;
	}
}