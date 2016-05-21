package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import de.homelab.madgaksha.logging.Logger;

public enum EAnimation implements IResource {
	ESTELLE_RUNNING(ETexture.ESTELLE_RUNNING, 64, 128, 64, 0.1f, Animation.PlayMode.LOOP),
	ESTELLE_STANDING(ETexture.ESTELLE_STANDING, 128, 128, 40, 0.2f, Animation.PlayMode.LOOP),
	ESTELLE_SWINGING(ETexture.ESTELLE_SWINGING, 128, 128, 20, 0.1f, Animation.PlayMode.LOOP),

	JOSHUA_RUNNING(ETexture.JOSHUA_RUNNING, 64, 128, 64, 0.1f, Animation.PlayMode.LOOP);

	private final static Logger LOG = Logger.getLogger(EAnimation.class);
	private final static EnumMap<EAnimation, Animation> animationCache = new EnumMap<EAnimation, Animation>(
			EAnimation.class);

	private ETexture eTexture;
	private int tileWidth;
	private int tileHeight;
	private int count;
	private float frameDuration;
	private Animation.PlayMode playMode;

	private EAnimation(ETexture et, int tw, int th, int c, float fd, Animation.PlayMode pm) {
		eTexture = et;
		tileWidth = tw;
		tileHeight = th;
		count = c;
		frameDuration = fd;
		playMode = pm;
	}

	public static void clearAll() {
		LOG.debug("clearing all animations");
		for (EAnimation anm : animationCache.keySet()) {
			anm.clear();
		}
	}

	@Override
	public Enum<?> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_ANIMATION;
	}

	@Override
	public Animation getObject() {
		// Get texture
		final Texture texture = ResourceCache.getTexture(eTexture);
		if (texture == null)
			return null;
		final TextureRegion[] textureRegion = splitTexture(texture, tileWidth, tileHeight, count);
		final Animation animation = new Animation(frameDuration, textureRegion);
		animation.setPlayMode(playMode);
		return animation;
	}

	@Override
	public void clear() {
		LOG.debug("clearing animation: " + String.valueOf(this));
		animationCache.remove(this);

	}

	@SuppressWarnings("rawtypes")
	@Override
	public EnumMap getMap() {
		return animationCache;
	}

	private static TextureRegion[] splitTexture(Texture texture, int tileWidth, int tileHeight, int count) {
		TextureRegion region = new TextureRegion(texture);
		int x = region.getRegionX();
		int y = region.getRegionY();
		int width = region.getRegionWidth();
		int cols = width / tileWidth;

		int startX = x;
		TextureRegion[] tiles = new TextureRegion[count];
		int col = 0;
		for (int i = 0; i != count; ++i) {
			tiles[i] = new TextureRegion(texture, x, y, tileWidth, tileHeight);
			++col;
			x += tileWidth;
			if (col == cols) {
				x = startX;
				col = 0;
				y += tileHeight;
			}
		}
		return tiles;
	}

}
