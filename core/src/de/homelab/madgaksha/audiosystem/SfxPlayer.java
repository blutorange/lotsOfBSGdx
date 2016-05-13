package de.homelab.madgaksha.audiosystem;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;

import de.homelab.madgaksha.logging.Logger;
import de.homelab.madgaksha.resourcecache.Resources.EMusic;

/**
 * Class for playing music.
 * 
 * @author madgaksha
 *
 */
public class SfxPlayer extends AAudioPlayer {
	private final static Logger LOG = Logger.getLogger(SfxPlayer.class);
	private Music currentClip = null;
	private Music nextClip = null;

	public SfxPlayer() {
		super();
	}

	public boolean play(ESfx sfx, float volume, PlayMode mode) {
		Gdx.audio.newSound(null);
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