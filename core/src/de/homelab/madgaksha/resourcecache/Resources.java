package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

public final class Resources {

	private final static Logger LOG = Logger.getLogger(Resources.class);

	private Resources() {
	}

	private final static EnumMap<EMusic, Music> musicCache = new EnumMap<EMusic, Music>(EMusic.class);
	private final static EnumMap<ETexture, Texture> textureCache = new EnumMap<ETexture, Texture>(ETexture.class);
	private final static EnumMap<EAnimation, Animation> animationCache = new EnumMap<EAnimation, Animation>(
			EAnimation.class);
	private final static EnumMap<EAnimationList, Animation[]> animationListCache = new EnumMap<EAnimationList, Animation[]>(
			EAnimationList.class);

	// ===========================
	// BGM
	// ===========================
	/**
	 * For loading, caching and disposing {@link Music} resources.
	 * 
	 * @author madgaksha
	 */
	@AnnotationResource
	public enum EMusic implements IResource {
		TEST_WAV("music/testbgm.wav"), TEST_ADX_STEREO("music/testbgm2_2channels.adx"), TEST_ADX_MONO(
				"music/testbgm2_1channel.adx");
		private final static int LIMIT = 5;
		private String filename;

		private EMusic(String f) {
			filename = f;
		}

		public static void clearAll() {
			LOG.debug("clearing all music");
			for (EMusic bgm : musicCache.keySet()) {
				bgm.clear();
			}
		}

		@Override
		public Music getObject() {
			final FileHandle fileHandle = Gdx.files.internal(filename);
			try {
				return Gdx.audio.newMusic(fileHandle);
			} catch (GdxRuntimeException e) {
				LOG.error("could not locate or open resource: " + String.valueOf(this));
				return null;
			}
		}

		@Override
		public Enum<?> getEnum() {
			return this;
		}

		@Override
		public int getLimit() {
			return LIMIT;
		}

		@Override
		public void clear() {
			LOG.debug("disposing music: " + String.valueOf(this));
			final Music m = musicCache.get(this);
			if (m != null)
				m.dispose();
			musicCache.remove(this);
		}

		@Override
		public EnumMap<EMusic, Music> getMap() {
			return musicCache;
		}
	}

	// ===========================
	// TEXTURES
	// ===========================
	/**
	 * For loading, caching and disposing {@link Texture} resources.
	 * 
	 * @author madgaksha
	 *
	 */
	@AnnotationResource
	public enum ETexture implements IResource {
		BADLOGIC("badlogic.jpg"),
		FOOLEVEL_BACKGROUND("texture/foolevelBackground.jpg"),
		
		ESTELLE_RUNNING("texture/ch00001.png"),
		ESTELLE_STANDING("texture/ch0010b.png"),
		ESTELLE_SWINGING("texture/ch00107.png"),
		
		JOSHUA_RUNNING("texture/ch00011.png");
		
		private final static int LIMIT = 25;
		
		private String filename;
		
		private ETexture(String f) {
			filename = f;
		}

		public static void clearAll() {
			LOG.debug("clearing all textures");
			for (ETexture txt : textureCache.keySet()) {
				txt.clear();
			}
		}

		public void toSprite(Sprite sprite) {
			final Texture texture = ResourceCache.getTexture(this);
			sprite.setTexture(texture);
			sprite.setSize(texture.getWidth(), texture.getHeight());
			sprite.setRegion(0, 0, texture.getWidth(), texture.getHeight());
		}
		
		public Sprite asSprite() {
			final Sprite sprite = new Sprite();
			toSprite(sprite);
			return sprite;
		}
		
		@Override
		public Texture getObject() {
			final FileHandle fileHandle = Gdx.files.internal(filename);
			try {
				return new Texture(fileHandle);
			} catch (GdxRuntimeException e) {
				LOG.error("could not locate or open resource: " + String.valueOf(this));
				return null;
			}
		}

		@Override
		public Enum<?> getEnum() {
			return this;
		}

		@Override
		public int getLimit() {
			return LIMIT;
		}

		@Override
		public void clear() {
			LOG.debug("disposing texture: " + String.valueOf(this));
			final Texture t = textureCache.get(this);
			if (t != null)
				t.dispose();
			textureCache.remove(this);
		}

		@Override
		public EnumMap<ETexture, Texture> getMap() {
			return textureCache;
		}
	}
	
	@AnnotationResource
	public enum EAnimation implements IResource {
		ESTELLE_RUNNING(ETexture.ESTELLE_RUNNING, 64, 128, 64, 0.1f, Animation.PlayMode.LOOP),
		ESTELLE_STANDING(ETexture.ESTELLE_STANDING, 128, 128, 40, 0.2f, Animation.PlayMode.LOOP),
		ESTELLE_SWINGING(ETexture.ESTELLE_SWINGING, 128, 128, 20, 0.1f, Animation.PlayMode.LOOP),
		
		JOSHUA_RUNNING(ETexture.JOSHUA_RUNNING, 64, 128, 64, 0.1f, Animation.PlayMode.LOOP)
		;
		
		private ETexture eTexture;
		private int tileWidth;
		private int tileHeight;
		private int count;
		private float frameDuration;
		private Animation.PlayMode playMode;
		private EAnimation(ETexture et, int tw, int th, int c, float fd,Animation.PlayMode pm) {
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
			return 50;
		}
		@Override
		public Animation getObject() {
			// Get texture
			final Texture texture = ResourceCache.getTexture(eTexture);
			if (texture == null) return null;
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
	}

	
	@AnnotationResource
	public enum EAnimationList implements IResource {
		ESTELLE_RUNNING(ETexture.ESTELLE_RUNNING, 64, 128, 64, 0.1f, Animation.PlayMode.LOOP, 8),
		JOSHUA_RUNNING(ETexture.JOSHUA_RUNNING, 64, 128, 64, 0.1f, Animation.PlayMode.LOOP, 8);
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
		public Enum<?> getEnum() {
			return this;
		}
		@Override
		public int getLimit() {
			return 50;
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

	
	private static TextureRegion[] splitTexture (Texture texture, int tileWidth, int tileHeight, int count) {
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
			if (col==cols) {
				x = startX;
				col = 0;
				y += tileHeight;
			}
		}
		return tiles;
	}

}