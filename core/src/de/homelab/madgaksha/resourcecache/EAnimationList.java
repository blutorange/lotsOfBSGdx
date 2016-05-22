package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.homelab.madgaksha.logging.Logger;

public enum EAnimationList implements IResource<EAnimationList,Animation[]> {
	// =================
	//      ESTELLE
	// =================
	ESTELLE_RUNNING(ETexture.ESTELLE_RUNNING, 64, 128, 64, 0.1f, Animation.PlayMode.LOOP, 8),
	ESTELLE_STANDING(ETexture.ESTELLE_STANDING, 128, 128, 64, 0.1f, Animation.PlayMode.LOOP, 8),
	
	// =================
	//      ESTELLE
	// =================
	JOSHUA_RUNNING(ETexture.JOSHUA_RUNNING, 64, 128, 64, 0.1f, Animation.PlayMode.LOOP, 8),
	
	// =================
	//      ENEMIES
	// =================
	SOLDIER_RED_0(ETexture.SOLIDER_RED_0, 128, 128, 64, 0.1f, Animation.PlayMode.LOOP, 8),
	
	;
	
	private final static Logger LOG = Logger.getLogger(EAnimationList.class);
	private final static EnumMap<EAnimationList, Animation[]> animationListCache = new EnumMap<EAnimationList, Animation[]>(
			EAnimationList.class);

	
	private ETexture eTexture;
	private int tileWidth;
	private int tileHeight;
	private int count;
	private float frameDuration;
	private Animation.PlayMode playMode;
	private int animationModeCount;
	private EAnimationList(ETexture et, int tw, int th, int c, float fd,Animation.PlayMode pm, int amc) {
		eTexture = et;
		tileWidth = tw;
		tileHeight = th;
		count = c;
		frameDuration = fd;
		playMode = pm;
		animationModeCount = amc;
	}
	public static void clearAll() {
		LOG.debug("clearing all animation lists");
		for (EAnimationList al : animationListCache.keySet()) {
			al.clear();
		}
	}
	@Override
	public Enum<EAnimationList> getEnum() {
		return this;
	}
	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_ANIMATION_LIST;
	}
	@Override
	public Animation[] getObject() {
		// Get texture
		final Texture texture = ResourceCache.getTexture(eTexture);
		if (texture == null) return null;
		final TextureRegion[][] textureRegion = TextureRegion.split(texture, tileWidth, tileHeight);
		final Animation[] animationList = new Animation[animationModeCount];
		for (int i = 0; i != animationModeCount; ++i) {
			animationList[i] = new Animation(frameDuration, textureRegion[i]);
			animationList[i].setPlayMode(playMode);
		}
		return animationList;
	}
	@Override
	public void clear() {
		LOG.debug("clearing animation list: " + String.valueOf(this));
		animationListCache.remove(this);
	}
	@SuppressWarnings("rawtypes")
	@Override
	public EnumMap getMap() {
		return animationListCache;
	}		
}
