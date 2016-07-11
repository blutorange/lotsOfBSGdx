package de.homelab.madgaksha.lotsofbs.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import de.homelab.madgaksha.lotsofbs.bettersprite.AtlasAnimation;
import de.homelab.madgaksha.lotsofbs.logging.Logger;

/**
 * Different animations for each mode, eg. different looking directions.
 * Uses {@link TextureAtlas} in favor of simple sprite sheets.
 *
 * @author madgaksha
 *
 */
public enum EAnimationList implements IResource<EAnimationList, AtlasAnimation[]> {
	// =================
	// ESTELLE
	// =================
	ESTELLE_RUNNING(ETextureAtlas.ESTELLE, "ESTELLE_RUNNING", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	ESTELLE_STANDING(ETextureAtlas.ESTELLE, "ESTELLE_STANDING", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	ESTELLE_HIT(ETextureAtlas.ESTELLE, "ESTELLE_HIT", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	ESTELLE_ON_KNEES(ETextureAtlas.ESTELLE, "ESTELLE_ON_KNEES", 8, 0.35f, AtlasAnimation.PlayMode.NORMAL),
	
	// =================
	// JOSHUA
	// =================
	JOSHUA_STANDING(ETextureAtlas.JOSHUA, "JOSHUA_STANDING", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),

	// =================
	// ENEMIES
	// =================
	SOLDIER_RED_0(ETextureAtlas.SOLDIER_RED, "SOLDIER_RED_0", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	SOLDIER_RED_1(ETextureAtlas.SOLDIER_RED, "SOLDIER_RED_1", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	SOLDIER_GREEN_0(ETextureAtlas.SOLDIER_GREEN, "SOLDIER_GREEN_0", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	SOLDIER_GREEN_1(ETextureAtlas.SOLDIER_GREEN, "SOLDIER_GREEN_1", 8, 0.1f, AtlasAnimation.PlayMode.LOOP),
	ICER_0(ETextureAtlas.ICER, "ICER_0", 8, 0.2f, AtlasAnimation.PlayMode.LOOP),
	ICER_1(ETextureAtlas.ICER, "ICER_1", 8, 0.2f, AtlasAnimation.PlayMode.LOOP),
	;

	private final static Logger LOG = Logger.getLogger(EAnimationList.class);
	private final static EnumMap<EAnimationList, AtlasAnimation[]> animationListCache = new EnumMap<EAnimationList, AtlasAnimation[]>(
			EAnimationList.class);

	private final ETextureAtlas textureAtlas;
	private final String name;
	private final float frameDuration;
	private final AtlasAnimation.PlayMode playMode;
	private final int animationModeCount;

	private EAnimationList(ETextureAtlas textureAtlas, String name, int animationModeCount, float frameDuration, AtlasAnimation.PlayMode playMode) {
		this.playMode = playMode;
		this.animationModeCount = animationModeCount;
		this.frameDuration = frameDuration;
		this.name = name;
		this.textureAtlas = textureAtlas;
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
	public AtlasAnimation[] getObject() {
		AtlasAnimation[] atlasAnimationList;
			TextureAtlas loadedTextureAtlas = ResourceCache.getTextureAtlas(textureAtlas);
			if (loadedTextureAtlas == null) return null;
			atlasAnimationList = new AtlasAnimation[animationModeCount];
			for (int i = 0; i != animationModeCount; ++i) {
				Array<AtlasRegion> atlasRegionArray = loadedTextureAtlas.findRegions(name + "_" + i);
				if (atlasRegionArray == null || atlasRegionArray.size == 0) {
					LOG.error("atlas region empty or does not exist: " + name + "_" + i);
					return null;
				}
				AtlasAnimation animation = new AtlasAnimation(frameDuration, atlasRegionArray);
				animation.setPlayMode(playMode);
				atlasAnimationList[i] = animation;
			}
		return atlasAnimationList;
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

	@Override
	public void clearAllOfThisKind() {
		EAnimationList.clearAll();
	}
}
