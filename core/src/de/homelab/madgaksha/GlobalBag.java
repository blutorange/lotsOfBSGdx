package de.homelab.madgaksha;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

import de.homelab.madgaksha.audiosystem.MusicPlayer;
import de.homelab.madgaksha.audiosystem.SoundPlayer;
import de.homelab.madgaksha.level.ALevel;
import de.homelab.madgaksha.level.GameViewport;
import de.homelab.madgaksha.level.InfoViewport;
import de.homelab.madgaksha.player.APlayer;

/**
 * Holds global (static) game data. Objects of which only
 * one instance will be created.
 * 
 * Better than putting it all inside the {@Game} class.
 * 
 * Also useful for sharing data between different entity system etc.
 * 
 * @author madgaksha
 *
 */
public final class GlobalBag {
	private GlobalBag(){}
		
	public static float worldVisibleMinX;
	public static float worldVisibleMaxX;
	public static float worldVisibleMinY;
	public static float worldVisibleMaxY;
	
	/** For playing music. Only one instance should be created. */
	public static MusicPlayer musicPlayer = null;
	/** For playing sound effects. Only one instance should be created. */
	public static SoundPlayer soundPlayer = null;
	
	/** Current level data. */
	public static ALevel level;
	/** Current player data. */
	public static APlayer player;
	
	public static GameViewport viewportGame;
	public static InfoViewport viewportInfo;
	public static Viewport viewportScreen;
	
	/** For drawing the game window. */
	public static SpriteBatch batchGame;
	/** For drawing the info screen, score etc. */
	public static SpriteBatch batchInfo;
	/** For drawing the background directly to the screen. */
	public static SpriteBatch batchScreen;
	
	/** The player entity the player plays */
	public static Entity playerEntity;
	
	/** Entity engine ASHLEY for the main game.*/
	public static Engine gameEntityEngine;
}
