package de.homelab.madgaksha.lotsofbs.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.lotsofbs.Game;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * A viewport for the game world where you can always draw at the area
 * (0..8)x(0..9) or (-4.0f..4.0f)x(-4.5f..4.5f), depending on whether
 * setCamera was set to false or true, respectively.
 * 
 * @author madgaksha
 *
 */
public class FixedGameViewport extends Viewport {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(FixedGameViewport.class);

	@Override
	public void apply() {
		apply(false);
	}

	public FixedGameViewport(int screenWidth, int screenHeight, boolean centerCamera) {
		Vector2 gameSize = GameViewport.computeGameViewportDimensions(screenWidth, screenHeight);

		// Create a new camera.
		Camera camera= new OrthographicCamera(Game.VIEWPORT_GAME_AR_NUM, Game.VIEWPORT_GAME_AR_DEN);

		// Position camera to show entire world height initially.
		camera.lookAt(Game.VIEWPORT_GAME_AR_NUM/2f, Game.VIEWPORT_GAME_AR_DEN/2f, 0f);

		setWorldHeight(Game.VIEWPORT_GAME_AR_NUM);
		setWorldWidth(Game.VIEWPORT_GAME_AR_DEN);

		// Setup clipping planes appropriately.
		camera.near = 0.5f;
		camera.far = 1.5f;

		// Connect camera.
		setCamera(camera);

		// Game window goes to the bottom left.
		setScreenBounds(0, 0, (int)gameSize.x, (int)gameSize.y);

		apply(centerCamera);
	}

	@Override
	public void update(int screenWidth, int screenHeight, boolean centerCamera) {
		// Compute dimensions of the game window in pixels.
		Vector2 gameSize = GameViewport.computeGameViewportDimensions(screenWidth, screenHeight);

		// Game window goes to the bottom left.
		setScreenBounds(0, 0, (int)gameSize.x, (int)gameSize.y);

		apply(centerCamera);
	}

	public OrthographicCamera getOrthographicCamera() {
		return (OrthographicCamera)getCamera();
	}
}