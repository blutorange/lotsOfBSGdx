package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

public final class Resources {

	private final static Logger LOG = Logger.getLogger(Resources.class);

	private Resources() {
	}

	private final static EnumMap<EMusic, Music> musicCache = new EnumMap<EMusic, Music>(EMusic.class);
	private final static EnumMap<ETexture, Texture> textureCache = new EnumMap<ETexture, Texture>(ETexture.class);

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
		BADLOGIC("badlogic.jpg"), FOOLEVEL_BACKGROUND("texture/foolevelBackground.jpg");
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

}