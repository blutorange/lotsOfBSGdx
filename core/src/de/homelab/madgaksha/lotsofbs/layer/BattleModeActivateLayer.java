package de.homelab.madgaksha.lotsofbs.layer;

import static de.homelab.madgaksha.lotsofbs.GlobalBag.batchPixel;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.game;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportGame;
import static de.homelab.madgaksha.lotsofbs.GlobalBag.viewportPixel;

import com.badlogic.gdx.math.Interpolation;

import de.homelab.madgaksha.lotsofbs.bettersprite.CroppableSprite;
import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.ETexture;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;

/**
 * Shows the cut-in when battle mode activates.
 * 
 * @author madgaksha
 *
 */
public class BattleModeActivateLayer extends ALayer {

	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(BattleModeActivateLayer.class);
	private CroppableSprite cutinSprite;
	private Interpolation interpolation;
	private float totalTime = 0.0f;
	private float durationInverse;
	private float duration;

	public BattleModeActivateLayer(Interpolation interpolation, float duration) {
		cutinSprite = ETexture.CUTIN_BATTLE_MODE_ACTIVATE.asSprite();
		cutinSprite.setOriginCenter();
		this.interpolation = interpolation;
		this.durationInverse = 1.0f / duration;
		this.duration = duration;
	}

	@Override
	public void draw(float deltaTime) {
		viewportPixel.apply();
		batchPixel.setProjectionMatrix(viewportPixel.getCamera().combined);
		batchPixel.begin();
		cutinSprite.draw(batchPixel);
		batchPixel.end();
	}

	@Override
	public void update(float deltaTime) {
		totalTime += deltaTime;
		float ratio = totalTime * durationInverse;
		if (ratio < 0.5f) {
			cutinSprite.setAlpha(interpolation.apply(0.0f, 1.0f, 2 * ratio));
		} else {
			cutinSprite.setAlpha(interpolation.apply(1.0f, 0.0f, 2.0f * (ratio - 0.5f)));
		}
		if (totalTime >= duration) {
			totalTime = duration;
			game.popLayer(this);
		}
	}

	@Override
	public void removedFromStack() {
	}

	@Override
	public void addedToStack() {
		ResourceCache.loadToRam(ETexture.CUTIN_BATTLE_MODE_ACTIVATE);
	}

	@Override
	public boolean isBlockDraw() {
		return false;
	}

	@Override
	public boolean isBlockUpdate() {
		return false;
	}

	@Override
	public void resize(int width, int height) {
		cutinSprite.setBounds(0.0f, 0.0f, viewportGame.getScreenWidth(), viewportGame.getScreenHeight());
	}
}
