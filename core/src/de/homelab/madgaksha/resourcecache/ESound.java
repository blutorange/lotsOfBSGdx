package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

public enum ESound implements IResource {
	TEST_SOUND("sound/test.wav");

	private final static Logger LOG = Logger.getLogger(ESound.class);
	private final static EnumMap<ESound, Sound> soundCache = new EnumMap<ESound, Sound>(ESound.class);

	private String filename;

	private ESound(String fn) {
		filename = fn;
	}

	public static void clearAll() {
		LOG.debug("clearing all sounds");
		for (ESound sn : soundCache.keySet()) {
			sn.clear();
		}
	}

	@Override
	public Enum<?> getEnum() {
		return this;
	}

	@Override
	public int getLimit() {
		return ResourceCache.LIMIT_SOUND;
	}

	@Override
	public Sound getObject() {
		final FileHandle fileHandle = Gdx.files.internal(filename);
		try {
			return Gdx.audio.newSound(fileHandle);
		} catch (GdxRuntimeException e) {
			LOG.error("could not locate or open resource: " + String.valueOf(this));
			return null;
		}
	}

	@Override
	public void clear() {
		LOG.debug("clearing sound: " + String.valueOf(this));
		final Sound s = soundCache.get(this);
		if (s != null)
			s.dispose();
		soundCache.remove(this);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public EnumMap getMap() {
		return soundCache;
	}

}