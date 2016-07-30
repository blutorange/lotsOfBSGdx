package de.homelab.madgaksha.lotsofbs.audiosystem;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.utils.Timer;

import de.homelab.madgaksha.lotsofbs.logging.Logger;
import de.homelab.madgaksha.lotsofbs.resourcecache.EMusic;
import de.homelab.madgaksha.lotsofbs.resourcecache.ResourceCache;
import de.homelab.madgaksha.lotsofbs.util.interpolator.AInterpolator;
import de.homelab.madgaksha.lotsofbs.util.interpolator.IInterpolatorCallback;
import de.homelab.madgaksha.lotsofbs.util.interpolator.IInterpolatorFinished;
import de.homelab.madgaksha.lotsofbs.util.interpolator.LinearFloatInterpolator;

public abstract class AAudioPlayer {

	private final static Logger LOG = Logger.getLogger(AAudioPlayer.class);

	private final static float INTERPOLATOR_UPDATE_INTERVAL = 0.050f;

	private volatile boolean wasDisposed = false;

	/**
	 * Timer with multiple tasks for fading the music and other effects.
	 */
	protected final Timer timer;

	/**
	 * Whether the music is playing currently.
	 */
	protected boolean playing = false;
	/**
	 * Whether transitions should cross-fade.
	 */
	protected boolean crossFade = false;

	protected AAudioPlayer() {
		playing = false;
		crossFade = false;
		timer = new Timer();
	}

	/**
	 * How to transition between two clips. If cross-fade is enabled, fades out
	 * the current clip while fading in the next clip. When disabled, fades out
	 * the current clip and when done, fades in the the next clip.
	 * 
	 * @param xf
	 *            Whether cross fade should be enabled.
	 */
	public void setCrossFade(boolean xf) {
		crossFade = xf;
	}

	/**
	 * Fetches the {@link Music} clip from the {@link ResourceCache}.
	 * 
	 * @param bgm
	 *            The music to load.
	 * @return The music, or null if it could not be loaded.
	 */
	protected Music load(EMusic music) {
		return ResourceCache.getMusic(music);
	}

	protected void stopAllTasks() {
		timer.clear();
	}

	protected void stop(Music clip) {
		if (clip != null && !wasDisposed)
			clip.stop();
	}

	protected void pause(Music clip) {
		if (clip != null && !wasDisposed)
			clip.pause();
	}

	protected void play(Music clip) {
		if (clip != null && !wasDisposed)
			clip.play();
	}

	protected void setVolume(Music clip, float volume) {
		if (clip != null && !wasDisposed)
			clip.setVolume(volume);
	}

	protected float getVolume(Music clip) {
		return (clip != null && !wasDisposed) ? clip.getVolume() : 0.0f;
	}

	protected boolean isPlaying(Music clip) {
		return !wasDisposed && clip != null && clip.isPlaying();
	}

	/**
	 * Fades the volume to the target volume.
	 * 
	 * @param time
	 *            Time for the transition in seconds.
	 * @param targetLevel
	 *            Target volume between 0.0 (mute) and 1.0.
	 */
	protected void shiftVolume(final Music clip, float targetLevel, float time) {
		if (clip != null) {
			fade(clip, time, targetLevel);
		}
	}

	/**
	 * Fades the volume to the target volume within 300ms.
	 * 
	 * @param time
	 *            Time for the transition in milliseconds.
	 * @param targetLevel
	 *            Target volume in dB.
	 */
	protected void shiftVolume(final Music clip, float targetLevel) {
		shiftVolume(clip, targetLevel, 0.3f);
	}

	/**
	 * Changes the volume to the target volume with a custom transition
	 * function.
	 * 
	 * @param music
	 *            Clip to transition.
	 * @param time
	 *            Time for the transitions is seconds.
	 * @param targetLevel
	 *            Target volume level, between 0.0 and 1.0.
	 */
	protected IInterpolatorFinished fade(final Music music, float time, float targetLevel) {
		return fade(music, time, targetLevel, new LinearFloatInterpolator(), null);
	}

	/**
	 * Changes the volume to the target volume with a custom transition
	 * function.
	 * 
	 * @param music
	 *            Clip to transition.
	 * @param time
	 *            Time for the transitions is seconds.
	 * @param targetLevel
	 *            Target volume level, between 0.0 and 1.0.
	 * @param interpolator
	 *            Transition function.
	 */
	protected IInterpolatorFinished fade(final Music music, float time, float targetLevel,
			final AInterpolator<Float> interpolator) {
		return fade(music, time, targetLevel, interpolator, null);
	}

	/**
	 * Changes the volume to the target volume with a custom transition
	 * function.
	 * 
	 * @param music
	 *            Clip to transition.
	 * @param time
	 *            Time for the transitions is seconds.
	 * @param targetLevel
	 *            Target volume level, between 0.0 and 1.0.
	 * @param ocl
	 *            Callback when updating the volume level.
	 */
	protected IInterpolatorFinished fade(final Music music, float time, float targetLevel,
			final OnCompletionListener ocl) {
		return fade(music, time, targetLevel, new LinearFloatInterpolator(), ocl);
	}

	/**
	 * Changes the volume to the target volume with a custom transition
	 * function.
	 * 
	 * @param music
	 *            Clip to transition.
	 * @param time
	 *            Time for the transitions is seconds.
	 * @param targetLevel
	 *            Target volume level, between 0.0 and 1.0.
	 * @param interpolator
	 *            Transition function.
	 * @param ocl
	 *            Callback when updating the volume level.
	 */
	protected IInterpolatorFinished fade(final Music music, final float time, final float targetLevel,
			final AInterpolator<Float> interpolator, final OnCompletionListener ocl) {
		if (music == null || wasDisposed)
			return null;
		final float currentLevel = music.getVolume();
		interpolator.setRange(currentLevel, targetLevel);
		final IInterpolatorCallback<Float> timerCallback = new IInterpolatorCallback<Float>() {
			@Override
			public void valueUpdated(Float val) {
				music.setVolume(val);
			}

			@Override
			public void finished(Float val) {
				music.setVolume(val);
				if (ocl != null)
					ocl.onCompletion(music);
			}

			@Override
			public void finished() {
				finished(targetLevel);
			}
		};
		final Timer.Task fadeTask = interpolator.run(timer, time, INTERPOLATOR_UPDATE_INTERVAL, timerCallback);
		if (fadeTask != null)
			fadeTask.run();
		return timerCallback;
	}

	protected void dispose() {
		wasDisposed = true;
		LOG.debug("disposing audio player " + this);
		stopAllTasks();
	}
}
