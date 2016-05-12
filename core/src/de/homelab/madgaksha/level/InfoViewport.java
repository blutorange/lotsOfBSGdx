package de.homelab.madgaksha.level;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

/**
 * The info viewport for highscores etc.
 * 
 * @author madgaksha
 *
 */
public class InfoViewport extends Viewport {
	private boolean landscapeMode;

	public InfoViewport(int screenWidth, int screenHeight) {
		// Compute dimensions of the game window in pixels.
		Vector2 screenDimensions = GameViewport.computeGameViewportDimensions(screenWidth, screenHeight);

		// Set info window to the right / top.
		int gameWidth = (int) screenDimensions.x;
		int gameHeight = (int) screenDimensions.y;
		int infoWidth, infoHeight;
		int infoX, infoY;
		if (screenWidth > screenHeight) {
			// Landscape
			landscapeMode = true;
			infoWidth = screenWidth - gameWidth;
			infoHeight = screenHeight;
			infoX = gameWidth;
			infoY = 0;
		} else {
			landscapeMode = false;
			infoHeight = screenHeight - gameWidth;
			infoWidth = screenWidth;
			infoX = 0;
			infoY = gameHeight;
		}

		// Create a new camera with the virtual screen resolution.
		Camera camera = new OrthographicCamera(ALevel.VIEWPORT_INFO_VIRTUAL_WIDTH, ALevel.VIEWPORT_INFO_VIRTUAL_HEIGHT);

		// Connect camera.
		setCamera(camera);

		// Game window goes to the top left.
		setScreenBounds(infoX, infoY, infoWidth, infoHeight);

		// Set camera position so that it looks at the area [0.0,1.0]x[0.0,1.0].
		camera.position.set(ALevel.VIEWPORT_INFO_VIRTUAL_WIDTH * 0.5f, ALevel.VIEWPORT_INFO_VIRTUAL_HEIGHT * 0.5f,
				1.0f);

		setWorldHeight(ALevel.VIEWPORT_INFO_VIRTUAL_HEIGHT);
		setWorldWidth(ALevel.VIEWPORT_INFO_VIRTUAL_WIDTH);

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
		Vector2 screenDimensions = GameViewport.computeGameViewportDimensions(screenWidth, screenHeight);

		// Set info window to the right / top.
		int gameWidth = (int) screenDimensions.x;
		int gameHeight = (int) screenDimensions.y;
		int infoWidth, infoHeight;
		int infoX, infoY;
		if (screenWidth > screenHeight) {
			// Landscape
			landscapeMode = true;
			infoWidth = screenWidth - gameWidth;
			infoHeight = screenHeight;
			infoX = gameWidth;
			infoY = 0;
		} else {
			landscapeMode = false;
			infoHeight = screenHeight - gameWidth;
			infoWidth = screenWidth;
			infoX = 0;
			infoY = gameHeight;
		}

		// Game window goes to the top left.
		setScreenBounds(infoX, infoY, infoWidth, infoHeight);

		// Apply game dimensions.
		apply(centerCamera);
	}

	public boolean isLandscapeMode() {
		return landscapeMode;
	}
}