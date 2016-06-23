package de.homelab.madgaksha.audiosystem;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.TimeUtils;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.ESound;
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

	private Sound currentClip;
	private long duration;
	private long startTime = TimeUtils.millis();

	public VoicePlayer() {
		super();
	}

	public boolean play(ESound sound) {
		return play(sound, 1.0f, 0.0f, 1.0f, true);
	}

	public boolean play(ESound sound, boolean stop) {
		return play(sound, 1.0f, 0.0f, 1.0f, stop);
	}

	public boolean play(ESound sound, float volume) {
		return play(sound, volume, 0.0f, 1.0f, true);
	}

	public boolean play(ESound sound, float volume, boolean stop) {
		return play(sound, volume, 0.0f, 1.0f, stop);
	}

	public boolean play(ESound sound, float volume, float pan) {
		return play(sound, volume, pan, 1.0f, true);
	}

	public boolean play(ESound sound, float volume, float pan, float pitch) {
		return play(sound, volume, pan, pitch, true);
	}

	public boolean play(ESound sound, float volume, float pan, float pitch, boolean stop) {
		if (currentClip != null) {
			if (isPlaying())
				return false;
			else if (stop)
				currentClip.stop();
		}
		final Sound s = ResourceCache.getSound(sound);
		if (s == null)
			return false;
		currentClip = s;
		duration = sound.getDurationInMilliseconds();
		startTime = TimeUtils.millis();
		s.play(volume, pitch, pan);
		return true;
	}

	private boolean isPlaying() {
		return TimeUtils.millis() - startTime < duration;
	}

	public void playUnconditionally(ESound sound) {
		if (currentClip != null)
			currentClip.stop();
		currentClip = null;
		play(sound, 1.0f, 0.0f, 1.0f);
	}

	public void stop() {
		if (currentClip != null)
			currentClip.stop();
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