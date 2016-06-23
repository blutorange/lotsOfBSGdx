package de.homelab.madgaksha.audiosystem;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.EMusic;
import de.homelab.madgaksha.util.interpolator.IInterpolatorFinished;

/**
 * Class for playing music.
 * 
 * @author madgaksha
 *
 */
public class MusicPlayer extends AAudioPlayer {
	@SuppressWarnings("unused")
	private final static Logger LOG = Logger.getLogger(MusicPlayer.class);

	// Singleton
	private static class SingletonHolder {
		private static final MusicPlayer INSTANCE = new MusicPlayer();
	}

	public static MusicPlayer getInstance() {
		return SingletonHolder.INSTANCE;
	}

	private MusicPlayer() {
		super();
	}

	private Music currentClip = null;
	private Music nextClip = null;

	private IInterpolatorFinished fadeTask1;
	private IInterpolatorFinished fadeTask2;

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
		return loadNext(music, music.defaultVolume);
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
		// Make sure all active transitions have been stopped.
		if (fadeTask1 != null) {
			fadeTask1.finished();
			fadeTask1 = null;
		}
		if (fadeTask2 != null) {
			fadeTask2.finished();
			fadeTask2 = null;
		}
		timer.clear();
		// Perform new transition.
		if (currentClip == null && nextClip != null) {
			// fade in
			currentClip = nextClip;
			nextClip = null;
			setVolume(currentClip, 0.0f);
			play(currentClip);
			fadeTask1 = fade(currentClip, time, targetLevel, new OnCompletionListener() {
				@Override
				public void onCompletion(Music music) {
					fadeTask1 = null;
				}
			});
		} else if (currentClip != null && nextClip == null) {
			// fade out
			final Music cc = currentClip;
			currentClip = null;
			play(cc);
			fadeTask1 = fade(cc, time, 0.0f, new OnCompletionListener() {
				@Override
				public void onCompletion(Music music) {
					pause(cc);
					playing = false;
					fadeTask1 = null;
				}
			});
		} else if (currentClip != null && nextClip != null) {
			// cross fade
			setVolume(nextClip, 0.0f);
			if (crossFade) {
				final Music nc = nextClip;
				final Music cc = currentClip;
				currentClip = nc;
				nextClip = null;
				play(nc);
				fadeTask1 = fade(cc, time, 0.0f, new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						stop(cc);
						fadeTask1 = null;
					}
				});
				fadeTask2 = fade(nc, time, targetLevel, new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						fadeTask2 = null;
					}
				});
			} else {
				// first fade out...
				final Music cc = currentClip;
				final Music nc = nextClip;
				currentClip = nextClip;
				nextClip = null;
				fadeTask1 = fade(cc, time * 0.5f, 0.0f, new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						// ...then fade in
						pause(cc);
						setVolume(nc, 0.0f);
						play(nc);
						fadeTask1 = null;
						fadeTask2 = fade(nc, time * 0.5f, targetLevel, new OnCompletionListener() {
							@Override
							public void onCompletion(Music music) {
								fadeTask2 = null;
							}
						});
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
	 *            The time for the transition in seconds.
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