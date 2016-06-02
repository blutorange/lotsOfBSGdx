package de.homelab.madgaksha.resourcepool;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasSprite;
import com.badlogic.gdx.utils.Array;

import de.homelab.madgaksha.logging.Logger;

/**
 * Helper class to use {@link Animation}s with {@link AtlasSprite}s instead of {@link Sprite}s.
 * This class makes sure you cannot put {@link Sprite}s in the keyFrames array that are not {@link AtlasSprite}s.
 *  
 * @author madgaksha
 */
public class AtlasAnimation extends Animation {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(AtlasAnimation.class);

	public AtlasAnimation(float frameDuration, AtlasRegion... keyFrames) {
		super(frameDuration, keyFrames);
	}
	
	public AtlasAnimation (float frameDuration, Array<? extends AtlasRegion> keyFrames) {
		super(frameDuration, keyFrames);
	}

	public AtlasAnimation(float frameDuration, Array<? extends AtlasRegion> keyFrames, PlayMode playMode) {
		super(frameDuration, keyFrames, playMode);
	}
	
	public AtlasRegion getKeyFrame (float stateTime) {
		return (AtlasRegion) super.getKeyFrame(stateTime);
	}
	
	public AtlasRegion getKeyFrame (float stateTime, boolean looping) {
		return (AtlasRegion) super.getKeyFrame(stateTime, looping);
	}
	
	public AtlasRegion[] getKeyFrames(){
		return (AtlasRegion[]) super.getKeyFrames();
	}
}
