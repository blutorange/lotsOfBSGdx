package de.homelab.madgaksha.audiosystem;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

import de.homelab.madgaksha.resourcecache.ResourceCache;
import de.homelab.madgaksha.resourcecache.Resources.Ebgm;
import de.homelab.madgaksha.util.interpolator.AInterpolator;
import de.homelab.madgaksha.util.interpolator.IInterpolatorCallback;
import de.homelab.madgaksha.util.interpolator.LinearFloatInterpolator;

public abstract class AAudioPlayer {
	
	protected static boolean playing = false;
	protected static boolean crossFade = false;
	private final static int INTERPOLATOR_UPDATE_INTERVAL = 50;
			
		
	/**
	 * How to transition between two clips.
	 * If cross-fade is enabled, fades out the current clip while fading in the next clip.
	 * When disabled, fades out the current clip and when done, fades in the the next clip.
	 * @param xf Whether cross fade should be enabled.
	 */
	public static void setCrossFade(boolean xf) {
		crossFade = xf;
	}
	
	/**
	 * Loads the specified music. Only for internal use, this does manage the clips.
	 * @param bgm The music to load.
	 * @return The loaded music.
	 */
	protected static Music load(Ebgm bgm) {
		return ResourceCache.getBgm(bgm);
	}
	
	/**
	 * Fades the volume to the target volume.
	 * @param time Time for the transition in milliseconds.
	 * @param targetLevel Target volume in dB.
	 */
	public static void shiftVolume(final Music clip, float targetLevel, double time) {
		if (clip != null) {
			fade(clip, time, targetLevel);
		}
	}
	/**
	 * Fades the volume to the target volume within 300ms.
	 * @param time Time for the transition in milliseconds.
	 * @param targetLevel Target volume in dB.
	 */
	public static void shiftVolume(final Music clip, float targetLevel) {
		shiftVolume(clip, targetLevel, 300.0d);
	}
	

	public static void fade(final Music music, double time, float targetLevel) {
		fade(music, time, targetLevel, new LinearFloatInterpolator(), null);
	}	
	public static void fade(final Music music, double time, float targetLevel, final AInterpolator<Float> interpolator) {
		fade(music, time, targetLevel, interpolator, null);
	}
	public static void fade(final Music music, double time, float targetLevel, final OnCompletionListener ocl) {
		fade(music, time,targetLevel, new LinearFloatInterpolator(), ocl);
	}

	/**
	 * Changes the volume to the target volume with a custom transition function. 
	 * @param clip
	 * @param time
	 * @param targetLevel
	 * @param interpolator
	 * @param ocl
	 */
	public static void fade(final Music clip, double time, float targetLevel, final AInterpolator<Float> interpolator, final OnCompletionListener ocl) {
		final float currentLevel = clip.getVolume();
		interpolator.setRange(currentLevel, targetLevel);
		interpolator.run(time, INTERPOLATOR_UPDATE_INTERVAL, new IInterpolatorCallback<Float>() {
			@Override
			public void valueUpdated(Float val) {
				clip.setVolume(val);
			}

			@Override
			public void finished(Float val) {
				clip.setVolume(val);
				if (ocl != null) ocl.onCompletion(clip);
			}
		});
	}
}
