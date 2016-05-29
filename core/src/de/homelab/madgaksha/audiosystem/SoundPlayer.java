package de.homelab.madgaksha.audiosystem;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * Class for playing sound effects.
 * 
 * Multiple sound effects may need to be played more than once. This class
 * keeps track of how many instances of one sound are playing and
 * opens reads to RAM only as much as necessary. 
 * 
 * @author madgaksha
 *
 */
public class SoundPlayer extends AAudioPlayer {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(SoundPlayer.class);

	private final Map<Long,Sound> soundMap = new HashMap<Long,Sound>();
	
	// Singleton
	private static class SingletonHolder {
		private static final SoundPlayer INSTANCE = new SoundPlayer();
	}
	public static SoundPlayer getInstance() {
		return SingletonHolder.INSTANCE;
	}
	protected SoundPlayer() {
		super();
	}

	public long play(ESound sound) {
		return play(sound, PlayMode.NORMAL, 1.0f, 1.0f, 0.0f);
	}
	public long play(ESound sound, PlayMode mode) {
		return play(sound, mode, 1.0f, 1.0f, 0.0f);
	}
	public long play(ESound sound, PlayMode mode, float volume) {
		return play(sound, mode, volume, 1.0f, 0.0f);
	}
	public long play(ESound sound, PlayMode mode, float volume, float pitch) {
		return play(sound, mode, volume, pitch, 0.0f);
	}
	public long play(ESound sound, float volume) {
		return play(sound, PlayMode.NORMAL, volume, 1.0f, 0.0f);
	}
	public long play(ESound sound, float volume, float pitch) {
		return play(sound, PlayMode.NORMAL, volume, pitch, 0.0f);
	}
	public long play(ESound sound, float volume, float pitch, float pan) {
		return play(sound, PlayMode.NORMAL, volume, pitch, pan);
	}	
	public long play(ESound sound, PlayMode mode, float volume, float pitch, float pan) {
		final Sound s = ResourceCache.getSound(sound);
		if (s == null) return -1;
		final long id = mode == PlayMode.LOOP ? s.loop(volume, pitch, pan) : s.play(volume, pitch, pan); 
		soundMap.put(id, s);
		return id; 
	}

	public void stop(long id) {
		if (soundMap.containsKey(id)) {
			soundMap.get(id).stop(id);
		}
	}	
	public void stopAll() {
		for (Sound sound : soundMap.values()) {
			sound.stop();
		}
		soundMap.clear();
	}
	
	/**
	 * Should be called only once when exiting the game.
	 */
	@Override
	public void dispose() {
		stopAll();
		super.dispose();
		// Disposing the sound clips is handled by the ResourceCache.
		// Its clearAll method is always called upon disposing the game.
	}
}