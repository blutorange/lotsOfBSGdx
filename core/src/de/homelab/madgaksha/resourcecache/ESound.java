package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

public enum ESound implements IResource<ESound,Sound> {
	TEXTBOX_NEXT("sound/ed6se002.wav"),
	ENEMY_SWITCH("sound/ed6se049.wav"),
	BATTLE_STIGMA_APPEAR("sound/ed6se543.wav"),
	BATTLE_STIGMA_ABSORB("sound/ed6se526.wav"),
	PLAYER_EXPLODE_ON_DEATH("sound/explodePlayer.wav"),
	
	// ===================
	//        VOICE
	// ===================
	
	// ===================
	//       ESTELLE
	// ===================
	ESTELLE_YOSOMI_SITARA_BUTTOBASU_WAYO("sound/ed6t1006.wav",1.998f),
	ESTELLE_GYAA("sound/ed6t1023.wav",0.421f),
	ESTELLE_UERRGH("sound/ed6t1024.wav",0.599f),
	ESTELLE_MINNA_GOMEN("sound/ed6t1025+1026.wav",1.463f),
	
	// ===================
	//       OTHER
	// ===================
	HORA_KOCCHI_DA_ZE("sound/ed6t1252.wav",1.192f),
	HOOORGH("sound/ed6t1257.wav",3.657f),
	NURRGH("sound/ed6t1270.wav",0.284f),
	UAARGH("sound/ed6t1271.wav",0.369f),
	NURUKATTA_KA("sound/ed6t1272.wav",1.079f),
	;

	private final static Logger LOG = Logger.getLogger(ESound.class);
	private final static EnumMap<ESound, Sound> soundCache = new EnumMap<ESound, Sound>(ESound.class);

	private String filename;
	/** Duration of the sound clip in milliseconds */
	private long duration = 0;
	
	private ESound(String fn) {
		this(fn, 0.0f);
	}
	private ESound(String fn, float duration) {
		filename = fn;
		this.duration = (int)(duration*1000.0f);
	}
	
	public static void clearAll() {
		LOG.debug("clearing all sounds");
		for (ESound sn : soundCache.keySet()) {
			sn.clear();
		}
	}

	@Override
	public Enum<ESound> getEnum() {
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
			LOG.error("could not locate or open resource: " + String.valueOf(this), e);
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

	public long getDuration() {
		return duration;
	}

}