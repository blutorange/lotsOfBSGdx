package de.homelab.madgaksha.lotsofbs.level;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.HdpiUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
	private final Vector3 shake = new Vector3(0f,0f,0f);
	private final OrthographicCamera camera;
	
	@Override
	public void apply() {
		apply(false);
	}

	public FixedGameViewport(int screenWidth, int screenHeight, boolean centerCamera) {
		Vector2 gameSize = GameViewport.computeGameViewportDimensions(screenWidth, screenHeight);

		// Create a new camera.
		camera = new OrthographicCamera(Game.VIEWPORT_GAME_AR_NUM, Game.VIEWPORT_GAME_AR_DEN);

		// Position camera to show entire world height initially.
		camera.setToOrtho(false, Game.VIEWPORT_GAME_AR, Game.VIEWPORT_GAME_AR_DEN);

		setWorldHeight(Game.VIEWPORT_GAME_AR_NUM);
		setWorldWidth(Game.VIEWPORT_GAME_AR_DEN);
		
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

		camera.setToOrtho(false, Game.VIEWPORT_GAME_AR, Game.VIEWPORT_GAME_AR_DEN);
		setCamera(camera);
		
		apply(centerCamera);
	}

	public OrthographicCamera getOrthographicCamera() {
		return (OrthographicCamera)getCamera();
	}
	
	@Override
	public void apply (boolean centerCamera) {
		HdpiUtils.glViewport(0, 0, getScreenWidth(), getScreenHeight());
		camera.viewportWidth = Game.VIEWPORT_GAME_AR_NUM;
		camera.viewportHeight = Game.VIEWPORT_GAME_AR_DEN;
		if (centerCamera) camera.position.set(Game.VIEWPORT_GAME_AR_NUM / 2f, Game.VIEWPORT_GAME_AR_DEN / 2f, 0);
		else camera.position.set(shake.x*Game.VIEWPORT_GAME_AR_NUM, shake.y*Game.VIEWPORT_GAME_AR_DEN, shake.z);
		camera.update();
	}
	
	public void apply (boolean centerCamera, Batch batch) {
		HdpiUtils.glViewport(0, 0, getScreenWidth(), getScreenHeight());
		camera.viewportWidth = Game.VIEWPORT_GAME_AR_NUM;
		camera.viewportHeight = Game.VIEWPORT_GAME_AR_DEN;
		if (centerCamera) camera.position.set(Game.VIEWPORT_GAME_AR_NUM / 2f, Game.VIEWPORT_GAME_AR_DEN / 2f, 0);
		else camera.position.set(shake.x*Game.VIEWPORT_GAME_AR_NUM, shake.y*Game.VIEWPORT_GAME_AR_DEN, shake.z);
		camera.update();
		batch.setProjectionMatrix(camera.combined);
	}

	
	public void setShake(Vector3 shake) {
		this.shake.set(shake);
	}
	
	public void setShake(float shakeX, float shakeY) {
		shake.x = shakeX;
		shake.y = shakeY;
		shake.z = 0.0f;
	}
	
	public void setShake(float shakeX, float shakeY, float shakeZ) {
		shake.x = shakeX;
		shake.y = shakeY;
		shake.z = shakeZ;
	}
}