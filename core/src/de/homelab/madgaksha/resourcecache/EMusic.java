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
public enum EMusic implements IResource<EMusic,Music> {
	// ==================
	//         BGM
	// ==================
	ROCK_ON_THE_ROAD("music/rockontheroad.adx"),
	SOPHISTICATED_FIGHT("music/sophisticatedfight.adx"),
	SILVER_WILL("music/silverwill.adx"),
	
	// ===================
	//       ESTELLE
	// ===================
	ESTELLE_YOSOMI_SITARA_BUTTOBASU_WAYO("sound/ed6t1006.wav"),
	ESTELLE_GYAA("sound/ed6t1023.wav"),
	ESTELLE_UERRGH("sound/ed6t1024.wav"),
	ESTELLE_MINNA_GOMEN("sound/ed6t1025+1026.wav"),
	
	// ===================
	//       OTHER
	// ===================
	HORA_KOCCHI_DA_ZE("sound/ed6t1252.wav"),
	HOOORGH("sound/ed6t1257.wav"),
	NURRGH("sound/ed6t1270.wav"),
	UAARGH("sound/ed6t1271.wav"),
	NURUKATTA_KA("sound/ed6t1272.wav"),
	
	;

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
		} catch (Exception e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
			return null;
		}
	}

	@Override
	public Enum<EMusic> getEnum() {
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