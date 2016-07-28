package de.homelab.madgaksha.lotsofbs.layer;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.game;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportPixel;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.Align;

import de.homelab.madgaksha.lotsofbs.KeyMapDesktop;
import de.homelab.madgaksha.lotsofbs.i18n.I18n;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EFreeTypeFontGenerator;
import de.homelab.madgaksha.lotsofbs.resourcecache.ENinePatch;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

/**
 * A simple pause screen with the option to exit the game.
 * 
 * @author madgaksha
 */
public class PauseLayer extends ALayer {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(PauseLayer.class);
	private final static float THRESHOLD_FOR_EXIT = 2.0f; // seconds
	private final static float PAUSE_MESSAGE_FONT_SIZE = 0.03f;

	private final NinePatch background;
	private final String pauseMessage;
	private BitmapFont bitmapFont = null;
	private boolean allowInput = false;
	private boolean anyButtonPressed = false;
	private boolean exitButtonPressed = false;
	private float exitPressedTime = 0.0f;
	private boolean blockUpdate;

	public PauseLayer(boolean blockUpdate) throws IOException {
		this.blockUpdate = blockUpdate;
		background = ResourceCache.getNinePatch(ENinePatch.WHITE_RECTANGLE);
		if (background == null)
			throw new IOException("failed to load resources");
		background.setColor(new Color(0.0f, 0.0f, 0.0f, 0.5f));
		pauseMessage = I18n.gameE("screen.pause.message");
	}

	@Override
	public void removedFromStack() {
		Gdx.input.setInputProcessor(null);
		if (bitmapFont != null)
			bitmapFont.dispose();
		game.unpause();

	}

	@Override
	public void addedToStack() {
		allowInput = !isAnythingPressed();
		Gdx.input.setInputProcessor(new InputProcessor() {

			@Override
			public boolean touchUp(int screenX, int screenY, int pointer, int button) {
				anyButtonPressed = true;
				if (!exitButtonPressed) {
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
				exitButtonPressed = KeyMapDesktop.isPauseButtonPressed();
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
				if (!exitButtonPressed) {
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
				exitButtonPressed = KeyMapDesktop.isPauseButtonPressed();
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
		bitmapFont.draw(batchPixel, pauseMessage, w * 0.1f, h * 0.5f, w * 0.8f, Align.center, true);
		batchPixel.end();
	}

	@Override
	public void update(float deltaTime) {
		deltaTime = Gdx.graphics.getRawDeltaTime();
		if (exitButtonPressed) {
			if (KeyMapDesktop.isPauseButtonPressed()) {
				exitPressedTime += deltaTime;
				if (allowInput && exitPressedTime > THRESHOLD_FOR_EXIT)
					Gdx.app.exit();
			} else
				exitButtonPressed = false;
		} else {
			exitPressedTime = 0.0f;
			if (allowInput && anyButtonPressed)
				game.popLayer(this);
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

	@Override
	public void resize(int width, int height) {
		FreeTypeFontGenerator g = ResourceCache.getFreeTypeFontGenerator(EFreeTypeFontGenerator.MAIN_FONT);
		FreeTypeFontParameter p = new FreeTypeFontParameter();
		p.size = Math.max(5, (int) (viewportGame.getScreenHeight() * PAUSE_MESSAGE_FONT_SIZE));
		p.color = Color.WHITE;
		p.borderWidth = 2;
		p.borderColor = Color.BLACK;
		p.characters = EFreeTypeFontGenerator.getRequiredCharacters(pauseMessage);
		bitmapFont = g.generateFont(p);
	}

	/**
	 * @return {@link #<code>true</code>} iff any button, key, screen etc. is
	 *         pressed / touched.
	 */
	private boolean isAnythingPressed() {
		// Touchscreen keys
		if (Gdx.input.isTouched())
			return true;
		// Keyboard keys
		for (int i = 0; i != 256; ++i)
			if (Gdx.input.isKeyPressed(i))
				return true;
		// Mouse keys
		if (Gdx.input.isButtonPressed(Buttons.BACK) || Gdx.input.isButtonPressed(Buttons.FORWARD)
				|| Gdx.input.isButtonPressed(Buttons.LEFT) || Gdx.input.isButtonPressed(Buttons.MIDDLE)
				|| Gdx.input.isButtonPressed(Buttons.RIGHT))
			return true;
		return false;
	}
}
