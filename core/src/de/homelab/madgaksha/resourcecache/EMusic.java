package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

/**
 * For loading, caching and disposing {@link Music} resources.
 * 
 * @author madgaksha
 */
public enum EMusic implements IResource {
	TEST_WAV("music/testbgm.wav"), TEST_ADX_STEREO("music/testbgm2_2channels.adx"), TEST_ADX_MONO(
			"music/testbgm2_1channel.adx");

	private final static Logger LOG = Logger.getLogger(EMusic.class);
	private final static EnumMap<EMusic, Music> musicCache = new EnumMap<EMusic, Music>(EMusic.class);

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
		return ResourceCache.LIMIT_MUSIC;
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