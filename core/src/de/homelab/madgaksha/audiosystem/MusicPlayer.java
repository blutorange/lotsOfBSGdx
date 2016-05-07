package de.homelab.madgaksha.audiosystem;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;

import de.homelab.madgaksha.resourcecache.Resources.Ebgm;

/**
 * Static class for playing music.
 * @author madgaksha
 *
 */
public class MusicPlayer extends AAudioPlayer {
	private MusicPlayer(){}
	private static Music currentClip = null;
	private static Music nextClip = null;

	
	/**
	 * Loads the next clip to be played.
	 * @param file
	 * @return
	 */
	public static Music loadNext(Ebgm bgm) {
		nextClip = load(bgm);
		return nextClip;
	}
	
	public static void transition(final double time, final float targetLevel) {
		if (currentClip == null && nextClip != null) {
			// fade in
			currentClip = nextClip;
			nextClip = null;
			currentClip.setVolume(0.0f);
			currentClip.play();
			fade(currentClip, time, targetLevel);
		}
		else if (currentClip != null && nextClip == null) {
			// fade out
			currentClip.play();
			fade(currentClip, time, 0.0f, new OnCompletionListener() {
				@Override
				public void onCompletion(Music music) {
					currentClip.pause();
					currentClip.dispose();
					currentClip = null;
					playing = false;
				}
			});
		}
		else if (currentClip != null && nextClip != null) {
			// cross fade
			nextClip.setVolume(0.0f);
			if (crossFade) {
				final Music nc = nextClip;
				final Music cc = currentClip;
				nextClip.play();
				fade(currentClip, time, 0.0f, new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						cc.pause();
						cc.dispose();
					}
				});
				fade(nextClip, time, targetLevel,new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						currentClip = nc;
						nextClip = null;
					}
				});
			}
			else {
				// first fade out
				fade(currentClip, time*0.5, 0.0f, new OnCompletionListener() {
					@Override
					public void onCompletion(Music music) {
						currentClip.pause();
						currentClip.dispose();
						currentClip = nextClip;
						nextClip = null;
						currentClip.setVolume(0.0f);
						currentClip.play();
						// then fade in
						fade(currentClip, time*0.5, targetLevel);						
					}
				});
			}
		}
	}
	
	/**
	 * Stop the current clip and start playing the next clip, applying some transition. 
	 * If no next clip is present, only stop playing.
	 * If no current clip is present, only start playing.
	 * Target level is set to the current level of the next clip.
	 * @param time The time for the transition in milliseconds.
	 */
	public static void transition(double time) {
		transition(time, nextClip != null ? nextClip.getVolume() : 0.0f);
	}
}