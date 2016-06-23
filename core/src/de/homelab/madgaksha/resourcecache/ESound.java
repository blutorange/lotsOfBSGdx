package de.homelab.madgaksha.resourcecache;

import java.util.EnumMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import de.homelab.madgaksha.logging.Logger;

public enum ESound implements IResource<ESound, Sound> {
	ENEMY_SWITCH("sound/ed6se049.wav"),
	EQUIP_WEAPON("sound/ed6se022.wav"),
	EQUIP_TOKUGI("sound/ed6se155.wav"),
	ENEMY_SPAWN_FLASH("sound/ed6se579.wav", 1.470f),
	ENEMY_DIE_EXPLOSION("sound/ed6se556Multi+565.wav"),
	CANNOT_EQUIP("sound/ed6se003.wav"),
	ACTIVATE_ITEM("sound/ed6se145.wav"),
	ACQUIRE_WEAPON("sound/ed6se045.wav", 1.706f),
	BATTLE_STIGMA_APPEAR("sound/ed6se543.wav"),
	BATTLE_STIGMA_ABSORB("sound/ed6se526.wav"),
	PLAYER_EXPLODE_ON_DEATH("sound/explodePlayer.wav"),
	SCORE_BULLET_HIT("sound/ed6se020.wav", 0.1f),
	POSAUNEN_CHORUS("sound/ed6se152.wav", 6.966f),

	TEXTBOX_ADVANCE("sound/ed6se002.wav", 0.200f),
	TEXT_ADVANCE("sound/ed6se005.wav", 0.033f),

	// ===================
	// WEAPONS
	// ===================
	WEAPON_BASIC_1("sound/ed6se504+504.wav"),

	// ===================
	// VOICE
	// ===================

	// ===================
	// ESTELLE
	// ===================
	ESTELLE_YOSOMI_SITARA_BUTTOBASU_WAYO("sound/ed6t1006.wav", 1.998f),
	ESTELLE_GYAA("sound/ed6t1023.wav", 0.421f),
	ESTELLE_UERRGH("sound/ed6t1024.wav", 0.599f),
	ESTELLE_MINNA_GOMEN("sound/ed6t1025+1026.wav", 1.463f),
	ESTELLE_MADA_MADA_IKERU_WA("sound/ed6t1030.wav", 1.230f),
	ESTELLE_CHOU_DEKI("sound/ed6t1029.wav", 1.028f),
	ESTELLE_SAA_IKU_WAYO("sound/ed6t1032.wav", 1.031f),

	// ===================
	// JOSHUA
	// ===================
	JOSHUA_IKU_YO("sound/ed6t1064.wav", 0.535f),

	// ===================
	// OTHER
	// ===================
	HORA_KOCCHI_DA_ZE("sound/ed6t1252.wav", 1.192f),
	HOOORGH("sound/ed6t1257.wav", 3.657f),
	NURRGH("sound/ed6t1270.wav", 0.284f),
	UAARGH("sound/ed6t1271.wav", 0.369f),
	NURUKATTA_KA("sound/ed6t1272.wav", 1.079f),;

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
		this.duration = (int) (duration * 1000.0f);
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

	public long getDurationInMilliseconds() {
		return duration;
	}

	public float getDuration() {
		return duration / 1000.0f;
	}

	@Override
	public void clearAllOfThisKind() {
		ESound.clearAll();
	}

}