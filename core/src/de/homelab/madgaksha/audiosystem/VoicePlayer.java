package de.homelab.madgaksha.audiosystem;

import com.badlogic.gdx.audio.Music;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.resourcecache.ResourceCache;

/**
 * Class for playing voice clips. Only once voice clip can be played at a time.
 * 
 * @author madgaksha
 *
 */
public class VoicePlayer extends AAudioPlayer {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(VoicePlayer.class);

	private Music currentClip;

	public VoicePlayer() {
		super();
	}

	public boolean play(EMusic sound) {
		return play(sound, 1.0f, 0.0f);
	}
	public boolean play(EMusic sound, float volume) {
		return play(sound, volume, 1.0f);
	}
	public boolean play(EMusic music, float volume, float pan) {
		if (currentClip != null) {
			if (currentClip.isPlaying()) return false;
			else currentClip.stop();
		}
		final Music m = ResourceCache.getMusic(music);
		if (m == null) return false;
		m.setPan(pan, volume);
		m.play();
		return true;
	}

	public void playUnconditionally(EMusic sound) {
		if (currentClip != null) currentClip.stop();
		currentClip = null;
		play(sound, 1.0f, 0.0f);
	}

	public void stop() {
		if (currentClip != null) currentClip.stop();
	}	
	
	/**
	 * Should be called only once when exiting the game.
	 */
	@Override
	public void dispose() {
		stop();
		super.dispose();
	}

}