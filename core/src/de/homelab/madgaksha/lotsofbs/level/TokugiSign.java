package de.homelab.madgaksha.lotsofbs.level;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool.Poolable;

import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableAtlasSprite;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;

public class TokugiSign implements Poolable {
	private final static float DURATION_IN = 0.5f;
	private final static float DURATION_MID = 1.5f;
	private final static float DURATION_OUT = 0.5f;

	private final static float DURATION_IN_INVERSE = 1.0f / DURATION_IN;
	// private final static float DURATION_MID_INVERSE = 1.0f/DURATION_MID;
	private final static float DURATION_OUT_INVERSE = 1.0f / DURATION_OUT;

	private final static float OFFSET_IN = 0.2f;
	private final static float OFFSET_OUT = 0.2f;

	private static enum Mode {
		SETUP,
		IN,
		MID,
		OUT,
		FIN;
	}

	private final static Interpolation INTERPOLATION_IN = Interpolation.sineOut;
	private final static Interpolation INTERPOLATION_OUT = Interpolation.sineOut;

	private final static float POSITION_X = 0.5f;
	private final static float POSITION_Y = 0.92f;
	private final static float HALFHEIGHT = 0.06f;

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(TokugiSign.class);
	private final Vector2 centerPosition = new Vector2();
	private Mode mode;
	private CroppableAtlasSprite sign;
	private float totalTime;

	public TokugiSign() {
		mode = Mode.SETUP;
	}

	public TokugiSign(ETexture texture) {
		this();
		setup(texture);
	}

	public void setup(ETexture texture) {
		sign = texture.asSprite();
		mode = Mode.IN;
		totalTime = 0.0f;
		resize();
	}

	@Override
	public void reset() {
		mode = Mode.SETUP;
		sign = null;
		totalTime = 0.0f;
		centerPosition.set(0f, 0f);
	}

	public void begin() {
	}

	@SuppressWarnings("incomplete-switch")
	public boolean update(float deltaTime) {
		totalTime += deltaTime;
		float x = centerPosition.x;
		float opacity = 1.0f;
		switch (mode) {
		case IN:
			if (totalTime >= DURATION_IN) {
				mode = Mode.MID;
				totalTime = 0.0f;
			} else {
				opacity = INTERPOLATION_IN.apply(totalTime * DURATION_IN_INVERSE);
				x += (1.0f - opacity) * OFFSET_IN * viewportGame.getScreenWidth();
			}
			break;
		case MID:
			if (totalTime >= DURATION_MID) {
				mode = Mode.OUT;
				totalTime = 0.0f;
			}
			break;
		case OUT:
			if (totalTime >= DURATION_OUT) {
				mode = Mode.FIN;
				return false;
			}
			float alpha = INTERPOLATION_OUT.apply(totalTime * DURATION_OUT_INVERSE);
			x -= OFFSET_OUT * alpha * viewportGame.getScreenWidth();
			opacity = 1.0f - alpha;
			break;
		default:
			break;

		}
		sign.setCenter(x, centerPosition.y);
		sign.setAlpha(opacity);
		return true;
	}

	public void resize() {
		centerPosition.set(POSITION_X * viewportGame.getScreenWidth(), viewportGame.getScreenHeight() * POSITION_Y);
		sign.setScale((2.0f * viewportGame.getScreenHeight() * HALFHEIGHT) / sign.getHeight());
	}

	@SuppressWarnings("incomplete-switch")
	public void render() {
		switch (mode) {
		case IN:
		case MID:
		case OUT:
			sign.draw(batchPixel);
		}
	}
}
