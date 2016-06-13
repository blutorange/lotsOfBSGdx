package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

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
	ROCK_ON_THE_ROAD("music/rockontheroad.adx", 0.2f),
	SOPHISTICATED_FIGHT("music/sophisticatedfight.adx", 0.5f),
	SILVER_WILL("music/silverwill.adx", 1.0f),
	FADING_STAR("music/fadingstar.adx", 0.7f),
	
	;

	private final static Logger LOG = Logger.getLogger(EMusic.class);
	private final static EnumMap<EMusic, Music> musicCache = new EnumMap<EMusic, Music>(EMusic.class);

	private String filename;
	public final float defaultVolume;
	
	private EMusic(String f) {
		this(f, 1.0f);
	}
	private EMusic(String f, float v) {
		filename = f;
		defaultVolume = v;
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
	
	@Override
	public void clearAllOfThisKind() {
		EMusic.clearAll();
	}
}