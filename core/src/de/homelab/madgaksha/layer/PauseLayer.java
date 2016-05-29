package de.homelab.madgaksha.layer;

import static de.homelab.madgaksha.GlobalBag.batchPixel;
import static de.homelab.madgaksha.GlobalBag.game;
import static de.homelab.madgaksha.GlobalBag.viewportGame;
import static de.homelab.madgaksha.GlobalBag.viewportPixel;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.KeyMap;
import de.homelab.madgaksha.i18n.i18n;
import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EBitmapFont;
import de.homelab.madgaksha.resourcecache.ENinePatch;
import de.homelab.madgaksha.resourcecache.ResourceCache;

public class PauseLayer extends ALayer {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PauseLayer.class);
	private final static float THRESHOLD_FOR_EXIT = 2.0f; //seconds
	private final NinePatch background;
	private final String pauseMessage;
	private boolean allowInput = false;
	private boolean anyButtonPressed = false;
	private boolean exitButtonJustPressed = false;
	private float exitPressedTime = 0.0f;
	private boolean blockUpdate;

	public PauseLayer(boolean blockUpdate) throws IOException {
		this.blockUpdate = blockUpdate;
		background = ResourceCache.getNinePatch(ENinePatch.PAUSE_LAYER_OVERLAY);
		if (background == null)
			throw new IOException("failed to load resources");
		background.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		pauseMessage = i18n.gameE("screen.pause.message");
	}

	@Override
	public void removedFromStack() {
		game.unpause();
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void addedToStack() {
		allowInput = false;
		Gdx.input.setInputProcessor(new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				anyButtonPressed = true;
				if (!exitButtonJustPressed) {
					allowInput = true;
					anyButtonPressed = false;
				}
				return true;
			}

			@Override
			public boolean touchDragged(int screenX, int screenY, int pointer) {
				anyButtonPressed = true;
				return true;
			}

			@Override
			public boolean touchDown(int screenX, int screenY, int pointer, int button) {
				anyButtonPressed = true;
				exitButtonJustPressed = KeyMap.isPauseButtonPressed();
				return true;
			}

			@Override
			public boolean scrolled(int amount) {
				return true;
			}

			@Override
			public boolean mouseMoved(int screenX, int screenY) {
				return true;
			}

			@Override
			public boolean keyUp(int keycode) {
				if (!exitButtonJustPressed) {
					allowInput = true;
					anyButtonPressed = false;
				}

				return true;
			}

			@Override
			public boolean keyTyped(char character) {
				return true;
			}

			@Override
			public boolean keyDown(int keycode) {
				anyButtonPressed = true;
				exitButtonJustPressed = KeyMap.isPauseButtonPressed();
				return true;
			}
		});
	}

	@Override
	public void draw(float deltaTime) {
		viewportPixel.apply();
		batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);
		batchPixel.begin();
		float w = viewportGame.getScreenWidth();
		float h = viewportGame.getScreenHeight();
		background.draw(batchPixel, 0.0f, 0.0f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		BitmapFont bitmapFont = ResourceCache.getBitmapFont(EBitmapFont.MAIN_FONT);
		bitmapFont.setColor(Color.WHITE);
		bitmapFont.draw(batchPixel, pauseMessage, w*0.1f, h * 0.5f, w*0.8f,
				Align.center, true);

		batchPixel.end();
	}

	@Override
	public void update(float deltaTime) {
		if (exitButtonJustPressed) {
			if (KeyMap.isPauseButtonPressed()) {
				exitPressedTime += deltaTime;
				if (allowInput && exitPressedTime > THRESHOLD_FOR_EXIT) Gdx.app.exit();
			}
			else exitButtonJustPressed = false;
		}
		else {
			exitPressedTime = 0.0f;
			if (allowInput && anyButtonPressed) game.popLayer(this);
		}
		anyButtonPressed = false;
	}

	@Override
	public boolean isBlockDraw() {
		return false;
	}

	@Override
	public boolean isBlockUpdate() {
		return blockUpdate;
	}
	
	public void disableInputThisFrame() {
		allowInput = false;
	}

	public void setBlockUpdate(boolean block) {
		this.blockUpdate = block;		
	}
}
