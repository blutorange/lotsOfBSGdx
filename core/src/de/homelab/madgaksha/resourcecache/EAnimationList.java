package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcepool.AtlasAnimation;

/**
 * Different animations for each mode, eg. different looking directions.
 * Can be initialized with a sprite sheet, in which case it must start
 * at the top-left.
 * <br><br>
 * Alternatively, it can be initialized with a list of {@link EAnimation}.
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

	private ETextureAtlas textureAtlas;
	private ETexture texture;
	private String name;
	private int tileWidth;
	private int tileHeight;
	private int frameCount;
	private float frameDuration;
	private AtlasAnimation.PlayMode playMode;
	private int animationModeCount;

	private EAnimationList(ETextureAtlas textureAtlas, String name, int animationModeCount, float frameDuration, AtlasAnimation.PlayMode playMode) {
		this.playMode = playMode;
		this.animationModeCount = animationModeCount;
		this.frameDuration = frameDuration;
		this.name = name;
		this.textureAtlas = textureAtlas;
	}

	
	private EAnimationList(ETexture et, int tw, int th, float fd, AtlasAnimation.PlayMode pm, int amc, int fc) {
		texture = et;
		tileWidth = tw;
		tileHeight = th;
		frameCount = fc;
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
	public AtlasAnimation[] getObject() {
		AtlasAnimation[] atlasAnimationList;
		// ETextureAtlas
		if (textureAtlas != null) {
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
		}
		// ETexture
		else {
			// Retrieve sprite sheet.
			AtlasRegion atlasRegion = ResourceCache.getTexture(texture);
			if (atlasRegion == null)
				return null;
			// Split sprite sheet.
			AtlasRegion[][] atlasRegionArray = splitAtlasRegion(atlasRegion);
			atlasAnimationList = new AtlasAnimation[animationModeCount];
			for (int i = 0; i != animationModeCount; ++i) {
				atlasAnimationList[i] = new AtlasAnimation(frameDuration, atlasRegionArray[i]);
				atlasAnimationList[i].setPlayMode(playMode);
			}			
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

	private AtlasRegion[][] splitAtlasRegion(AtlasRegion atlasRegion) {
		int width = atlasRegion.getRegionWidth();
		int height = atlasRegion.getRegionHeight();
		// Origin of the coordinate system is to the top left.
		// Sprite sheets (should) should start on the top-left as well.
		int x = atlasRegion.getRegionX();
		int y = atlasRegion.getRegionY();

		int rows = Math.min(animationModeCount, height / tileHeight);
		int cols = Math.min(frameCount, width / tileWidth);

		int startX = x;
		AtlasRegion[][] tiles = new AtlasRegion[rows][cols];
		for (int row = 0; row < rows; row++, y += tileHeight) {
			x = startX;
			for (int col = 0; col < cols; col++, x += tileWidth) {
				tiles[row][col] = new AtlasRegion(atlasRegion.getTexture(), x, y, tileWidth, tileHeight);
			}
		}

		return tiles;
	}

	@Override
	public void clearAllOfThisKind() {
		EAnimationList.clearAll();
	}
}
