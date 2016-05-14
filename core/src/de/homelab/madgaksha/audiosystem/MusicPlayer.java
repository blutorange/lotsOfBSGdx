package de.homelab.madgaksha.audiosystem;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EMusic;

/**
 * Class for playing music.
 * 
 * @author madgaksha
 *
 */
public class MusicPlayer extends AAudioPlayer {
	private final static Logger LOG = Logger.getLogger(MusicPlayer.class);
	private Music currentClip = null;
	private Music nextClip = null;

	public MusicPlayer() {
		super();
	}

	/**
	 * Loads the next clip to be played.
	 * 
	 * @param music
	 *            Audio track to load.
	 * @param volume
	 *            Volume of the new audio track. (0.0 <= volume <= 1.0)
	 * @return Whether the file was loaded successfully.
	 */
	public boolean loadNext(EMusic music, float volume) {
		nextClip = load(music);
		if (nextClip != null)
			setVolume(nextClip, volume);
		return nextClip != null;
	}

	/**
	 * Loads the next clip to be played.
	 * 
	 * @param music
	 *            File to load.
	 * @return Whether the file was loaded successfully.
	 */
	public boolean loadNext(EMusic music) {
		return loadNext(music, 1.0f);
	}

	/**
	 * Opposite of {@link #loadNext(EMusic, float)}. Removes the track schedule
	 * next.
	 */
	public void unloadNext() {
		nextClip = null;
	}

	/**
	 * Stops the current tracks, unloads it and the next track.
	 */
	public void unload() {
		stop();
		currentClip = null;
		unloadNext();
	}

	public void stop() {
		stop(currentClip);
	}

	public void pause() {
		pause(currentClip);
	}

	public void play() {
		play(currentClip);
	}

	public void setVolume(float volume) {
		setVolume(currentClip, volume);
	}

	public float getVolume() {
		return getVolume(currentClip);
	}

	public boolean isPlaying() {
		return isPlaying(currentClip);
	}

	/**
	 * Plays the next track. Stops the current one if there is any.
	 */
	public void playNext() {
		stop();
		currentClip = nextClip;
		nextClip = null;
		play();
	}

	public void playNext(EMusic music) {
		loadNext(music);
		playNext();
	}

	public void transition(final float time, final float targetLevel) {
		if (currentClip == null && nextClip != null) {
			// fade in
			currentClip = nextClip;
			nextClip = null;
			setVolume(currentClip, 0.0f);
			play(currentClip);
			fade(currentClip, time, targetLevel);
		} else if (currentClip != null && nextClip == null) {
			// fade out
			play(currentClip);
			fade(currentClip, time, 0.0f, new OnCompletionListener() {
				@Override
				public void onCompletion(Music music) {
					pause(currentClip);
					currentClip = null;
					playing = false;
				}
			});
		} else if (currentClip != null && nextClip != null) {
			// cross fade
			setVolume(nextClip, 0.0f);
			if (crossFade) {
				final Music nc = nextClip;
				final Music cc = currentClip;
				play(nextClip);
				fade(currentClip, time, 0.0f, new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						stop(cc);
					}
				});
				fade(nextClip, time, targetLevel, new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						currentClip = nc;
						nextClip = null;
					}
				});
			} else {
				// first fade out
				fade(currentClip, time * 0.5f, 0.0f, new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						pause(currentClip);
						currentClip = nextClip;
						nextClip = null;
						setVolume(currentClip, 0.0f);
						play(currentClip);
						// then fade in
						fade(currentClip, time * 0.5f, targetLevel);
					}
				});
			}
		}
	}

	/**
	 * Stop the current clip and start playing the next clip, applying some
	 * transition. If no next clip is present, only stop playing. If no current
	 * clip is present, only start playing. Target level is set to the current
	 * level of the next clip.
	 * 
	 * @param time
	 *            The time for the transition in milliseconds.
	 */
	public void transition(float time) {
		transition(time, nextClip != null ? getVolume(nextClip) : 0.0f);
	}

	/**
	 * Should be called only once when exiting the game.
	 */
	@Override
	public void dispose() {
		super.dispose();
		currentClip = null;
		nextClip = null;
		// Disposing the Music clips is handled by the ResourceCache.
		// Its clearAll method is always called upon disposing the game.
	}
}