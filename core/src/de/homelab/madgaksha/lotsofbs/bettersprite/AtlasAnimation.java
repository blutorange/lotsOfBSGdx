package de.homelab.madgaksha.lotsofbs.bettersprite;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Helper class to use {@link Animation}s with {@link AtlasRegion}s instead of
 * {@link TextureRegion}s. This class makes sure you cannot put
 * {@link TextureRegion}s in the keyFrames array that are not
 * {@link AtlasRegion}s.
 * 
 * @author madgaksha
 */
public class AtlasAnimation extends Animation {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AtlasAnimation.class);

	public AtlasAnimation(float frameDuration, AtlasRegion... keyFrames) {
		super(frameDuration, keyFrames);
	}

	public AtlasAnimation(float frameDuration, Array<? extends AtlasRegion> keyFrames) {
		super(frameDuration, keyFrames);
	}

	public AtlasAnimation(float frameDuration, Array<? extends AtlasRegion> keyFrames, PlayMode playMode) {
		super(frameDuration, keyFrames, playMode);
	}

	@Override
	public AtlasRegion getKeyFrame(float stateTime) {
		return (AtlasRegion) super.getKeyFrame(stateTime);
	}

	@Override
	public AtlasRegion getKeyFrame(float stateTime, boolean looping) {
		return (AtlasRegion) super.getKeyFrame(stateTime, looping);
	}

	@Override
	public AtlasRegion[] getKeyFrames() {
		return (AtlasRegion[]) super.getKeyFrames();
	}
}
